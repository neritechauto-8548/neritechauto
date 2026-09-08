import { Routes } from '@angular/router';
import { Dashboard } from '../dashboard/dashboard';

const pendingDashboard = (
  title: string,
  description: string,
  links: { title: string; description: string; route: string; icon: string }[],
  statusDescription: string
) => ({
  loadComponent: () => import('../system/module-workspace').then(m => m.ModuleWorkspace),
  data: {
    title,
    description,
    eyebrow: 'Home',
    links,
    statusTitle: 'Indicadores aguardando read model',
    statusDescription,
  },
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
      'Acompanhe recebimentos, pagamentos e caixa a partir das fontes financeiras oficiais.',
      [
        {
          title: 'Contas a receber',
          description: 'Consulte títulos, vencimentos e baixas.',
          route: '/financeiro/contas',
          icon: 'cash',
        },
        {
          title: 'Contas a pagar',
          description: 'Acompanhe compromissos e pagamentos.',
          route: '/financeiro/contas-pagar',
          icon: 'receipt',
        },
        {
          title: 'Caixa',
          description: 'Acesse a operação diária do caixa.',
          route: '/financeiro/caixa',
          icon: 'arrows-exchange',
        },
      ],
      'Saldos e totais não são compostos no navegador. A visão será preenchida quando a API financeira agregada estiver disponível.'
    ),
  },
  {
    path: 'orcamentos',
    ...pendingDashboard(
      'Dashboard de Orçamentos',
      'Acompanhe propostas, aprovações e conversões sem perder o contexto do atendimento.',
      [
        {
          title: 'Orçamentos',
          description: 'Consulte propostas e seus estados atuais.',
          route: '/orcamentos',
          icon: 'file-invoice',
        },
        {
          title: 'Novo orçamento',
          description: 'Inicie uma proposta vinculada a cliente e veículo.',
          route: '/orcamentos/novo',
          icon: 'plus',
        },
        {
          title: 'Aprovações',
          description: 'Continue decisões dentro dos fluxos conectados.',
          route: '/aprovacoes',
          icon: 'clipboard-check',
        },
      ],
      'Conversão, aging e follow-up dependem do read model documentado. Até lá, use a listagem oficial sem indicadores estimados.'
    ),
  },
  {
    path: 'operacional',
    ...pendingDashboard(
      'Dashboard Operacional',
      'Coordene pátio, agenda, ordens de serviço e estoque no fluxo diário da oficina.',
      [
        {
          title: 'Gestão de pátio',
          description: 'Visualize a situação operacional dos veículos.',
          route: '/gestao-patio',
          icon: 'car',
        },
        {
          title: 'Ordens de serviço',
          description: 'Acompanhe execução, pendências e fechamento.',
          route: '/ordens-servico',
          icon: 'tool',
        },
        {
          title: 'Agenda',
          description: 'Organize recepção e compromissos.',
          route: '/agendamentos/calendario',
          icon: 'calendar',
        },
      ],
      'A consolidação depende dos read models de OS, agenda, recepção e estoque. Os fluxos individuais seguem acessíveis acima.'
    ),
  },
];
