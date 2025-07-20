import { Routes } from '@angular/router';
import { DoctorDashboardComponent } from './dashboard/doctor-dashboard.component';

export const doctorRoutes: Routes = [
  {
    path: 'dashboard',
    component: DoctorDashboardComponent
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  }
];