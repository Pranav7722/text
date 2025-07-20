export interface MedicalDocument {
  id?: string;
  patientId: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  fileSizeFormatted?: string;
  category?: string;
  description?: string;
  uploadedBy?: string;
  gridFSFileId?: string;
  isPublic?: boolean;
  uploadDate?: Date;
  updatedAt?: Date;
}

export interface DocumentUploadRequest {
  file: File;
  patientId?: string;
  category?: string;
  description?: string;
  isPublic?: boolean;
}

export interface DocumentUpdateRequest {
  category?: string;
  description?: string;
  isPublic?: boolean;
}

export interface DocumentResponse {
  documents: MedicalDocument[];
  totalCount: number;
  searchQuery?: string;
}

export interface DocumentStats {
  totalDocuments: number;
  publicDocuments: number;
  privateDocuments: number;
  patientName?: string;
}

export const DOCUMENT_CATEGORIES = [
  'General',
  'Lab Results',
  'X-Ray',
  'MRI/CT Scan',
  'Prescription',
  'Vaccination',
  'Surgery',
  'Consultation',
  'Insurance',
  'Other'
];

export const ALLOWED_FILE_TYPES = [
  'application/pdf',
  'image/jpeg',
  'image/jpg',
  'image/png',
  'image/gif',
  'application/msword',
  'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
];

export const MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB