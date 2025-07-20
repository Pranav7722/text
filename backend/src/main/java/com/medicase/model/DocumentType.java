package com.medicase.model;

public enum DocumentType {
    LAB_REPORT("Lab Report"),
    PRESCRIPTION("Prescription"),
    X_RAY("X-Ray"),
    MRI_SCAN("MRI Scan"),
    CT_SCAN("CT Scan"),
    ULTRASOUND("Ultrasound"),
    ECG("ECG/EKG"),
    BLOOD_TEST("Blood Test"),
    DISCHARGE_SUMMARY("Discharge Summary"),
    MEDICAL_CERTIFICATE("Medical Certificate"),
    VACCINATION_RECORD("Vaccination Record"),
    ALLERGY_REPORT("Allergy Report"),
    SURGICAL_REPORT("Surgical Report"),
    CONSULTATION_NOTES("Consultation Notes"),
    OTHER("Other");
    
    private final String displayName;
    
    DocumentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}