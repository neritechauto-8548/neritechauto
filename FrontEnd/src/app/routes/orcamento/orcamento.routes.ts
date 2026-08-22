import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
    path: '',
    canActivate: [permissionGuard],
    data: { title: 'Orçamentos', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./orcamento').then(m => m.OrcamentoComponent),
  },
  { path: 'orcamento', redirectTo: '', pathMatch: 'full' },
  {
    path: 'novo',
    canActivate: [permissionGuard],
    data: { title: 'Novo Orçamento', permissions: ['OS_INCLUIR'] },
    loadComponent: () => import('./cadastro-orcamento').then(m => m.CadastroOrcamentoComponent),
  },
  { path: 'cadastro-orcamento', redirectTo: 'novo', pathMatch: 'full' },
  { path: 'visualizar-editar-orcamento/:numero', loadComponent: () => import('./visualizar-editar-orcamento').then(m => m.VisualizarEditarOrcamentoComponent) },
  { path: 'visualizar-orcamento/:numero', loadComponent: () => import('../os/visualizar-os/visualizar-os').then(m => m.VisualizarOS) },
  { path: 'editar-orcamento/:id', loadComponent: () => import('../os/cadastro-os/cadastro-os').then(m => m.CadastroOS) },
  {
    path: ':id',
    canActivate: [permissionGuard],
    data: { title: 'Detalhe do Orçamento', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./detalhe-orcamento').then(m => m.DetalheOrcamentoComponent),
  },
];
