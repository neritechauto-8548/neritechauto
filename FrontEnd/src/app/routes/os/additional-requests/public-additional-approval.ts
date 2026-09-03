import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NeriTechIcon } from '../../../shared/components';
import { OsAdditionalPublicRequest } from './os-additional.models';
import { OsAdditionalService } from './os-additional.service';

interface DecisionDraft { decision: 'APPROVED' | 'REJECTED' | ''; comment: string; }

@Component({
  selector: 'public-additional-approval',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, NeriTechIcon],
  template: `
    <main class="public-page">
      <section class="approval-shell">
        <header class="brand-header">
          <div class="brand-mark">N</div>
          <div><strong>NeriTech Auto</strong><span>Aprovação segura de serviço adicional</span></div>
        </header>

        <div *ngIf="state === 'loading'" class="state-card" role="status"><div class="skeleton title"></div><div class="skeleton body"></div><span>Validando link seguro…</span></div>
        <div *ngIf="state === 'error'" class="state-card error" role="alert">
          <nt-icon name="alert-triangle" [size]="24" />
          <div><strong>Não foi possível abrir esta aprovação</strong><span>{{ message }}</span></div>
        </div>

        <ng-container *ngIf="request && state === 'ready'">
          <div class="hero">
            <span class="eyebrow">Ordem de Serviço {{ request.orderNumber }}</span>
            <h1>{{ request.title }}</h1>
            <p>{{ request.reason }}</p>
            <div class="summary">
              <article><span>Impacto no valor</span><strong>{{ request.amountDelta | currency:'BRL':'symbol':'1.2-2' }}</strong></article>
              <article><span>Tempo adicional</span><strong>+{{ request.timeDeltaMinutes || 0 }} min</strong></article>
              <article><span>Validade</span><strong>{{ request.expiresAt | date:'dd/MM/yyyy HH:mm' }}</strong></article>
            </div>
          </div>

          <div *ngIf="isFinal" class="decision-receipt">
            <nt-icon name="shield-check" [size]="22" />
            <div><strong>Decisão registrada</strong><span>{{ finalMessage }}</span></div>
          </div>

          <form *ngIf="!isFinal" (ngSubmit)="submitDecision()" class="decision-form">
            <header><div><span class="eyebrow">Decisão por item</span><h2>Revise cada item</h2><p>Aprovar um item autoriza somente esta versão da proposta. A oficina ainda precisa aplicar o escopo aprovado antes da execução.</p></div></header>

            <article class="item-card" *ngFor="let item of request.items; trackBy: trackById">
              <div class="item-copy">
                <span>{{ itemTypeLabel(item.itemType) }} · {{ operationLabel(item.operation) }}</span>
                <strong>{{ item.description }}</strong>
                <small>{{ item.quantity }} {{ item.unit || 'un' }} · {{ item.amountDelta | currency:'BRL':'symbol':'1.2-2' }} · +{{ item.timeDeltaMinutes || 0 }} min</small>
              </div>
              <div class="choice-row">
                <button type="button" [class.selected]="drafts[item.id]?.decision === 'APPROVED'" class="approve" (click)="choose(item.id, 'APPROVED')">Aprovar</button>
                <button type="button" [class.selected]="drafts[item.id]?.decision === 'REJECTED'" class="reject" (click)="choose(item.id, 'REJECTED')">Recusar</button>
              </div>
              <label *ngIf="drafts[item.id]?.decision"><span>Comentário (opcional)</span><textarea [name]="'comment-' + item.id" rows="2" maxlength="500" [(ngModel)]="drafts[item.id].comment" placeholder="Observação sobre sua decisão"></textarea></label>
            </article>

            <div *ngIf="actionMessage" class="form-error" role="alert">{{ actionMessage }}</div>
            <footer>
              <div><nt-icon name="shield-check" [size]="17" /><span>Sua decisão é registrada para esta versão. Nenhum pagamento é realizado nesta página.</span></div>
              <button type="submit" class="confirm-button" [disabled]="!allDecided || submitting">{{ submitting ? 'Registrando…' : 'Confirmar decisões' }}</button>
            </footer>
          </form>
        </ng-container>
      </section>
    </main>
  `,
  styles: `
    :host{display:block;min-height:100vh;background:#f6f8fb;color:#0f172a}.public-page{min-height:100vh;padding:2rem 1rem}.approval-shell{max-width:760px;margin:0 auto}.brand-header{display:flex;align-items:center;gap:.7rem;margin-bottom:1rem}.brand-mark{display:grid;place-items:center;width:2.35rem;height:2.35rem;border-radius:10px;background:#2563eb;color:#fff;font-weight:900}.brand-header>div:last-child{display:grid;gap:.08rem}.brand-header strong{font-size:.82rem}.brand-header span{color:#64748b;font-size:.64rem}.state-card,.hero,.decision-form,.decision-receipt{border:1px solid #e2e8f0;border-radius:16px;background:#fff;box-shadow:0 1px 3px rgb(15 23 42/.04)}.state-card{display:grid;gap:.6rem;padding:1.2rem;color:#64748b}.state-card.error{grid-template-columns:auto 1fr;color:#991b1b;border-color:#fecaca;background:#fff7f7}.state-card.error>div{display:grid;gap:.15rem}.skeleton{border-radius:8px;background:linear-gradient(90deg,#f1f5f9,#e2e8f0,#f1f5f9);background-size:200% 100%;animation:shimmer 1.4s infinite linear}.skeleton.title{width:45%;height:1.5rem}.skeleton.body{height:8rem}.hero{padding:1.35rem}.eyebrow{display:block;color:#2563eb;font-size:.62rem;font-weight:850;letter-spacing:.07em;text-transform:uppercase}.hero h1,.decision-form h2{margin:.2rem 0 0;letter-spacing:-.025em}.hero h1{font-size:1.45rem}.hero p,.decision-form header p{margin:.45rem 0 0;color:#64748b;font-size:.76rem;line-height:1.55}.summary{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:.6rem;margin-top:1.1rem}.summary article{display:grid;gap:.18rem;border:1px solid #e2e8f0;border-radius:10px;padding:.7rem}.summary span{color:#64748b;font-size:.59rem;font-weight:700}.summary strong{font-size:.77rem}.decision-receipt{display:flex;align-items:center;gap:.7rem;margin-top:1rem;border-color:#bbf7d0;background:#f0fdf4;padding:1rem;color:#166534}.decision-receipt>div{display:grid;gap:.1rem}.decision-receipt strong{font-size:.78rem}.decision-receipt span{font-size:.67rem}.decision-form{margin-top:1rem;overflow:hidden}.decision-form>header{padding:1.1rem 1.2rem;border-bottom:1px solid #e2e8f0;background:#fbfdff}.decision-form h2{font-size:1rem}.item-card{display:grid;gap:.7rem;padding:1rem 1.2rem;border-bottom:1px solid #e2e8f0}.item-copy{display:grid;gap:.18rem}.item-copy>span{color:#64748b;font-size:.6rem;font-weight:750}.item-copy strong{font-size:.78rem}.item-copy small{color:#94a3b8;font-size:.61rem}.choice-row{display:grid;grid-template-columns:1fr 1fr;gap:.5rem}.choice-row button{min-height:2.5rem;border-radius:9px;background:#fff;font:inherit;font-size:.7rem;font-weight:850;cursor:pointer}.choice-row .approve{border:1px solid #86efac;color:#166534}.choice-row .approve.selected{background:#dcfce7;border-color:#22c55e}.choice-row .reject{border:1px solid #fecaca;color:#b91c1c}.choice-row .reject.selected{background:#fee2e2;border-color:#ef4444}.item-card label{display:grid;gap:.25rem}.item-card label span{color:#475569;font-size:.61rem;font-weight:750}.item-card textarea{box-sizing:border-box;width:100%;resize:vertical;border:1px solid #cbd5e1;border-radius:8px;padding:.55rem .65rem;font:inherit;font-size:.7rem;outline:none}.item-card textarea:focus{border-color:#60a5fa;box-shadow:0 0 0 3px rgb(59 130 246/.1)}.form-error{margin:1rem;border:1px solid #fecaca;border-radius:9px;background:#fff1f2;padding:.7rem;color:#9f1239;font-size:.68rem}.decision-form>footer{display:flex;align-items:center;justify-content:space-between;gap:1rem;padding:1rem 1.2rem;background:#fbfdff}.decision-form>footer>div{display:flex;align-items:center;gap:.45rem;color:#64748b;font-size:.61rem;line-height:1.4}.confirm-button{border:1px solid #2563eb;border-radius:9px;background:#2563eb;padding:.65rem .9rem;color:#fff;font:inherit;font-size:.7rem;font-weight:850;cursor:pointer;white-space:nowrap}.confirm-button:disabled{cursor:not-allowed;opacity:.5}@keyframes shimmer{from{background-position:200% 0}to{background-position:-200% 0}}@media(max-width:620px){.public-page{padding:1rem}.summary{grid-template-columns:1fr}.decision-form>footer{align-items:stretch;flex-direction:column}.confirm-button{width:100%}.hero h1{font-size:1.2rem}}
  `,
})
export class PublicAdditionalApproval implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(OsAdditionalService);
  private readonly cdr = inject(ChangeDetectorRef);

  token = '';
  state: 'loading' | 'ready' | 'error' = 'loading';
  message = '';
  actionMessage = '';
  submitting = false;
  request?: OsAdditionalPublicRequest;
  drafts: Record<number, DecisionDraft> = {};

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token')?.trim() ?? '';
    if (!this.token) { this.state = 'error'; this.message = 'O link informado é inválido.'; return; }
    this.load();
  }

  get isFinal(): boolean { return !!this.request && ['APROVADA','PARCIAL','RECUSADA'].includes(this.request.status); }
  get finalMessage(): string {
    if (this.request?.status === 'APROVADA') return 'Todos os itens foram aprovados.';
    if (this.request?.status === 'PARCIAL') return 'A proposta foi aprovada parcialmente.';
    if (this.request?.status === 'RECUSADA') return 'Os itens foram recusados.';
    return '';
  }
  get allDecided(): boolean { return !!this.request?.items.length && this.request.items.every(item => !!this.drafts[item.id]?.decision); }

  load(): void {
    this.state = 'loading';
    this.service.publicFind(this.token).subscribe({
      next: request => {
        this.request = request;
        this.drafts = Object.fromEntries(request.items.map(item => [item.id, { decision: item.decision === 'APPROVED' || item.decision === 'REJECTED' ? item.decision : '', comment: item.decisionComment ?? '' }]));
        this.state = 'ready'; this.cdr.markForCheck();
      },
      error: error => {
        this.state = 'error';
        this.message = error?.status === 410 ? 'Este link expirou. Solicite um novo link à oficina.' : 'O link não existe, foi revogado ou não está mais disponível.';
        this.cdr.markForCheck();
      },
    });
  }

  choose(itemId: number, decision: 'APPROVED' | 'REJECTED'): void {
    this.drafts[itemId] = { decision, comment: this.drafts[itemId]?.comment ?? '' };
  }

  submitDecision(): void {
    if (!this.request || !this.allDecided || this.submitting) return;
    this.submitting = true; this.actionMessage = '';
    this.service.publicDecide(this.token, { items: this.request.items.map(item => ({ itemId: item.id, decision: this.drafts[item.id].decision as 'APPROVED' | 'REJECTED', comment: this.drafts[item.id].comment.trim() || null })) }).subscribe({
      next: request => { this.request = request; this.submitting = false; this.cdr.markForCheck(); },
      error: error => { this.submitting = false; this.actionMessage = error?.error?.message || error?.message || 'Não foi possível registrar sua decisão.'; this.cdr.markForCheck(); },
    });
  }

  itemTypeLabel(value: string): string { return value === 'SERVICE' ? 'Serviço' : value === 'PRODUCT' ? 'Peça/produto' : 'Outro'; }
  operationLabel(value: string): string { return value === 'ADD' ? 'Adicionar' : value === 'UPDATE' ? 'Alterar' : 'Remover'; }
  trackById(_: number, item: { id: number }): number { return item.id; }
}
