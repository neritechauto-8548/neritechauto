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
    return new Promise<void>((resolve, reject) => {
      this.authService
        .change()
        .pipe(
          tap((user: any) => {
            console.log('[StartupService] User profile received:', user);
            this.setPermissions(user);
            
            // Verificação de Assinatura Stripe
            if (user && user.assinaturaAtiva === false && user.stripeUrl) {
               console.warn('Assinatura inativa. Redirecionando para o Stripe...');
               window.location.href = user.stripeUrl;
            }
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
    const planLevel = user?.planoNivel || 1;
    const filteredMenu = this.filterMenuByPlan(menu, planLevel);
    
    this.menuService.addNamespace(filteredMenu, 'menu');
    this.menuService.set(filteredMenu);
  }

  private filterMenuByPlan(menu: any[], planLevel: number): any[] {
    return menu.filter(item => {
      // Filtro por Nível de Plano (única restrição que esconde o menu)
      if (item.minPlan && item.minPlan > planLevel) {
        return false;
      }
      
      // Recursividade para filhos
      if (item.children && item.children.length > 0) {
        item.children = this.filterMenuByPlan(item.children, planLevel);
        if (item.type === 'sub' && item.children.length === 0) {
          return false;
        }
      }
      
      return true;
    });
  }

  private setPermissions(user: User) {
    const permissions = user.permissions || [];
    this.permissonsService.loadPermissions(permissions);
    
    this.rolesService.flushRoles();
    if (user.funcoes) {
      user.funcoes.forEach((role: string) => {
        this.rolesService.addRole(role, permissions);
      });
    }
  }
}
