import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { Veiculo } from './veiculo/veiculo';
import { CadastroVeiculo } from './cadastro-veiculo/cadastro-veiculo';

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
    data: { title: 'Novo Veículo', permissions: ['VEICULO_CRIAR'] },
  },
  { path: 'cadastro', redirectTo: 'novo', pathMatch: 'full' },
  {
    path: ':id/editar',
    component: CadastroVeiculo,
    canActivate: [permissionGuard],
    data: { title: 'Editar Veículo', permissions: ['VEICULO_EDITAR'] },
  },
  { path: 'editar/:id', redirectTo: ':id/editar', pathMatch: 'full' },
];
