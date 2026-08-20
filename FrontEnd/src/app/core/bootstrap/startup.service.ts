import { Injectable, inject } from '@angular/core';
import { AuthService, User } from '@core/authentication';
import { NgxPermissionsService, NgxRolesService } from 'ngx-permissions';
import { switchMap, tap } from 'rxjs';
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

    this.menuService.addNamespace(filteredMenu, 'menu');
    this.menuService.set(filteredMenu);
  }

  private filterMenu<T extends Menu | MenuChildrenItem>(
    menu: T[],
    planLevel: number,
    grantedPermissions: Set<string>
  ): T[] {
    return menu
      .filter(item => !('minPlan' in item) || !(item as any).minPlan || (item as any).minPlan <= planLevel)
      .filter(item => this.hasDeclaredPermission(item.permissions, grantedPermissions))
      .map(item => {
        const copy = { ...item } as T;
        if (copy.children?.length) {
          copy.children = this.filterMenu(copy.children, planLevel, grantedPermissions);
        }
        return copy;
      })
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

    // O frontend nunca amplia autoridade. As permissoes efetivas sao exatamente
    // as retornadas pelo backend para a identidade/sessao atual.
    this.permissionsService.flushPermissions();
    this.permissionsService.loadPermissions(permissions);

    this.rolesService.flushRoles();
    (user?.funcoes || []).forEach((role: string) => {
      this.rolesService.addRole(role, permissions);
    });
  }
}
