import { LocalStorageService } from '@shared/services/storage.service';

/**
 * Context only for composing UI requests, never authorization. The backend
 * always checks the authenticated JWT tenant against these values.
 */
export function empresaIdAtual(storage: LocalStorageService): number | null {
  const normalized = (input: unknown): number | null => {
    const raw = input && typeof input === 'object'
      ? (input as { id?: unknown; empresaId?: unknown; tenantId?: unknown }).empresaId
        ?? (input as { id?: unknown }).id
        ?? (input as { tenantId?: unknown }).tenantId
      : input;
    const num = Number(raw);
    return raw !== null && raw !== undefined && raw !== '' && Number.isSafeInteger(num) && num > 0 ? num : null;
  };
  if (typeof localStorage !== 'undefined') {
    try {
      const rawToken = localStorage.getItem('ng-matero-token');
      const auth = rawToken ? JSON.parse(rawToken) : null;
      const token = auth?.access_token || auth?.accessToken;
      if (typeof token === 'string' && token.includes('.')) {
        const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
        const fromToken = normalized(payload?.empresaId ?? payload?.tenantId);
        if (fromToken) return fromToken;
      }
    } catch {
      // Invalid cached token; never guess another company's identifier.
    }
  }
  return normalized(storage.has('empresaId') ? storage.get('empresaId')
    : storage.has('tenantId') ? storage.get('tenantId') : null);
}
