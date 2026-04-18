import { Routes } from '@angular/router';
import { OrdemServico } from './ordem-servico/ordem-servico';
import { CadastroOS } from './cadastro-os/cadastro-os';

export const routes: Routes = [
  { path: '', component: OrdemServico },
  { path: 'cadastro', component: CadastroOS },
  { path: 'cadastro/:id', component: CadastroOS },
  { path: 'visualizar-editar-os/:numero', loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS) },
  // Alias para compatibilidade com navegação "Visualizar OS"
  { path: 'visualizar-os/:numero', loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS) },
  // Rotas sem parâmetro para cliques que não enviam :numero
  { path: 'visualizar-editar-os', loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS) },
  { path: 'visualizar-os', loadComponent: () => import('./visualizar-os/visualizar-os').then(m => m.VisualizarOS) },
  // Redirecionamentos para cobrir variações de caminho
  { path: 'visualizar', redirectTo: 'visualizar-os', pathMatch: 'full' },
  { path: 'visualizar-editar', redirectTo: 'visualizar-os', pathMatch: 'full' },
];
