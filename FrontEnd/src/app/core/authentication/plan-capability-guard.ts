import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { catchError, filter, map, of, take, timeout } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from './interface';

type PlanCapability = 'acessoFiscal';

/**
 * Protege rotas premium usando capacidades calculadas pelo backend.
 *
 * A rota deve declarar `data.capability`. `fallbackMinPlan` existe apenas para
 * compatibilidade durante deploys em que o frontend novo conversa por alguns
 * instantes com uma versão anterior da API.
 */
export const planCapabilityGuard = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.check()) {
    return router.parseUrl('/auth/login');
  }

  const capability = route.data['capability'] as PlanCapability | undefined;
  const fallbackMinPlan = Number(route.data['fallbackMinPlan'] ?? 0);

  if (!capability) {
    return router.parseUrl('/403');
  }

  return auth.user().pipe(
    filter(user => !!user && Object.keys(user).length > 0),
    take(1),
    timeout(1500),
    map((user: User) => {
      const explicitCapability = user[capability];

      if (explicitCapability === true) {
        return true;
      }

      if (explicitCapability === false) {
        return router.parseUrl('/configuracoes/assinatura');
      }

      // Compatibilidade com API anterior: nível 2+ corresponde ao Ultra para Fiscal.
      if (fallbackMinPlan > 0 && Number(user.planoNivel ?? 0) >= fallbackMinPlan) {
        return true;
      }

      return router.parseUrl('/configuracoes/assinatura');
    }),
    catchError(() => {
      // Recursos premium nunca devem ser liberados quando a capacidade não pôde ser confirmada.
      return of(router.parseUrl('/configuracoes/assinatura'));
    })
  );
};
