import { User } from './user.model';

export enum DocumentType {
  LAB_REPORT = 'LAB_REPORT',
  PRESCRIPTION = 'PRESCRIPTION',
  X_RAY = 'X_RAY',
  MRI_SCAN = 'MRI_SCAN',
  CT_SCAN = 'CT_SCAN',
  ULTRASOUND = 'ULTRASOUND',
  ECG = 'ECG',
  BLOOD_TEST = 'BLOOD_TEST',
  DISCHARGE_SUMMARY = 'DISCHARGE_SUMMARY',
  MEDICAL_CERTIFICATE = 'MEDICAL_CERTIFICATE',
  VACCINATION_RECORD = 'VACCINATION_RECORD',
  ALLERGY_REPORT = 'ALLERGY_REPORT',
  SURGICAL_REPORT = 'SURGICAL_REPORT',
  CONSULTATION_NOTES = 'CONSULTATION_NOTES',
  OTHER = 'OTHER'
}

export interface MedicalDocument {
  id: string;
  title: string;
  description?: string;
  type: DocumentType;
  fileName: string;
  filePath: string;
  fileType: string;
  fileSize: number;
  patient: User;
  uploadedBy: User;
  hospitalName?: string;
  doctorName?: string;
  documentDate?: string;
  tags?: string[];
  notes?: string;
  isShared: boolean;
  sharedWithDoctors?: string[];
  createdAt: string;
  updatedAt: string;
}

export interface DocumentUploadRequest {
  file: File;
  title: string;
  description?: string;
  type: DocumentType;
  patientId: string;
  hospitalName?: string;
  doctorName?: string;
  documentDate?: string;
}

export interface PatientInfo {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth?: string;
  gender?: string;
  bloodGroup?: string;
  allergies?: string;
  emergencyContact?: string;
}