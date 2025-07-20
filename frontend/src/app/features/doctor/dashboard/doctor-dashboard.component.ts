import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-doctor-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatToolbarModule],
  template: `
    <mat-toolbar color="primary">
      <span>🏥 MediCase - Doctor Dashboard</span>
      <span class="spacer"></span>
      <button mat-icon-button (click)="logout()">
        <mat-icon>logout</mat-icon>
      </button>
    </mat-toolbar>
    
    <div class="dashboard-container">
      <h1>👨‍⚕️ Doctor Dashboard</h1>
      <mat-card>
        <mat-card-content>
          <p>Welcome to the Doctor Dashboard!</p>
          <p>Here you can scan patient QR codes and view their medical documents.</p>
          <p><strong>Features coming soon:</strong></p>
          <ul>
            <li>QR Code Scanner</li>
            <li>Patient Document Viewer</li>
            <li>Add Medical Notes</li>
            <li>Patient Search</li>
          </ul>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .dashboard-container { padding: 20px; }
    h1 { color: #333; margin-bottom: 20px; }
  `]
})
export class DoctorDashboardComponent {
  constructor(private authService: AuthService) {}
  
  logout(): void {
    this.authService.logout();
  }
}