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
    let permissions = user.permissions || [];
    
    // Se o usuário possuir a função de ADMIN, garante que tenha todas as permissões no frontend
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
