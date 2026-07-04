import { Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';

export const routes: Routes = [
  { path: 'dashboard', component: AdminDashboardComponent, data: { title: 'Admin Dashboard' } },
];
