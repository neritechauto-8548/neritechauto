import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { environment } from '@env/environment';
import { filter, map, take, timeout, catchError, of } from 'rxjs';

/**
 * Guard para controlar o acesso baseado no status da assinatura SaaS.
 * Em UX Preview, o frontend funciona sem backend para homologação visual.
 */
export const subscriptionGuard = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (environment.uxPreview) {
    return true;
  }

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
    catchError(() => of(true as any)),
    map(userOrTrue => {
      if (userOrTrue === true) return true;

      const user = userOrTrue;
      const status = user.subscriptionStatus;
      const isAtiva = user.assinaturaAtiva;

      if (status === undefined && isAtiva === undefined) return true;

      const allowedStatus = ['ATIVO', 'TESTE', 'active', 'trialing', 'ACTIVE', 'TRIAL'];
      if (isAtiva === true || (status && allowedStatus.includes(status))) {
        return true;
      }

      return router.parseUrl('/configuracoes/assinatura');
    })
  );
};
