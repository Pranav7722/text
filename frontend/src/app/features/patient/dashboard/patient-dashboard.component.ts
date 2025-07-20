import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';
import { QrService } from '../../../core/services/qr.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-patient-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    RouterModule
  ],
  templateUrl: './patient-dashboard.component.html',
  styleUrl: './patient-dashboard.component.scss'
})
export class PatientDashboardComponent implements OnInit {
  currentUser: User | null = null;
  qrCodeUrl = '';

  constructor(
    private authService: AuthService,
    private qrService: QrService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser?.qrCodeId) {
      this.generateQRCodeUrl();
    }
  }

  private generateQRCodeUrl(): void {
    this.qrService.getQRCodeUrl().subscribe({
      next: (response) => {
        this.qrCodeUrl = response.url;
      },
      error: (error) => {
        console.error('Error generating QR code URL:', error);
      }
    });
  }

  downloadQRCode(): void {
    this.qrService.downloadQRCode().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'medicase-qr-code.png';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Error downloading QR code:', error);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}