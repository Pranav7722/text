package com.medicase.service;

import com.medicase.model.MedicalDocument;
import com.medicase.repository.MedicalDocumentRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private MedicalDocumentRepository documentRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private final List<String> allowedFileTypes = List.of(
            "application/pdf",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    public MedicalDocument uploadDocument(String patientId, MultipartFile file, String category, String description, String uploadedBy) throws IOException {
        // Validate file type
        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new RuntimeException("File type not allowed: " + file.getContentType());
        }

        // Validate file size (50MB max)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds maximum limit of 50MB");
        }

        // Store file in GridFS
        String gridFSFileId = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        ).toString();

        // Create document record
        MedicalDocument document = new MedicalDocument(
                patientId,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                gridFSFileId
        );

        document.setCategory(category);
        document.setDescription(description);
        document.setUploadedBy(uploadedBy);

        return documentRepository.save(document);
    }

    public List<MedicalDocument> getPatientDocuments(String patientId) {
        return documentRepository.findByPatientId(patientId);
    }

    public Page<MedicalDocument> getPatientDocuments(String patientId, Pageable pageable) {
        return documentRepository.findByPatientId(patientId, pageable);
    }

    public List<MedicalDocument> getPublicPatientDocuments(String patientId) {
        return documentRepository.findPublicDocumentsByPatientId(patientId);
    }

    public Page<MedicalDocument> getPublicPatientDocuments(String patientId, Pageable pageable) {
        return documentRepository.findPublicDocumentsByPatientId(patientId, pageable);
    }

    public Optional<MedicalDocument> getDocumentById(String documentId) {
        return documentRepository.findById(documentId);
    }

    public InputStream downloadDocument(String documentId) throws IOException {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        GridFSFile gridFSFile = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(document.getGridFSFileId()))
        );

        if (gridFSFile == null) {
            throw new RuntimeException("File not found in GridFS");
        }

        return gridFsTemplate.getResource(gridFSFile).getInputStream();
    }

    public MedicalDocument updateDocument(String documentId, String category, String description, boolean isPublic) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setCategory(category);
        document.setDescription(description);
        document.setPublic(isPublic);

        return documentRepository.save(document);
    }

    public void deleteDocument(String documentId) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Delete from GridFS
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(document.getGridFSFileId())));

        // Delete document record
        documentRepository.deleteById(documentId);
    }

    public List<MedicalDocument> searchDocuments(String patientId, String searchTerm) {
        return documentRepository.findByPatientIdAndFileNameContaining(patientId, searchTerm);
    }

    public List<MedicalDocument> getDocumentsByCategory(String patientId, String category) {
        return documentRepository.findByPatientIdAndCategory(patientId, category);
    }

    public long getPatientDocumentCount(String patientId) {
        return documentRepository.countByPatientId(patientId);
    }

    public long getPublicDocumentCount(String patientId) {
        return documentRepository.countPublicDocumentsByPatientId(patientId);
    }

    public List<MedicalDocument> getDocumentsByUploader(String uploaderId) {
        return documentRepository.findByUploadedBy(uploaderId);
    }

    public boolean hasAccessToDocument(String documentId, String userId, String userRole) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // Admin can access all documents
        if ("ADMIN".equals(userRole)) {
            return true;
        }

        // Patient can access their own documents
        if ("PATIENT".equals(userRole) && document.getPatientId().equals(userId)) {
            return true;
        }

        // Doctor can access public documents
        if ("DOCTOR".equals(userRole) && document.isPublic()) {
            return true;
        }

        // Uploader can access documents they uploaded
        if (document.getUploadedBy() != null && document.getUploadedBy().equals(userId)) {
            return true;
        }

        return false;
    }

    public void toggleDocumentVisibility(String documentId, String patientId) {
        MedicalDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getPatientId().equals(patientId)) {
            throw new RuntimeException("Unauthorized access to document");
        }

        document.setPublic(!document.isPublic());
        documentRepository.save(document);
    }
}