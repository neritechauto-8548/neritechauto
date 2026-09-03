import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { CadastroCliente } from './cadastro-cliente/cadastro-cliente';
import { Cliente } from './cliente/cliente';
import { DetalheCliente } from './detalhe-cliente/detalhe-cliente';

export const routes: Routes = [
  {
    path: '',
    component: Cliente,
    canActivate: [permissionGuard],
    data: { title: 'Clientes', permissions: ['GERAL_USUARIO'] },
  },
  { path: 'listar', redirectTo: '', pathMatch: 'full' },
  {
    path: 'novo',
    component: CadastroCliente,
    canActivate: [permissionGuard],
    data: { title: 'Novo Cliente', permissions: ['CLIENTE_CRIAR'] },
  },
  { path: 'cadastro', redirectTo: 'novo', pathMatch: 'full' },
  {
    path: 'editar/:uuid',
    component: CadastroCliente,
    canActivate: [permissionGuard],
    data: { title: 'Editar Cliente', permissions: ['CLIENTE_EDITAR'], legacyRoute: true },
  },
  {
    path: ':uuid/editar',
    component: CadastroCliente,
    canActivate: [permissionGuard],
    data: { title: 'Editar Cliente', permissions: ['CLIENTE_EDITAR'] },
  },
  {
    path: ':uuid',
    component: DetalheCliente,
    canActivate: [permissionGuard],
    data: { title: 'Detalhe do Cliente', permissions: ['GERAL_USUARIO'] },
  },
];
