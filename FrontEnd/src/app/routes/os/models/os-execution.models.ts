export type OsWorkSessionStatus = 'EM_EXECUCAO' | 'PAUSADA' | 'FINALIZADA';
export type OsExecutionAction = 'START' | 'PAUSE' | 'RESUME' | 'FINISH';

export interface OsWorkSessionResponse {
  sessionId: number;
  ordemServicoId: number;
  serviceId: number;
  technicianId: number;
  technicianName: string;
  status: OsWorkSessionStatus;
  source: string;
  startedAt: string;
  pausedAt?: string | null;
  endedAt?: string | null;
  pauseReason?: string | null;
  pauseNote?: string | null;
  elapsedSeconds: number;
  sessionVersion: number;
  serverTime: string;
  serviceStatus?: string | null;
  blockers: string[];
  allowedActions: OsExecutionAction[];
}

export interface OsExecutionService {
  id: number;
  catalogServiceId?: number | null;
  description?: string | null;
  technicianId?: number | null;
  status: string;
  authorized: boolean;
  estimatedMinutes?: number | null;
  realMinutes?: number | null;
  soldMinutes?: number | null;
  startedAt?: string | null;
  completedAt?: string | null;
  blockers: string[];
  allowedActions: OsExecutionAction[];
}

export interface OrdemServicoExecutionResponse {
  ordemServicoId: number;
  numero: string;
  serverTime: string;
  summary: {
    totalServices: number;
    inProgressServices: number;
    elapsedSeconds: number;
    estimatedMinutes: number;
    soldMinutes?: number | null;
    blockers: number;
  };
  activeSession?: OsWorkSessionResponse | null;
  services: OsExecutionService[];
}

export const OS_PAUSE_REASONS = [
  { value: 'ALMOCO_INTERVALO', label: 'Almoço / intervalo' },
  { value: 'AGUARDANDO_PECA', label: 'Aguardando peça' },
  { value: 'AGUARDANDO_CLIENTE', label: 'Aguardando cliente' },
  { value: 'FALHA_TECNICA', label: 'Falha técnica' },
  { value: 'SEGURANCA', label: 'Segurança' },
  { value: 'TROCA_PRIORIDADE', label: 'Troca de prioridade' },
  { value: 'AUSENCIA', label: 'Ausência' },
  { value: 'SISTEMA', label: 'Sistema' },
  { value: 'OUTRO', label: 'Outro' },
] as const;

export function formatExecutionDuration(totalSeconds?: number | null): string {
  const safeSeconds = Math.max(0, Math.floor(totalSeconds ?? 0));
  const hours = Math.floor(safeSeconds / 3600);
  const minutes = Math.floor((safeSeconds % 3600) / 60);
  const seconds = safeSeconds % 60;
  return [hours, minutes, seconds].map(value => String(value).padStart(2, '0')).join(':');
}

export function executionBlockerLabel(code: string): string {
  const labels: Record<string, string> = {
    OS_SERVICE_NOT_AUTHORIZED: 'Aguardando autorização do serviço',
    OS_SERVICE_CANCELLED: 'Serviço cancelado',
    OS_TECHNICIAN_SESSION_ACTIVE: 'Você já possui outra sessão de execução aberta',
  };
  return labels[code] ?? code;
}
