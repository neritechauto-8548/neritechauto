import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { filter, map, take, timeout, catchError } from 'rxjs';
import { of } from 'rxjs';

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

  // Se não há token válido, nem tentamos checar assinatura
  if (!auth.check()) {
    return router.parseUrl('/auth/login');
  }

  return auth.user().pipe(
    // Aguarda até 3 segundos pelo perfil
    filter(user => {
      console.log('[SubscriptionGuard] Recebeu emissão de user$:', user);
      return !!user && Object.keys(user).length > 0;
    }),
    take(1),
    timeout(1000),
    catchError((err) => {
      // Timeout ou erro: se tem token válido, deixa passar
      console.warn('[SubscriptionGuard] Perfil demorou a carregar, liberando acesso via fallback.');
      return of(true as any);
    }),
    map(userOrTrue => {
      // Se o catchError retornou true, apenas repassa
      if (userOrTrue === true) {
        console.log('[SubscriptionGuard] Prosseguindo via Fallback (true)');
        return true;
      }

      const user = userOrTrue;
      const status = user.subscriptionStatus;
      const isAtiva = user.assinaturaAtiva;
      console.log('[SubscriptionGuard] Verificando assinatura:', { email: user.email, status, isAtiva });

      // Se não há dados de assinatura, libera (usuário pode não ter assinatura configurada ainda)
      if (status === undefined && isAtiva === undefined) {
        console.warn('[SubscriptionGuard] Sem dados de assinatura. Liberando acesso.');
        return true;
      }

      // Permitir acesso se a assinatura estiver ativa
      const allowedStatus = ['ATIVO', 'TESTE', 'active', 'trialing', 'ACTIVE', 'TRIAL'];
      if (isAtiva === true || (status && allowedStatus.includes(status))) {
        return true;
      }

      // Caso contrário, redireciona para a tela de assinatura
      console.warn(`[SubscriptionGuard] Acesso bloqueado para status: ${status}. Redirecionando.`);
      return router.parseUrl('/configuracoes/assinatura');
    })
  );
};
