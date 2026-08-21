import { Routes } from '@angular/router';
import { authGuard, permissionGuard, subscriptionGuard } from '@core';
import { AdminLayout } from '@theme/admin-layout/admin-layout';
import { AuthLayout } from '@theme/auth-layout/auth-layout';
import { Dashboard } from './routes/dashboard/dashboard';
import { Error403 } from './routes/sessions/error-403';
import { Error404 } from './routes/sessions/error-404';
import { Error500 } from './routes/sessions/error-500';
import { Login } from './routes/sessions/login/login';
import { Recover } from './routes/sessions/recover/recover';
import { ResetPassword } from './routes/sessions/reset-password/reset-password';

const placeholder = (title: string, description: string) => ({
  loadComponent: () =>
    import('./routes/system/module-placeholder').then(m => m.ModulePlaceholder),
  data: { title, description },
});

export const routes: Routes = [
  {
    path: '',
    component: AdminLayout,
    canActivate: [authGuard, subscriptionGuard],
    canActivateChild: [authGuard, subscriptionGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: Dashboard, data: { title: 'Home' } },

      // Aliases mantidos durante a reorganização para não quebrar links internos legados.
      { path: 'dashboard', redirectTo: 'home', pathMatch: 'full' },

      { path: '403', component: Error403, data: { title: 'Acesso negado' } },
      { path: '404', component: Error404, data: { title: 'Página não encontrada' } },
      { path: '500', component: Error500, data: { title: 'Erro inesperado' } },

      // 01. Gestão de Pátio — raiz prevista no UI Master; implementação funcional ainda pendente.
      {
        path: 'gestao-patio',
        ...placeholder(
          'Gestão de Pátio',
          'A experiência de pátio será conectada à aplicação interna sem alterar a árvore oficial de navegação.'
        ),
      },

      // 02. Home
      // /home definido acima.

      // 03. Clientes
      {
        path: 'clientes',
        canActivate: [permissionGuard],
        data: { permissions: ['GERAL_USUARIO'], title: 'Clientes' },
        loadChildren: () => import('./routes/cliente/cliente.routes').then(m => m.routes),
      },
      {
        path: 'cliente',
        canActivate: [permissionGuard],
        data: { permissions: ['GERAL_USUARIO'], title: 'Clientes' },
        loadChildren: () => import('./routes/cliente/cliente.routes').then(m => m.routes),
      },
      {
        path: 'veiculos',
        canActivate: [permissionGuard],
        data: { permissions: ['GERAL_USUARIO'], title: 'Veículos' },
        loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes),
      },
      {
        path: 'veiculo',
        loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes),
      },

      // 04. Operacional
      {
        path: 'operacional',
        loadChildren: () =>
          import('./routes/produtos-servicos/produtos-servicos.routes').then(m => m.routes),
      },
      {
        path: 'produtos-servicos',
        loadChildren: () =>
          import('./routes/produtos-servicos/produtos-servicos.routes').then(m => m.routes),
      },

      // 05. Cadastros
      {
        path: 'cadastros',
        loadChildren: () =>
          import('./routes/configuracoes/configuracoes.routes').then(m => m.routes),
      },
      {
        path: 'configuracoes',
        loadChildren: () =>
          import('./routes/configuracoes/configuracoes.routes').then(m => m.routes),
      },
      {
        path: 'fornecedor',
        loadChildren: () => import('./routes/fornecedor/fornecedor.routes').then(m => m.routes),
      },

      // 06. Movimentação
      {
        path: 'orcamento',
        loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes),
      },
      {
        path: 'os',
        loadChildren: () => import('./routes/os/os.routes').then(m => m.routes),
      },
      {
        path: 'checklists-operacionais',
        ...placeholder(
          'Checklists',
          'Checklists operacionais serão implementados conforme as especificações próprias do fluxo de atendimento e OS.'
        ),
      },
      {
        path: 'aprovacoes',
        ...placeholder(
          'Aprovações',
          'A central de aprovações permanecerá separada de cadastros e será ligada aos fluxos de orçamento e OS.'
        ),
      },
      {
        path: 'pecas-movimentacao',
        ...placeholder(
          'Peças',
          'A movimentação de peças será liberada quando o fluxo operacional e o estoque estiverem reconciliados.'
        ),
      },
      {
        path: 'faturamento-operacional',
        ...placeholder(
          'Faturamento',
          'O faturamento operacional será conectado à finalização da OS, financeiro e fiscal conforme a documentação.'
        ),
      },

      // PDV continua disponível no produto, mas não é promovido como raiz fora da árvore oficial.
      {
        path: 'pdv',
        loadChildren: () => import('./routes/pdv/pdv.routes').then(m => m.routes),
      },

      // 07. Financeiro
      {
        path: 'financeiro',
        loadChildren: () => import('./routes/financeiro/financeiro.routes').then(m => m.routes),
      },

      // 08. Fiscal
      {
        path: 'fiscal',
        loadChildren: () => import('./routes/fiscal/fiscal.routes').then(m => m.routes),
      },

      // 09. Histórico
      {
        path: 'historico',
        ...placeholder(
          'Histórico',
          'A visão histórica consolidará eventos de clientes, veículos e operações quando o domínio estiver implementado.'
        ),
      },

      // 10. Gráficos
      {
        path: 'graficos',
        ...placeholder(
          'Gráficos',
          'Os painéis analíticos serão adicionados após a consolidação dos indicadores e contratos de dados.'
        ),
      },

      // 11. Agendamentos
      {
        path: 'agendamentos',
        loadChildren: () =>
          import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },
      {
        path: 'agendamento',
        loadChildren: () =>
          import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },

      // 12. Relatórios
      {
        path: 'relatorios',
        loadChildren: () => import('./routes/relatorios/relatorios.routes').then(m => m.routes),
      },

      // Administração técnica permanece roteável, porém fora da raiz visual oficial.
      {
        path: 'admin',
        loadChildren: () => import('./routes/admin/admin.routes').then(m => m.routes),
      },
    ],
  },
  {
    path: 'auth',
    component: AuthLayout,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: Login, data: { title: 'Entrar' } },
      { path: 'recover', component: Recover, data: { title: 'Recuperar senha' } },
      { path: 'reset-password', component: ResetPassword, data: { title: 'Redefinir senha' } },
    ],
  },
  { path: '**', redirectTo: 'home' },
];
