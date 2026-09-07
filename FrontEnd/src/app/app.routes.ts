import { Routes } from '@angular/router';
import { authGuard, permissionGuard, planGuard, subscriptionGuard } from '@core';
import { AdminLayout } from '@theme/admin-layout/admin-layout';
import { AuthLayout } from '@theme/auth-layout/auth-layout';
import { Error403 } from './routes/sessions/error-403';
import { Error404 } from './routes/sessions/error-404';
import { Error500 } from './routes/sessions/error-500';
import { Login } from './routes/sessions/login/login';
import { Recover } from './routes/sessions/recover/recover';
import { ResetPassword } from './routes/sessions/reset-password/reset-password';

const workspace = (
  title: string,
  description: string,
  permissions: string | string[],
  links: { title: string; description: string; route: string; icon: string; label?: string }[] = [],
  statusTitle?: string,
  statusDescription?: string
) => ({
  loadComponent: () => import('./routes/system/module-workspace').then(m => m.ModuleWorkspace),
  canActivate: [permissionGuard],
  data: { title, description, permissions, links, statusTitle, statusDescription },
});

export const routes: Routes = [
  {
    path: '',
    component: AdminLayout,
    canActivate: [authGuard, subscriptionGuard],
    canActivateChild: [authGuard, subscriptionGuard],
    children: [
      { path: '', redirectTo: 'home/gerencial', pathMatch: 'full' },
      { path: 'home', loadChildren: () => import('./routes/home/home.routes').then(m => m.routes) },
      { path: 'dashboard', redirectTo: 'home/gerencial', pathMatch: 'full' },
      { path: '403', component: Error403, data: { title: 'Acesso negado' } },
      { path: '404', component: Error404, data: { title: 'Página não encontrada' } },
      { path: '500', component: Error500, data: { title: 'Erro inesperado' } },

      {
        path: 'gestao-patio',
        loadComponent: () => import('./routes/patio/patio').then(m => m.PatioComponent),
        canActivate: [permissionGuard],
        data: { title: 'Gestão de Pátio', permissions: ['GERAL_USUARIO'] },
      },

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
        canActivate: [permissionGuard],
        data: { permissions: ['GERAL_USUARIO'], title: 'Veículos' },
        loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes),
      },

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

      {
        path: 'orcamentos',
        loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes),
      },
      {
        path: 'orcamento',
        loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes),
      },
      {
        path: 'ordens-servico',
        loadChildren: () => import('./routes/os/os.routes').then(m => m.routes),
      },
      { path: 'os', loadChildren: () => import('./routes/os/os.routes').then(m => m.routes) },
      {
        path: 'checklists-operacionais',
        ...workspace(
          'Checklists',
          'Prepare modelos e acompanhe checklists vinculados às ordens de serviço.',
          'OS_VIS_CHECKLIST',
          [
            {
              title: 'Modelos de checklist',
              description: 'Cadastre perguntas e organize os roteiros da oficina.',
              route: '/configuracoes/checklist',
              icon: 'clipboard-list',
            },
            {
              title: 'Ordens de serviço',
              description: 'Abra uma OS para executar e revisar seu checklist.',
              route: '/ordens-servico',
              icon: 'tool',
            },
          ],
          'Central de checklists aguardando read model',
          'A listagem transversal depende de uma consulta tenant-safe. Os checklists continuam acessíveis dentro de cada OS e os modelos permanecem disponíveis em Cadastros.'
        ),
      },
      {
        path: 'aprovacoes',
        ...workspace(
          'Aprovações',
          'Acompanhe decisões pendentes sem separar orçamento, cliente e veículo.',
          'ORCAMENTO_DESCONTO_APROVAR',
          [
            {
              title: 'Orçamentos',
              description: 'Revise valores, itens e situação de cada orçamento.',
              route: '/orcamentos',
              icon: 'file-invoice',
            },
            {
              title: 'Ordens de serviço',
              description: 'Acompanhe adicionais e autorizações vinculadas à execução.',
              route: '/ordens-servico',
              icon: 'clipboard-check',
            },
          ],
          'Fila unificada aguardando contrato de dados',
          'A central não estima pendências no navegador. Enquanto a API agregadora não estiver disponível, as decisões permanecem nos detalhes de orçamento e OS.'
        ),
      },
      {
        path: 'pecas-movimentacao',
        ...workspace(
          'Peças',
          'Consulte o estoque e acompanhe o consumo de peças no fluxo operacional.',
          'PS_LISTAR_PROD',
          [
            {
              title: 'Estoque',
              description: 'Consulte disponibilidade e cadastros de produtos.',
              route: '/operacional/estoque',
              icon: 'package',
            },
            {
              title: 'Ordens de serviço',
              description: 'Gerencie peças reservadas e consumidas em cada OS.',
              route: '/ordens-servico',
              icon: 'tool',
            },
          ],
          'Movimentações consolidadas aguardando integração',
          'Saldos e reservas não são recalculados no navegador. Consulte o estoque ou a OS de origem até a API de movimentações estar disponível.'
        ),
      },
      {
        path: 'faturamento-operacional',
        ...workspace(
          'Faturamento',
          'Continue da finalização operacional para cobrança, pagamento e emissão fiscal.',
          'GERAL_FATURAS',
          [
            {
              title: 'Ordens de serviço',
              description: 'Revise conclusão e pendências antes de faturar.',
              route: '/ordens-servico',
              icon: 'clipboard-check',
            },
            {
              title: 'Contas a receber',
              description: 'Acompanhe títulos gerados após a finalização.',
              route: '/financeiro/contas',
              icon: 'cash',
            },
            {
              title: 'Fiscal',
              description: 'Acesse documentos fiscais conforme o plano contratado.',
              route: '/fiscal',
              icon: 'receipt-tax',
            },
          ],
          'Fila de faturamento aguardando read model',
          'A visão unificada depende da conciliação entre OS, financeiro e fiscal. Nenhum valor é composto localmente.'
        ),
      },
      { path: 'pdv', loadChildren: () => import('./routes/pdv/pdv.routes').then(m => m.routes) },

      {
        path: 'financeiro',
        loadChildren: () => import('./routes/financeiro/financeiro.routes').then(m => m.routes),
      },
      {
        path: 'fiscal',
        canActivate: [planGuard],
        data: { minPlan: 3, title: 'Fiscal' },
        loadChildren: () => import('./routes/fiscal/fiscal.routes').then(m => m.routes),
      },
      {
        path: 'historico',
        ...workspace(
          'Histórico',
          'Consulte a jornada mantendo cliente, veículo e operação como contexto.',
          'GERAL_USUARIO',
          [
            {
              title: 'Clientes',
              description: 'Acesse vínculos, veículos e histórico individual.',
              route: '/clientes',
              icon: 'users',
            },
            {
              title: 'Veículos',
              description: 'Consulte revisões e atendimentos por veículo.',
              route: '/veiculos',
              icon: 'car',
            },
            {
              title: 'Ordens de serviço',
              description: 'Abra a linha do tempo auditável de uma OS.',
              route: '/ordens-servico',
              icon: 'history',
            },
          ],
          'Linha do tempo global aguardando read model',
          'Os históricos individuais seguem disponíveis nas entidades de origem. A consolidação global será liberada somente com paginação e isolamento por empresa no backend.'
        ),
      },
      {
        path: 'graficos',
        ...workspace(
          'Gráficos',
          'Analise indicadores confiáveis da oficina sem métricas fabricadas no frontend.',
          'REL_GRAFICOS',
          [
            {
              title: 'Dashboard gerencial',
              description: 'Acompanhe a visão geral disponível da oficina.',
              route: '/home/gerencial',
              icon: 'layout-dashboard',
            },
            {
              title: 'Relatórios',
              description: 'Acesse consultas detalhadas e exportações disponíveis.',
              route: '/relatorios',
              icon: 'report-analytics',
            },
          ],
          'Indicadores analíticos aguardando read models',
          'Gráficos só serão exibidos quando suas métricas, períodos e estados de ausência estiverem definidos pela API. Isso evita totais divergentes entre módulos.'
        ),
      },

      {
        path: 'agenda',
        loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },
      {
        path: 'agendamentos',
        loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },
      {
        path: 'agendamento',
        loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes),
      },
      {
        path: 'relatorios',
        loadChildren: () => import('./routes/relatorios/relatorios.routes').then(m => m.routes),
      },
      {
        path: 'suporte',
        ...workspace(
          'Suporte',
          'Encontre ajuda sem sair do contexto da sua operação.',
          'GERAL_USUARIO',
          [
            {
              title: 'Configurações',
              description: 'Revise dados da empresa e parâmetros do sistema.',
              route: '/configuracoes',
              icon: 'settings',
            },
          ],
          'Canal de atendimento em configuração',
          'O canal oficial de suporte ainda não foi conectado. Para evitar o envio de informações a um destino incorreto, nenhuma ação de contato é exibida neste momento.'
        ),
      },
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
  {
    path: 'aprovar-adicional/:token',
    loadComponent: () =>
      import('./routes/os/additional-requests/public-additional-approval').then(
        m => m.PublicAdditionalApproval
      ),
    data: { title: 'Aprovação de serviço adicional' },
  },
  { path: '**', redirectTo: 'home/gerencial' },
];
