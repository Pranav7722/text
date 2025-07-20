package com.medicase.controller;

import com.medicase.model.User;
import com.medicase.service.QRCodeService;
import com.medicase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qr")
@Tag(name = "QR Code", description = "QR code generation and patient lookup endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QRController {
    
    @Autowired
    private QRCodeService qrCodeService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/generate")
    @Operation(summary = "Generate QR code", description = "Generate QR code for authenticated patient")
    public ResponseEntity<?> generateQRCode(Authentication authentication) {
        try {
            User user = userService.findByEmail(authentication.getName());
            
            if (user.getQrCodeId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("QR code not available for this user");
            }
            
            byte[] qrCodeImage = qrCodeService.generateQRCode(user.getQrCodeId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(qrCodeImage.length);
            headers.set("Content-Disposition", "inline; filename=qr-code.png");
            
            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error generating QR code: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{qrCodeId}")
    @Operation(summary = "Get patient by QR code", description = "Retrieve patient information using QR code ID")
    public ResponseEntity<?> getPatientByQrCode(@PathVariable String qrCodeId) {
        try {
            User patient = userService.findByQrCodeId(qrCodeId);
            
            // Return only necessary patient information
            PatientInfoResponse response = new PatientInfoResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getAllergies(),
                patient.getEmergencyContact()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Patient not found with QR code: " + qrCodeId);
        }
    }
    
    @GetMapping("/url")
    @Operation(summary = "Get QR code URL", description = "Get the URL that the QR code points to")
    public ResponseEntity<?> getQRCodeUrl(Authentication authentication) {
        try {
            User user = userService.findByEmail(authentication.getName());
            
            if (user.getQrCodeId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("QR code not available for this user");
            }
            
            String qrUrl = qrCodeService.generateQRCodeUrl(user.getQrCodeId());
            
            return ResponseEntity.ok(new QRUrlResponse(qrUrl, user.getQrCodeId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error generating QR URL: " + e.getMessage());
        }
    }
    
    // Inner classes for response DTOs
    public static class PatientInfoResponse {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String dateOfBirth;
        private String gender;
        private String bloodGroup;
        private String allergies;
        private String emergencyContact;
        
        public PatientInfoResponse(String id, String firstName, String lastName, String email, 
                                 String phoneNumber, String dateOfBirth, String gender, 
                                 String bloodGroup, String allergies, String emergencyContact) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.bloodGroup = bloodGroup;
            this.allergies = allergies;
            this.emergencyContact = emergencyContact;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getDateOfBirth() { return dateOfBirth; }
        public String getGender() { return gender; }
        public String getBloodGroup() { return bloodGroup; }
        public String getAllergies() { return allergies; }
        public String getEmergencyContact() { return emergencyContact; }
    }
    
    public static class QRUrlResponse {
        private String url;
        private String qrCodeId;
        
        public QRUrlResponse(String url, String qrCodeId) {
            this.url = url;
            this.qrCodeId = qrCodeId;
        }
        
        public String getUrl() { return url; }
        public String getQrCodeId() { return qrCodeId; }
    }
}