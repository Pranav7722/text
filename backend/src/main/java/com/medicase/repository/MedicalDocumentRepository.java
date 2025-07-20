package com.medicase.repository;

import com.medicase.model.MedicalDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalDocumentRepository extends MongoRepository<MedicalDocument, String> {
    
    List<MedicalDocument> findByPatientId(String patientId);
    
    Page<MedicalDocument> findByPatientId(String patientId, Pageable pageable);
    
    List<MedicalDocument> findByPatientIdAndCategory(String patientId, String category);
    
    @Query("{'patientId': ?0, 'isPublic': true}")
    List<MedicalDocument> findPublicDocumentsByPatientId(String patientId);
    
    @Query("{'patientId': ?0, 'isPublic': true}")
    Page<MedicalDocument> findPublicDocumentsByPatientId(String patientId, Pageable pageable);
    
    List<MedicalDocument> findByUploadedBy(String uploadedBy);
    
    @Query("{'fileName': {$regex: ?0, $options: 'i'}}")
    List<MedicalDocument> findByFileNameContaining(String fileName);
    
    @Query("{'patientId': ?0, 'fileName': {$regex: ?1, $options: 'i'}}")
    List<MedicalDocument> findByPatientIdAndFileNameContaining(String patientId, String fileName);
    
    @Query("{'uploadDate': {$gte: ?0, $lt: ?1}}")
    List<MedicalDocument> findDocumentsUploadedBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{'patientId': ?0, 'uploadDate': {$gte: ?1, $lt: ?2}}")
    List<MedicalDocument> findByPatientIdAndUploadDateBetween(String patientId, LocalDateTime start, LocalDateTime end);
    
    long countByPatientId(String patientId);
    
    @Query("{'patientId': ?0, 'isPublic': true}")
    long countPublicDocumentsByPatientId(String patientId);
    
    List<MedicalDocument> findByFileType(String fileType);
    
    @Query("{'fileSize': {$gte: ?0, $lt: ?1}}")
    List<MedicalDocument> findByFileSizeBetween(Long minSize, Long maxSize);
}