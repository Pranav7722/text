package com.medicase.controller;

import com.medicase.model.User;
import com.medicase.model.MedicalDocument;
import com.medicase.service.UserService;
import com.medicase.service.DocumentService;
import com.medicase.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> response = createUserProfileResponse(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            User updatedUser = new User();
            updatedUser.setFirstName(request.getFirstName());
            updatedUser.setLastName(request.getLastName());
            updatedUser.setPhoneNumber(request.getPhoneNumber());
            updatedUser.setAddress(request.getAddress());

            User savedUser = userService.updateUserProfile(currentUser.getId(), updatedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            response.put("user", createUserProfileResponse(savedUser));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            userService.changePassword(user.getId(), request.getCurrentPassword(), request.getNewPassword());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/qr")
    public ResponseEntity<?> getQRCode() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getQrCode() == null) {
                String newQrCode = userService.generateNewQRCode(user.getId());
                user.setQrCode(newQrCode);
            }

            String qrCodeImage = qrCodeService.generateQRCodeDataUrl(user.getQrCode());

            Map<String, Object> response = new HashMap<>();
            response.put("qrCode", user.getQrCode());
            response.put("qrCodeImage", qrCodeImage);
            response.put("shareUrl", "http://localhost:4200/patient/" + user.getQrCode());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/qr/regenerate")
    public ResponseEntity<?> regenerateQRCode() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newQrCode = userService.generateNewQRCode(user.getId());
            String qrCodeImage = qrCodeService.generateQRCodeDataUrl(newQrCode);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "QR code regenerated successfully");
            response.put("qrCode", newQrCode);
            response.put("qrCodeImage", qrCodeImage);
            response.put("shareUrl", "http://localhost:4200/patient/" + newQrCode);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<?> getDocuments() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<MedicalDocument> documents = documentService.getPatientDocuments(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("documents", documents);
            response.put("totalCount", documents.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/documents/stats")
    public ResponseEntity<?> getDocumentStats() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            long totalDocuments = documentService.getPatientDocumentCount(user.getId());
            long publicDocuments = documentService.getPublicDocumentCount(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("totalDocuments", totalDocuments);
            response.put("publicDocuments", publicDocuments);
            response.put("privateDocuments", totalDocuments - publicDocuments);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private Map<String, Object> createUserProfileResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("email", user.getEmail());
        userResponse.put("firstName", user.getFirstName());
        userResponse.put("lastName", user.getLastName());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("phoneNumber", user.getPhoneNumber());
        userResponse.put("role", user.getRole());
        userResponse.put("address", user.getAddress());
        userResponse.put("qrCode", user.getQrCode());
        userResponse.put("createdAt", user.getCreatedAt());
        userResponse.put("updatedAt", user.getUpdatedAt());
        return userResponse;
    }

    // Request DTOs
    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private com.medicase.model.Address address;

        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public com.medicase.model.Address getAddress() { return address; }
        public void setAddress(com.medicase.model.Address address) { this.address = address; }
    }

    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        // Getters and setters
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}