import { HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { HotToastService } from '@ngxpert/hot-toast';
import { mergeMap, of, throwError } from 'rxjs';

export function apiInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const toast = inject(HotToastService);

  if (!req.url.includes('/api/')) {
    return next(req);
  }

  return next(req).pipe(
    mergeMap((event: HttpEvent<any>) => {
      if (event instanceof HttpResponse) {
        const body: any = event.body;

        // failure: { code: **, msg: 'failure' }
        // success: { code: 0,  msg: 'success', data: {} }
        // Correção: Verifica se 'code' existe e é diferente de 0/null/undefined
        if (body && 'code' in body && body.code != null && body.code !== 0 && body.code !== '0') {
          const errorMsg = body.msg || body.message || 'Erro na API';
          toast.error(errorMsg);
          return throwError(() => body.errors || [errorMsg]);
        }
        // unwrap ApiResponse { data, message, errors, timestamp }
        if (body && typeof body === 'object' && 'data' in body) {
          return of(event.clone({ body: body.data }));
        }
      }
      // Pass down event if everything is OK
      return of(event);
    })
  );
}
