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

  // Compatibilidade temporária. Nenhuma rota legada pode contornar a mesma
  // autorização exigida pelas telas canônicas durante a migração.
  {
    path: 'visualizar-editar-orcamento/:numero',
    canActivate: [permissionGuard],
    data: { title: 'Editar Orçamento', permissions: ['OS_ALTERAR'] },
    loadComponent: () => import('./visualizar-editar-orcamento').then(m => m.VisualizarEditarOrcamentoComponent),
  },
  {
    path: 'visualizar-orcamento/:numero',
    canActivate: [permissionGuard],
    data: { title: 'Visualizar Orçamento', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('../os/visualizar-os/visualizar-os').then(m => m.VisualizarOS),
  },
  {
    path: 'editar-orcamento/:id',
    canActivate: [permissionGuard],
    data: { title: 'Editar Orçamento', permissions: ['OS_ALTERAR'] },
    loadComponent: () => import('../os/cadastro-os/cadastro-os').then(m => m.CadastroOS),
  },
  {
    path: ':id/itens',
    canActivate: [permissionGuard],
    data: { title: 'Itens do Orçamento', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./itens-orcamento').then(m => m.ItensOrcamentoComponent),
  },
  {
    path: ':id',
    canActivate: [permissionGuard],
    data: { title: 'Detalhe do Orçamento', permissions: ['GERAL_USUARIO'] },
    loadComponent: () => import('./detalhe-orcamento').then(m => m.DetalheOrcamentoComponent),
  },
];
