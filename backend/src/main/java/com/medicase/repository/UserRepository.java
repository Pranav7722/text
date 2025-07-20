package com.medicase.repository;

import com.medicase.model.User;
import com.medicase.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByQrCode(String qrCode);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(UserRole role);
    
    @Query("{'role': ?0, 'enabled': true}")
    List<User> findActiveUsersByRole(UserRole role);
    
    @Query("{'$or': [{'firstName': {$regex: ?0, $options: 'i'}}, {'lastName': {$regex: ?0, $options: 'i'}}, {'email': {$regex: ?0, $options: 'i'}}]}")
    List<User> findByNameOrEmailContaining(String searchTerm);
    
    long countByRole(UserRole role);
    
    @Query("{'createdAt': {$gte: ?0, $lt: ?1}}")
    List<User> findUsersCreatedBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}