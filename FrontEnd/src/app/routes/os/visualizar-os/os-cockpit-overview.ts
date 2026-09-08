import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, inject } from '@angular/core';
import { Router } from '@angular/router';
import { NeriTechIcon, PageHeader } from '@shared';
import {
  getRenderableNextAction,
  OrdemServicoCockpitResponse,
  OsCockpitNextAction,
} from '../models/os-cockpit.models';

@Component({
  selector: 'os-cockpit-overview',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, PageHeader, NeriTechIcon],
  template: `
    <section class="cockpit-overview" aria-labelledby="os-cockpit-title">
      <page-header
        canonical
        [title]="'Ordem de Serviço #' + (cockpit.numero || cockpit.id)"
        titleId="os-cockpit-title"
        eyebrow="Operação"
        description="Visão 360 do atendimento, com contexto operacional e próxima ação autorizada pelo backend."
      />

      <div class="context-strip" aria-label="Contexto da Ordem de Serviço">
        <div class="context-item">
          <nt-icon name="users" [size]="18" />
          <span>
            <small>Cliente</small>
            <strong>{{ cockpit.customer.name || 'Não informado' }}</strong>
          </span>
        </div>

        <div class="context-item">
          <nt-icon name="car" [size]="18" />
          <span>
            <small>Veículo</small>
            <strong>{{ cockpit.vehicle.description || 'Não informado' }}</strong>
            <em>{{ cockpit.vehicle.plate || 'Placa não informada' }}</em>
          </span>
        </div>

        <div class="context-item">
          <nt-icon name="clipboard-check" [size]="18" />
          <span>
            <small>Estágio</small>
            <strong>{{ cockpit.stage.label }}</strong>
          </span>
          <span class="stage-dot" [class]="stageClass" aria-hidden="true"></span>
        </div>

        <div class="context-item">
          <nt-icon name="history" [size]="18" />
          <span>
            <small>Versão</small>
            <strong>{{ cockpit.version ?? '—' }}</strong>
            <em *ngIf="cockpit.audit.updatedAt">Atualizada {{ cockpit.audit.updatedAt | date:'dd/MM/yyyy HH:mm' }}</em>
          </span>
        </div>
      </div>

      <div class="overview-grid">
        <article class="next-action-card" [class.is-blocked]="cockpit.blocks.length > 0">
          <div class="card-kicker">
            <nt-icon [name]="cockpit.blocks.length ? 'alert-triangle' : 'check'" [size]="18" />
            <span>Próxima ação</span>
          </div>

          <ng-container *ngIf="nextAction; else noNextAction">
            <h2>{{ nextAction.label }}</h2>
            <p>{{ nextAction.reason }}</p>
            <button
              *ngIf="nextAction.route"
              type="button"
              class="next-action-button"
              (click)="goTo(nextAction)"
            >
              {{ nextAction.label }}
              <nt-icon name="external-link" [size]="17" />
            </button>
          </ng-container>

          <ng-template #noNextAction>
            <h2>{{ cockpit.blocks.length ? 'Resolva os bloqueios da OS' : 'Nenhuma ação primária disponível' }}</h2>
            <p>
              {{ cockpit.blocks.length
                ? 'O backend não liberou uma próxima ação até que as pendências atuais sejam tratadas.'
                : 'Não há próxima ação autorizada para este usuário e estado operacional.' }}
            </p>
          </ng-template>

          <div *ngIf="cockpit.allowedActions.length" class="allowed-actions" aria-label="Ações autorizadas">
            <span *ngFor="let action of cockpit.allowedActions">{{ action.label }}</span>
          </div>
        </article>

        <article id="execucao" class="metric-card">
          <div class="metric-title">
            <nt-icon name="tool" [size]="18" />
            <span>Execução</span>
          </div>
          <strong class="metric-value">{{ executionLabel }}</strong>
          <div class="progress-track" role="progressbar" aria-label="Progresso da execução" [attr.aria-valuenow]="cockpit.execution.progress" aria-valuemin="0" aria-valuemax="100">
            <span [style.width.%]="boundedProgress"></span>
          </div>
          <div class="metric-meta">
            <span>{{ boundedProgress }}%</span>
            <span *ngIf="cockpit.execution.plannedEnd">Previsão {{ cockpit.execution.plannedEnd | date:'dd/MM HH:mm' }}</span>
          </div>
        </article>

        <article class="metric-card">
          <div class="metric-title">
            <nt-icon name="package" [size]="18" />
            <span>Escopo relacionado</span>
          </div>
          <div class="stats-row">
            <span><strong>{{ cockpit.parts.totalItems }}</strong> peças</span>
            <span><strong>{{ cockpit.relatedCounts.checklists }}</strong> checklist</span>
            <span><strong>{{ cockpit.relatedCounts.evidences }}</strong> evidências</span>
            <span><strong>{{ cockpit.relatedCounts.additionalRequests }}</strong> adicionais</span>
          </div>
        </article>

        <article class="metric-card">
          <div class="metric-title">
            <nt-icon name="cash" [size]="18" />
            <span>Financeiro</span>
          </div>
          <ng-container *ngIf="cockpit.financial; else noFinancial">
            <strong class="metric-value">{{ cockpit.financial.status || 'Sem status' }}</strong>
            <div class="financial-grid">
              <span>Total <strong>{{ cockpit.financial.totalReceivable | currency:'BRL' }}</strong></span>
              <span>Pago <strong>{{ cockpit.financial.paidAmount | currency:'BRL' }}</strong></span>
              <span>Saldo <strong>{{ cockpit.financial.remainingAmount | currency:'BRL' }}</strong></span>
            </div>
          </ng-container>
          <ng-template #noFinancial>
            <strong class="metric-value muted">Sem título financeiro relacionado</strong>
          </ng-template>
        </article>
      </div>

      <section *ngIf="cockpit.blocks.length" id="bloqueios" class="blocks-panel" aria-labelledby="os-blocks-title">
        <div class="blocks-heading">
          <nt-icon name="alert-triangle" [size]="20" />
          <div>
            <h2 id="os-blocks-title">Bloqueios e pendências</h2>
            <p>Fatos retornados pelo backend que exigem atenção antes do avanço operacional.</p>
          </div>
        </div>
        <div class="blocks-list">
          <div *ngFor="let block of cockpit.blocks" class="block-item" [class.is-danger]="block.severity === 'danger'">
            <strong>{{ block.label }}</strong>
            <span>{{ block.code }}</span>
          </div>
        </div>
      </section>
    </section>
  `,
  styles: `
    :host { display: block; }
    .cockpit-overview { padding: 0 1.5rem; }
    .context-strip { position: sticky; top: 0; z-index: 20; display: grid; grid-template-columns: repeat(4,minmax(0,1fr)); gap: 1px; margin: 0 0 1rem; overflow: hidden; border: 1px solid var(--nt-border,#e2e8f0); border-radius: 12px; background: var(--nt-border,#e2e8f0); box-shadow: 0 1px 2px rgb(15 23 42 / .04); }
    .context-item { position: relative; display: flex; align-items: center; gap: .65rem; min-width: 0; padding: .8rem .9rem; background: var(--nt-surface,#fff); color: var(--nt-text,#0f172a); }
    .context-item > span:not(.stage-dot) { display:flex; min-width:0; flex-direction:column; gap:.1rem; }
    .context-item small { color: var(--nt-muted,#64748b); font-size:.68rem; font-weight:700; text-transform:uppercase; letter-spacing:.04em; }
    .context-item strong { overflow:hidden; text-overflow:ellipsis; white-space:nowrap; font-size:.82rem; }
    .context-item em { overflow:hidden; text-overflow:ellipsis; white-space:nowrap; color:var(--nt-muted,#64748b); font-size:.7rem; font-style:normal; }
    .stage-dot { width:.55rem; height:.55rem; margin-left:auto; border-radius:999px; background:#64748b; }
    .stage-success { background:#16a34a; } .stage-warning { background:#d97706; } .stage-danger { background:#dc2626; } .stage-info { background:#2563eb; }
    .overview-grid { display:grid; grid-template-columns: 1.5fr repeat(3,minmax(0,1fr)); gap:1rem; margin-bottom:1rem; }
    .next-action-card,.metric-card { min-width:0; border:1px solid var(--nt-border,#e2e8f0); border-radius:14px; background:var(--nt-surface,#fff); box-shadow:0 1px 2px rgb(15 23 42 / .04); }
    .next-action-card { padding:1.1rem 1.15rem; border-left:3px solid var(--nt-primary,#2563eb); }
    .next-action-card.is-blocked { border-left-color:var(--nt-warning,#d97706); }
    .card-kicker,.metric-title { display:flex; align-items:center; gap:.45rem; color:var(--nt-muted,#64748b); font-size:.72rem; font-weight:800; text-transform:uppercase; letter-spacing:.04em; }
    .next-action-card h2 { margin:.65rem 0 .3rem; color:var(--nt-text,#0f172a); font-size:1.15rem; }
    .next-action-card p { margin:0; color:var(--nt-muted,#64748b); font-size:.82rem; line-height:1.45; }
    .next-action-button { display:inline-flex; align-items:center; gap:.45rem; margin-top:.85rem; border:0; border-radius:9px; padding:.55rem .8rem; background:var(--nt-primary,#2563eb); color:white; font-size:.78rem; font-weight:800; cursor:pointer; }
    .allowed-actions { display:flex; flex-wrap:wrap; gap:.35rem; margin-top:.85rem; }
    .allowed-actions span { border:1px solid var(--nt-border,#e2e8f0); border-radius:999px; background:#f8fafc; padding:.25rem .48rem; color:#475569; font-size:.65rem; font-weight:700; }
    .metric-card { padding:1rem; }
    .metric-value { display:block; margin:.7rem 0; color:var(--nt-text,#0f172a); font-size:.95rem; }
    .metric-value.muted { color:var(--nt-muted,#64748b); font-size:.8rem; }
    .progress-track { height:.38rem; overflow:hidden; border-radius:999px; background:#e2e8f0; }
    .progress-track span { display:block; height:100%; border-radius:inherit; background:var(--nt-primary,#2563eb); }
    .metric-meta { display:flex; justify-content:space-between; gap:.5rem; margin-top:.4rem; color:var(--nt-muted,#64748b); font-size:.68rem; }
    .stats-row { display:grid; grid-template-columns:repeat(2,minmax(0,1fr)); gap:.55rem; margin-top:.75rem; }
    .stats-row span,.financial-grid span { display:flex; flex-direction:column; gap:.12rem; color:var(--nt-muted,#64748b); font-size:.68rem; }
    .stats-row strong,.financial-grid strong { color:var(--nt-text,#0f172a); font-size:.88rem; }
    .financial-grid { display:grid; grid-template-columns:repeat(3,minmax(0,1fr)); gap:.4rem; }
    .blocks-panel { display:flex; gap:1rem; margin-bottom:1rem; border:1px solid #fcd34d; border-radius:12px; background:#fffbeb; padding:.9rem 1rem; color:#92400e; }
    .blocks-heading { display:flex; align-items:flex-start; gap:.6rem; min-width:15rem; }
    .blocks-heading h2 { margin:0; font-size:.86rem; } .blocks-heading p { margin:.2rem 0 0; color:#a16207; font-size:.7rem; line-height:1.35; }
    .blocks-list { display:flex; flex:1; flex-wrap:wrap; gap:.5rem; }
    .block-item { display:flex; flex-direction:column; gap:.1rem; min-width:12rem; border:1px solid #fde68a; border-radius:9px; background:#fff; padding:.55rem .65rem; }
    .block-item.is-danger { border-color:#fecaca; color:#991b1b; } .block-item strong { font-size:.72rem; } .block-item span { opacity:.68; font-size:.6rem; }
    @media (max-width: 1180px) { .overview-grid { grid-template-columns:repeat(2,minmax(0,1fr)); } }
    @media (max-width: 800px) { .cockpit-overview { padding:0 1rem; } .context-strip { position:static; grid-template-columns:repeat(2,minmax(0,1fr)); } .overview-grid { grid-template-columns:1fr; } .blocks-panel { flex-direction:column; } }
    @media (max-width: 520px) { .context-strip { grid-template-columns:1fr; } .financial-grid { grid-template-columns:1fr; } }
  `,
})
export class OsCockpitOverview {
  private readonly router = inject(Router);

  @Input({ required: true }) cockpit!: OrdemServicoCockpitResponse;

  get nextAction(): OsCockpitNextAction | null {
    return getRenderableNextAction(this.cockpit);
  }

  get boundedProgress(): number {
    const progress = Number(this.cockpit.execution.progress ?? 0);
    return Math.min(100, Math.max(0, Number.isFinite(progress) ? progress : 0));
  }

  get executionLabel(): string {
    const labels: Record<string, string> = {
      NOT_STARTED: 'Não iniciada',
      IN_PROGRESS: 'Em execução',
      COMPLETED: 'Execução concluída',
    };
    return labels[this.cockpit.execution.status] || this.cockpit.execution.status || 'Não informada';
  }

  get stageClass(): string {
    const severity = this.cockpit.stage.severity || 'info';
    return `stage-dot stage-${severity}`;
  }

  goTo(action: OsCockpitNextAction): void {
    if (!action.route) return;
    void this.router.navigateByUrl(action.route);
  }
}
