import { Injectable, inject } from '@angular/core';
import { AuthService, User } from '@core/authentication';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { switchMap, tap } from 'rxjs';
import { Menu, MenuService } from './menu.service';

@Injectable({
  providedIn: 'root',
})
export class StartupService {
  private readonly authService = inject(AuthService);
  private readonly menuService = inject(MenuService);
  private readonly permissonsService = inject(NgxPermissionsService);
  private readonly rolesService = inject(NgxRolesService);

  load() {
    return new Promise<void>(resolve => {
      this.authService
        .change()
        .pipe(
          tap((user: User) => {
            this.setPermissions(user);
          }),
          switchMap((user: User) => this.authService.menu().pipe(
            tap((menu: Menu[]) => this.setMenu(menu, user))
          ))
        )
        .subscribe({
          next: () => resolve(),
          error: () => resolve(),
        });
    });
  }

  private setMenu(menu: Menu[], user: User) {
    const filteredMenu = this.filterMenuByPlan(menu ?? [], user);

    this.menuService.addNamespace(filteredMenu, 'menu');
    this.menuService.set(filteredMenu);
  }

  /**
   * Filtra uma cópia do menu sem alterar o JSON carregado da aplicação.
   *
   * Quando um item declara `capability`, a flag entregue pelo backend é a
   * fonte principal. `minPlan` funciona como fallback para compatibilidade
   * durante deploys com versões diferentes de frontend/backend.
   */
  private filterMenuByPlan(menu: any[], user: User): any[] {
    const planLevel = Number(user?.planoNivel ?? 0);

    return menu.reduce<any[]>((result, originalItem) => {
      const item = { ...originalItem };
      const capability = item.capability as string | undefined;
      const capabilityValue = capability ? user?.[capability] : undefined;
      const minPlanAllowed = !item.minPlan || Number(item.minPlan) <= planLevel;

      const hasAccess = capability
        ? capabilityValue === true || (capabilityValue === undefined && minPlanAllowed)
        : minPlanAllowed;

      if (!hasAccess) {
        return result;
      }

      if (Array.isArray(originalItem.children) && originalItem.children.length > 0) {
        item.children = this.filterMenuByPlan(originalItem.children, user);
        if (item.type === 'sub' && item.children.length === 0) {
          return result;
        }
      }

      result.push(item);
      return result;
    }, []);
  }

  private setPermissions(user: User) {
    let permissions = user.permissions || [];

    const hasAdmin = user.funcoes && user.funcoes.some((role: string) => {
      const r = (role || '').toUpperCase();
      return r === 'ADMIN' || r.includes('ADMIN') || r.includes('ADMINISTRADOR');
    });

    if (hasAdmin) {
      const allPermissions = [
        'CLIENTE_CRIAR', 'CLIENTE_EDITAR', 'CLIENTE_EXCLUIR', 'CLIENTE_EXPORTAR',
        'VEICULO_CRIAR', 'VEICULO_EDITAR', 'VEICULO_EXCLUIR', 'VEICULO_EXPORTAR',
        'AGENDAMENTO_CRIAR', 'AGENDAMENTO_EDITAR', 'AGENDAMENTO_EXCLUIR',
        'OS_INCLUIR', 'OS_EDITAR', 'OS_EXCLUIR', 'OS_ALT_FUNCIONARIO', 'OS_ALT_STATUS',
        'GERAL_USUARIO', 'GERAL_CALENDARIO', 'GERAL_AGENDAMENTO_VISUALIZAR', 'GERAL_FATURAS',
        'GERAL_CONFIG_SISTEMA', 'GERAL_MEU_CALENDARIO', 'GERAL_CONFIG_CHECKLIST', 'GERAL_ORCAMENTO',
        'GERAL_AGENDAMENTO_EDITAR', 'GERAL_CONFIG_SITE', 'FIN_VIS_CAIXA', 'FIN_FECHAMENTO'
      ];
      permissions = Array.from(new Set([...permissions, ...allPermissions]));
    }

    this.permissonsService.loadPermissions(permissions);

    this.rolesService.flushRoles();
    if (user.funcoes) {
      user.funcoes.forEach((role: string) => {
        this.rolesService.addRole(role, permissions);
      });
    }
  }
}
