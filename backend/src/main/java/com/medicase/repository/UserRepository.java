package com.medicase.repository;

import com.medicase.model.Role;
import com.medicase.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByQrCodeId(String qrCodeId);
    
    List<User> findByRole(Role role);
    
    List<User> findByRoleAndEnabledTrue(Role role);
    
    boolean existsByEmail(String email);
    
    boolean existsByQrCodeId(String qrCodeId);
    
    @Query("{ 'role': ?0, 'enabled': true, '$or': [ " +
           "{ 'firstName': { $regex: ?1, $options: 'i' } }, " +
           "{ 'lastName': { $regex: ?1, $options: 'i' } }, " +
           "{ 'email': { $regex: ?1, $options: 'i' } } ] }")
    List<User> findByRoleAndSearchTerm(Role role, String searchTerm);
    
    @Query("{ 'role': 'DOCTOR', 'specialization': { $regex: ?0, $options: 'i' } }")
    List<User> findDoctorsBySpecialization(String specialization);
    
    long countByRole(Role role);
    
    long countByEnabledTrue();
}