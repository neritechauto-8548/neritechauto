import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'pagar',
    loadComponent: () => import('./contas-pagar/contas-pagar').then(m => m.ContasPagarComponent),
  },
  {
    path: 'receber',
    loadComponent: () => import('./contas-receber/contas-receber').then(m => m.ContasReceberComponent),
  },
  {
    path: 'contas',
    loadComponent: () => import('./contas/contas').then(m => m.ContasComponent),
  },
  {
    path: 'caixa',
    loadComponent: () => import('./caixa/caixa').then(m => m.CaixaComponent),
  },
  {
    path: 'caixas-fechado',
    loadComponent: () => import('./caixas-fechado/caixas-fechado').then(m => m.CaixasFechadoComponent),
  },
  {
    path: 'nfe',
    loadComponent: () => import('./nfe/nfe').then(m => m.NfeComponent),
  },
  {
    path: 'transferencia',
    loadComponent: () => import('./transferencia/transferencia').then(m => m.TransferenciaComponent),
  },
  {
    path: 'notas-compra',
    loadComponent: () => import('./notas-compra/notas-compra').then(m => m.NotasCompraComponent),
  },
  {
    path: 'comissoes',
    loadComponent: () => import('./comissoes/comissoes').then(m => m.ComissoesComponent),
  },
  { path: '', redirectTo: 'pagar', pathMatch: 'full' },
];
