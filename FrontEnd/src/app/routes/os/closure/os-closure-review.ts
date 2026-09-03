import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, inject } from '@angular/core';
import { Router } from '@angular/router';
import { NeriTechIcon } from '../../../shared/components';
import { OrdemServicoCockpitResponse } from '../models/os-cockpit.models';
import { OsClosureGuard, OsClosureReviewModel } from './os-closure.models';
import { OsClosureService } from './os-closure.service';

@Component({
  selector: 'os-closure-review',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, NeriTechIcon],
  templateUrl: './os-closure-review.html',
  styleUrl: './os-closure-review.scss',
})
export class OsClosureReview implements OnChanges {
  private readonly closureService = inject(OsClosureService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) cockpit!: OrdemServicoCockpitResponse;

  closureReview?: OsClosureReviewModel;
  loadingReview = false;
  validating = false;
  completing = false;
  feedback = '';
  feedbackType: 'success' | 'warning' | 'error' | '' = '';
  private commandKey?: string;

  ngOnChanges(): void {
    if (this.cockpit?.id) this.loadReview();
  }

  get blockers(): OsClosureGuard[] {
    return this.closureReview?.guards?.filter(guard => guard.status === 'BLOQUEIO') ?? [];
  }

  get alerts(): OsClosureGuard[] {
    return this.closureReview?.guards?.filter(guard => guard.status === 'ALERTA') ?? [];
  }

  get okGuards(): number {
    return this.closureReview?.guards?.filter(guard => guard.status === 'OK').length ?? 0;
  }

  get hasPartialSources(): boolean {
    return Boolean(this.closureReview?.partialSources?.length || this.cockpit?.partialSources?.length);
  }

  get pendingApprovals(): number {
    return this.cockpit?.approvals?.pending ?? 0;
  }

  get executionProgress(): number {
    return Math.max(0, Math.min(100, this.cockpit?.execution?.progress ?? 0));
  }

  get executionDone(): boolean {
    const status = (this.cockpit?.execution?.status ?? '').toUpperCase();
    return status === 'COMPLETED'
      || status === 'CONCLUIDO'
      || status === 'CONCLUIDA'
      || this.executionProgress >= 100;
  }

  get readinessLabel(): string {
    if (this.loadingReview) return 'Calculando guardas';
    if (!this.closureReview) return 'Revisão indisponível';
    if (this.closureReview.alreadyCompleted) return 'Concluída operacionalmente';
    if (this.closureReview.readyToComplete) return 'Pronta para conclusão';
    if (this.hasPartialSources) return 'Validação parcial';
    return 'Pendências operacionais';
  }

  get canComplete(): boolean {
    return Boolean(
      this.closureReview?.readyToComplete
      && !this.closureReview?.alreadyCompleted
      && Number.isInteger(this.closureReview?.aggregateVersion)
      && !this.completing
      && !this.validating
    );
  }

  loadReview(): void {
    if (!this.cockpit?.id) return;
    this.loadingReview = true;
    this.feedback = '';
    this.feedbackType = '';
    this.closureService.review(this.cockpit.id).subscribe({
      next: review => {
        this.closureReview = review;
        this.loadingReview = false;
        this.cdr.markForCheck();
      },
      error: error => {
        this.loadingReview = false;
        this.feedbackType = 'error';
        this.feedback = this.resolveError(error, 'Não foi possível carregar a revisão autoritativa do fechamento.');
        this.cdr.markForCheck();
      },
    });
  }

  revalidate(): void {
    if (!this.cockpit?.id || this.validating) return;
    this.validating = true;
    this.feedback = '';
    this.feedbackType = '';
    this.closureService.validate(this.cockpit.id).subscribe({
      next: review => {
        this.closureReview = review;
        this.validating = false;
        this.feedbackType = review.readyToComplete ? 'success' : 'warning';
        this.feedback = review.readyToComplete
          ? 'Guardas revalidadas. A OS está pronta para conclusão operacional.'
          : 'A revisão foi atualizada e ainda existem pendências.';
        this.cdr.markForCheck();
      },
      error: error => {
        this.validating = false;
        this.feedbackType = 'error';
        this.feedback = this.resolveError(error, 'Não foi possível revalidar o fechamento.');
        this.cdr.markForCheck();
      },
    });
  }

  completeOperationally(): void {
    if (!this.canComplete || !this.closureReview) return;
    this.completing = true;
    this.feedback = '';
    this.feedbackType = '';
    this.commandKey ??= this.newIdempotencyKey();

    this.closureService.completeOperationally(
      this.closureReview.ordemServicoId,
      this.closureReview.aggregateVersion,
      this.commandKey
    ).subscribe({
      next: result => {
        this.completing = false;
        this.commandKey = undefined;
        this.feedbackType = 'success';
        this.feedback = `OS #${result.numeroOS} concluída operacionalmente. O faturamento permanece separado.`;
        this.loadReviewPreservingFeedback();
      },
      error: error => {
        this.completing = false;
        const status = Number(error?.status ?? 0);
        const code = error?.error?.code ?? error?.error?.errorCode;
        if (status === 409 && code === 'OS_VERSION_CONFLICT') {
          this.commandKey = undefined;
          this.feedbackType = 'warning';
          this.feedback = 'A OS mudou desde a última revisão. As guardas serão recalculadas antes de uma nova tentativa.';
          this.revalidatePreservingFeedback();
        } else {
          this.feedbackType = status === 422 ? 'warning' : 'error';
          this.feedback = this.resolveError(error, 'Não foi possível concluir operacionalmente a OS.');
          this.cdr.markForCheck();
        }
      },
    });
  }

  goToGuard(guard: OsClosureGuard): void {
    if (!guard.route) return;
    void this.router.navigateByUrl(guard.route);
  }

  private loadReviewPreservingFeedback(): void {
    const feedback = this.feedback;
    const feedbackType = this.feedbackType;
    this.loadingReview = true;
    this.closureService.review(this.cockpit.id).subscribe({
      next: review => {
        this.closureReview = review;
        this.loadingReview = false;
        this.feedback = feedback;
        this.feedbackType = feedbackType;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loadingReview = false;
        this.feedback = feedback;
        this.feedbackType = feedbackType;
        this.cdr.markForCheck();
      },
    });
  }

  private revalidatePreservingFeedback(): void {
    const feedback = this.feedback;
    const feedbackType = this.feedbackType;
    this.validating = true;
    this.closureService.validate(this.cockpit.id).subscribe({
      next: review => {
        this.closureReview = review;
        this.validating = false;
        this.feedback = feedback;
        this.feedbackType = feedbackType;
        this.cdr.markForCheck();
      },
      error: () => {
        this.validating = false;
        this.feedback = feedback;
        this.feedbackType = feedbackType;
        this.cdr.markForCheck();
      },
    });
  }

  private newIdempotencyKey(): string {
    if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
      return crypto.randomUUID();
    }
    return `os-close-${this.cockpit.id}-${Date.now()}-${Math.random().toString(36).slice(2)}`;
  }

  private resolveError(error: any, fallback: string): string {
    return error?.error?.message || error?.error?.detail || error?.message || fallback;
  }
}
