import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';
import { Role } from './core/models/user.model';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.authRoutes)
  },
  {
    path: 'patient',
    loadChildren: () => import('./features/patient/patient.routes').then(m => m.patientRoutes),
    canActivate: [authGuard, roleGuard([Role.PATIENT])]
  },
  {
    path: 'doctor',
    loadChildren: () => import('./features/doctor/doctor.routes').then(m => m.doctorRoutes),
    canActivate: [authGuard, roleGuard([Role.DOCTOR])]
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.routes').then(m => m.adminRoutes),
    canActivate: [authGuard, roleGuard([Role.ADMIN])]
  },
  {
    path: 'qr/:qrCodeId',
    loadChildren: () => import('./features/patient/patient.routes').then(m => m.publicPatientRoutes)
  },
  {
    path: '**',
    redirectTo: '/auth/login'
  }
];
