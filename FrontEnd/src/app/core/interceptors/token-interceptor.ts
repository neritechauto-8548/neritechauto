import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '@core/authentication';
import { BASE_URL, hasHttpScheme } from './base-url-interceptor';

export function tokenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const baseUrl = inject(BASE_URL, { optional: true });
  const tokenService = inject(TokenService);

  const includeBaseUrl = (url: string) => {
    if (!baseUrl) {
      return false;
    }
    return url === baseUrl || url.startsWith(`${baseUrl.replace(/\/$/, '')}/`);
  };

  const shouldAppendToken = (url: string) => !hasHttpScheme(url) || includeBaseUrl(url);
  if (/\/auth\/(?:login|refresh|recover-password|reset-password)(?:\?|$)/.test(req.url)) {
    return next(req);
  }

  if (shouldAppendToken(req.url) && tokenService.getBearerToken()) {
    return next(
      req.clone({
        headers: req.headers.append('Authorization', tokenService.getBearerToken()),
        withCredentials: true,
      })
    );
  }

  return next(req);
}
