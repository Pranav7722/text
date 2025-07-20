package com.medicase.repository;

import com.medicase.model.DocumentType;
import com.medicase.model.MedicalDocument;
import com.medicase.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalDocumentRepository extends MongoRepository<MedicalDocument, String> {
    
    List<MedicalDocument> findByPatientOrderByCreatedAtDesc(User patient);
    
    List<MedicalDocument> findByPatientAndTypeOrderByCreatedAtDesc(User patient, DocumentType type);
    
    List<MedicalDocument> findByUploadedByOrderByCreatedAtDesc(User uploadedBy);
    
    List<MedicalDocument> findByPatientAndIsSharedTrueOrderByCreatedAtDesc(User patient);
    
    @Query("{ 'patient': ?0, 'title': { $regex: ?1, $options: 'i' } }")
    List<MedicalDocument> findByPatientAndTitleContaining(User patient, String title);
    
    @Query("{ 'patient': ?0, 'tags': { $in: ?1 } }")
    List<MedicalDocument> findByPatientAndTagsIn(User patient, List<String> tags);
    
    @Query("{ 'patient': ?0, 'documentDate': { $gte: ?1, $lte: ?2 } }")
    List<MedicalDocument> findByPatientAndDocumentDateBetween(User patient, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{ 'patient': ?0, '$or': [ " +
           "{ 'title': { $regex: ?1, $options: 'i' } }, " +
           "{ 'description': { $regex: ?1, $options: 'i' } }, " +
           "{ 'doctorName': { $regex: ?1, $options: 'i' } }, " +
           "{ 'hospitalName': { $regex: ?1, $options: 'i' } } ] }")
    List<MedicalDocument> searchByPatientAndKeyword(User patient, String keyword);
    
    long countByPatient(User patient);
    
    long countByType(DocumentType type);
    
    @Query("{ 'createdAt': { $gte: ?0, $lte: ?1 } }")
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}