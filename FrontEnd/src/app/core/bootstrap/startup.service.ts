import { Injectable, inject } from '@angular/core';
import { AuthService, User } from '@core/authentication';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { catchError, of, switchMap, tap } from 'rxjs';
import { Menu, MenuChildrenItem, MenuService } from './menu.service';
import { environment } from '@env/environment';

@Injectable({ providedIn: 'root' })
export class StartupService {
  private readonly authService = inject(AuthService);
  private readonly menuService = inject(MenuService);
  private readonly permissonsService = inject(NgxPermissionsService);
  private readonly rolesService = inject(NgxRolesService);

  load() {
    return new Promise<void>((resolve) => {
      this.authService.change().pipe(
        tap((user: any) => {
          this.setPermissions(user);
          if (user && user.assinaturaAtiva === false && user.stripeUrl && !environment.uxPreview) {
            window.location.href = user.stripeUrl;
          }
        }),
        switchMap((user: User) => {
          // O mesmo menu.json alimenta produção e UX Preview. A prévia carrega
          // o recurso estático sem token; nunca solicita dados ao backend.
          return this.authService.menu().pipe(
            catchError(() => of([] as Menu[])),
            tap((menu: Menu[]) => {
              const resolved = Array.isArray(menu) ? menu : [];
              this.setMenu(
                environment.uxPreview && resolved.length === 0 ? this.obterMenuPadrao() : resolved,
                environment.uxPreview ? ({ planoNivel: 99 } as User) : user
              );
            })
          );
        })
      ).subscribe({
        next: () => resolve(),
        error: () => {
          if (environment.uxPreview) {
            this.setMenu(this.obterMenuPadrao(), { planoNivel: 99 } as User);
          }
          resolve();
        },
      });
    });
  }

  private setMenu(menu: Menu[], user: User) {
    const planLevel = user?.planoNivel || 1;
    const filteredMenu = this.filterMenuByPlan(this.filterMenuByPermissions(menu), planLevel);
    this.menuService.addNamespace(filteredMenu, 'menu');
    this.menuService.set(filteredMenu);
  }

  /** Menu visibility is UX only: backend permissions remain authoritative. */
  private filterMenuByPermissions<T extends Menu | MenuChildrenItem>(menu: T[]): T[] {
    const hasAny = (value?: string | string[]) => {
      if (!value) return false;
      const names = Array.isArray(value) ? value : [value];
      return names.some(name => !!this.permissonsService.getPermission(name));
    };

    return menu.filter(item => {
      if (item.permissions?.only && !hasAny(item.permissions.only)) return false;
      if (item.permissions?.except && hasAny(item.permissions.except)) return false;
      if (item.children?.length) {
        item.children = this.filterMenuByPermissions(item.children);
        if (item.type === 'sub' && item.children.length === 0) return false;
      }
      return true;
    });
  }

  private filterMenuByPlan(menu: any[], planLevel: number): any[] {
    return menu.filter(item => {
      if (item.minPlan && item.minPlan > planLevel) return false;
      if (item.children?.length) {
        item.children = this.filterMenuByPlan(item.children, planLevel);
        if (item.type === 'sub' && item.children.length === 0) return false;
      }
      return true;
    });
  }

