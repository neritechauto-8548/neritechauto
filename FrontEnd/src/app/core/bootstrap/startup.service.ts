import { Injectable, inject } from '@angular/core';
import { AuthService, User } from '@core/authentication';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { switchMap, tap } from 'rxjs';
import { Menu, MenuService } from './menu.service';
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
          if (environment.uxPreview) {
            this.setMenu(this.getPreviewMenu(), { planoNivel: 99 } as User);
            return [user];
          }
          return this.authService.menu().pipe(
            tap((menu: Menu[]) => this.setMenu(menu, user))
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
    const filteredMenu = this.filterMenuByPlan(menu, planLevel);
    this.menuService.addNamespace(filteredMenu, 'menu');
    this.menuService.set(filteredMenu);
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
      return r === 'ADMIN' || r.includes('ADMIN') || r.includes('ADMINISTRADOR');
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
    return [
      { route: 'patio', name: 'Gestão de Pátio', type: 'extLink', icon: 'warehouse' },
      { route: 'home', name: 'Início', type: 'link', icon: 'home' },
      { route: 'clientes', name: 'Clientes', type: 'sub', icon: 'people', children: [
        { route: 'listar', name: 'Clientes', type: 'link' },
        { route: 'crm', name: 'CRM', type: 'sub', children: [
          { route: 'comunicacao', name: 'Comunicação', type: 'link' },
          { route: 'pesquisa', name: 'Questionamento de pesquisa', type: 'link' },
        ] },
      ] },
      { route: 'oficina', name: 'Oficina', type: 'sub', icon: 'oficina', children: [
        ...['Agenda','Orçamentos','Ordens de Serviço','Diagnósticos','Inspeções','Serviços','Veículos','Entregas','Vendas','Histórico']
          .map((name, i) => ({ route: ['agendamento','orcamento','os','diagnostico','inspecao','produtos-servicos','veiculo','entrega','pdv','historico'][i], name, type: 'link' as const })),
      ] },
      { route: 'estoque-compras', name: 'Estoque & Compras', type: 'sub', icon: 'estoque', children: [
        ...['Estoque','Produtos','Kits','Compras','Cotações','Inventário','Movimentações'].map((name, i) => ({ route: ['estoque','produtos','kits','compras','cotacoes','inventario','movimentacoes'][i], name, type: 'link' as const })),
      ] },
      { route: 'financeiro', name: 'Financeiro', type: 'sub', icon: 'financeiro', children: [
        ...['Contas','Contas a Receber','Contas a Pagar','Lançamentos Bancários','Movimento de Caixa','Movimento Bancário','Comissões'].map((name, i) => ({ route: ['contas','receber','pagar','lancamentos','caixa','bancario','comissoes'][i], name, type: 'link' as const })),
      ] },
      { route: 'fiscal', name: 'Fiscal', type: 'sub', icon: 'fiscal', children: [
        ...['Documentos Fiscais','NF-e','NFC-e / Cupom','NFS-e','Manifestação','Inutilização','NCM','SINTEGRA'].map((name, i) => ({ route: ['documentos','nfe','nfce','nfse','manifestacao','inutilizacao','ncm','sin­tegra'][i], name, type: 'link' as const })),
      ] },
      { route: 'inteligencia-artificial', name: 'Inteligência Artificial', type: 'sub', icon: 'ia', children: [
        { route: 'assistente', name: 'Assistente', type: 'link' },
        { route: 'automacoes', name: 'Automações IA', type: 'link' },
      ] },
      { route: 'graficos-gerenciais', name: 'Gráficos Gerenciais', type: 'link', icon: 'analytics' },
    ];
  }
}
