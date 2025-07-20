import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatToolbarModule],
  template: `
    <mat-toolbar color="primary">
      <span>🏥 MediCase - Admin Dashboard</span>
      <span class="spacer"></span>
      <button mat-icon-button (click)="logout()">
        <mat-icon>logout</mat-icon>
      </button>
    </mat-toolbar>
    
    <div class="dashboard-container">
      <h1>⚙️ Admin Dashboard</h1>
      <mat-card>
        <mat-card-content>
          <p>Welcome to the Admin Dashboard!</p>
          <p>Here you can manage users, monitor system activity, and view analytics.</p>
          <p><strong>Features coming soon:</strong></p>
          <ul>
            <li>User Management</li>
            <li>System Analytics</li>
            <li>Activity Monitoring</li>
            <li>System Configuration</li>
            <li>Reports & Statistics</li>
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
export class AdminDashboardComponent {
  constructor(private authService: AuthService) {}
  
  logout(): void {
    this.authService.logout();
  }
}