  private setPermissions(user: User) {
    let permissions = user?.permissions || [];
    const hasAdmin = user?.funcoes?.some((role: string) => {
      const r = (role || '').toUpperCase();
      return r === 'ADMIN' || r === 'ADMINISTRADOR' || r === 'SUPER_ADMIN';
    });
    if (hasAdmin || environment.uxPreview) {
      permissions = Array.from(new Set([
        ...permissions,
        'ADMIN',
        'CLIENTE_CRIAR', 'CLIENTE_EDITAR', 'CLIENTE_EXCLUIR', 'CLIENTE_EXPORTAR',
        'VEICULO_CRIAR', 'VEICULO_EDITAR', 'VEICULO_EXCLUIR', 'VEICULO_EXPORTAR',
        'AGENDAMENTO_CRIAR', 'AGENDAMENTO_EDITAR', 'AGENDAMENTO_EXCLUIR',
        'OS_INCLUIR', 'OS_EDITAR', 'OS_EXCLUIR', 'OS_ALT_FUNCIONARIO', 'OS_ALT_STATUS',
        'GERAL_USUARIO', 'GERAL_CALENDARIO', 'GERAL_AGENDAMENTO_VISUALIZAR', 'GERAL_FATURAS',
        'GERAL_CONFIG_SISTEMA', 'GERAL_MEU_CALENDARIO', 'GERAL_CONFIG_CHECKLIST', 'GERAL_ORCAMENTO',
        'GERAL_AGENDAMENTO_EDITAR', 'GERAL_CONFIG_SITE', 'FIN_VIS_CAIXA', 'FIN_FECHAMENTO'
      ]));
    }
    this.permissonsService.loadPermissions(permissions);
    this.rolesService.flushRoles();
    user?.funcoes?.forEach((role: string) => this.rolesService.addRole(role, permissions));
    if (environment.uxPreview) this.rolesService.addRole('ADMIN', permissions);
  }

  /** Menu padrão usado apenas quando a configuração estática de navegação falha. */
  obterMenuPadrao(): Menu[] {
    const menu: Menu[] = [
      { route: 'home', name: 'Visão geral', type: 'link', icon: 'dashboard' },
      { route: 'patio', name: 'Gestão de pátio', type: 'link', icon: 'patio', permissions: { only: ['GERAL_USUARIO', 'ADMIN'] } },
      { route: 'agendamento/calendario', name: 'Agendamentos', type: 'link', icon: 'calendar', disabled: true, label: { color: 'em-breve', value: 'Em breve' } },
      { route: '', name: 'Área do cliente', type: 'sub', icon: 'clientes', children: [
        { route: 'cliente/solicitacoes', name: 'Solicitações', type: 'link', icon: 'calendar', disabled: true },
      ] },
      { route: '', name: 'Orçamentos & Serviços', type: 'sub', icon: 'oficina', children: [
        { route: 'configuracoes/checklist', name: 'Checklist', type: 'link', icon: 'clipboard' },
        { route: 'diagnostico', name: 'Diagnósticos', type: 'link', icon: 'pulse', disabled: true },
        { route: 'orcamento/orcamento', name: 'Orçamentos', type: 'link', icon: 'document' },
        { route: 'os', name: 'Ordens de serviço', type: 'link', icon: 'oficina' },
      ] },
      { route: '', name: 'Clientes', type: 'sub', icon: 'clientes', children: [
        { route: 'cliente/listar', name: 'Gerenciar clientes', type: 'link', icon: 'clientes' },
        { route: 'cliente/historico-veiculos', name: 'Histórico de veículos', type: 'link', icon: 'car' },
      ] },
      { route: 'configuracoes/colaboradores', name: 'Gestão de pessoas', type: 'link', icon: 'users' },
      { route: '', name: 'Gestão de recursos', type: 'sub', icon: 'settings', children: [
        { route: 'produtos-servicos/recursos', name: 'Cadastros', type: 'link', icon: 'clipboard' },
        { route: 'configuracoes/seguradoras', name: 'Seguradoras', type: 'link', icon: 'recursos' },
        { route: 'fornecedor/lista', name: 'Fornecedores', type: 'link', icon: 'recursos', permissions: { only: ['PS_LISTAR_FORN', 'FORNECEDOR_VISUALIZAR', 'GERAL_CONFIG_SISTEMA', 'ADMIN'] } },
        { route: 'configuracoes/transportadoras', name: 'Transportadoras', type: 'link', icon: 'truck' },
      ] },
      { route: '', name: 'Peças & Estoque', type: 'sub', icon: 'estoque', children: [
        { route: 'produtos-servicos/estoque', name: 'Produtos', type: 'link', icon: 'estoque' },
        { route: 'consulta-pecas', name: 'Consulta de peças', type: 'link', icon: 'search', disabled: true },
        { route: 'kits', name: 'Kits', type: 'link', icon: 'estoque', disabled: true },
        { route: 'cotacoes', name: 'Cotações', type: 'link', icon: 'search', disabled: true },
        { route: 'fornecedor/compras', name: 'Compras', type: 'link', icon: 'bag' },
        { route: 'entregas-devolucoes', name: 'Entregas & Devoluções', type: 'link', icon: 'bag', disabled: true },
        { route: 'configuracoes/inventario', name: 'Inventários', type: 'link', icon: 'clipboard' },
        { route: 'correcoes-estoque', name: 'Correções de estoque', type: 'link', icon: 'estoque', disabled: true },
        { route: 'transferencias-estoque', name: 'Transferências de estoque', type: 'link', icon: 'transfer', disabled: true },
      ] },
      { route: 'financeiro', name: 'Financeiro', type: 'sub', icon: 'financeiro', children: [
        { route: 'contas', name: 'Contas', type: 'link', icon: 'financeiro' },
        { route: 'receber', name: 'Pagar & Receber', type: 'link', icon: 'financeiro' },
        { route: 'caixa', name: 'Movimentação financeira', type: 'link', icon: 'financeiro', permissions: { only: ['FIN_VIS_CAIXA', 'ADMIN'] } },
        { route: 'boletos', name: 'Boletos bancários', type: 'link', icon: 'document', disabled: true },
        { route: 'conciliacao', name: 'Conciliação bancária', type: 'link', icon: 'financeiro', disabled: true },
      ] },
      { route: 'fiscal', name: 'Fiscal', type: 'sub', icon: 'fiscal', children: [
        { route: 'nfe/nfe-lista', name: 'Nota fiscal', type: 'link', icon: 'document' },
        { route: 'manifestacao', name: 'Manifestação do destinatário', type: 'link', icon: 'document', disabled: true },
        { route: 'sintegra', name: 'Sintegra', type: 'link', icon: 'refresh', disabled: true },
        { route: 'ncm', name: 'NCM', type: 'link', icon: 'number', disabled: true },
        { route: 'nfe/inutilizar-numeracao', name: 'Inutilizar faixa de numeração', type: 'link', icon: 'document' },
      ] },
      { route: 'importacoes', name: 'Importações', type: 'link', icon: 'import', disabled: true, badge: { color: 'novo', value: 'Novo' } },
      { route: 'relatorios/relatorio-vendas', name: 'Relatórios', type: 'link', icon: 'analytics' },
      { route: 'pdv', name: 'Venda direta', type: 'link', icon: 'pdv' },
    ];

    // Apenas destinos confirmados no app.routes.ts e seus módulos lazy-loaded.
    // A árvore oficial continua visível; funcionalidades futuras não redirecionam
    // silenciosamente para /home e não simulam telas prontas.
    const registeredRoutes = new Set([
      '/home', '/patio', '/configuracoes/checklist', '/orcamento/orcamento', '/os',
      '/cliente/listar', '/cliente/historico-veiculos', '/configuracoes/colaboradores',
      '/produtos-servicos/recursos', '/configuracoes/seguradoras', '/fornecedor/lista',
      '/configuracoes/transportadoras', '/produtos-servicos/estoque', '/fornecedor/compras',
      '/configuracoes/inventario', '/financeiro/contas', '/financeiro/receber',
      '/financeiro/caixa', '/fiscal/nfe/nfe-lista', '/fiscal/nfe/inutilizar-numeracao',
      '/relatorios/relatorio-vendas', '/pdv',
    ]);
    const markUnavailable = (items: Array<Menu | MenuChildrenItem>, parents: string[] = []) => {
      for (const item of items) {
        const segments = [...parents, item.route];
        if (item.type === 'link') {
          item.disabled = item.disabled || !registeredRoutes.has(this.menuService.buildRoute(segments));
        } else if (item.type === 'extLink' || item.type === 'extTabLink') {
          // Links externos só ficam ativos com URL real.
          item.disabled = !/^https?:\/\/[^\s/]+/i.test(item.route);
        }
        if (item.children?.length) markUnavailable(item.children, segments);
      }
    };
    markUnavailable(menu);
    return menu;
  }
}
