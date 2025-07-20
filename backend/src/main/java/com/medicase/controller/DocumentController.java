package com.medicase.controller;

import com.medicase.model.DocumentType;
import com.medicase.model.MedicalDocument;
import com.medicase.model.User;
import com.medicase.service.MedicalDocumentService;
import com.medicase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
@Tag(name = "Documents", description = "Medical document management endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DocumentController {
    
    @Autowired
    private MedicalDocumentService documentService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/upload")
    @Operation(summary = "Upload document", description = "Upload a medical document")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") DocumentType type,
            @RequestParam("patientId") String patientId,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "doctorName", required = false) String doctorName,
            @RequestParam(value = "documentDate", required = false) String documentDate,
            Authentication authentication) {
        
        try {
            User currentUser = userService.findByEmail(authentication.getName());
            
            MedicalDocument document = documentService.uploadDocument(
                file, title, description, type, patientId, currentUser.getId(),
                hospitalName, doctorName, documentDate
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error uploading document: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get patient documents", description = "Retrieve all documents for a patient")
    public ResponseEntity<?> getPatientDocuments(@PathVariable String patientId, Authentication authentication) {
        try {
            User currentUser = userService.findByEmail(authentication.getName());
            
            List<MedicalDocument> documents;
            
            // If current user is the patient, return all documents
            if (currentUser.getId().equals(patientId)) {
                documents = documentService.getPatientDocuments(patientId);
            } else {
                // If current user is not the patient, return only shared documents
                documents = documentService.getPatientSharedDocuments(patientId);
            }
            
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error retrieving documents: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}/type/{type}")
    @Operation(summary = "Get documents by type", description = "Retrieve patient documents by type")
    public ResponseEntity<?> getDocumentsByType(@PathVariable String patientId, 
                                               @PathVariable DocumentType type,
                                               Authentication authentication) {
        try {
            List<MedicalDocument> documents = documentService.getDocumentsByType(patientId, type);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error retrieving documents: " + e.getMessage());
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search documents", description = "Search patient documents by keyword")
    public ResponseEntity<?> searchDocuments(@RequestParam String patientId,
                                           @RequestParam String keyword,
                                           Authentication authentication) {
        try {
            List<MedicalDocument> documents = documentService.searchDocuments(patientId, keyword);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error searching documents: " + e.getMessage());
        }
    }
    
    @GetMapping("/{documentId}")
    @Operation(summary = "Get document details", description = "Retrieve document metadata")
    public ResponseEntity<?> getDocument(@PathVariable String documentId, Authentication authentication) {
        try {
            MedicalDocument document = documentService.getDocument(documentId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Document not found: " + e.getMessage());
        }
    }
    
    @GetMapping("/{documentId}/download")
    @Operation(summary = "Download document file", description = "Download the actual document file")
    public ResponseEntity<?> downloadDocument(@PathVariable String documentId, Authentication authentication) {
        try {
            User currentUser = userService.findByEmail(authentication.getName());
            MedicalDocument document = documentService.getDocument(documentId);
            
            byte[] fileContent = documentService.getDocumentFile(documentId, currentUser.getId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(document.getFileType()));
            headers.setContentDispositionFormData("attachment", document.getFileName());
            headers.setContentLength(fileContent.length);
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error downloading document: " + e.getMessage());
        }
    }
    
    @PutMapping("/{documentId}")
    @Operation(summary = "Update document", description = "Update document metadata")
    public ResponseEntity<?> updateDocument(@PathVariable String documentId,
                                          @RequestParam("title") String title,
                                          @RequestParam("description") String description,
                                          @RequestParam(value = "hospitalName", required = false) String hospitalName,
                                          @RequestParam(value = "doctorName", required = false) String doctorName,
                                          @RequestParam(value = "notes", required = false) String notes,
                                          Authentication authentication) {
        try {
            MedicalDocument updatedDocument = documentService.updateDocument(
                documentId, title, description, hospitalName, doctorName, notes
            );
            
            return ResponseEntity.ok(updatedDocument);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error updating document: " + e.getMessage());
        }
    }
    
    @PatchMapping("/{documentId}/toggle-sharing")
    @Operation(summary = "Toggle document sharing", description = "Toggle document sharing with doctors")
    public ResponseEntity<?> toggleDocumentSharing(@PathVariable String documentId, Authentication authentication) {
        try {
            User currentUser = userService.findByEmail(authentication.getName());
            MedicalDocument document = documentService.toggleDocumentSharing(documentId, currentUser.getId());
            
            return ResponseEntity.ok(document);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error toggling sharing: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{documentId}")
    @Operation(summary = "Delete document", description = "Delete a medical document")
    public ResponseEntity<?> deleteDocument(@PathVariable String documentId, Authentication authentication) {
        try {
            User currentUser = userService.findByEmail(authentication.getName());
            documentService.deleteDocument(documentId, currentUser.getId());
            
            return ResponseEntity.ok("Document deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error deleting document: " + e.getMessage());
        }
    }
    
    @GetMapping("/types")
    @Operation(summary = "Get document types", description = "Get all available document types")
    public ResponseEntity<?> getDocumentTypes() {
        return ResponseEntity.ok(DocumentType.values());
    }
}