import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { LocalStorageService } from '@shared/services/storage.service';

// Appends tenant/company headers from local storage (defaults to '7')
export function tenantInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const storage = inject(LocalStorageService);
  let tenantId: any =
    storage.has('empresaId') ? storage.get('empresaId') :
    storage.has('tenantId') ? storage.get('tenantId') : '1';

  if (tenantId && typeof tenantId === 'object') {
    tenantId = tenantId.id || tenantId.tenantId || tenantId.empresaId || '1';
  }

  const cleanId = String(tenantId);
  if (!tenantId || cleanId === '' || cleanId.includes('[object') || cleanId === 'undefined') {
    tenantId = '1';
  }

  // Ensure both common header names are present for multi-tenant backends
  const headers = req.headers
    .set('X-Tenant-Id', String(tenantId));

  return next(
    req.clone({
      headers,
    })
  );
}
