import { HttpErrorResponse, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '@core/authentication';
import { LocalStorageService } from '@shared/services/storage.service';
import { MessageService } from 'primeng/api';
import { catchError, throwError } from 'rxjs';
import { BASE_URL, hasHttpScheme } from './base-url-interceptor';

export enum STATUS {
  UNAUTHORIZED = 401,
  PAYMENT_REQUIRED = 402,
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

/**
 * Mostra o erro real retornado pela API (sem substituí-lo por um toast genérico).
 * O backend somente envia classe/causa da exceção no diagnóstico local autorizado.
 */
function serverErrorDetail(error: HttpErrorResponse): { summary: string; detail: string } | null {
  const body: unknown = error.error;
  if (!body || typeof body !== 'object' || Array.isArray(body)) {
    const fallback = typeof body === 'string' ? body.trim() : '';
    return fallback ? { summary: `HTTP ${error.status}`, detail: fallback.slice(0, 1400) } : null;
  }

  const data = body as Record<string, unknown>;
  const value = (key: string) => typeof data[key] === 'string' ? (data[key] as string).trim() : '';
  const exception = value('exception');
  const type = exception || value('type') || value('code') || error.name;
  const message = value('message') || value('detail') || value('title');
  const cause = value('cause');
  const validation = data['errors'];
  let fields = '';
  if (Array.isArray(validation)) {
    fields = validation.map(x => {
      if (!x || typeof x !== 'object') return '';
      const item = x as Record<string, unknown>;
      return `${String(item['field'] || 'Campo')}: ${String(item['message'] || '')}`;
    }).filter(Boolean).join(' | ');
  } else if (validation && typeof validation === 'object') {
    fields = Object.entries(validation as Record<string, unknown>)
      .map(([key, val]) => `${key}: ${String(val)}`).join(' | ');
  }

  const detail = [message, cause && cause !== message ? `Causa: ${cause}` : '', fields]
    .filter(Boolean).join(' | ').slice(0, 1400);
  return detail ? { summary: `HTTP ${error.status} • ${type}`, detail } : null;
}

export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const router = inject(Router);
  const messages = inject(MessageService);
  const tokens = inject(TokenService);
  const storage = inject(LocalStorageService);
  const baseUrl = inject(BASE_URL, { optional: true });
  // Uma API externa não pode invalidar a sessão da NERITECH AUTO.
  const internalRequest = !hasHttpScheme(req.url) ||
    !!(baseUrl && hasHttpScheme(baseUrl) &&
      (req.url === baseUrl || req.url.startsWith(`${baseUrl.replace(/\/$/, '')}/`)));
  const skipToast = req.headers.has('X-Skip-Toast');
  const isLogin = /\/auth\/login(?:\?|$)/.test(req.url);
  const isPublicAuth = /\/auth\/(?:login|recover-password|reset-password)(?:\?|$)/.test(req.url);

  const outgoing = skipToast ? req.clone({ headers: req.headers.delete('X-Skip-Toast') }) : req;
  return next(outgoing).pipe(catchError((error: HttpErrorResponse) => {
    if (!(error instanceof HttpErrorResponse)) return throwError(() => error);

    if (error.status === STATUS.UNAUTHORIZED && !isPublicAuth && internalRequest) {
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

    if (error.status === STATUS.PAYMENT_REQUIRED && internalRequest) {
      if (!router.url.startsWith('/configuracoes/assinatura')) {
        if (!skipToast) messages.add({ severity: 'warn', summary: 'Teste encerrado', detail: 'Escolha um plano para continuar.' });
        router.navigateByUrl('/configuracoes/assinatura');
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
    } else if (error.status === STATUS.UNPROCESSABLE_ENTITY) {
      // O corpo original permanece no HttpErrorResponse para que os formulários
      // associem erros 422 aos campos; o toast nunca revela detalhes internos.
      summary = 'Verifique os campos';
      detail = 'Existem dados inválidos. Corrija os campos indicados e tente novamente.';
    } else if (error.status === STATUS.CONFLICT) {
      summary = 'Alteração em conflito';
      detail = 'O registro pode ter sido alterado por outra pessoa. Atualize os dados antes de tentar novamente.';
    } else if (error.status === STATUS.BAD_REQUEST) {
      summary = 'Solicitação inválida';
      detail = 'Confira os dados da solicitação e tente novamente.';
    } else {
      detail = 'Tente novamente. Se o problema persistir, contate o suporte.';
    }

    // Na fase de desenvolvimento não esconder o motivo original em mensagens genéricas.
    // Classe e causa só chegam quando o backend habilita diagnóstico local explicitamente.
    if (!isLogin && error.status !== 0) {
      const server = serverErrorDetail(error);
      if (server) {
        summary = server.summary;
        detail = server.detail;
      }
    }

    // O objeto HTTP completo continua disponível para inspeção (status, corpo, campos).
    // Não registrar headers nem tokens de autenticação.
    if (!isLogin && error.status !== 0 && error.error?.exception) {
      console.error('[NERITECH] Exceção da API', {
        status: error.status,
        url: error.url,
        exception: error.error.exception,
        cause: error.error.cause,
        message: error.error.message,
      });
    }

    messages.add({ severity, summary, detail, sticky: error.status === 0 || error.status >= 500 });
    return throwError(() => error);
  }));
}
