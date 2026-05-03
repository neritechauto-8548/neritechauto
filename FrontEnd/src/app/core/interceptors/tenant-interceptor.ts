import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { LocalStorageService } from '@shared/services/storage.service';

// Appends tenant/company headers from local storage (defaults to '7')
export function tenantInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const storage = inject(LocalStorageService);
  let tenantId: any = null;

  // 1. Tenta extrair o tenantId diretamente do token JWT no localStorage
  const tokenStr = localStorage.getItem('ng-matero-token');
  if (tokenStr) {
    try {
      const tokenObj = JSON.parse(tokenStr);
      if (tokenObj && tokenObj.access_token) {
        const payloadPart = tokenObj.access_token.split('.')[1];
        const payload = JSON.parse(atob(payloadPart.replace(/-/g, '+').replace(/_/g, '/')));
        tenantId = payload.empresaId || payload.tenantId;
      }
    } catch (e) {
      // Silenciosamente falha se o token não for um JWT válido
    }
  }

  // 2. Se não encontrou no token, busca no storage (fallback)
  if (!tenantId) {
    tenantId =
      storage.has('empresaId') ? storage.get('empresaId') :
      storage.has('tenantId') ? storage.get('tenantId') : null;
  }

  if (tenantId && typeof tenantId === 'object') {
    tenantId = tenantId.id || tenantId.tenantId || tenantId.empresaId || null;
  }

  const cleanId = String(tenantId);
  
  // Evita adicionar cabeçalhos em requisições externas (Ex: ViaCEP)
  const isInternal = req.url.includes('/api/') || !req.url.startsWith('http');
  if (!isInternal) {
    return next(req);
  }

  let headers = req.headers;
  
  if (tenantId && cleanId !== '' && !cleanId.includes('[object') && cleanId !== 'undefined') {
    headers = req.headers.set('X-Tenant-Id', cleanId);
  }

  return next(
    req.clone({
      headers,
    })
  );
}
