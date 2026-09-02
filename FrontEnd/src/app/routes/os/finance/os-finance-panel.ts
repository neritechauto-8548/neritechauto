import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { OsFinanceState, OsInvoiceSummary, OsPaymentSummary } from './os-finance.models';
import { OsFinanceService } from './os-finance.service';

@Component({
  selector: 'os-finance-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, NeriTechIcon],
  templateUrl: './os-finance-panel.html',
  styleUrl: './os-finance-panel.scss',
})
export class OsFinancePanel implements OnChanges {
  private readonly service = inject(OsFinanceService);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  state: OsFinanceState = 'idle';
  invoice?: OsInvoiceSummary | null;
  payments: OsPaymentSummary[] = [];
  message = '';
  private pendingReads = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['osId'] && Number.isInteger(this.osId) && this.osId > 0) this.load();
  }

  get canView(): boolean {
    return this.hasPermission('FIN_LISTAR_CONTAS') || this.hasPermission('OS_NEG_PAGAMENTO');
  }

  get canOpenFinance(): boolean {
    return this.hasPermission('FIN_LISTAR_CONTAS');
  }

  get paidTotal(): number {
    return this.payments
      .filter(payment => String(payment.status ?? '').toUpperCase() === 'CONFIRMADO')
      .reduce((total, payment) => total + Number(payment.valorTotal ?? 0), 0);
  }

  get pendingTotal(): number {
    return Number(this.invoice?.valorPendente ?? Math.max(0, Number(this.invoice?.valorTotal ?? 0) - this.paidTotal));
  }

  load(): void {
    if (!this.canView) {
      this.state = 'forbidden';
      this.message = 'Seu perfil não possui permissão para visualizar os dados financeiros desta OS.';
      return;
    }

    this.state = 'loading';
    this.message = '';
    this.invoice = undefined;
    this.payments = [];
    this.pendingReads = 2;

    this.service.getInvoiceByOrder(this.osId).subscribe({
      next: invoice => { this.invoice = invoice ?? null; this.completeRead(); },
      error: error => this.failRead(error),
    });

    this.service.listPaymentsByOrder(this.osId).subscribe({
      next: page => { this.payments = page?.content ?? []; this.completeRead(); },
      error: error => this.failRead(error),
    });
  }

  openFinance(): void {
    if (!this.canOpenFinance) return;
    void this.router.navigate(['/financeiro/contas-receber'], { queryParams: { osId: this.osId } });
  }

  statusLabel(status?: string | null): string {
    const normalized = String(status ?? '').toUpperCase();
    const labels: Record<string, string> = {
      ABERTA: 'Aberta', PENDENTE: 'Pendente', PARCIAL: 'Parcial', PAGA: 'Paga',
      VENCIDA: 'Vencida', CANCELADA: 'Cancelada', CONFIRMADO: 'Confirmado',
      PROCESSANDO: 'Processando', FALHOU: 'Falhou', ESTORNADO: 'Estornado',
    };
    return labels[normalized] ?? (status || 'Não informado');
  }

  statusTone(status?: string | null): string {
    const normalized = String(status ?? '').toUpperCase();
    if (['PAGA', 'CONFIRMADO'].includes(normalized)) return 'success';
    if (['VENCIDA', 'FALHOU', 'CANCELADA'].includes(normalized)) return 'danger';
    if (['PARCIAL', 'PENDENTE', 'PROCESSANDO'].includes(normalized)) return 'warning';
    return 'neutral';
  }

  trackById(_: number, item: OsPaymentSummary): number { return item.id; }

  private completeRead(): void {
    this.pendingReads -= 1;
    if (this.pendingReads <= 0) this.state = 'ready';
    this.cdr.markForCheck();
  }

  private failRead(error: any): void {
    this.pendingReads = 0;
    this.state = error?.status === 403 ? 'forbidden' : 'error';
    this.message = error?.status === 403
      ? 'Seu perfil não possui permissão para visualizar os dados financeiros desta OS.'
      : 'Não foi possível carregar o resumo financeiro desta Ordem de Serviço.';
    this.cdr.markForCheck();
  }

  private hasPermission(permission: string): boolean {
    const user = this.auth.snapshot();
    const permissions = (user.permissions ?? []).map(value => String(value));
    const roles = (user.roles ?? []).map(value => String(value));
    return permissions.includes(permission) || roles.includes('ADMIN') || roles.includes('ROLE_ADMIN');
  }
}
