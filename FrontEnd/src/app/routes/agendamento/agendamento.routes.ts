import { Routes } from '@angular/router';
import { permissionGuard } from '@core';
import { AgendamentosAlertas } from './agendamentos-alertas/agendamentos-alertas';
import { CadastrarAgendamento } from './cadastrar-agendamento/cadastrar-agendamento';
import { CalendarioAgendamento } from './calendario-agendamento/calendario-agendamento';

export const routes: Routes = [
  { path: '', redirectTo: 'calendario', pathMatch: 'full' },
  {
    path: 'novo',
    component: CadastrarAgendamento,
    canActivate: [permissionGuard],
    data: { title: 'Novo Agendamento', permissions: ['GERAL_AGENDAMENTO_EDITAR'] },
  },
  {
    path: ':id/editar',
    component: CadastrarAgendamento,
    canActivate: [permissionGuard],
    data: { title: 'Editar Agendamento', permissions: ['GERAL_AGENDAMENTO_EDITAR'] },
  },
  { path: 'cadastro', redirectTo: 'novo', pathMatch: 'full' },
  { path: 'agendamentos-alertas', component: AgendamentosAlertas },
  { path: 'calendario', component: CalendarioAgendamento },
  {
    path: 'aniversario',
    loadComponent: () => import('../system/module-placeholder').then(m => m.ModulePlaceholder),
    data: {
      title: 'Aniversários',
      description:
        'A experiência será habilitada quando o read model de aniversariantes e o envio de comunicações estiverem reconciliados sem empresaId controlado pelo navegador.',
    },
  },
];
