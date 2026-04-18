import { HttpErrorResponse, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
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
  const toast = inject(HotToastService);
  const errorPages = [STATUS.FORBIDDEN, STATUS.NOT_FOUND, STATUS.INTERNAL_SERVER_ERROR];

  const getMessage = (error: HttpErrorResponse): string => {
    // Prioridade 1: Mensagem direta do backend
    if (error.error?.message) {
      return error.error.message;
    }
    // Prioridade 2: Mensagem alternativa
    if (error.error?.msg) {
      return error.error.msg;
    }
    // Prioridade 3: Erros de validação (422) - compilar mensagens dos campos
    if (error.status === STATUS.UNPROCESSABLE_ENTITY && error.error?.details) {
      const details = error.error.details as ValidationError[];
      if (Array.isArray(details) && details.length > 0) {
        const messages = details.map(d => `${d.field}: ${d.message}`).join('; ');
        return `Erros de validação: ${messages}`;
      }
    }
    // Fallback: status e statusText
    return `${error.status} ${error.statusText}`;
  };

  const getValidationErrors = (error: HttpErrorResponse): ValidationError[] => {
    if (error.status === STATUS.UNPROCESSABLE_ENTITY && error.error?.details) {
      const details = error.error.details;
      if (Array.isArray(details)) {
        return details as ValidationError[];
      }
    }
    return [];
  };

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const isApi = req.url.includes('/api/');

      if (isApi) {
        console.error('API ERROR', error);

        // Extrair erros do formato ApiResponse { errors: string[] }
        const apiErrors = error.error?.errors;

        if (error.status === STATUS.UNAUTHORIZED) {
          const isLoginRequest = req.url.includes('/api/auth/login');
          if (isLoginRequest) {
            toast.error('Credenciais inválidas');
          } else {
            router.navigateByUrl('/auth/login');
          }
        } else if (error.status === STATUS.UNPROCESSABLE_ENTITY) {
          // Erros de validação
          if (Array.isArray(apiErrors) && apiErrors.length > 0) {
             // Backend novo retorna lista de strings "Campo: Erro"
             apiErrors.forEach((err: string) => {
                toast.error(err, { duration: 5000, position: 'top-right' });
             });
          } else {
             const validationErrors = getValidationErrors(error);
             if (validationErrors.length > 0) {
                validationErrors.forEach(err => {
                  toast.error(`${err.field}: ${err.message}`, { duration: 5000, position: 'top-right' });
                });
             } else {
                toast.error(getMessage(error));
             }
          }
        } else {
           // Outros erros
           if (Array.isArray(apiErrors) && apiErrors.length > 0) {
              apiErrors.forEach((err: string) => toast.error(err));
           } else {
              toast.error(getMessage(error));
           }
        }
        return throwError(() => error);
      }

      // Para erros de navegação/rotas
      if (errorPages.includes(error.status)) {
        router.navigateByUrl(`/${error.status}`, { skipLocationChange: true });
      } else {
        console.error('ERROR', error);
        toast.error(getMessage(error));
        if (error.status === STATUS.UNAUTHORIZED) {
          router.navigateByUrl('/auth/login');
        }
      }

      return throwError(() => error);
    })
  );
}
