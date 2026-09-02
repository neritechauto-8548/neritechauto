import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OsAdditionalPanel } from '../additional-requests/os-additional-panel';
import { OsClosureReview } from '../closure/os-closure-review';
import { OsCommunicationPanel } from '../communication/os-communication-panel';
import { OsExecutionPanel } from '../execution/os-execution-panel';
import { PainelFinanceiroOrdemServico } from '../finance/os-finance-panel';
import { PainelDiarioOrdemServico } from '../journal/os-journal-panel';
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
  imports: [
    CommonModule,
    OsCockpitOverview,
    OsExecutionPanel,
    OsOperationsPanel,
    OsAdditionalPanel,
    PainelDiarioOrdemServico,
    PainelFinanceiroOrdemServico,
    OsCommunicationPanel,
    OsClosureReview,
    VisualizarOS,
  ],
  template: `
    <main class="canonical-cockpit-shell">
      <div *ngIf="estado === 'loading'" class="shell-state" role="status" aria-live="polite">
        <div class="skeleton title"></div><div class="skeleton strip"></div><div class="skeleton cards"></div>
        <span>Carregando visão 360 da Ordem de Serviço…</span>
      </div>

      <div *ngIf="estado === 'forbidden' || estado === 'not-found' || estado === 'conflict' || estado === 'error'" class="shell-error" [class.is-conflict]="estado === 'conflict'" role="alert">
        <strong>{{ estado === 'conflict' ? 'Dados desatualizados' : 'Não foi possível abrir a Ordem de Serviço' }}</strong>
        <span>{{ mensagem }}</span>
        <button type="button" *ngIf="ordemServicoId" (click)="carregar(ordemServicoId)">Atualizar</button>
      </div>

      <ng-container *ngIf="visao360 && estado === 'ready'">
        <os-cockpit-overview [cockpit]="visao360" />
        <os-execution-panel [osId]="visao360.id" />

        <os-closure-review *ngIf="revisaoPrimeiro" [cockpit]="visao360" />
        <os-communication-panel *ngIf="comunicacaoPrimeiro" [osId]="visao360.id" />
        <os-finance-panel *ngIf="financeiroPrimeiro" [osId]="visao360.id" />
        <os-journal-panel *ngIf="diarioPrimeiro" [osId]="visao360.id" />
        <os-additional-panel *ngIf="adicionaisPrimeiro" [osId]="visao360.id" />

        <os-operations-panel [osId]="visao360.id" [initialTab]="abaOperacoes" />
        <os-additional-panel *ngIf="!adicionaisPrimeiro" [osId]="visao360.id" />
        <os-journal-panel *ngIf="!diarioPrimeiro" [osId]="visao360.id" />
        <os-finance-panel *ngIf="!financeiroPrimeiro" [osId]="visao360.id" />
        <os-communication-panel *ngIf="!comunicacaoPrimeiro" [osId]="visao360.id" />
        <os-closure-review *ngIf="!revisaoPrimeiro" [cockpit]="visao360" />

        <details class="legacy-details">
          <summary>
            <span>
              <strong>Recursos adicionais ainda em migração</strong>
              <small>Impressão, fiscal e demais fluxos legados permanecem acessíveis durante a reconstrução.</small>
            </span>
            <span class="legacy-badge">Legado controlado</span>
          </summary>
          <section class="legacy-detail-sections" aria-label="Recursos adicionais da Ordem de Serviço ainda em migração"><visualizar-os /></section>
        </details>
      </ng-container>
    </main>
  `,
  styles: `
    :host { display:block; }
    .canonical-cockpit-shell { min-height:100%; background:#f8fafc; padding-top:1rem; padding-bottom:1rem; }
    .shell-state,.shell-error { margin:1rem 1.5rem; border:1px solid #dbeafe; border-radius:14px; background:#eff6ff; padding:1rem; color:#1e40af; }
    .shell-state { display:grid; gap:.65rem; }.shell-state span { font-size:.78rem; font-weight:700; }
    .skeleton { border-radius:8px; background:linear-gradient(90deg,#dbeafe,#eff6ff,#dbeafe); background-size:200% 100%; animation:pulse 1.4s ease infinite; }
    .skeleton.title { width:40%; height:1.5rem; }.skeleton.strip { height:4rem; }.skeleton.cards { height:8rem; }
    .shell-error { display:flex; flex-direction:column; align-items:flex-start; gap:.35rem; border-color:#fecaca; background:#fff1f2; color:#9f1239; }
    .shell-error.is-conflict { border-color:#fde68a; background:#fffbeb; color:#92400e; }.shell-error strong { font-size:.9rem; }.shell-error span { font-size:.8rem; }
    .shell-error button { margin-top:.35rem; border:1px solid currentColor; border-radius:8px; background:transparent; padding:.4rem .65rem; color:inherit; font-size:.72rem; font-weight:800; cursor:pointer; }
    .legacy-details { margin:1rem 1.5rem 0; border:1px solid #e2e8f0; border-radius:14px; background:#fff; overflow:hidden; }
    .legacy-details > summary { display:flex; align-items:center; justify-content:space-between; gap:1rem; list-style:none; padding:.9rem 1rem; cursor:pointer; background:#fbfdff; }
    .legacy-details > summary::-webkit-details-marker { display:none; }.legacy-details > summary > span:first-child { display:grid; gap:.15rem; }
    .legacy-details summary strong { color:#334155; font-size:.74rem; }.legacy-details summary small { color:#94a3b8; font-size:.63rem; line-height:1.4; }
    .legacy-badge { border-radius:999px; background:#f1f5f9; padding:.2rem .5rem; color:#64748b; font-size:.58rem; font-weight:800; white-space:nowrap; }
    .legacy-detail-sections { border-top:1px solid #e2e8f0; padding-top:.5rem; }
    @keyframes pulse { from { background-position:200% 0; } to { background-position:-200% 0; } }
    @media(max-width:800px){ .shell-state,.shell-error,.legacy-details{ margin-left:1rem; margin-right:1rem; }.legacy-details > summary{ align-items:flex-start; flex-direction:column; } }
  `,
})
export class OsCockpitShell implements OnInit {
  private readonly rota = inject(ActivatedRoute);
  private readonly ordemServicoService = inject(OrdemServicoService);

