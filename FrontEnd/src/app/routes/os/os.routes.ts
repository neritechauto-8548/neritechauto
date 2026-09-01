import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { OrdemServico } from './ordem-servico/ordem-servico';
import { CadastroOS } from './cadastro-os/cadastro-os';

export const routes: Routes = [
  {
    path: '',
    component: OrdemServico,
    canActivate: [permissionGuard],
    data: { title: 'Ordens de Serviço', permissions: ['GERAL_USUARIO'] },
  },
  {
    path: 'cadastro',
    component: CadastroOS,
    canActivate: [permissionGuard],
    data: { title: 'Nova Ordem de Serviço', permissions: ['OS_INCLUIR'] },
  },
  {
    path: 'cadastro/:id',
    component: CadastroOS,
    canActivate: [permissionGuard],
    data: { title: 'Editar Ordem de Serviço', permissions: ['OS_EDITAR'] },
  },
  {
    path: 'visualizar-editar-os/:numero',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS),
  },
  {
    path: 'visualizar-os/:numero',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS),
  },
  {
    path: 'visualizar-editar-os',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS),
  },
  {
    path: 'visualizar-os',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS),
  },
  { path: 'visualizar', redirectTo: 'visualizar-os', pathMatch: 'full' },
  { path: 'visualizar-editar', redirectTo: 'visualizar-os', pathMatch: 'full' },
];
