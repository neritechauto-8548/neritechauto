import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { catchError, filter, map, of, take, timeout } from 'rxjs';

/**
 * Guard para controlar o acesso baseado no status comercial da assinatura SaaS.
 *
 * O backend é a fonte de verdade por meio de `assinaturaAtiva`.
 * `subscriptionStatus` é usado apenas como fallback de compatibilidade quando
 * o backend ainda não fornece a flag explícita.
 */
export const subscriptionGuard = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

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

  if (!auth.check()) {
    return router.parseUrl('/auth/login');
  }

  return auth.user().pipe(
    filter(user => !!user && Object.keys(user).length > 0),
    take(1),
    timeout(1000),
    catchError(() => {
      // Mantém o comportamento resiliente para rotas gerais. Recursos premium
      // possuem guard próprio e falham fechados quando a capacidade é desconhecida.
      console.warn('[SubscriptionGuard] Perfil demorou a carregar; mantendo acesso geral por fallback.');
      return of(true as const);
    }),
    map(userOrTrue => {
      if (userOrTrue === true) {
        return true;
      }

      const user = userOrTrue;
      const status = user.subscriptionStatus;
      const isAtiva = user.assinaturaAtiva;

      // A flag explícita do backend tem precedência. Isso evita liberar um trial
      // expirado apenas porque o status persistido ainda é TESTE.
      if (isAtiva === true) {
        return true;
      }

      if (isAtiva === false) {
        return router.parseUrl('/configuracoes/assinatura');
      }

      // Compatibilidade temporária com respostas antigas do backend.
      const allowedStatus = ['ATIVO', 'TESTE', 'active', 'trialing', 'ACTIVE', 'TRIAL'];
      if (status && allowedStatus.includes(status)) {
        return true;
      }

      if (status === undefined) {
        return true;
      }

      return router.parseUrl('/configuracoes/assinatura');
    })
  );
};
