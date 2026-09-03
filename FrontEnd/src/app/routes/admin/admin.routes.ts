import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';

export const routes: Routes = [
  {
    path: 'dashboard',
    component: AdminDashboardComponent,
    canActivate: [permissionGuard],
    data: { title: 'Admin Dashboard', permissions: ['GERAL_CONFIG_SISTEMA'] },
  },
];
