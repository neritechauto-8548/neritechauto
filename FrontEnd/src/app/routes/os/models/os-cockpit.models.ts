export interface OsCockpitStage {
  code: string;
  label: string;
  severity: 'success' | 'info' | 'warning' | 'danger' | string;
  color?: string | null;
}

export interface OsCockpitNextAction {
  code: string;
  label: string;
  reason: string;
  route?: string | null;
  event?: string | null;
}

export interface OsCockpitAllowedAction {
  code: string;
  label: string;
}

export interface OsCockpitBlock {
  code: string;
  label: string;
  severity: 'success' | 'info' | 'warning' | 'danger' | string;
}

export interface OrdemServicoCockpitResponse {
  id: number;
  numero: string;
  tenantId: number;
  unitId?: number | null;
  version?: number | null;
  stage: OsCockpitStage;
  nextAction?: OsCockpitNextAction | null;
  allowedActions: OsCockpitAllowedAction[];
  customer: {
    id?: number | null;
    name?: string | null;
  };
  vehicle: {
    id?: number | null;
    plate?: string | null;
    description?: string | null;
  };
  execution: {
    status: string;
    responsibleId?: number | null;
    plannedStart?: string | null;
    plannedEnd?: string | null;
    startedAt?: string | null;
    completedAt?: string | null;
    progress: number;
  };
  parts: {
    totalItems: number;
    reservedItems?: number | null;
    missingItems?: number | null;
  };
  approvals: {
    pending: number;
    approved: number;
    rejected?: number | null;
  };
  blocks: OsCockpitBlock[];
  relatedCounts: {
    checklists: number;
    evidences: number;
    additionalRequests: number;
  };
  financial?: {
    status?: string | null;
    totalReceivable: number;
    paidAmount: number;
    remainingAmount: number;
  } | null;
  fiscal?: {
    status: string;
    documents: string[];
  } | null;
  audit: {
    createdAt?: string | null;
    updatedAt?: string | null;
    traceId?: string | null;
  };
  partialSources?: string[];
}

/**
 * RN-AUTO-OS-041: o frontend nunca promove uma ação que não esteja dentro
 * de allowedActions. O backend permanece owner da decisão de próxima ação.
 */
export function getRenderableNextAction(
  cockpit?: OrdemServicoCockpitResponse | null
): OsCockpitNextAction | null {
  const nextAction = cockpit?.nextAction;
  if (!nextAction?.code) return null;

  return cockpit?.allowedActions?.some(action => action.code === nextAction.code)
    ? nextAction
    : null;
}
