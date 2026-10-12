import { HttpErrorResponse, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '@core/authentication';
import { LocalStorageService } from '@shared/services/storage.service';
import { MessageService } from 'primeng/api';
import { catchError, throwError } from 'rxjs';

export enum STATUS {
  UNAUTHORIZED = 401,
  FORBIDDEN = 403,
  NOT_FOUND = 404,
  UNPROCESSABLE_ENTITY = 422,
  CONFLICT = 409,
  INTERNAL_SERVER_ERROR = 500,
  BAD_REQUEST = 400,
}

export interface ValidationError {
  field: string;
  message: string;
}

export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const router = inject(Router);
  const messages = inject(MessageService);
  const tokens = inject(TokenService);
  const storage = inject(LocalStorageService);
  const skipToast = req.headers.has('X-Skip-Toast');
  const isLogin = /\/auth\/login(?:\?|$)/.test(req.url);
  const isPublicAuth = /\/auth\/(?:login|recover-password|reset-password)(?:\?|$)/.test(req.url);

  const outgoing = skipToast ? req.clone({ headers: req.headers.delete('X-Skip-Toast') }) : req;
  return next(outgoing).pipe(catchError((error: HttpErrorResponse) => {
    if (!(error instanceof HttpErrorResponse)) return throwError(() => error);

    if (error.status === STATUS.UNAUTHORIZED && !isPublicAuth) {
      tokens.clear();
      storage.remove('tenantId');
      storage.remove('empresaId');
      if (!router.url.startsWith('/auth/')) {
        if (!skipToast) messages.add({ severity: 'warn', summary: 'Sessão expirada', detail: 'Entre novamente para continuar.', sticky: true });
        const returnUrl = router.url.startsWith('/') ? router.url : '/home';
        router.navigateByUrl(router.createUrlTree(['/auth/login'], { queryParams: { returnUrl } }));
      }
      return throwError(() => error);
    }

    if (skipToast) return throwError(() => error);

    let detail: string;
    let severity = 'error';
    let summary = 'Não foi possível concluir';
    if (isLogin && error.status === STATUS.UNAUTHORIZED) {
      detail = 'Credenciais inválidas.';
    } else if (error.status === 0) {
      detail = 'Sem conexão com o servidor. Verifique sua rede e tente novamente.';
    } else if (error.status === STATUS.FORBIDDEN) {
      severity = 'warn';
      summary = 'Acesso negado';
      detail = 'Seu perfil não possui permissão para esta operação.';
    } else if (error.status === STATUS.NOT_FOUND) {
      detail = 'O recurso solicitado não foi encontrado.';
    } else if (error.status >= 500) {
      detail = 'O servidor não conseguiu concluir a operação. Tente novamente mais tarde.';
    } else if (error.status === STATUS.UNPROCESSABLE_ENTITY || error.status === STATUS.BAD_REQUEST || error.status === STATUS.CONFLICT) {
      const body = error.error;
      detail = typeof body?.message === 'string' && body.message.trim()
        ? body.message : 'Confira os dados informados e tente novamente.';
    } else {
      detail = 'Tente novamente. Se o problema persistir, contate o suporte.';
    }

    messages.add({ severity, summary, detail, sticky: error.status === 0 || error.status >= 500 });
    return throwError(() => error);
  }));
}
