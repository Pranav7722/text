package com.medicase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "medical_documents")
public class MedicalDocument {
    
    @Id
    private String id;
    
    @NotBlank(message = "Patient ID is required")
    @Indexed
    private String patientId;
    
    @NotBlank(message = "File name is required")
    private String fileName;
    
    @NotBlank(message = "File type is required")
    private String fileType;
    
    @NotNull(message = "File size is required")
    private Long fileSize;
    
    private String category;
    
    private String description;
    
    private String uploadedBy; // User ID who uploaded
    
    @NotBlank(message = "GridFS file ID is required")
    private String gridFSFileId;
    
    private boolean isPublic = false; // If true, accessible to doctors
    
    @CreatedDate
    private LocalDateTime uploadDate;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public MedicalDocument() {}
    
    public MedicalDocument(String patientId, String fileName, String fileType, Long fileSize, String gridFSFileId) {
        this.patientId = patientId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.gridFSFileId = gridFSFileId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    public String getGridFSFileId() {
        return gridFSFileId;
    }
    
    public void setGridFSFileId(String gridFSFileId) {
        this.gridFSFileId = gridFSFileId;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getFileSizeFormatted() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        } else {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        }
    }
}