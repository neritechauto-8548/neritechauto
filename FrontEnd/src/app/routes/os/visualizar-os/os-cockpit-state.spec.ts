import {
  authoritativeOsTotal,
  resolveCockpitLoadError,
  resolveItemState,
} from './os-cockpit-state';

describe('os cockpit state', () => {
  it('representa 403, 404 e 409 sem fallback ficticio', () => {
    expect(resolveCockpitLoadError(403).state).toBe('forbidden');
    expect(resolveCockpitLoadError(404).state).toBe('not-found');
    expect(resolveCockpitLoadError(409).state).toBe('conflict');
    expect(resolveCockpitLoadError(500).state).toBe('error');
  });

  it('nao inventa recusa quando aprovadoCliente e falso ou ausente', () => {
    expect(resolveItemState(true)).toBe('aprovado');
    expect(resolveItemState(false)).toBe('aguardando');
    expect(resolveItemState(undefined)).toBe('aguardando');
  });

  it('usa valorTotal da OS como verdade financeira do cockpit', () => {
    expect(authoritativeOsTotal({ valorTotal: 1234.56 })).toBe(1234.56);
    expect(authoritativeOsTotal({ valorTotal: Number.NaN })).toBe(0);
    expect(authoritativeOsTotal(undefined)).toBe(0);
  });
});
