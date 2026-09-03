import { Routes } from '@angular/router';
import { Dashboard } from '../dashboard/dashboard';

const pendingDashboard = (title: string, description: string) => ({
  loadComponent: () => import('../system/module-placeholder').then(m => m.ModulePlaceholder),
  data: { title, description },
});

export const routes: Routes = [
  { path: '', redirectTo: 'gerencial', pathMatch: 'full' },
  {
    path: 'gerencial',
    component: Dashboard,
    data: { title: 'Dashboard Gerencial' },
  },
  {
    path: 'financeiro',
    ...pendingDashboard(
      'Dashboard Financeiro',
      'A rota oficial está preparada. O conteúdo será conectado ao read model financeiro documentado, sem compor saldos no frontend.'
    ),
  },
  {
    path: 'orcamentos',
    ...pendingDashboard(
      'Dashboard de Orçamentos',
      'A rota oficial está preparada. O conteúdo será conectado ao read model de orçamentos, conversão, aging e follow-up documentado.'
    ),
  },
  {
    path: 'operacional',
    ...pendingDashboard(
      'Dashboard Operacional',
      'A rota oficial está preparada. O conteúdo será conectado aos read models de OS, agenda, recepção e estoque documentados.'
    ),
  },
];
