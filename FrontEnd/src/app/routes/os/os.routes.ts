import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { OrdemServico } from './ordem-servico/ordem-servico';
import { CadastroOS } from './cadastro-os/cadastro-os';

const legacyCockpitRoute = {
  canActivate: [permissionGuard],
  data: { title: 'Cockpit da Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
  loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS),
};

const canonicalCockpitRoute = {
  canActivate: [permissionGuard],
  data: { title: 'Cockpit da Ordem de Serviço', permissions: ['GERAL_USUARIO'] },
  loadComponent: () => import('./visualizar-os/os-cockpit-shell').then(m => m.OsCockpitShell),
};

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

  // Aliases legados preservados para não quebrar links existentes durante o rebuild.
  { path: 'visualizar-editar-os/:numero', ...legacyCockpitRoute },
  { path: 'visualizar-os/:numero', ...legacyCockpitRoute },
  { path: 'visualizar-editar-os', ...legacyCockpitRoute },
  { path: 'visualizar-os', ...legacyCockpitRoute },
  { path: 'visualizar', redirectTo: 'visualizar-os', pathMatch: 'full' },
  { path: 'visualizar-editar', redirectTo: 'visualizar-os', pathMatch: 'full' },

  // Quando montado em /ordens-servico, forma a rota canônica D4 /ordens-servico/{id}.
  // O shell oficial consome GET /v1/ordens-servico/{id}/cockpit antes das seções de detalhe.
  // Fica por último para não capturar `cadastro` e aliases fixos.
  { path: ':id', ...canonicalCockpitRoute },
];
