import { ChangeDetectionStrategy, Component } from '@angular/core';

type ColunaPatio = { titulo: string; cor: string; itens: { ordem: string; cliente: string; veiculo: string; placa: string; tempo: string }[] };

@Component({
  standalone: true,
  selector: 'app-patio',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="patio-page">
      <header class="page-heading">
        <div><span class="eyebrow">Operacional</span><h1>Gestão de pátio</h1><p>Acompanhe cada veículo e a próxima ação da equipe.</p></div>
        <button class="primary" type="button">+ Registrar entrada</button>
      </header>
      <div class="toolbar"><input aria-label="Buscar no pátio" placeholder="Buscar por OS, cliente, veículo ou placa" /><button type="button">Filtros</button><button type="button">Atualizar</button></div>
      <div class="board" aria-label="Quadro do pátio">
        @for (coluna of colunas; track coluna.titulo) {
          <section class="column"><div class="column-title"><span class="dot" [style.background]="coluna.cor"></span><strong>{{ coluna.titulo }}</strong><span class="count">{{ coluna.itens.length }}</span></div>
            @for (item of coluna.itens; track item.ordem) {<article class="vehicle-card"><div class="card-top"><strong>{{ item.ordem }}</strong><span>{{ item.tempo }}</span></div><h2>{{ item.veiculo }}</h2><p>{{ item.cliente }} · {{ item.placa }}</p><button type="button">Abrir atendimento</button></article>}
            @if (!coluna.itens.length) {<div class="empty">Nenhum veículo nesta etapa</div>}
          </section>
        }
      </div>
    </section>
  `,
  styles: [`
    :host{display:block}.patio-page{padding:24px;max-width:1800px;margin:auto;color:#172033}.page-heading{display:flex;justify-content:space-between;align-items:flex-end;gap:24px;margin-bottom:24px}.eyebrow{color:#2563eb;font-size:12px;font-weight:700;text-transform:uppercase;letter-spacing:.08em}.page-heading h1{margin:5px 0;font-size:28px;letter-spacing:-.03em}.page-heading p{margin:0;color:#667085}.primary,.toolbar button{border:1px solid #dbe3ef;border-radius:8px;background:white;padding:10px 14px;font-weight:600;color:#344054}.primary{background:#2563eb;color:white;border-color:#2563eb}.toolbar{display:flex;gap:10px;margin-bottom:18px}.toolbar input{flex:1;border:1px solid #dbe3ef;border-radius:8px;padding:11px 14px;font:inherit}.board{display:grid;grid-template-columns:repeat(5,minmax(220px,1fr));gap:14px;overflow:auto}.column{background:#f8fafc;border:1px solid #e6ebf2;border-radius:12px;padding:12px;min-height:300px}.column-title{display:flex;align-items:center;gap:8px;margin-bottom:12px;font-size:13px}.dot{width:8px;height:8px;border-radius:50%}.count{margin-left:auto;background:#e8eef7;border-radius:999px;padding:2px 8px;font-size:11px}.vehicle-card{background:white;border:1px solid #e5eaf1;border-radius:10px;padding:13px;margin-bottom:10px;box-shadow:0 1px 2px #1018280d}.card-top{display:flex;justify-content:space-between;color:#667085;font-size:12px}.vehicle-card h2{font-size:14px;margin:10px 0 4px}.vehicle-card p{font-size:12px;color:#667085;margin:0 0 12px}.vehicle-card button{border:0;background:none;color:#2563eb;font-size:12px;font-weight:700;padding:0}.empty{padding:24px 8px;text-align:center;color:#98a2b3;font-size:12px}@media(max-width:900px){.board{grid-template-columns:repeat(5,260px)}.page-heading{align-items:flex-start;flex-direction:column}}
  `],
})
export class PatioComponent {
  readonly colunas: ColunaPatio[] = [
    { titulo: 'Entrada', cor: '#64748b', itens: [{ ordem: 'OS-1048', cliente: 'Marcos Silva', veiculo: 'Honda Civic', placa: 'PGH-2A31', tempo: 'há 20 min' }] },
    { titulo: 'Diagnóstico', cor: '#8b5cf6', itens: [{ ordem: 'OS-1044', cliente: 'Ana Paula', veiculo: 'Fiat Argo', placa: 'RZF-8B12', tempo: 'há 1 h' }] },
    { titulo: 'Aguardando aprovação', cor: '#f59e0b', itens: [] },
    { titulo: 'Em execução', cor: '#2563eb', itens: [{ ordem: 'OS-1039', cliente: 'João Costa', veiculo: 'VW T-Cross', placa: 'KLM-4C52', tempo: 'há 2 h' }] },
    { titulo: 'Pronto', cor: '#16a34a', itens: [] },
  ];
}
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

const placeholder = (title: string, description: string) => ({
  loadComponent: () => import('./routes/system/module-placeholder').then(m => m.ModulePlaceholder),
  data: { title, description },
});

const protectedPlaceholder = (title: string, description: string, permissions: string | string[]) => ({
  loadComponent: () => import('./routes/system/module-placeholder').then(m => m.ModulePlaceholder),
  canActivate: [permissionGuard],
  data: { title, description, permissions },
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

      { path: 'gestao-patio', ...protectedPlaceholder('Gestão de Pátio', 'A experiência de pátio será conectada à aplicação interna sem alterar a árvore oficial de navegação.', 'GERAL_USUARIO') },

      { path: 'clientes', canActivate: [permissionGuard], data: { permissions: ['GERAL_USUARIO'], title: 'Clientes' }, loadChildren: () => import('./routes/cliente/cliente.routes').then(m => m.routes) },
      { path: 'cliente', canActivate: [permissionGuard], data: { permissions: ['GERAL_USUARIO'], title: 'Clientes' }, loadChildren: () => import('./routes/cliente/cliente.routes').then(m => m.routes) },
      { path: 'veiculos', canActivate: [permissionGuard], data: { permissions: ['GERAL_USUARIO'], title: 'Veículos' }, loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes) },
      { path: 'veiculo', canActivate: [permissionGuard], data: { permissions: ['GERAL_USUARIO'], title: 'Veículos' }, loadChildren: () => import('./routes/veiculo/veiculo.routes').then(m => m.routes) },

      { path: 'operacional', loadChildren: () => import('./routes/produtos-servicos/produtos-servicos.routes').then(m => m.routes) },
      { path: 'produtos-servicos', loadChildren: () => import('./routes/produtos-servicos/produtos-servicos.routes').then(m => m.routes) },

      { path: 'cadastros', loadChildren: () => import('./routes/configuracoes/configuracoes.routes').then(m => m.routes) },
      { path: 'configuracoes', loadChildren: () => import('./routes/configuracoes/configuracoes.routes').then(m => m.routes) },
      { path: 'fornecedor', loadChildren: () => import('./routes/fornecedor/fornecedor.routes').then(m => m.routes) },

      { path: 'orcamentos', loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes) },
      { path: 'orcamento', loadChildren: () => import('./routes/orcamento/orcamento.routes').then(m => m.routes) },
      { path: 'ordens-servico', loadChildren: () => import('./routes/os/os.routes').then(m => m.routes) },
      { path: 'os', loadChildren: () => import('./routes/os/os.routes').then(m => m.routes) },
      { path: 'checklists-operacionais', ...protectedPlaceholder('Checklists', 'Checklists operacionais serão implementados conforme as especificações próprias do fluxo de atendimento e OS.', 'OS_VIS_CHECKLIST') },
      { path: 'aprovacoes', ...protectedPlaceholder('Aprovações', 'A central de aprovações permanecerá separada de cadastros e será ligada aos fluxos de orçamento e OS.', 'ORCAMENTO_DESCONTO_APROVAR') },
      { path: 'pecas-movimentacao', ...protectedPlaceholder('Peças', 'A movimentação de peças será liberada quando o fluxo operacional e o estoque estiverem reconciliados.', 'PS_LISTAR_PROD') },
      { path: 'faturamento-operacional', ...protectedPlaceholder('Faturamento', 'O faturamento operacional será conectado à finalização da OS, financeiro e fiscal conforme a documentação.', 'GERAL_FATURAS') },
      { path: 'pdv', loadChildren: () => import('./routes/pdv/pdv.routes').then(m => m.routes) },

      { path: 'financeiro', loadChildren: () => import('./routes/financeiro/financeiro.routes').then(m => m.routes) },
      { path: 'fiscal', canActivate: [planGuard], data: { minPlan: 3, title: 'Fiscal' }, loadChildren: () => import('./routes/fiscal/fiscal.routes').then(m => m.routes) },
      { path: 'historico', ...protectedPlaceholder('Histórico', 'A visão histórica consolidará eventos de clientes, veículos e operações quando o domínio estiver implementado.', 'GERAL_USUARIO') },
      { path: 'graficos', ...protectedPlaceholder('Gráficos', 'Os painéis analíticos serão adicionados após a consolidação dos indicadores e contratos de dados.', 'REL_GRAFICOS') },

      { path: 'agenda', loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes) },
      { path: 'agendamentos', loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes) },
      { path: 'agendamento', loadChildren: () => import('./routes/agendamento/agendamento.routes').then(m => m.routes) },
      { path: 'relatorios', loadChildren: () => import('./routes/relatorios/relatorios.routes').then(m => m.routes) },
      { path: 'suporte', ...placeholder('Suporte', 'A central de suporte será conectada ao canal oficial definido para atendimento sem alterar a navegação principal.') },
      { path: 'admin', loadChildren: () => import('./routes/admin/admin.routes').then(m => m.routes) },
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
    loadComponent: () => import('./routes/os/additional-requests/public-additional-approval').then(m => m.PublicAdditionalApproval),
    data: { title: 'Aprovação de serviço adicional' },
  },
  { path: '**', redirectTo: 'home/gerencial' },
];
