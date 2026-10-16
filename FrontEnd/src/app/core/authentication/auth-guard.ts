import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { environment } from '@env/environment';

export const authGuard = (route?: ActivatedRouteSnapshot, state?: RouterStateSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // Preview UX local: permite validar o frontend sem autenticação/backend.
  if (environment.uxPreview) {
    return true;
  }

  const returnUrl = state?.url;
  return auth.check() ? true : router.createUrlTree(['/auth/login'], {
    queryParams: returnUrl?.startsWith('/') ? { returnUrl } : undefined,
  });
};
