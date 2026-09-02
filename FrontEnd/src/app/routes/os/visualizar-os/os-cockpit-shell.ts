import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OsExecutionPanel } from '../execution/os-execution-panel';
import { OrdemServicoCockpitResponse } from '../models/os-cockpit.models';
import { OsOperationsTab } from '../operations/os-operations.models';
import { OsOperationsPanel } from '../operations/os-operations-panel';
import { OrdemServicoService } from '../ordem-servico.service';
import { OsCockpitOverview } from './os-cockpit-overview';
import { CockpitLoadState, resolveCockpitLoadError } from './os-cockpit-state';
import { VisualizarOS } from './visualizar-os';

@Component({
  selector: 'os-cockpit-shell',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, OsCockpitOverview, OsExecutionPanel, OsOperationsPanel, VisualizarOS],
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

        <os-execution-panel [osId]="cockpit.id" />

        <os-operations-panel [osId]="cockpit.id" [initialTab]="operationsTab" />

        <details class="legacy-details">
          <summary>
            <span>
              <strong>Recursos adicionais ainda em migração</strong>
              <small>Financeiro, impressão, comunicação, fiscal e demais fluxos legados permanecem acessíveis durante o rebuild.</small>
            </span>
            <span class="legacy-badge">Legado controlado</span>
          </summary>
          <section class="legacy-detail-sections" aria-label="Recursos adicionais da Ordem de Serviço ainda em migração">
            <visualizar-os />
          </section>
        </details>
      </ng-container>
    </main>
  `,
  styles: `
    :host { display:block; }
    .canonical-cockpit-shell { min-height:100%; background:#f8fafc; padding-top:1rem; padding-bottom:1rem; }
    .shell-state,.shell-error { margin:1rem 1.5rem; border:1px solid #dbeafe; border-radius:14px; background:#eff6ff; padding:1rem; color:#1e40af; }
    .shell-state { display:grid; gap:.65rem; }
    .shell-state span { font-size:.78rem; font-weight:700; }
    .skeleton { border-radius:8px; background:linear-gradient(90deg,#dbeafe,#eff6ff,#dbeafe); background-size:200% 100%; animation:pulse 1.4s ease infinite; }
    .skeleton.title { width:40%; height:1.5rem; } .skeleton.strip { height:4rem; } .skeleton.cards { height:8rem; }
    .shell-error { display:flex; flex-direction:column; align-items:flex-start; gap:.35rem; border-color:#fecaca; background:#fff1f2; color:#9f1239; }
    .shell-error.is-conflict { border-color:#fde68a; background:#fffbeb; color:#92400e; }
    .shell-error strong { font-size:.9rem; } .shell-error span { font-size:.8rem; }
    .shell-error button { margin-top:.35rem; border:1px solid currentColor; border-radius:8px; background:transparent; padding:.4rem .65rem; color:inherit; font-size:.72rem; font-weight:800; cursor:pointer; }
    .legacy-details { margin:1rem 1.5rem 0; border:1px solid #e2e8f0; border-radius:14px; background:#fff; overflow:hidden; }
    .legacy-details > summary { display:flex; align-items:center; justify-content:space-between; gap:1rem; list-style:none; padding:.9rem 1rem; cursor:pointer; background:#fbfdff; }
    .legacy-details > summary::-webkit-details-marker { display:none; }
    .legacy-details > summary > span:first-child { display:grid; gap:.15rem; }
    .legacy-details summary strong { color:#334155; font-size:.74rem; }
    .legacy-details summary small { color:#94a3b8; font-size:.63rem; line-height:1.4; }
    .legacy-badge { border-radius:999px; background:#f1f5f9; padding:.2rem .5rem; color:#64748b; font-size:.58rem; font-weight:800; white-space:nowrap; }
    .legacy-detail-sections { border-top:1px solid #e2e8f0; padding-top:.5rem; }
    @keyframes pulse { from { background-position:200% 0; } to { background-position:-200% 0; } }
    @media(max-width:800px){ .shell-state,.shell-error,.legacy-details{ margin-left:1rem; margin-right:1rem; } .legacy-details > summary{ align-items:flex-start; flex-direction:column; } }
  `,
})
export class OsCockpitShell implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly osService = inject(OrdemServicoService);

  osId?: number;
  cockpit?: OrdemServicoCockpitResponse;
  operationsTab: OsOperationsTab = 'scope';
  state: CockpitLoadState = 'idle';
  message = '';

  ngOnInit(): void {
    const configuredTab = this.route.snapshot.data['operationsTab'];
    if (configuredTab === 'scope' || configuredTab === 'diagnostics' || configuredTab === 'checklist' || configuredTab === 'evidence') {
      this.operationsTab = configuredTab;
    }

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
