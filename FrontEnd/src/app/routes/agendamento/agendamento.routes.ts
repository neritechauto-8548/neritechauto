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
  {
    path: 'agendamentos-alertas',
    component: AgendamentosAlertas,
    canActivate: [permissionGuard],
    data: { title: 'Agenda e Alertas', permissions: ['GERAL_AGENDAMENTO_VISUALIZAR'] },
  },
  {
    path: 'calendario',
    component: CalendarioAgendamento,
    canActivate: [permissionGuard],
    data: { title: 'Calendário', permissions: ['GERAL_AGENDAMENTO_VISUALIZAR'] },
  },
  {
    path: 'aniversario',
    canActivate: [permissionGuard],
    data: {
      title: 'Aniversários',
      permissions: ['GERAL_USUARIO'],
      description:
        'Acompanhe aniversariantes e prepare comunicações com uso responsável dos dados do cliente.',
      eyebrow: 'Agendamentos',
      links: [
        {
          title: 'Clientes',
          description: 'Consulte dados e preferências de contato cadastradas.',
          route: '/clientes',
          icon: 'users',
        },
        {
          title: 'Relatório de aniversariantes',
          description: 'Acesse a consulta disponível no módulo de relatórios.',
          route: '/relatorios/relatorio-aniversariantes',
          icon: 'report-analytics',
        },
      ],
      statusTitle: 'Comunicações aguardando integração segura',
      statusDescription:
        'O envio em massa depende do read model tenant-safe, consentimento e canal oficial. Nenhuma mensagem é simulada ou disparada pelo navegador.',
    },
    loadComponent: () => import('../system/module-workspace').then(m => m.ModuleWorkspace),
  },
];
