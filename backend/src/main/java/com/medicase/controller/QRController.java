package com.medicase.controller;

import com.medicase.model.User;
import com.medicase.model.MedicalDocument;
import com.medicase.service.UserService;
import com.medicase.service.DocumentService;
import com.medicase.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qr")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QRController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/scan/{qrCode}")
    public ResponseEntity<?> scanQRCode(@PathVariable String qrCode) {
        try {
            // Validate QR code format
            if (!qrCodeService.isValidQRCode(qrCode)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid QR code format");
                return ResponseEntity.badRequest().body(error);
            }

            // Find patient by QR code
            User patient = userService.findByQrCode(qrCode)
                    .orElseThrow(() -> new RuntimeException("Patient not found for this QR code"));

            // Get public documents for this patient
            List<MedicalDocument> publicDocuments = documentService.getPublicPatientDocuments(patient.getId());

            // Create response with patient info and public documents
            Map<String, Object> response = new HashMap<>();
            response.put("patient", createPatientPublicProfile(patient));
            response.put("documents", publicDocuments);
            response.put("documentCount", publicDocuments.size());
            response.put("qrCode", qrCode);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateQRCode(@RequestBody QRValidationRequest request) {
        try {
            String qrCode = request.getQrCode();
            
            // Validate QR code format
            if (!qrCodeService.isValidQRCode(qrCode)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "Invalid QR code format");
                return ResponseEntity.ok(response);
            }

            // Check if patient exists for this QR code
            boolean exists = userService.findByQrCode(qrCode).isPresent();

            Map<String, Object> response = new HashMap<>();
            response.put("valid", exists);
            response.put("message", exists ? "Valid QR code" : "QR code not found");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/patient/{qrCode}/info")
    public ResponseEntity<?> getPatientInfoByQR(@PathVariable String qrCode) {
        try {
            // Validate QR code format
            if (!qrCodeService.isValidQRCode(qrCode)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid QR code format");
                return ResponseEntity.badRequest().body(error);
            }

            // Find patient by QR code
            User patient = userService.findByQrCode(qrCode)
                    .orElseThrow(() -> new RuntimeException("Patient not found for this QR code"));

            // Return only basic patient info (no sensitive data)
            Map<String, Object> response = new HashMap<>();
            response.put("patient", createPatientPublicProfile(patient));
            response.put("qrCode", qrCode);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/patient/{qrCode}/documents")
    public ResponseEntity<?> getPatientDocumentsByQR(@PathVariable String qrCode) {
        try {
            // Validate QR code format
            if (!qrCodeService.isValidQRCode(qrCode)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid QR code format");
                return ResponseEntity.badRequest().body(error);
            }

            // Find patient by QR code
            User patient = userService.findByQrCode(qrCode)
                    .orElseThrow(() -> new RuntimeException("Patient not found for this QR code"));

            // Get only public documents
            List<MedicalDocument> publicDocuments = documentService.getPublicPatientDocuments(patient.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("documents", publicDocuments);
            response.put("totalCount", publicDocuments.size());
            response.put("patientName", patient.getFullName());
            response.put("qrCode", qrCode);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/patient/{qrCode}/stats")
    public ResponseEntity<?> getPatientStatsByQR(@PathVariable String qrCode) {
        try {
            // Validate QR code format
            if (!qrCodeService.isValidQRCode(qrCode)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid QR code format");
                return ResponseEntity.badRequest().body(error);
            }

            // Find patient by QR code
            User patient = userService.findByQrCode(qrCode)
                    .orElseThrow(() -> new RuntimeException("Patient not found for this QR code"));

            // Get document statistics
            long totalDocuments = documentService.getPatientDocumentCount(patient.getId());
            long publicDocuments = documentService.getPublicDocumentCount(patient.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("patientName", patient.getFullName());
            response.put("totalDocuments", totalDocuments);
            response.put("publicDocuments", publicDocuments);
            response.put("privateDocuments", totalDocuments - publicDocuments);
            response.put("qrCode", qrCode);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private Map<String, Object> createPatientPublicProfile(User patient) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", patient.getId());
        profile.put("firstName", patient.getFirstName());
        profile.put("lastName", patient.getLastName());
        profile.put("fullName", patient.getFullName());
        profile.put("phoneNumber", patient.getPhoneNumber());
        profile.put("createdAt", patient.getCreatedAt());
        // Note: Email and other sensitive information are excluded
        return profile;
    }

    // Request DTOs
    public static class QRValidationRequest {
        private String qrCode;

        // Getters and setters
        public String getQrCode() { return qrCode; }
        public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    }
}