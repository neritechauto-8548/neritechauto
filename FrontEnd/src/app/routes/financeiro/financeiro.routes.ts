import { Routes } from '@angular/router';
import { permissionGuard } from '@core';

export const routes: Routes = [
  {
    path: 'pagar',
    canActivate: [permissionGuard],
    data: { title: 'Contas a Pagar', permissions: ['FIN_LISTAR_CONTAS'] },
    loadComponent: () => import('./contas-pagar/contas-pagar').then(m => m.ContasPagarComponent),
  },
  {
    path: 'receber',
    canActivate: [permissionGuard],
    data: { title: 'Contas a Receber', permissions: ['FIN_LISTAR_CONTAS'] },
    loadComponent: () => import('./contas-receber/contas-receber').then(m => m.ContasReceberComponent),
  },
  {
    path: 'contas',
    canActivate: [permissionGuard],
    data: { title: 'Contas', permissions: ['FIN_LISTAR_CONTAS'] },
    loadComponent: () => import('./contas/contas').then(m => m.ContasComponent),
  },
  {
    path: 'caixa',
    canActivate: [permissionGuard],
    data: { title: 'Caixa', permissions: ['FIN_VIS_CAIXA'] },
    loadComponent: () => import('./caixa/caixa').then(m => m.CaixaComponent),
  },
  {
    path: 'caixas-fechado',
    canActivate: [permissionGuard],
    data: { title: 'Caixas Fechados', permissions: ['FIN_VIS_CAIXA'] },
    loadComponent: () => import('./caixas-fechado/caixas-fechado').then(m => m.CaixasFechadoComponent),
  },
  {
    path: 'nfe',
    canActivate: [permissionGuard],
    data: { title: 'Nota Fiscal de Compra', permissions: ['FIN_INC_NFE_COMPRA'] },
    loadComponent: () => import('./nfe/nfe').then(m => m.NfeComponent),
  },
  {
    path: 'transferencia',
    canActivate: [permissionGuard],
    data: { title: 'Transferência entre Contas', permissions: ['FIN_FAZER_TRANSF'] },
    loadComponent: () => import('./transferencia/transferencia').then(m => m.TransferenciaComponent),
  },
  {
    path: 'notas-compra',
    canActivate: [permissionGuard],
    data: { title: 'Notas de Compra', permissions: ['FIN_INC_NFE_COMPRA'] },
    loadComponent: () => import('./notas-compra/notas-compra').then(m => m.NotasCompraComponent),
  },
  {
    path: 'comissoes',
    canActivate: [permissionGuard],
    data: { title: 'Comissões', permissions: ['FIN_VIS_REL_COMISSOES'] },
    loadComponent: () => import('./comissoes/comissoes').then(m => m.ComissoesComponent),
  },
  { path: '', redirectTo: 'pagar', pathMatch: 'full' },
];
