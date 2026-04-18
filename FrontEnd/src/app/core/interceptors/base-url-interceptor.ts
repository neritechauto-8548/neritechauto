import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { InjectionToken, inject } from '@angular/core';

export const BASE_URL = new InjectionToken<string>('BASE_URL');

export function hasHttpScheme(url: string) {
  return new RegExp('^http(s)?://', 'i').test(url);
}

export function baseUrlInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const baseUrl = inject(BASE_URL, { optional: true });

  // If no baseUrl provided, do nothing
  if (!baseUrl) {
    return next(req);
  }

  const url = req.url;

  // Skip absolute URLs
  if (hasHttpScheme(url)) {
    return next(req);
  }

  // Skip i18n JSON loading
  if (url.startsWith('i18n/') || url.startsWith('/i18n/')) {
    return next(req);
  }

  const isPathPrefix = baseUrl.startsWith('/') && !hasHttpScheme(baseUrl);

  // When baseUrl is a path prefix (e.g. '/api'), avoid double-prefixing
  if (isPathPrefix) {
    const normalizedBase = baseUrl.replace(/\/$/, '');
    const normalizedUrl = url.replace(/^\/?/, '/');
    if (normalizedUrl.startsWith(normalizedBase + '/')) {
      return next(req);
    }
    const joined = [normalizedBase, normalizedUrl.replace(/^\//, '')].join('/');
    return next(req.clone({ url: joined }));
  }

  // Otherwise treat baseUrl as full origin and prepend
  const joined = [baseUrl.replace(/\/$/, ''), url.replace(/^\.?\//, '')].join('/');
  return next(req.clone({ url: joined }));
}
