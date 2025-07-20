package com.medicase.service;

import com.medicase.exception.ResourceNotFoundException;
import com.medicase.model.DocumentType;
import com.medicase.model.MedicalDocument;
import com.medicase.model.Role;
import com.medicase.model.User;
import com.medicase.repository.MedicalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalDocumentService {
    
    @Autowired
    private MedicalDocumentRepository documentRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private UserService userService;
    
    public MedicalDocument uploadDocument(MultipartFile file, String title, String description, 
                                        DocumentType type, String patientId, String uploadedById,
                                        String hospitalName, String doctorName, String documentDate) {
        
        User patient = userService.findById(patientId);
        User uploadedBy = userService.findById(uploadedById);
        
        // Ensure only patients can have documents uploaded for them
        if (patient.getRole() != Role.PATIENT) {
            throw new IllegalArgumentException("Documents can only be uploaded for patients");
        }
        
        // Store the file
        String fileName = fileStorageService.storeFile(file, patientId);
        
        MedicalDocument document = new MedicalDocument();
        document.setTitle(title);
        document.setDescription(description);
        document.setType(type);
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(fileName);
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setPatient(patient);
        document.setUploadedBy(uploadedBy);
        document.setHospitalName(hospitalName);
        document.setDoctorName(doctorName);
        
        if (documentDate != null && !documentDate.trim().isEmpty()) {
            try {
                document.setDocumentDate(LocalDateTime.parse(documentDate));
            } catch (Exception e) {
                document.setDocumentDate(LocalDateTime.now());
            }
        } else {
            document.setDocumentDate(LocalDateTime.now());
        }
        
        return documentRepository.save(document);
    }
    
    public List<MedicalDocument> getPatientDocuments(String patientId) {
        User patient = userService.findById(patientId);
        return documentRepository.findByPatientOrderByCreatedAtDesc(patient);
    }
    
    public List<MedicalDocument> getPatientSharedDocuments(String patientId) {
        User patient = userService.findById(patientId);
        return documentRepository.findByPatientAndIsSharedTrueOrderByCreatedAtDesc(patient);
    }
    
    public List<MedicalDocument> getDocumentsByType(String patientId, DocumentType type) {
        User patient = userService.findById(patientId);
        return documentRepository.findByPatientAndTypeOrderByCreatedAtDesc(patient, type);
    }
    
    public List<MedicalDocument> searchDocuments(String patientId, String keyword) {
        User patient = userService.findById(patientId);
        return documentRepository.searchByPatientAndKeyword(patient, keyword);
    }
    
    public MedicalDocument getDocument(String documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
    }
    
    public byte[] getDocumentFile(String documentId, String userId) {
        MedicalDocument document = getDocument(documentId);
        User user = userService.findById(userId);
        
        // Check access permissions
        if (!canUserAccessDocument(user, document)) {
            throw new SecurityException("Access denied to this document");
        }
        
        return fileStorageService.loadFileAsBytes(document.getFilePath());
    }
    
    public MedicalDocument updateDocument(String documentId, String title, String description, 
                                        String hospitalName, String doctorName, String notes) {
        MedicalDocument document = getDocument(documentId);
        
        document.setTitle(title);
        document.setDescription(description);
        document.setHospitalName(hospitalName);
        document.setDoctorName(doctorName);
        document.setNotes(notes);
        
        return documentRepository.save(document);
    }
    
    public void deleteDocument(String documentId, String userId) {
        MedicalDocument document = getDocument(documentId);
        User user = userService.findById(userId);
        
        // Only patient or admin can delete documents
        if (!user.getId().equals(document.getPatient().getId()) && user.getRole() != Role.ADMIN) {
            throw new SecurityException("Access denied to delete this document");
        }
        
        // Delete the file
        fileStorageService.deleteFile(document.getFilePath());
        
        // Delete the document record
        documentRepository.delete(document);
    }
    
    public MedicalDocument toggleDocumentSharing(String documentId, String userId) {
        MedicalDocument document = getDocument(documentId);
        User user = userService.findById(userId);
        
        // Only patient can toggle sharing
        if (!user.getId().equals(document.getPatient().getId())) {
            throw new SecurityException("Only patients can control document sharing");
        }
        
        document.setShared(!document.isShared());
        return documentRepository.save(document);
    }
    
    public long getPatientDocumentCount(String patientId) {
        User patient = userService.findById(patientId);
        return documentRepository.countByPatient(patient);
    }
    
    public long getDocumentCountByType(DocumentType type) {
        return documentRepository.countByType(type);
    }
    
    private boolean canUserAccessDocument(User user, MedicalDocument document) {
        // Patient can access their own documents
        if (user.getId().equals(document.getPatient().getId())) {
            return true;
        }
        
        // Admin can access all documents
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        
        // Doctor can access shared documents
        if (user.getRole() == Role.DOCTOR && document.isShared()) {
            return true;
        }
        
        return false;
    }
}