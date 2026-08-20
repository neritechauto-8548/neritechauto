import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '@core/authentication';
import { tap } from 'rxjs';
import { BASE_URL, hasHttpScheme } from './base-url-interceptor';

export function tokenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const router = inject(Router);
  const baseUrl = inject(BASE_URL, { optional: true });
  const tokenService = inject(TokenService);

  const includeBaseUrl = (url: string) => {
    if (!baseUrl) {
      return false;
    }
    return new RegExp(`^${baseUrl.replace(/\/$/, '')}`, 'i').test(url);
  };

  const shouldAppendToken = (url: string) => !hasHttpScheme(url) || includeBaseUrl(url);
  const isPublicAuthRequest = ['/auth/login', '/auth/refresh', '/auth/recover-password', '/auth/reset-password']
    .some(path => req.url.includes(path));

  const handler = () => {
    if (req.url.includes('/auth/logout')) {
      router.navigateByUrl('/auth/login');
    }

    if (router.url.includes('/auth/login') && tokenService.valid()) {
      router.navigateByUrl('/dashboard');
    }
  };

  const bearerToken = tokenService.getBearerToken();
  if (!isPublicAuthRequest && shouldAppendToken(req.url) && bearerToken) {
    return next(
      req.clone({
        headers: req.headers.set('Authorization', bearerToken),
        withCredentials: true,
      })
    ).pipe(tap(() => handler()));
  }

  return next(req).pipe(tap(() => handler()));
}
