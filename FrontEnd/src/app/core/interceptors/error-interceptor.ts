import { HttpErrorResponse, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { catchError, throwError } from 'rxjs';

export enum STATUS {
  UNAUTHORIZED = 401,
  FORBIDDEN = 403,
  NOT_FOUND = 404,
  UNPROCESSABLE_ENTITY = 422,
  CONFLICT = 409,
  INTERNAL_SERVER_ERROR = 500,
}

export interface ValidationError {
  field: string;
  message: string;
}

export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const router = inject(Router);
  const messageService = inject(MessageService);
  const errorPages = [STATUS.FORBIDDEN, STATUS.NOT_FOUND, STATUS.INTERNAL_SERVER_ERROR];
  const skipToast = req.headers.has('X-Skip-Toast');

  const getMessage = (error: HttpErrorResponse): string => {
    if (error.error?.message) return error.error.message;
    if (error.error?.msg) return error.error.msg;
    if (error.status === STATUS.UNPROCESSABLE_ENTITY && error.error?.details) {
      const details = error.error.details as ValidationError[];
      if (Array.isArray(details) && details.length > 0) {
        return details.map(d => `${d.field}: ${d.message}`).join('; ');
      }
    }
    if (error.status === 0) return 'Servidor indisponível ou erro de conexão.';
    return `${error.status} ${error.statusText}`;
  };

  const getValidationErrors = (error: HttpErrorResponse): ValidationError[] => {
    if (error.status === STATUS.UNPROCESSABLE_ENTITY && error.error?.details) {
      const details = error.error.details;
      if (Array.isArray(details)) return details as ValidationError[];
    }
    return [];
  };

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const isApi = req.url.includes('/api/');

      if (isApi) {
        console.error('API ERROR', error);
        const apiErrors = error.error?.errors;

        if (error.status === STATUS.UNAUTHORIZED) {
          if (req.url.includes('/api/auth/login')) {
            if (!skipToast) messageService.add({ severity: 'error', summary: 'Erro', detail: 'Credenciais inválidas' });
          } else {
            router.navigateByUrl('/auth/login');
          }
        } else if (error.status === STATUS.UNPROCESSABLE_ENTITY) {
          if (Array.isArray(apiErrors) && apiErrors.length > 0) {
            if (!skipToast) apiErrors.forEach((err: string) => {
              messageService.add({ severity: 'error', summary: 'Validação', detail: err });
            });
          } else {
            const validationErrors = getValidationErrors(error);
            if (validationErrors.length > 0) {
              if (!skipToast) validationErrors.forEach(err => {
                messageService.add({ severity: 'error', summary: 'Validação', detail: `${err.field}: ${err.message}` });
              });
            } else {
              if (!skipToast) messageService.add({ severity: 'error', summary: 'Erro', detail: getMessage(error) });
            }
          }
        } else {
          if (Array.isArray(apiErrors) && apiErrors.length > 0) {
            if (!skipToast) apiErrors.forEach((err: string) => messageService.add({ severity: 'error', summary: 'Erro', detail: err }));
          } else {
            if (!skipToast) messageService.add({ severity: 'error', summary: 'Erro', detail: getMessage(error) });
          }
        }
        return throwError(() => error);
      }

      if (errorPages.includes(error.status)) {
        router.navigateByUrl(`/${error.status}`, { skipLocationChange: true });
      } else {
        console.error('ERROR', error);
        if (!skipToast) messageService.add({ severity: 'error', summary: 'Erro', detail: getMessage(error) });
        if (error.status === STATUS.UNAUTHORIZED) {
          router.navigateByUrl('/auth/login');
        }
      }

      return throwError(() => error);
    })
  );
}
