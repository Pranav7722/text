import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatToolbarModule],
  template: `
    <mat-toolbar color="primary">
      <span>🏥 MediCase - Patient Profile</span>
      <span class="spacer"></span>
      <button mat-icon-button (click)="logout()">
        <mat-icon>logout</mat-icon>
      </button>
    </mat-toolbar>
    
    <div class="profile-container">
      <h1>👤 Patient Profile</h1>
      <mat-card>
        <mat-card-content>
          <p>Patient profile and medical information will be displayed here.</p>
          <p><strong>Features coming soon:</strong></p>
          <ul>
            <li>Personal Information Management</li>
            <li>Medical History</li>
            <li>Emergency Contacts</li>
            <li>Allergies & Medical Conditions</li>
            <li>Insurance Information</li>
          </ul>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .profile-container { padding: 20px; }
    h1 { color: #333; margin-bottom: 20px; }
  `]
})
export class PatientProfileComponent {
  constructor(private authService: AuthService) {}
  
  logout(): void {
    this.authService.logout();
  }
}