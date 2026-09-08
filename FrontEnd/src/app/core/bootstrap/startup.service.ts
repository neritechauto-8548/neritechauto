import { Injectable, inject } from '@angular/core';
import { AuthService, User } from '@core/authentication';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { switchMap, tap } from 'rxjs';
import { applyCanonicalMenuContract } from './menu-contract';
import { Menu, MenuChildrenItem, MenuPermissions, MenuService } from './menu.service';

@Injectable({
  providedIn: 'root',
})
export class StartupService {
  private readonly authService = inject(AuthService);
  private readonly menuService = inject(MenuService);
  private readonly permissionsService = inject(NgxPermissionsService);
  private readonly rolesService = inject(NgxRolesService);

  load() {
    return new Promise<void>(resolve => {
      this.authService
        .change()
        .pipe(
          tap((user: User) => {
            this.setPermissions(user);

            if (user && user.assinaturaAtiva === false && user.stripeUrl) {
              window.location.href = user.stripeUrl;
            }
          }),
          switchMap((user: User) =>
            this.authService.menu().pipe(tap((menu: Menu[]) => this.setMenu(menu, user)))
          )
        )
        .subscribe({
          next: () => resolve(),
          error: () => resolve(),
        });
    });
  }

  private setMenu(menu: Menu[], user: User) {
    const planLevel = user?.planoNivel || 1;
    const grantedPermissions = new Set((user?.permissions || []).map(permission => String(permission)));
    const filteredMenu = this.filterMenu(menu || [], planLevel, grantedPermissions);

    // UI-MASTER-001 é a autoridade da navegação visual. O backend/permissões
    // continuam decidindo quais destinos o usuário pode acessar; aqui apenas
    // garantimos a ordem e o agrupamento oficiais entre os itens já autorizados.
    const canonicalMenu = applyCanonicalMenuContract(filteredMenu);

    this.menuService.addNamespace(canonicalMenu, 'menu');
    this.menuService.set(canonicalMenu);
  }

  private filterMenu<T extends MenuChildrenItem>(
    menu: T[],
    planLevel: number,
    grantedPermissions: Set<string>
  ): T[] {
    return menu
      .filter(item => !item.minPlan || item.minPlan <= planLevel)
      .filter(item => this.hasDeclaredPermission(item.permissions, grantedPermissions))
      .map(item => {
        const copy = { ...item } as T;

        if (copy.children?.length) {
          copy.children = this.filterMenu(copy.children, planLevel, grantedPermissions);
        }

        return copy;
      })
      // Grupos que perderam todos os filhos por plano/permissão desaparecem da navegação.
      .filter(item => item.type !== 'sub' || Boolean(item.children?.length));
  }

  private hasDeclaredPermission(
    permissions: MenuPermissions | undefined,
    grantedPermissions: Set<string>
  ): boolean {
    if (!permissions) {
      return true;
    }

    const only = this.asArray(permissions.only);
    const except = this.asArray(permissions.except);

    if (except.some(permission => grantedPermissions.has(permission))) {
      return false;
    }

    if (only.length === 0) {
      return true;
    }

    return only.some(permission => grantedPermissions.has(permission));
  }

  private asArray(value?: string | string[]): string[] {
    if (!value) {
      return [];
    }

    return Array.isArray(value) ? value : [value];
  }

  private setPermissions(user: User) {
    const permissions = (user?.permissions || []).map(permission => String(permission));

    // O frontend nunca amplia autoridade. As permissões efetivas são exatamente
    // as retornadas pelo backend para a identidade/sessão atual.
    this.permissionsService.flushPermissions();
    this.permissionsService.loadPermissions(permissions);

    this.rolesService.flushRoles();
    (user?.funcoes || []).forEach((role: string) => {
      this.rolesService.addRole(role, permissions);
    });
  }
}
