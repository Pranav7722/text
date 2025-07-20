export enum Role {
  PATIENT = 'PATIENT',
  DOCTOR = 'DOCTOR',
  ADMIN = 'ADMIN'
}

export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth?: string;
  gender?: string;
  address?: string;
  role: Role;
  enabled: boolean;
  
  // Doctor specific fields
  licenseNumber?: string;
  specialization?: string;
  hospitalAffiliation?: string;
  
  // Patient specific fields
  emergencyContact?: string;
  bloodGroup?: string;
  allergies?: string;
  qrCodeId?: string;
  
  createdAt?: string;
  updatedAt?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  qrCodeId?: string;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface UserRegistrationRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber: string;
  dateOfBirth?: string;
  gender?: string;
  address?: string;
  role: Role;
  
  // Doctor specific fields
  licenseNumber?: string;
  specialization?: string;
  hospitalAffiliation?: string;
  
  // Patient specific fields
  emergencyContact?: string;
  bloodGroup?: string;
  allergies?: string;
}