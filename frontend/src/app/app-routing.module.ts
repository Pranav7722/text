import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

// Authentication components
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';

// Dashboard components
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PatientDashboardComponent } from './components/patient/patient-dashboard/patient-dashboard.component';
import { DoctorDashboardComponent } from './components/doctor/doctor-dashboard/doctor-dashboard.component';

// Patient components
import { PatientProfileComponent } from './components/patient/patient-profile/patient-profile.component';

// Document components
import { DocumentUploadComponent } from './components/documents/document-upload/document-upload.component';
import { DocumentListComponent } from './components/documents/document-list/document-list.component';

// QR components
import { QrCodeDisplayComponent } from './components/qr/qr-code-display/qr-code-display.component';
import { QrScannerComponent } from './components/qr/qr-scanner/qr-scanner.component';

const routes: Routes = [
  // Public routes
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  
  // QR code public access
  { path: 'patient/:qrCode', component: PatientProfileComponent },
  
  // Protected routes
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  
  // Patient routes
  {
    path: 'patient-dashboard',
    component: PatientDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['PATIENT'] }
  },
  {
    path: 'profile',
    component: PatientProfileComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['PATIENT'] }
  },
  {
    path: 'my-qr',
    component: QrCodeDisplayComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['PATIENT'] }
  },
  
  // Doctor routes
  {
    path: 'doctor-dashboard',
    component: DoctorDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['DOCTOR'] }
  },
  {
    path: 'scan-qr',
    component: QrScannerComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['DOCTOR', 'ADMIN'] }
  },
  
  // Document routes
  {
    path: 'documents',
    children: [
      {
        path: '',
        component: DocumentListComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'upload',
        component: DocumentUploadComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'patient/:patientId',
        component: DocumentListComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: { expectedRoles: ['DOCTOR', 'ADMIN'] }
      }
    ]
  },
  
  // Admin routes (future implementation)
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule),
    canActivate: [AuthGuard, RoleGuard],
    data: { expectedRoles: ['ADMIN'] }
  },
  
  // Fallback route
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: false, // Set to true for debugging
    scrollPositionRestoration: 'top'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }