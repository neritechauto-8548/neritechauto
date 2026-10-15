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
                environment.uxPreview && resolved.length === 0 ? this.getPreviewMenu() : resolved,
                environment.uxPreview ? ({ planoNivel: 99 } as User) : user
              );
            })
          );
        })
      ).subscribe({
        next: () => resolve(),
        error: () => {
          if (environment.uxPreview) {
            this.setMenu(this.getPreviewMenu(), { planoNivel: 99 } as User);
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

  /** Menu canônico para homologação visual. Não é usado em produção. */
  private getPreviewMenu(): Menu[] {
    const menu: Menu[] = [
      { route: 'patio', name: 'Gestão de Pátio', type: 'link', icon: 'warehouse' },
      { route: 'home', name: 'Início', type: 'link', icon: 'home' },
      { route: 'cliente', name: 'Clientes', type: 'sub', icon: 'people', children: [
        { route: 'listar', name: 'Clientes', type: 'link' },
        { route: 'crm', name: 'CRM', type: 'sub', children: [
          { route: '', name: 'Visão geral', type: 'link' },
          { route: 'comunicacao', name: 'Comunicação', type: 'link' },
          { route: 'pesquisa', name: 'Questionamento de pesquisa', type: 'link' },
        ] },
      ] },
      { route: '', name: 'Oficina', type: 'sub', icon: 'oficina', children: [
        ...['Agenda','Orçamentos','Ordens de Serviço','Diagnósticos','Inspeções','Serviços','Veículos','Entregas','Vendas','Histórico']
          .map((name, i) => ({ route: ['agendamento/calendario','orcamento/orcamento','os','diagnostico','inspecao','produtos-servicos/servicos','veiculo','entrega','pdv','historico'][i], name, type: 'link' as const })),
      ] },
      { route: '', name: 'Estoque & Compras', type: 'sub', icon: 'estoque', children: [
        ...['Estoque','Produtos','Kits','Compras','Cotações','Inventário','Movimentações'].map((name, i) => ({ route: ['produtos-servicos/estoque','produtos','kits','compras','cotacoes','configuracoes/inventario','movimentacoes'][i], name, type: 'link' as const })),
      ] },
      { route: 'financeiro', name: 'Financeiro', type: 'sub', icon: 'financeiro', children: [
        ...['Contas','Contas a Receber','Contas a Pagar','Lançamentos Bancários','Movimento de Caixa','Movimento Bancário','Comissões'].map((name, i) => ({ route: ['contas','receber','pagar','lancamentos','caixa','bancario','comissoes'][i], name, type: 'link' as const })),
      ] },
      { route: 'fiscal', name: 'Fiscal', type: 'sub', icon: 'fiscal', children: [
        ...['Documentos Fiscais','NF-e','NFC-e / Cupom','NFS-e','Manifestação','Inutilização','NCM','SINTEGRA'].map((name, i) => ({ route: ['documentos','nfe','nfce','nfse','manifestacao','nfe/inutilizar-numeracao','ncm','sintegra'][i], name, type: 'link' as const })),
      ] },
      { route: '', name: 'Inteligência Artificial', type: 'sub', icon: 'ia', children: [
        { route: 'assistente', name: 'Assistente', type: 'link' },
        { route: 'automacoes', name: 'Automações IA', type: 'link' },
      ] },
      { route: 'graficos-gerenciais', name: 'Gráficos Gerenciais', type: 'link', icon: 'analytics' },
      { route: '', name: 'Relatórios', type: 'sub', icon: 'analytics', children: [
        ...['Comercial','Clientes','Serviços','Estoque','Compras','Financeiro','Comissões','Produtividade','Fiscal','Gerenciais']
          .map((name, i) => ({ route: [
            'relatorios/relatorio-vendas', 'relatorios/relatorio-clientes',
            'relatorios/relatorio-servicos', 'relatorios/relatorio-estoque',
            'relatorios/relatorio-compras', 'relatorios/relatorio-contas',
            'relatorios/relatorio-comissoes', 'relatorios/relatorio-produtividade',
            'relatorios/relatorio-fiscal', 'relatorios/relatorio-receitas-despesas'
          ][i], name, type: 'link' as const })),
      ] },
      { route: '', name: 'Administração', type: 'sub', icon: 'admin', children: [
        ...['Empresa','Usuários','Funcionários','Cargos','Permissões','Áreas','Fornecedores','Seguradoras','Transportadoras']
          .map((name, i) => ({ route: [
            'configuracoes/empresa','configuracoes/usuarios','configuracoes/colaboradores',
            'configuracoes/cargos','configuracoes/permissoes','configuracoes/areas',
            'fornecedor/lista','configuracoes/seguradoras','configuracoes/transportadoras'
          ][i], name, type: 'link' as const })),
        { route: '', name: 'Configurações', type: 'sub', children: [
          ...['Departamentos','Setores','Assinatura','Contas bancárias','Formas de pagamento',
            'Checklist','Localização','Categorias','Opções de envio']
            .map((name, i) => ({ route: [
              'configuracoes/departamentos','configuracoes/setores','configuracoes/assinatura',
              'configuracoes/contas','configuracoes/formas-pagamento','configuracoes/checklist',
              'configuracoes/localizacao','configuracoes/categoria','configuracoes/opcoes-envio'
            ][i], name, type: 'link' as const })),
        ] },
      ] },
    ];

    // Apenas destinos confirmados no app.routes.ts e seus módulos lazy-loaded.
    // A árvore oficial continua visível; funcionalidades futuras não redirecionam
    // silenciosamente para /home e não simulam telas prontas.
    const registeredRoutes = new Set([
      '/home', '/cliente/listar', '/cliente/crm', '/cliente/crm/comunicacao', '/cliente/crm/pesquisa', '/agendamento/calendario', '/orcamento/orcamento', '/os',
      '/produtos-servicos/servicos', '/veiculo', '/pdv', '/produtos-servicos/estoque',
      '/configuracoes/inventario', '/financeiro/contas', '/financeiro/receber',
      '/financeiro/pagar', '/financeiro/caixa', '/financeiro/comissoes',
      '/fiscal/nfe', '/fiscal/nfce', '/fiscal/nfse', '/fiscal/nfe/inutilizar-numeracao',
      '/fornecedor/compras', '/fornecedor/lista', '/relatorios/relatorio-vendas',
      '/relatorios/relatorio-clientes', '/relatorios/relatorio-estoque',
      '/relatorios/relatorio-contas', '/relatorios/relatorio-receitas-despesas',
      '/configuracoes/empresa', '/configuracoes/usuarios', '/configuracoes/colaboradores',
      '/configuracoes/cargos', '/configuracoes/permissoes', '/configuracoes/departamentos',
      '/configuracoes/setores', '/configuracoes/assinatura', '/configuracoes/contas',
      '/configuracoes/formas-pagamento', '/configuracoes/checklist',
      '/configuracoes/localizacao', '/configuracoes/categoria', '/configuracoes/opcoes-envio',
    ]);
    const markUnavailable = (items: Array<Menu | MenuChildrenItem>, parents: string[] = []) => {
      for (const item of items) {
        const segments = [...parents, item.route];
        if (item.type === 'link') {
          item.disabled = !registeredRoutes.has(this.menuService.buildRoute(segments));
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
