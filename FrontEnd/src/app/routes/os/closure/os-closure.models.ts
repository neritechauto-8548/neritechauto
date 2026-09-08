export type OsClosureGuardStatus = 'OK' | 'ALERTA' | 'BLOQUEIO';

export interface OsClosureGuard {
  code: string;
  label: string;
  status: OsClosureGuardStatus;
  message: string;
  owner: string;
  route?: string | null;
  overrideAllowed: boolean;
}

export interface OsClosureReviewModel {
  ordemServicoId: number;
  numeroOS: string;
  aggregateVersion: number;
  operationalState: 'EM_REVISAO' | 'PENDENCIA_CORRECAO' | 'CONCLUIDA_OPERACIONAL' | string;
  readyToComplete: boolean;
  alreadyCompleted: boolean;
  guards: OsClosureGuard[];
  partialSources: string[];
  snapshotId?: number | null;
  operationallyCompletedAt?: string | null;
}

export interface OsClosureCommandResult {
  ordemServicoId: number;
  numeroOS: string;
  operationalState: string;
  aggregateVersion: number;
  snapshotId: number;
  operationallyCompletedAt: string;
  downstreamRequestId?: string | null;
}
