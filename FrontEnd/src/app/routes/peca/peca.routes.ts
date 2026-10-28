import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  { path: '', canActivate: [permissionGuard], data: { title: 'Peças, Reservas e Consumo', permissions: ['PS_LISTAR_PROD'] }, loadComponent: () => import('./pecas-central').then(m => m.PecasCentral) },
  { path: 'preparar-reserva', canActivate: [permissionGuard], data: { title: 'Preparar Reserva', permissions: ['PS_LISTAR_PROD'] }, loadComponent: () => import('./preparar-reserva').then(m => m.PrepararReserva) },
];
