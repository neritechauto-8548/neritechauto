import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  { path: '', canActivate: [permissionGuard], data: { title: 'Faturamento Operacional', permissions: ['GERAL_FATURAS'] }, loadComponent: () => import('./faturamento-fila').then(m => m.FaturamentoFila) },
  { path: 'preparar-cobranca', canActivate: [permissionGuard], data: { title: 'Preparar Cobrança', permissions: ['GERAL_FATURAS'] }, loadComponent: () => import('./preparar-cobranca').then(m => m.PrepararCobranca) },
];
