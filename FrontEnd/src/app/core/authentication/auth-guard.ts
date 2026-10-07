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

  return auth.check() ? true : router.parseUrl('/auth/login');
};
