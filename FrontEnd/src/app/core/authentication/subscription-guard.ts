import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { filter, map, take } from 'rxjs';

/**
 * Guard para controlar o acesso baseado no status da assinatura SaaS.
 * 
 * Regras:
 * - ACTIVE ou TRIAL: Acesso total liberado.
 * - PAST_DUE, SUSPENDED, CANCELED: Redireciona para /configuracoes/assinatura.
 * - Rotas liberadas: /configuracoes/assinatura, /profile, /auth/logout, /suporte.
 */
export const subscriptionGuard = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // Lista de rotas que sempre podem ser acessadas
  const whitelistedPaths = [
    '/configuracoes/assinatura',
    '/profile',
    '/auth/logout',
    '/suporte'
  ];

  const currentPath = state.url.split('?')[0];

  if (whitelistedPaths.some(path => currentPath.startsWith(path))) {
    return true;
  }

  return auth.user().pipe(
    filter(user => !!user && Object.keys(user).length > 0), // Aguarda o perfil ser carregado
    take(1),
    map(user => {
      const status = user.subscriptionStatus;
      const isAtiva = user.assinaturaAtiva;
      console.log('[SubscriptionGuard] User Status:', status, 'Ativa:', isAtiva, 'User:', user.email);
      
      // Permitir acesso se a assinatura estiver ativa (via boolean ou via status string)
      const allowedStatus = ['ATIVO', 'TESTE', 'active', 'trialing', 'ACTIVE', 'TRIAL'];
      if (isAtiva === true || (status && allowedStatus.includes(status))) {
        return true;
      }

      // Caso contrário, redireciona para a tela de assinatura
      console.warn(`[SubscriptionGuard] Acesso bloqueado para status: ${status}. Redirecionando para assinatura.`);
      return router.parseUrl('/configuracoes/assinatura');
    })
  );
};
