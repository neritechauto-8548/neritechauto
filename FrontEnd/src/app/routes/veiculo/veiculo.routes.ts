import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { Veiculo } from './veiculo/veiculo';
import {
  CadastroVeiculo,
  pendingVehicleChangesGuard,
} from './cadastro-veiculo/cadastro-veiculo';

export const routes: Routes = [
  {
    path: '',
    component: Veiculo,
    canActivate: [permissionGuard],
    data: { permissions: 'GERAL_USUARIO' },
  },
  {
    path: 'novo',
    component: CadastroVeiculo,
    canActivate: [permissionGuard],
    canDeactivate: [pendingVehicleChangesGuard],
    data: { title: 'Novo Veículo', permissions: ['VEICULO_CRIAR'] },
  },
  { path: 'cadastro', redirectTo: 'novo', pathMatch: 'full' },
  {
    path: ':id/editar',
    component: CadastroVeiculo,
    canActivate: [permissionGuard],
    canDeactivate: [pendingVehicleChangesGuard],
    data: { title: 'Editar Veículo', permissions: ['VEICULO_EDITAR'] },
  },
  { path: 'editar/:id', redirectTo: ':id/editar', pathMatch: 'full' },
  {
    path: ':id/vinculos',
    loadComponent: () => import('./vinculos-veiculo/vinculos-veiculo').then(m => m.VinculosVeiculo),
    canActivate: [permissionGuard],
    data: { title: 'Vínculos do Veículo', permissions: ['GERAL_USUARIO'] },
  },
  {
    path: ':id/revisoes',
    loadComponent: () => import('./revisoes-veiculo/revisoes-veiculo').then(m => m.RevisoesVeiculo),
    canActivate: [permissionGuard],
    data: { title: 'Próximas Revisões', permissions: ['GERAL_USUARIO'] },
  },
  {
    path: ':id',
    loadComponent: () => import('./detalhe-veiculo/detalhe-veiculo').then(m => m.DetalheVeiculo),
    canActivate: [permissionGuard],
    data: { title: 'Passaporte do Veículo', permissions: ['GERAL_USUARIO'] },
  },
];
