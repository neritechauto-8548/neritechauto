import { HttpHandlerFn, HttpRequest } from '@angular/common/http';

/**
 * Compatibilidade temporaria para imports legados.
 *
 * A identidade da empresa nunca pode ser escolhida pelo navegador. O backend
 * deriva e autoriza o tenant a partir da sessao autenticada, portanto este
 * interceptor nao adiciona cabecalhos nem consulta qualquer armazenamento.
 */
export function tenantInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  return next(req);
}
