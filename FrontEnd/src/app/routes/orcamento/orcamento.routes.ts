import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'orcamento', pathMatch: 'full' },
  { path: 'orcamento', loadComponent: () => import('./orcamento').then(m => m.OrcamentoComponent) },
  { path: 'cadastro-orcamento', loadComponent: () => import('./cadastro-orcamento').then(m => m.CadastroOrcamentoComponent) },
  { path: 'visualizar-editar-orcamento/:numero', loadComponent: () => import('./visualizar-editar-orcamento').then(m => m.VisualizarEditarOrcamentoComponent) },
];