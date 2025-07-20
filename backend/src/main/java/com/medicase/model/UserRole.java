package com.medicase.model;

public enum UserRole {
    PATIENT("Patient"),
    DOCTOR("Doctor"),
    ADMIN("Administrator");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}