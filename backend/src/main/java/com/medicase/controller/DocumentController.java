package com.medicase.controller;

import com.medicase.model.MedicalDocument;
import com.medicase.model.User;
import com.medicase.service.DocumentService;
import com.medicase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "patientId", required = false) String patientId,
            @RequestParam(value = "category", required = false, defaultValue = "General") String category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "isPublic", required = false, defaultValue = "false") boolean isPublic) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If patientId is not provided, use current user's ID (for patients uploading their own documents)
            if (patientId == null) {
                patientId = currentUser.getId();
            }

            // Validate access rights
            if (!currentUser.getRole().name().equals("ADMIN") && 
                !currentUser.getRole().name().equals("DOCTOR") && 
                !patientId.equals(currentUser.getId())) {
                throw new RuntimeException("Unauthorized to upload documents for this patient");
            }

            MedicalDocument document = documentService.uploadDocument(
                    patientId, file, category, description, currentUser.getId());

            // Set visibility if specified
            if (isPublic) {
                document.setPublic(true);
                documentService.updateDocument(document.getId(), category, description, isPublic);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Document uploaded successfully");
            response.put("document", document);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientDocuments(
            @PathVariable String patientId,
            @RequestParam(value = "publicOnly", required = false, defaultValue = "false") boolean publicOnly) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<MedicalDocument> documents;

            // Check access rights
            if (currentUser.getRole().name().equals("ADMIN") || 
                patientId.equals(currentUser.getId())) {
                // Admin or own documents - can see all
                documents = documentService.getPatientDocuments(patientId);
            } else if (currentUser.getRole().name().equals("DOCTOR") || publicOnly) {
                // Doctor or public access - only public documents
                documents = documentService.getPublicPatientDocuments(patientId);
            } else {
                throw new RuntimeException("Unauthorized access to patient documents");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("documents", documents);
            response.put("totalCount", documents.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<?> downloadDocument(@PathVariable String documentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MedicalDocument document = documentService.getDocumentById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            // Check access rights
            if (!documentService.hasAccessToDocument(documentId, currentUser.getId(), currentUser.getRole().name())) {
                throw new RuntimeException("Unauthorized access to document");
            }

            InputStream fileStream = documentService.downloadDocument(documentId);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, document.getFileType());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(document.getFileType()))
                    .body(new InputStreamResource(fileStream));

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<?> updateDocument(
            @PathVariable String documentId,
            @RequestBody UpdateDocumentRequest request) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MedicalDocument document = documentService.getDocumentById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            // Check if user can update this document
            if (!currentUser.getRole().name().equals("ADMIN") && 
                !document.getPatientId().equals(currentUser.getId()) &&
                (document.getUploadedBy() == null || !document.getUploadedBy().equals(currentUser.getId()))) {
                throw new RuntimeException("Unauthorized to update this document");
            }

            MedicalDocument updatedDocument = documentService.updateDocument(
                    documentId, 
                    request.getCategory(), 
                    request.getDescription(), 
                    request.isPublic());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Document updated successfully");
            response.put("document", updatedDocument);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable String documentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MedicalDocument document = documentService.getDocumentById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            // Check if user can delete this document
            if (!currentUser.getRole().name().equals("ADMIN") && 
                !document.getPatientId().equals(currentUser.getId()) &&
                (document.getUploadedBy() == null || !document.getUploadedBy().equals(currentUser.getId()))) {
                throw new RuntimeException("Unauthorized to delete this document");
            }

            documentService.deleteDocument(documentId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Document deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{documentId}/toggle-visibility")
    public ResponseEntity<?> toggleDocumentVisibility(@PathVariable String documentId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MedicalDocument document = documentService.getDocumentById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));

            // Only patient or admin can toggle visibility
            if (!currentUser.getRole().name().equals("ADMIN") && 
                !document.getPatientId().equals(currentUser.getId())) {
                throw new RuntimeException("Unauthorized to change document visibility");
            }

            documentService.toggleDocumentVisibility(documentId, currentUser.getId());

            MedicalDocument updatedDocument = documentService.getDocumentById(documentId).orElseThrow();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Document visibility updated successfully");
            response.put("document", updatedDocument);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDocuments(
            @RequestParam String query,
            @RequestParam(value = "patientId", required = false) String patientId) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If patientId not provided, search in current user's documents
            if (patientId == null) {
                patientId = currentUser.getId();
            }

            // Check access rights
            if (!currentUser.getRole().name().equals("ADMIN") && 
                !patientId.equals(currentUser.getId())) {
                throw new RuntimeException("Unauthorized access to patient documents");
            }

            List<MedicalDocument> documents = documentService.searchDocuments(patientId, query);

            Map<String, Object> response = new HashMap<>();
            response.put("documents", documents);
            response.put("searchQuery", query);
            response.put("totalCount", documents.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Request DTOs
    public static class UpdateDocumentRequest {
        private String category;
        private String description;
        private boolean isPublic;

        // Getters and setters
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    }
}