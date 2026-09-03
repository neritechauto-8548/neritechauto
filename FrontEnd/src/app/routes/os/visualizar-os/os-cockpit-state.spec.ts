import { getRenderableNextAction, OrdemServicoCockpitResponse } from '../models/os-cockpit.models';
import {
  authoritativeOsTotal,
  resolveCockpitLoadError,
  resolveItemState,
} from './os-cockpit-state';

const cockpit = (
  nextAction: OrdemServicoCockpitResponse['nextAction'],
  allowedActions: OrdemServicoCockpitResponse['allowedActions']
): OrdemServicoCockpitResponse => ({
  id: 10,
  numero: 'OS-10',
  tenantId: 1,
  stage: { code: 'ABERTA', label: 'Aberta', severity: 'info' },
  nextAction,
  allowedActions,
  customer: {},
  vehicle: {},
  execution: { status: 'NOT_STARTED', progress: 0 },
  parts: { totalItems: 0 },
  approvals: { pending: 0, approved: 0 },
  blocks: [],
  relatedCounts: { checklists: 0, evidences: 0, additionalRequests: 0 },
  audit: {},
});

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

  it('usa valorTotal da OS como verdade financeira das seções legadas', () => {
    expect(authoritativeOsTotal({ valorTotal: 1234.56 })).toBe(1234.56);
    expect(authoritativeOsTotal({ valorTotal: Number.NaN })).toBe(0);
    expect(authoritativeOsTotal(undefined)).toBe(0);
  });

  it('renderiza nextAction somente quando o backend também a autoriza', () => {
    const model = cockpit(
      {
        code: 'START_EXECUTION',
        label: 'Iniciar execução',
        reason: 'OS pronta para execução',
        event: 'os.start-execution',
      },
      [{ code: 'START_EXECUTION', label: 'Iniciar execução' }]
    );

    expect(getRenderableNextAction(model)?.code).toBe('START_EXECUTION');
  });

  it('oculta nextAction inconsistente com allowedActions', () => {
    const model = cockpit(
      {
        code: 'START_EXECUTION',
        label: 'Iniciar execução',
        reason: 'OS pronta para execução',
      },
      [{ code: 'ADD_EVIDENCE', label: 'Adicionar evidência' }]
    );

    expect(getRenderableNextAction(model)).toBeNull();
  });

  it('nao inventa próxima ação quando backend retorna null', () => {
    expect(getRenderableNextAction(cockpit(null, []))).toBeNull();
  });
});
