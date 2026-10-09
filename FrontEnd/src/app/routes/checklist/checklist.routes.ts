import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
    path: '',
    canActivate: [permissionGuard],
    data: { title: 'Central de Inspeções', permissions: ['OS_VIS_CHECKLIST'] },
    loadComponent: () => import('./inspecoes-central').then(m => m.InspecoesCentral),
  },
  {
    path: 'nova',
    canActivate: [permissionGuard],
    data: { title: 'Nova Inspeção', permissions: ['OS_VIS_CHECKLIST'] },
    loadComponent: () => import('./nova-inspecao').then(m => m.NovaInspecao),
  },
];
