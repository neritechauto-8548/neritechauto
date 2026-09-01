import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { catchError, filter, map, of, take, timeout } from 'rxjs';
import { AuthService } from './auth.service';

/**
 * Bloqueia módulos cujo nível mínimo de plano não é atendido pela assinatura atual.
 * O menu usa a mesma propriedade `minPlan`; a rota é a barreira efetiva contra URL direta.
 *
 * Falha fechada: ausência/timeout do perfil nunca amplia acesso a recursos premium.
 */
export const planGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const requiredLevel = Number(route.data?.['minPlan'] ?? 1);

  if (!Number.isFinite(requiredLevel) || requiredLevel <= 1) {
    return true;
  }

  if (!auth.check()) {
    return router.parseUrl('/auth/login');
  }

  return auth.user().pipe(
    filter(user => Boolean(user && Object.keys(user).length > 0)),
    take(1),
    timeout(1500),
    map(user => {
      const currentLevel = Number(user.planoNivel ?? 0);
      return currentLevel >= requiredLevel ? true : router.parseUrl('/403');
    }),
    catchError(() => of(router.parseUrl('/403')))
  );
};
