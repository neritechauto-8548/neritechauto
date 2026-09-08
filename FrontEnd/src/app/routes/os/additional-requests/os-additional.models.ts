export type OsAdditionalStatus =
  | 'RASCUNHO' | 'PRONTA_PARA_ENVIO' | 'PENDENTE' | 'VISUALIZADA'
  | 'APROVADA' | 'PARCIAL' | 'RECUSADA' | 'EXPIRADA' | 'REVOGADA'
  | 'SUBSTITUIDA' | 'CANCELADA';

export type OsAdditionalOperation = 'ADD' | 'UPDATE' | 'REMOVE';
export type OsAdditionalItemType = 'SERVICE' | 'PRODUCT' | 'OTHER';
export type OsAdditionalDecision = 'PENDING' | 'APPROVED' | 'REJECTED';

export interface OsAdditionalItemDraft {
  operation: OsAdditionalOperation;
  itemType: OsAdditionalItemType;
  sourceItemId?: number | null;
  catalogItemId?: number | null;
  description: string;
  quantity: number;
  unit?: string | null;
  amountDelta?: number | null;
  timeDeltaMinutes?: number | null;
}

export interface OsAdditionalCreateRequest {
  title: string;
  reason: string;
  items: OsAdditionalItemDraft[];
}

export interface OsAdditionalSubmitRequest {
  recipientName: string;
  channel: string;
  recipientMasked: string;
  expiresAt: string;
}

export interface OsAdditionalItem {
  id: number;
  operation: OsAdditionalOperation;
  itemType: OsAdditionalItemType;
  sourceItemId?: number | null;
  catalogItemId?: number | null;
  description: string;
  quantity: number;
  unit?: string | null;
  amountDelta: number;
  timeDeltaMinutes: number;
  decision: OsAdditionalDecision;
  decisionComment?: string | null;
}

export interface OsAdditionalRequest {
  id: number;
  ordemServicoId: number;
  baseOsVersion?: number | null;
  title: string;
  reason: string;
  status: OsAdditionalStatus;
  amountDelta: number;
  timeDeltaMinutes: number;
  recipientName?: string | null;
  recipientChannel?: string | null;
  recipientMasked?: string | null;
  tokenExpiresAt?: string | null;
  submittedAt?: string | null;
  viewedAt?: string | null;
  decidedAt?: string | null;
  revokedAt?: string | null;
  version?: number | null;
  createdAt?: string | null;
  items: OsAdditionalItem[];
  allowedActions: string[];
}

export interface OsAdditionalSubmitResponse {
  request: OsAdditionalRequest;
  approvalToken: string;
}

export interface OsAdditionalPublicRequest {
  requestId: number;
  orderNumber: string;
  title: string;
  reason: string;
  status: OsAdditionalStatus;
  amountDelta: number;
  timeDeltaMinutes: number;
  expiresAt?: string | null;
  items: OsAdditionalItem[];
}

export interface OsAdditionalPublicDecision {
  items: Array<{ itemId: number; decision: 'APPROVED' | 'REJECTED'; comment?: string | null }>;
}

export type OsAdditionalLoadState = 'idle' | 'loading' | 'ready' | 'empty' | 'forbidden' | 'error';

export const OS_ADDITIONAL_STATUS_LABELS: Record<OsAdditionalStatus, string> = {
  RASCUNHO: 'Rascunho', PRONTA_PARA_ENVIO: 'Pronta para envio', PENDENTE: 'Aguardando cliente',
  VISUALIZADA: 'Visualizada', APROVADA: 'Aprovada', PARCIAL: 'Aprovada parcialmente',
  RECUSADA: 'Recusada', EXPIRADA: 'Expirada', REVOGADA: 'Revogada', SUBSTITUIDA: 'Substituída', CANCELADA: 'Cancelada',
};
