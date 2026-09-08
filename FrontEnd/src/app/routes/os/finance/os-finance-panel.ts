import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@core';
import { NeriTechIcon } from '../../../shared/components';
import { EstadoFinanceiroOS, ResumoFaturaOS, ResumoPagamentoOS } from './os-finance.models';
import { FinanceiroOrdemServicoService } from './os-finance.service';

@Component({
  selector: 'os-finance-panel',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, NeriTechIcon],
  templateUrl: './os-finance-panel.html',
  styleUrl: './os-finance-panel.scss',
})
export class PainelFinanceiroOrdemServico implements OnChanges {
  private readonly servico = inject(FinanceiroOrdemServicoService);
  private readonly autenticacao = inject(AuthService);
  private readonly roteador = inject(Router);
  private readonly detectorMudancas = inject(ChangeDetectorRef);

  @Input({ required: true }) osId!: number;

  estado: EstadoFinanceiroOS = 'ocioso';
  fatura?: ResumoFaturaOS | null;
  pagamentos: ResumoPagamentoOS[] = [];
  mensagem = '';
  private leiturasPendentes = 0;

  ngOnChanges(alteracoes: SimpleChanges): void {
    if (alteracoes['osId'] && Number.isInteger(this.osId) && this.osId > 0) this.carregar();
  }

  get podeVisualizar(): boolean {
    return this.temPermissao('FIN_LISTAR_CONTAS') || this.temPermissao('OS_NEG_PAGAMENTO');
  }

  get podeAbrirFinanceiro(): boolean {
    return this.temPermissao('FIN_LISTAR_CONTAS');
  }

  get totalRecebido(): number {
    return this.pagamentos
      .filter(pagamento => String(pagamento.status ?? '').toUpperCase() === 'CONFIRMADO')
      .reduce((total, pagamento) => total + Number(pagamento.valorTotal ?? 0), 0);
  }

  get totalPendente(): number {
    return Number(this.fatura?.valorPendente ?? Math.max(0, Number(this.fatura?.valorTotal ?? 0) - this.totalRecebido));
  }

  carregar(): void {
    if (!this.podeVisualizar) {
      this.estado = 'proibido';
      this.mensagem = 'Seu perfil não possui permissão para visualizar os dados financeiros desta OS.';
      return;
    }

    this.estado = 'carregando';
    this.mensagem = '';
    this.fatura = undefined;
    this.pagamentos = [];
    this.leiturasPendentes = 2;

    this.servico.buscarFaturaPorOrdem(this.osId).subscribe({
      next: fatura => { this.fatura = fatura ?? null; this.concluirLeitura(); },
      error: erro => this.falharLeitura(erro),
    });

    this.servico.listarPagamentosPorOrdem(this.osId).subscribe({
      next: pagina => { this.pagamentos = pagina?.content ?? []; this.concluirLeitura(); },
      error: erro => this.falharLeitura(erro),
    });
  }

  abrirFinanceiro(): void {
    if (!this.podeAbrirFinanceiro) return;
    void this.roteador.navigate(['/financeiro/receber'], { queryParams: { osId: this.osId } });
  }

  rotuloStatus(status?: string | null): string {
    const statusNormalizado = String(status ?? '').toUpperCase();
    const rotulos: Record<string, string> = {
      ABERTA: 'Aberta', PENDENTE: 'Pendente', PARCIAL: 'Parcial', PAGA: 'Paga',
      VENCIDA: 'Vencida', CANCELADA: 'Cancelada', CONFIRMADO: 'Confirmado',
      PROCESSANDO: 'Processando', FALHOU: 'Falhou', ESTORNADO: 'Estornado',
    };
    return rotulos[statusNormalizado] ?? (status || 'Não informado');
  }

  tomStatus(status?: string | null): string {
    const statusNormalizado = String(status ?? '').toUpperCase();
    if (['PAGA', 'CONFIRMADO'].includes(statusNormalizado)) return 'sucesso';
    if (['VENCIDA', 'FALHOU', 'CANCELADA'].includes(statusNormalizado)) return 'perigo';
    if (['PARCIAL', 'PENDENTE', 'PROCESSANDO'].includes(statusNormalizado)) return 'atencao';
    return 'neutro';
  }

  identificarPorId(_: number, pagamento: ResumoPagamentoOS): number { return pagamento.id; }

  private concluirLeitura(): void {
    this.leiturasPendentes -= 1;
    if (this.leiturasPendentes <= 0) this.estado = 'pronto';
    this.detectorMudancas.markForCheck();
  }

  private falharLeitura(erro: any): void {
    this.leiturasPendentes = 0;
    this.estado = erro?.status === 403 ? 'proibido' : 'erro';
    this.mensagem = erro?.status === 403
      ? 'Seu perfil não possui permissão para visualizar os dados financeiros desta OS.'
      : 'Não foi possível carregar o resumo financeiro desta Ordem de Serviço.';
    this.detectorMudancas.markForCheck();
  }

  private temPermissao(permissao: string): boolean {
    const usuario = this.autenticacao.snapshot();
    const permissoes = (usuario.permissions ?? []).map(valor => String(valor));
    const perfis = (usuario.roles ?? []).map(valor => String(valor));
    return permissoes.includes(permissao) || perfis.includes('ADMIN') || perfis.includes('ROLE_ADMIN');
  }
}
