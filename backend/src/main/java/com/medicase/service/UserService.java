package com.medicase.service;

import com.medicase.dto.UserRegistrationRequest;
import com.medicase.exception.ResourceNotFoundException;
import com.medicase.exception.UserAlreadyExistsException;
import com.medicase.model.Role;
import com.medicase.model.User;
import com.medicase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
    
    public User createUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        
        // Set role-specific fields
        if (request.getRole() == Role.DOCTOR) {
            user.setLicenseNumber(request.getLicenseNumber());
            user.setSpecialization(request.getSpecialization());
            user.setHospitalAffiliation(request.getHospitalAffiliation());
        } else if (request.getRole() == Role.PATIENT) {
            user.setEmergencyContact(request.getEmergencyContact());
            user.setBloodGroup(request.getBloodGroup());
            user.setAllergies(request.getAllergies());
            
            // Generate unique QR code ID for patients
            String qrCodeId;
            do {
                qrCodeId = generateQrCodeId();
            } while (userRepository.existsByQrCodeId(qrCodeId));
            user.setQrCodeId(qrCodeId);
        }
        
        return userRepository.save(user);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User findByQrCodeId(String qrCodeId) {
        return userRepository.findByQrCodeId(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with QR code ID: " + qrCodeId));
    }
    
    public List<User> findByRole(Role role) {
        return userRepository.findByRoleAndEnabledTrue(role);
    }
    
    public List<User> searchUsers(Role role, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findByRole(role);
        }
        return userRepository.findByRoleAndSearchTerm(role, searchTerm.trim());
    }
    
    public List<User> findDoctorsBySpecialization(String specialization) {
        return userRepository.findDoctorsBySpecialization(specialization);
    }
    
    public User updateUser(String id, User updatedUser) {
        User existingUser = findById(id);
        
        // Update allowed fields
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setAddress(updatedUser.getAddress());
        
        // Update role-specific fields
        if (existingUser.getRole() == Role.DOCTOR) {
            existingUser.setLicenseNumber(updatedUser.getLicenseNumber());
            existingUser.setSpecialization(updatedUser.getSpecialization());
            existingUser.setHospitalAffiliation(updatedUser.getHospitalAffiliation());
        } else if (existingUser.getRole() == Role.PATIENT) {
            existingUser.setEmergencyContact(updatedUser.getEmergencyContact());
            existingUser.setBloodGroup(updatedUser.getBloodGroup());
            existingUser.setAllergies(updatedUser.getAllergies());
        }
        
        return userRepository.save(existingUser);
    }
    
    public void changePassword(String userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public void enableUser(String userId) {
        User user = findById(userId);
        user.setEnabled(true);
        userRepository.save(user);
    }
    
    public void disableUser(String userId) {
        User user = findById(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    public long getUserCount() {
        return userRepository.countByEnabledTrue();
    }
    
    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    private String generateQrCodeId() {
        return "MED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}