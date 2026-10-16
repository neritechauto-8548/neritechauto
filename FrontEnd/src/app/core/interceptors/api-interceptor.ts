import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { mergeMap, of, throwError } from 'rxjs';

/**
 * Preserve the actual backend ApiResponse envelope contract:
 * success { data, message, code: null }, failure { errors, code }.
 * A 200 response carrying application errors must not look like a successful mutation.
 * Synthetic errors remain HttpErrorResponse so the shared error interceptor handles
 * toast/status once, while callers can still inspect original validation details.
 */
export function apiInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  if (!req.url.includes('/api/')) return next(req);

  return next(req).pipe(
    mergeMap((event: HttpEvent<unknown>) => {
      if (!(event instanceof HttpResponse)) return of(event);
      const body = event.body;
      if (!body || typeof body !== 'object' || Array.isArray(body)) return of(event);

      const payload = body as Record<string, unknown>;
      const code = payload['code'];
      const hasFailureCode = code != null && code !== 0 && code !== '0';
      const errors = payload['errors'];
      const hasFailureDetails = Array.isArray(errors) && errors.length > 0 &&
        (payload['data'] === null || payload['data'] === undefined);

      if (hasFailureCode || hasFailureDetails) {
        return throwError(() => new HttpErrorResponse({
          error: body,
          headers: event.headers,
          status: 400,
          statusText: 'Business rule violation',
          url: event.url || req.url,
        }));
      }

      // Unwrap only recognized backend envelopes, never arbitrarily unwrap
      // domain objects that happen to contain a field named "data".
      const isApiEnvelope = Object.prototype.hasOwnProperty.call(payload, 'data') &&
        (Object.prototype.hasOwnProperty.call(payload, 'timestamp') ||
          Object.prototype.hasOwnProperty.call(payload, 'code') ||
          Object.prototype.hasOwnProperty.call(payload, 'errors'));
      if (isApiEnvelope) return of(event.clone({ body: payload['data'] }));
      return of(event);
    })
  );
}
