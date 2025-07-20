import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { PatientInfo } from '../models/document.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QrService {
  private readonly API_URL = environment.apiUrl + '/qr';

  constructor(private http: HttpClient) { }

  generateQRCode(): Observable<Blob> {
    return this.http.get(`${this.API_URL}/generate`, {
      responseType: 'blob'
    });
  }

  getQRCodeUrl(): Observable<{url: string, qrCodeId: string}> {
    return this.http.get<{url: string, qrCodeId: string}>(`${this.API_URL}/url`);
  }

  getPatientByQrCode(qrCodeId: string): Observable<PatientInfo> {
    return this.http.get<PatientInfo>(`${this.API_URL}/patient/${qrCodeId}`);
  }

  downloadQRCode(): Observable<Blob> {
    return this.generateQRCode();
  }
}