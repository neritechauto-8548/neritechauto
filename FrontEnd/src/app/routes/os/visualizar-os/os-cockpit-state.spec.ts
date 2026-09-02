import {
  authoritativeOsTotal,
  deriveCockpitNextAction,
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

  it('prioriza fonte parcial antes de sugerir mutacao operacional', () => {
    const next = deriveCockpitNextAction({
      partialSources: true,
      os: { consultorResponsavelId: 10, dataInicioExecucao: '2026-09-02T08:00:00' },
    });

    expect(next.actionKey).toBe('refresh');
    expect(next.blocking).toBeTrue();
  });

  it('pede responsavel antes de iniciar execucao', () => {
    const next = deriveCockpitNextAction({ os: {} });

    expect(next.actionKey).toBe('assign-owner');
    expect(next.stage).toBe('preparacao');
  });

  it('prioriza aprovacao pendente antes de checklist e execucao', () => {
    const next = deriveCockpitNextAction({
      os: { consultorResponsavelId: 10 },
      pendingApprovalCount: 2,
      checklistPendingCount: 3,
    });

    expect(next.actionKey).toBe('review-approvals');
    expect(next.stage).toBe('autorizacao');
  });

  it('orienta checklist quando nao ha aprovacao pendente', () => {
    const next = deriveCockpitNextAction({
      os: { mecanicoResponsavelId: 20 },
      checklistPendingCount: 1,
    });

    expect(next.actionKey).toBe('complete-checklist');
    expect(next.blocking).toBeTrue();
  });

  it('encaminha OS finalizada para financeiro sem criar pagamento no cockpit', () => {
    const next = deriveCockpitNextAction({
      os: { consultorResponsavelId: 10, dataInicioExecucao: '2026-09-02T08:00:00', dataFimExecucao: '2026-09-02T10:00:00' },
      finalized: true,
      paid: false,
    });

    expect(next.actionKey).toBe('open-finance');
    expect(next.stage).toBe('financeiro');
  });

  it('considera concluida apenas quando finalizacao pagamento e entrega existem', () => {
    const next = deriveCockpitNextAction({
      os: {
        consultorResponsavelId: 10,
        dataInicioExecucao: '2026-09-02T08:00:00',
        dataFimExecucao: '2026-09-02T10:00:00',
        dataEntrega: '2026-09-02T11:00:00',
      },
      finalized: true,
      paid: true,
    });

    expect(next.actionKey).toBe('completed');
    expect(next.stage).toBe('concluida');
  });
});
