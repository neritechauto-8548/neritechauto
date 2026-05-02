import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { LocalStorageService } from '@shared/services/storage.service';

// Appends tenant/company headers from local storage (defaults to '7')
export function tenantInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const storage = inject(LocalStorageService);
  let tenantId: any =
    storage.has('empresaId') ? storage.get('empresaId') :
    storage.has('tenantId') ? storage.get('tenantId') : null;

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