  ordemServicoId?: number;
  visao360?: OrdemServicoCockpitResponse;
  abaOperacoes: OsOperationsTab = 'scope';
  adicionaisPrimeiro = false;
  diarioPrimeiro = false;
  financeiroPrimeiro = false;
  comunicacaoPrimeiro = false;
  revisaoPrimeiro = false;
  estado: CockpitLoadState = 'idle';
  mensagem = '';

  ngOnInit(): void {
    const abaConfigurada = this.rota.snapshot.data['operationsTab'];
    if (abaConfigurada === 'scope' || abaConfigurada === 'diagnostics' || abaConfigurada === 'checklist' || abaConfigurada === 'evidence') this.abaOperacoes = abaConfigurada;
    const secaoFoco = this.rota.snapshot.data['focusSection'];
    this.adicionaisPrimeiro = secaoFoco === 'additional';
    this.diarioPrimeiro = secaoFoco === 'journal';
    this.financeiroPrimeiro = secaoFoco === 'finance';
    this.comunicacaoPrimeiro = secaoFoco === 'communication';
    this.revisaoPrimeiro = secaoFoco === 'closure';

    const idInformado = this.rota.snapshot.paramMap.get('id');
    const id = idInformado ? Number(idInformado) : Number.NaN;
    if (!Number.isInteger(id) || id <= 0) { this.estado = 'not-found'; this.mensagem = 'Não foi possível identificar a Ordem de Serviço solicitada.'; return; }
    this.ordemServicoId = id;
    this.carregar(id);
  }

  carregar(id: number): void {
    this.estado = 'loading'; this.mensagem = ''; this.visao360 = undefined;
    this.ordemServicoService.getCockpit(id).subscribe({
      next: visao360 => { this.visao360 = visao360; this.estado = 'ready'; },
      error: erro => { const resultado = resolveCockpitLoadError(erro?.status); this.estado = resultado.state; this.mensagem = resultado.message; },
    });
  }
}
