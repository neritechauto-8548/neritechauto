import { OsClosureReview } from './os-closure-review';
import { OrdemServicoCockpitResponse } from '../models/os-cockpit.models';

function cockpit(overrides: Partial<OrdemServicoCockpitResponse> = {}): OrdemServicoCockpitResponse {
  return {
    id: 10,
    numero: 'OS-10',
    tenantId: 42,
    stage: { code: 'EM_EXECUCAO', label: 'Em execução', severity: 'info' },
    nextAction: null,
    allowedActions: [],
    customer: {},
    vehicle: {},
    execution: { status: 'EM_EXECUCAO', progress: 70 },
    parts: { totalItems: 0 },
    approvals: { pending: 0, approved: 0 },
    blocks: [],
    relatedCounts: { checklists: 0, evidences: 0, additionalRequests: 0 },
    audit: {},
    ...overrides,
  };
}

describe('OsClosureReview', () => {
  it('prioriza fonte parcial como impeditivo de avaliação segura', () => {
    const component = new OsClosureReview();
    component.cockpit = cockpit({ partialSources: ['estoque'] });
    expect(component.readinessLabel).toBe('Validação parcial');
  });

  it('mantém pendência quando execução ou blockers não estão resolvidos', () => {
    const component = new OsClosureReview();
    component.cockpit = cockpit({
      blocks: [{ code: 'OS_CHECKLIST_INCOMPLETE', label: 'Checklist incompleto', severity: 'warning' }],
    });
    expect(component.readinessLabel).toBe('Pendências operacionais');
  });

  it('não inventa ação de fechamento mesmo sem blocker explícito', () => {
    const component = new OsClosureReview();
    component.cockpit = cockpit({ execution: { status: 'CONCLUIDO', progress: 100 } });
    expect(component.readinessLabel).toBe('Aguardando comando seguro');
    expect(component.expectedCommand).toContain('/complete-operationally');
  });
});
