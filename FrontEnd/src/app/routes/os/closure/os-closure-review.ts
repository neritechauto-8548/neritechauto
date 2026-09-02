import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { NeriTechIcon } from '../../../shared/components';
import { OrdemServicoCockpitResponse } from '../models/os-cockpit.models';

@Component({
  selector: 'os-closure-review',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, NeriTechIcon],
  templateUrl: './os-closure-review.html',
  styleUrl: './os-closure-review.scss',
})
export class OsClosureReview {
  @Input({ required: true }) cockpit!: OrdemServicoCockpitResponse;

  readonly expectedCommand = 'POST /api/v1/ordens-servico/{id}/complete-operationally';

  get blockers() { return this.cockpit?.blocks ?? []; }
  get hasPartialSources(): boolean { return Boolean(this.cockpit?.partialSources?.length); }
  get pendingApprovals(): number { return this.cockpit?.approvals?.pending ?? 0; }
  get executionProgress(): number { return Math.max(0, Math.min(100, this.cockpit?.execution?.progress ?? 0)); }
  get executionDone(): boolean {
    const status = (this.cockpit?.execution?.status ?? '').toUpperCase();
    return status === 'CONCLUIDO' || status === 'CONCLUIDA' || this.executionProgress >= 100;
  }

  get readinessLabel(): string {
    if (this.hasPartialSources) return 'Validação parcial';
    if (this.blockers.length || this.pendingApprovals > 0 || !this.executionDone) return 'Pendências operacionais';
    return 'Aguardando comando seguro';
  }
}
