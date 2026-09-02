import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
  inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NeriTechIcon } from '../../../shared/components';
import {
  OS_PAUSE_REASONS,
  OrdemServicoExecutionResponse,
  OsExecutionAction,
  OsExecutionService,
  OsWorkSessionResponse,
  executionBlockerLabel,
  formatExecutionDuration,
} from './os-execution.service.types';
import { OsExecutionService } from './os-execution.service';

export type OsExecutionLoadState = 'idle' | 'loading' | 'ready' | 'forbidden' | 'not-found' | 'error';

@Component({
  selector: 'os-execution-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  templateUrl: './os-execution-panel.html',
  styleUrl: './os-execution-panel.scss',
})
export class OsExecutionPanel implements OnChanges, OnDestroy {
  private readonly service = inject(OsExecutionService);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  readonly pauseReasons = OS_PAUSE_REASONS;
  readonly blockerLabel = executionBlockerLabel;

  execution?: OrdemServicoExecutionResponse;
  state: OsExecutionLoadState = 'idle';
  message = '';
  commandMessage = '';
  commandConflict = false;
  busyAction?: string;
  pauseOpen = false;
  pauseReason = '';
  pauseNote = '';
  displayElapsedSeconds = 0;

  private clockHandle?: ReturnType<typeof setInterval>;
  private syncedAtLocalMs = 0;
  private syncedElapsedSeconds = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['osId'] && Number.isInteger(this.osId) && this.osId > 0) {
      this.load();
    }
  }

  ngOnDestroy(): void {
    this.stopClock();
  }

  load(): void {
    if (!Number.isInteger(this.osId) || this.osId <= 0) return;

    this.state = 'loading';
    this.message = '';
    this.commandMessage = '';
    this.commandConflict = false;
    this.stopClock();
    this.cdr.markForCheck();

    this.service.getExecution(this.osId).subscribe({
      next: execution => {
        this.execution = execution;
        this.state = 'ready';
        this.syncClock(execution.activeSession ?? null);
        this.cdr.markForCheck();
      },
      error: error => {
        this.execution = undefined;
        this.resolveLoadError(error?.status);
        this.cdr.markForCheck();
      },
    });
  }

  start(serviceId: number): void {
    if (!this.canServiceAction(serviceId, 'START')) return;
    this.runCommand(
      `start-${serviceId}`,
      this.service.start(this.osId, serviceId, this.idempotencyKey('start', serviceId))
    );
  }

  openPause(): void {
    const session = this.execution?.activeSession;
    if (!session || !this.hasSessionAction('PAUSE')) return;
    this.pauseReason = '';
    this.pauseNote = '';
    this.pauseOpen = true;
    this.commandMessage = '';
    this.cdr.markForCheck();
  }

  cancelPause(): void {
    this.pauseOpen = false;
    this.pauseReason = '';
    this.pauseNote = '';
    this.cdr.markForCheck();
  }

  confirmPause(): void {
    const session = this.execution?.activeSession;
    if (!session || !this.hasSessionAction('PAUSE') || !this.pauseReason) return;
    if (this.pauseReason === 'OUTRO' && !this.pauseNote.trim()) return;

    this.runCommand(
      `pause-${session.sessionId}`,
      this.service.pause(
        session.sessionId,
        session.sessionVersion,
        this.pauseReason,
        this.pauseNote.trim() || null,
        this.idempotencyKey('pause', session.sessionId)
      ),
      () => this.cancelPause()
    );
  }

  resume(): void {
    const session = this.execution?.activeSession;
    if (!session || !this.hasSessionAction('RESUME')) return;
    this.runCommand(
      `resume-${session.sessionId}`,
      this.service.resume(
        session.sessionId,
        session.sessionVersion,
        this.idempotencyKey('resume', session.sessionId)
      )
    );
  }

  finish(): void {
    const session = this.execution?.activeSession;
    if (!session || !this.hasSessionAction('FINISH')) return;
    this.runCommand(
      `finish-${session.sessionId}`,
      this.service.finish(
        session.sessionId,
        session.sessionVersion,
        this.idempotencyKey('finish', session.sessionId)
      )
    );
  }

  hasSessionAction(action: OsExecutionAction): boolean {
    return this.execution?.activeSession?.allowedActions?.includes(action) ?? false;
  }

  canServiceAction(serviceId: number, action: OsExecutionAction): boolean {
    return this.execution?.services
      ?.find(service => service.id === serviceId)
      ?.allowedActions?.includes(action) ?? false;
  }

  isBusy(key?: string): boolean {
    return Boolean(this.busyAction && (!key || this.busyAction === key));
  }

  formatTimer(seconds?: number | null): string {
    return formatExecutionDuration(seconds);
  }

  formatMinutes(minutes?: number | null): string {
    if (minutes === null || minutes === undefined) return 'Não disponível';
    if (minutes <= 0) return '0 min';
    const hours = Math.floor(minutes / 60);
    const rest = minutes % 60;
    if (!hours) return `${rest} min`;
    return rest ? `${hours}h ${rest}min` : `${hours}h`;
  }

  statusLabel(status?: string | null): string {
    const labels: Record<string, string> = {
      NAO_INICIADO: 'Não iniciado',
      PRONTO: 'Pronto',
      PENDENTE: 'Pendente',
      EM_EXECUCAO: 'Em execução',
      PAUSADO: 'Pausado',
      PAUSADA: 'Pausada',
      BLOQUEADO: 'Bloqueado',
      CONCLUIDO: 'Concluído',
      REPROVADO_QUALIDADE: 'Reprovado na qualidade',
      REABERTO: 'Reaberto',
      CANCELADO: 'Cancelado',
      FINALIZADA: 'Finalizada',
    };
    return status ? labels[status] ?? status : 'Não informado';
  }

  statusTone(status?: string | null): string {
    if (status === 'CONCLUIDO' || status === 'FINALIZADA') return 'success';
    if (status === 'EM_EXECUCAO') return 'info';
    if (status === 'PAUSADO' || status === 'PAUSADA' || status === 'PENDENTE') return 'warning';
    if (status === 'BLOQUEADO' || status === 'CANCELADO' || status === 'REPROVADO_QUALIDADE') return 'danger';
    return 'neutral';
  }

  trackService(_: number, service: { id: number }): number {
    return service.id;
  }

  private runCommand(
    busyKey: string,
    request: import('rxjs').Observable<OsWorkSessionResponse>,
    beforeReload?: () => void
  ): void {
    if (this.busyAction) return;

    this.busyAction = busyKey;
    this.commandMessage = '';
    this.commandConflict = false;
    this.cdr.markForCheck();

    request.subscribe({
      next: () => {
        beforeReload?.();
        this.busyAction = undefined;
        this.load();
      },
      error: error => {
        this.busyAction = undefined;
        this.commandConflict = error?.status === 409 || error?.status === 412 || error?.status === 428;
        this.commandMessage = this.extractErrorMessage(error);
        this.cdr.markForCheck();
      },
    });
  }

  private syncClock(session: OsWorkSessionResponse | null): void {
    this.stopClock();
    this.syncedElapsedSeconds = session?.elapsedSeconds ?? this.execution?.summary.elapsedSeconds ?? 0;
    this.displayElapsedSeconds = this.syncedElapsedSeconds;
    this.syncedAtLocalMs = Date.now();

    if (session?.status !== 'EM_EXECUCAO') return;

    this.clockHandle = setInterval(() => {
      const localDeltaSeconds = Math.max(0, Math.floor((Date.now() - this.syncedAtLocalMs) / 1000));
      this.displayElapsedSeconds = this.syncedElapsedSeconds + localDeltaSeconds;
      this.cdr.markForCheck();
    }, 1000);
  }

  private stopClock(): void {
    if (this.clockHandle) {
      clearInterval(this.clockHandle);
      this.clockHandle = undefined;
    }
  }

  private resolveLoadError(status?: number): void {
    if (status === 403) {
      this.state = 'forbidden';
      this.message = 'Você não possui permissão para visualizar a execução desta Ordem de Serviço.';
      return;
    }
    if (status === 404) {
      this.state = 'not-found';
      this.message = 'A execução desta Ordem de Serviço não foi encontrada.';
      return;
    }
    this.state = 'error';
    this.message = 'Não foi possível carregar a execução. Tente atualizar os dados.';
  }

  private extractErrorMessage(error: any): string {
    const code = error?.error?.errors?.code ?? error?.error?.type;
    const message = error?.error?.message;
    if (message) return message;

    const known: Record<string, string> = {
      OS_TECHNICIAN_SESSION_ACTIVE: 'Você já possui uma sessão de execução aberta.',
      OS_SERVICE_NOT_AUTHORIZED: 'Este serviço ainda não está autorizado para execução.',
      OS_SESSION_VERSION_CONFLICT: 'A sessão mudou em outro dispositivo. Atualize os dados antes de continuar.',
      OS_SESSION_NOT_ACTIVE: 'Esta sessão não está no estado necessário para a ação solicitada.',
    };
    return known[code] ?? 'Não foi possível concluir a ação de execução.';
  }

  private idempotencyKey(action: string, target: number): string {
    const randomUuid = globalThis.crypto?.randomUUID?.();
    if (randomUuid) return `os-${action}-${target}-${randomUuid}`;
    return `os-${action}-${target}-${Date.now()}-${Math.random().toString(36).slice(2, 12)}`;
  }
}
