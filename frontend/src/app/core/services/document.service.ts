import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MedicalDocument, DocumentUploadRequest, DocumentType } from '../models/document.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private readonly API_URL = environment.apiUrl + '/documents';

  constructor(private http: HttpClient) { }

  uploadDocument(request: DocumentUploadRequest): Observable<MedicalDocument> {
    const formData = new FormData();
    formData.append('file', request.file);
    formData.append('title', request.title);
    formData.append('type', request.type);
    formData.append('patientId', request.patientId);
    
    if (request.description) {
      formData.append('description', request.description);
    }
    if (request.hospitalName) {
      formData.append('hospitalName', request.hospitalName);
    }
    if (request.doctorName) {
      formData.append('doctorName', request.doctorName);
    }
    if (request.documentDate) {
      formData.append('documentDate', request.documentDate);
    }

    return this.http.post<MedicalDocument>(`${this.API_URL}/upload`, formData);
  }

  getPatientDocuments(patientId: string): Observable<MedicalDocument[]> {
    return this.http.get<MedicalDocument[]>(`${this.API_URL}/patient/${patientId}`);
  }

  getDocumentsByType(patientId: string, type: DocumentType): Observable<MedicalDocument[]> {
    return this.http.get<MedicalDocument[]>(`${this.API_URL}/patient/${patientId}/type/${type}`);
  }

  searchDocuments(patientId: string, keyword: string): Observable<MedicalDocument[]> {
    return this.http.get<MedicalDocument[]>(`${this.API_URL}/search`, {
      params: { patientId, keyword }
    });
  }

  getDocument(documentId: string): Observable<MedicalDocument> {
    return this.http.get<MedicalDocument>(`${this.API_URL}/${documentId}`);
  }

  downloadDocument(documentId: string): Observable<Blob> {
    return this.http.get(`${this.API_URL}/${documentId}/download`, {
      responseType: 'blob'
    });
  }

  updateDocument(documentId: string, updates: any): Observable<MedicalDocument> {
    const formData = new FormData();
    Object.keys(updates).forEach(key => {
      if (updates[key] !== null && updates[key] !== undefined) {
        formData.append(key, updates[key]);
      }
    });

    return this.http.put<MedicalDocument>(`${this.API_URL}/${documentId}`, formData);
  }

  toggleDocumentSharing(documentId: string): Observable<MedicalDocument> {
    return this.http.patch<MedicalDocument>(`${this.API_URL}/${documentId}/toggle-sharing`, {});
  }

  deleteDocument(documentId: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/${documentId}`);
  }

  getDocumentTypes(): Observable<DocumentType[]> {
    return this.http.get<DocumentType[]>(`${this.API_URL}/types`);
  }
}