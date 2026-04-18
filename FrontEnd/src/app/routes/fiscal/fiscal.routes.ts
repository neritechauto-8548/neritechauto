import { Routes } from '@angular/router';
import { NfeLista } from './nfe-lista/nfe-lista';

export const routes: Routes = [
  {
    path: 'nfe',
    children: [
      { path: '', redirectTo: 'nfe-lista', pathMatch: 'full' },
      { path: 'nfe-lista', component: NfeLista },
      { path: 'inutilizar-numeracao', loadComponent: () => import('./inutilizar-numeracao/inutilizar-numeracao').then(m => m.InutilizarNumeracao) },
      { path: 'carta-correcao', loadComponent: () => import('./carta-correcao/carta-correcao').then(m => m.CartaCorrecao) },
      { path: 'entrada', loadComponent: () => import('./entrada/entrada').then(m => m.EntradaNfe) },
      { path: 'devolucao', loadComponent: () => import('./devolucao/devolucao').then(m => m.DevolucaoNfe) },
    ],
  },
  {
    path: 'nfce',
    children: [
      { path: '', redirectTo: 'nota-nfce', pathMatch: 'full' },
      { path: 'nota-nfce', loadComponent: () => import('./nfce-nota/nfce-nota').then(m => m.NotaNfce) },
      { path: 'inutilizar-numeracao', loadComponent: () => import('./nfce-inutilizar/nfce-inutilizar').then(m => m.NfceInutilizarNumeracao) },
    ],
  },
  {
    path: 'nfse',
    children: [
      { path: '', redirectTo: 'nfse-lista', pathMatch: 'full' },
      { path: 'nfse-lista', loadComponent: () => import('./nfse-lista/nfse-lista').then(m => m.NfseListaComponent) },
    ],
  },
  {
    path: 'configuracao-nota-fiscal',
    loadComponent: () => import('./nf-config/nf-config').then(m => m.NfConfigComponent),
  },
];
