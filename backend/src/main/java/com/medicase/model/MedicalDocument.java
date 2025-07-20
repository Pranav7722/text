package com.medicase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "medical_documents")
public class MedicalDocument {
    
    @Id
    private String id;
    
    @NotBlank(message = "Document title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Document type is required")
    private DocumentType type;
    
    @NotBlank(message = "File name is required")
    private String fileName;
    
    @NotBlank(message = "File path is required")
    private String filePath;
    
    private String fileType; // MIME type
    private long fileSize;
    
    @DBRef
    private User patient; // Patient this document belongs to
    
    @DBRef
    private User uploadedBy; // User who uploaded this document (could be patient or doctor)
    
    private String hospitalName;
    private String doctorName;
    private LocalDateTime documentDate; // Date when the medical procedure/test was done
    
    private List<String> tags; // For categorization and search
    private String notes; // Additional notes
    
    private boolean isShared = true; // Whether document is shared with doctors
    private List<String> sharedWithDoctors; // Specific doctor IDs who can access
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public MedicalDocument() {}
    
    public MedicalDocument(String title, DocumentType type, String fileName, String filePath, User patient) {
        this.title = title;
        this.type = type;
        this.fileName = fileName;
        this.filePath = filePath;
        this.patient = patient;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public DocumentType getType() { return type; }
    public void setType(DocumentType type) { this.type = type; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
    
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
    
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public LocalDateTime getDocumentDate() { return documentDate; }
    public void setDocumentDate(LocalDateTime documentDate) { this.documentDate = documentDate; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isShared() { return isShared; }
    public void setShared(boolean shared) { isShared = shared; }
    
    public List<String> getSharedWithDoctors() { return sharedWithDoctors; }
    public void setSharedWithDoctors(List<String> sharedWithDoctors) { this.sharedWithDoctors = sharedWithDoctors; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}