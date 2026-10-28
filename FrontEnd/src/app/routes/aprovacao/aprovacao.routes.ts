import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

const permissaoLegada = ['ORCAMENTO_DESCONTO_APROVAR'];

export const routes: Routes = [
  {
    path: '',
    canActivate: [permissionGuard],
    data: { title: 'Central de Aprovações', permissions: permissaoLegada },
    loadComponent: () => import('./aprovacoes-central').then(m => m.AprovacoesCentral),
  },
  {
    path: 'registrar-decisao',
    canActivate: [permissionGuard],
    data: { title: 'Registrar Decisão Recebida', permissions: permissaoLegada },
    loadComponent: () => import('./registrar-decisao').then(m => m.RegistrarDecisao),
  },
];
