import { Routes } from '@angular/router';
import { PatientDashboardComponent } from './dashboard/patient-dashboard.component';
import { PatientProfileComponent } from './profile/patient-profile.component';

export const patientRoutes: Routes = [
  {
    path: 'dashboard',
    component: PatientDashboardComponent
  },
  {
    path: 'profile',
    component: PatientProfileComponent
  },
  {
    path: 'documents',
    loadChildren: () => import('./documents/documents.routes').then(m => m.documentsRoutes)
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  }
];

export const publicPatientRoutes: Routes = [
  {
    path: '',
    component: PatientProfileComponent
  }
];