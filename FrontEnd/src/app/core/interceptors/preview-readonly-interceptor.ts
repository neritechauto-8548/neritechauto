import { HttpErrorResponse, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { environment } from '@env/environment';
import { throwError } from 'rxjs';

/**
 * Frontend-only evaluation: NEVER sends data or mutation requests to any API,
 * even when the browser has a previously stored token or tenant identifier.
 * Only static files served by this frontend may be requested with HttpClient.
 * This is not an authenticated session and never grants backend permissions.
 */
export function isPreviewStaticResource(url: string, method: string): boolean {
  if (method.toUpperCase() !== 'GET') return false;
  try {
    const currentOrigin = typeof window === 'undefined' ? 'https://preview.local' : window.location.origin;
    const parsed = new URL(url, currentOrigin + '/');
    if (parsed.origin !== currentOrigin) return false;
    return /^(?:\/i18n\/|\/data\/|\/assets\/|\/images\/)/.test(parsed.pathname);
  } catch {
    return false;
  }
}

export function previewReadonlyInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  if (!environment.uxPreview || isPreviewStaticResource(req.url, req.method)) return next(req);

  // Status 503 is deliberate: features show their unavailable/error state.
  // Never return a fabricated 200 response or allow an unsafe write.
  return throwError(() => new HttpErrorResponse({
    status: 503,
    statusText: 'Prévia visual: dados e operações exigem login',
    url: req.url,
    error: { message: 'Prévia sem login: apenas interface. Nenhum dado real foi acessado.' },
  }));
}
