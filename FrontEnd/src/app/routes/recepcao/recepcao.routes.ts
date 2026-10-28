import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  { path: '', redirectTo: 'fila', pathMatch: 'full' },
  {
    path: 'fila',
    canActivate: [permissionGuard],
    data: { title: 'Fila de Recepção', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./recepcao-fila').then(m => m.RecepcaoFila),
  },
  {
    path: 'check-in',
    canActivate: [permissionGuard],
    data: { title: 'Novo Check-in', permissions: ['OS_INCLUIR'] },
    loadComponent: () => import('./recepcao-check-in').then(m => m.RecepcaoCheckIn),
  },
  {
    path: 'check-in/:agendamentoId',
    canActivate: [permissionGuard],
    data: { title: 'Check-in de Agendamento', permissions: ['OS_INCLUIR'] },
    loadComponent: () => import('./recepcao-check-in').then(m => m.RecepcaoCheckIn),
  },
];
