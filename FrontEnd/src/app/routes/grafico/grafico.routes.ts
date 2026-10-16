import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
export const routes: Routes = [{ path: '', canActivate: [permissionGuard], data: { title: 'Análises e Gráficos', permissions: ['REL_GRAFICOS'] }, loadComponent: () => import('./graficos-central').then(m => m.GraficosCentral) }];
