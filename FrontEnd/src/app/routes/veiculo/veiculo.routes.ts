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
    path: 'cadastro',
    component: CadastroVeiculo,
    canActivate: [permissionGuard],
    data: { permissions: 'VEICULO_CRIAR' },
  },
  {
    path: 'editar/:id',
    component: CadastroVeiculo,
    canActivate: [permissionGuard],
    data: { permissions: 'VEICULO_EDITAR' },
  },
];
