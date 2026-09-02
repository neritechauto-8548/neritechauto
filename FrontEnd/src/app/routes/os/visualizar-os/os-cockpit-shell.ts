import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrdemServicoCockpitResponse } from '../models/os-cockpit.models';
import { OrdemServicoService } from '../ordem-servico.service';
import { OsCockpitOverview } from './os-cockpit-overview';
import { CockpitLoadState, resolveCockpitLoadError } from './os-cockpit-state';
import { VisualizarOS } from './visualizar-os';

@Component({
  selector: 'os-cockpit-shell',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, OsCockpitOverview, VisualizarOS],
  template: `
    <main class="canonical-cockpit-shell">
      <div *ngIf="state === 'loading'" class="shell-state" role="status" aria-live="polite">
        <div class="skeleton title"></div>
        <div class="skeleton strip"></div>
        <div class="skeleton cards"></div>
        <span>Carregando visão 360 da Ordem de Serviço…</span>
      </div>

      <div
        *ngIf="state === 'forbidden' || state === 'not-found' || state === 'conflict' || state === 'error'"
        class="shell-error"
        [class.is-conflict]="state === 'conflict'"
        role="alert"
      >
        <strong>{{ state === 'conflict' ? 'Dados desatualizados' : 'Não foi possível abrir a Ordem de Serviço' }}</strong>
        <span>{{ message }}</span>
        <button type="button" *ngIf="osId" (click)="load(osId)">Atualizar</button>
      </div>

      <ng-container *ngIf="cockpit && state === 'ready'">
        <os-cockpit-overview [cockpit]="cockpit" />

        <div class="details-heading">
          <strong>Detalhes operacionais</strong>
          <span>Itens, inspeções, evidências, observações e informações relacionadas à execução.</span>
        </div>

        <section class="legacy-detail-sections" aria-label="Detalhes operacionais da Ordem de Serviço">
          <visualizar-os />
        </section>
      </ng-container>
    </main>
  `,
  styles: `
    :host { display:block; }
    .canonical-cockpit-shell { min-height:100%; background:#f8fafc; padding-top:1rem; }
    .shell-state,.shell-error { margin:1rem 1.5rem; border:1px solid #dbeafe; border-radius:14px; background:#eff6ff; padding:1rem; color:#1e40af; }
    .shell-state { display:grid; gap:.65rem; }
    .shell-state span { font-size:.78rem; font-weight:700; }
    .skeleton { border-radius:8px; background:linear-gradient(90deg,#dbeafe,#eff6ff,#dbeafe); background-size:200% 100%; animation:pulse 1.4s ease infinite; }
    .skeleton.title { width:40%; height:1.5rem; } .skeleton.strip { height:4rem; } .skeleton.cards { height:8rem; }
    .shell-error { display:flex; flex-direction:column; align-items:flex-start; gap:.35rem; border-color:#fecaca; background:#fff1f2; color:#9f1239; }
    .shell-error.is-conflict { border-color:#fde68a; background:#fffbeb; color:#92400e; }
    .shell-error strong { font-size:.9rem; } .shell-error span { font-size:.8rem; }
    .shell-error button { margin-top:.35rem; border:1px solid currentColor; border-radius:8px; background:transparent; padding:.4rem .65rem; color:inherit; font-size:.72rem; font-weight:800; cursor:pointer; }
    .details-heading { display:flex; align-items:center; gap:.6rem; margin:.25rem 1.5rem 0; border-top:1px solid #e2e8f0; padding:1rem 0 .25rem; color:#64748b; }
    .details-heading strong { color:#334155; font-size:.75rem; } .details-heading span { font-size:.7rem; }
    .legacy-detail-sections { margin-top:.25rem; }
    @keyframes pulse { from { background-position:200% 0; } to { background-position:-200% 0; } }
    @media(max-width:800px){ .shell-state,.shell-error,.details-heading{ margin-left:1rem; margin-right:1rem; } .details-heading{ align-items:flex-start; flex-direction:column; gap:.2rem; } }
  `,
})
export class OsCockpitShell implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly osService = inject(OrdemServicoService);

  osId?: number;
  cockpit?: OrdemServicoCockpitResponse;
  state: CockpitLoadState = 'idle';
  message = '';

  ngOnInit(): void {
    const rawId = this.route.snapshot.paramMap.get('id');
    const id = rawId ? Number(rawId) : Number.NaN;

    if (!Number.isInteger(id) || id <= 0) {
      this.state = 'not-found';
      this.message = 'Não foi possível identificar a Ordem de Serviço solicitada.';
      return;
    }

    this.osId = id;
    this.load(id);
  }

  load(id: number): void {
    this.state = 'loading';
    this.message = '';
    this.cockpit = undefined;

    this.osService.getCockpit(id).subscribe({
      next: cockpit => {
        this.cockpit = cockpit;
        this.state = 'ready';
      },
      error: error => {
        const resolved = resolveCockpitLoadError(error?.status);
        this.state = resolved.state;
        this.message = resolved.message;
      },
    });
  }
}
