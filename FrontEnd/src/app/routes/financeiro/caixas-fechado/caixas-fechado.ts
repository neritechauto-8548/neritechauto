import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TabsModule } from 'primeng/tabs';
import { DatePickerModule } from 'primeng/datepicker';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { FinanceiroService } from '../financeiro.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { RelatoriosService } from '../../relatorios/relatorios.service';

interface RegistroFechamento {
  cod: number;
  dataReferente: string;
  dataHoraOperacao: string;
  responsavel: string;
  saldoInicial: number;
  saldoFinal: number;
  totalEntradas: number;
  totalSaidas: number;
  situacao: string;
  dataAberturaRaw?: string;
  dataFechamentoRaw?: string;
}

interface LinhaMov {
  id: number;
  tipo: string;
  data: string;
  descricao: string;
  conta: string;
  centroCusto: string;
  formaPagamento: string;
  entrada: number;
  saida: number;
  saldoAcumulado: number;
}

@Component({
  standalone: true,
  selector: 'app-caixas-fechado',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SelectModule,
    InputTextModule,
    DialogModule,
    ToastModule,
    ConfirmDialogModule,
    TableModule,
    TagModule,
    TabsModule,
    DatePickerModule,
    ButtonModule,
    SkeletonModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './caixas-fechado.html',
  styleUrls: ['./caixas-fechado.scss'],
})
export class CaixasFechadoComponent implements OnInit {
  private service = inject(FinanceiroService);
  private messageService = inject(MessageService);
  private relatoriosService = inject(RelatoriosService);

  // ─── Dialog state ─────────────────────────────────────
  detalhesDialogVisible = false;
  fechamentoSelecionado: RegistroFechamento | null = null;
  movimentacoesFechadas: LinhaMov[] = [];
  loadingDetalhes = false;
  activeTab = 0;

  // ─── Loading / pagination ──────────────────────────────
  loading = false;
  totalRecords = 0;

  // ─── Filtros ──────────────────────────────────────────
  dataInicio: Date | null = null;
  dataFim: Date | null = null;
  responsavel: string | null = null;
  responsavelOptions = [{ label: 'TODOS', value: null }];

  registros: RegistroFechamento[] = [];

  // ─── Computed: Agregações do fechamento aberto ────────
  get totalEntradasFech(): number {
    return this.movimentacoesFechadas.reduce((s, m) => s + m.entrada, 0);
  }
  get totalSaidasFech(): number {
    return this.movimentacoesFechadas.reduce((s, m) => s + m.saida, 0);
  }
  get resultadoFech(): number {
    return this.totalEntradasFech - this.totalSaidasFech;
  }

  get entradasPorForma(): { forma: string; total: number }[] {
    const map: Record<string, number> = {};
    this.movimentacoesFechadas
      .filter(m => m.tipo === 'ENTRADA')
      .forEach(m => { map[m.formaPagamento] = (map[m.formaPagamento] || 0) + m.entrada; });
    return Object.entries(map).map(([forma, total]) => ({ forma, total }))
      .sort((a, b) => b.total - a.total);
  }

  get saidasPorForma(): { forma: string; total: number }[] {
    const map: Record<string, number> = {};
    this.movimentacoesFechadas
      .filter(m => m.tipo === 'SAIDA')
      .forEach(m => { map[m.formaPagamento] = (map[m.formaPagamento] || 0) + m.saida; });
    return Object.entries(map).map(([forma, total]) => ({ forma, total }))
      .sort((a, b) => b.total - a.total);
  }

  get entradasPorConta(): { conta: string; total: number }[] {
    const map: Record<string, number> = {};
    this.movimentacoesFechadas
      .filter(m => m.tipo === 'ENTRADA')
      .forEach(m => { const k = m.conta || 'Sem conta'; map[k] = (map[k] || 0) + m.entrada; });
    return Object.entries(map).map(([conta, total]) => ({ conta, total }))
      .sort((a, b) => b.total - a.total);
  }

  get saidasPorConta(): { conta: string; total: number }[] {
    const map: Record<string, number> = {};
    this.movimentacoesFechadas
      .filter(m => m.tipo === 'SAIDA')
      .forEach(m => { const k = m.conta || 'Sem conta'; map[k] = (map[k] || 0) + m.saida; });
    return Object.entries(map).map(([conta, total]) => ({ conta, total }))
      .sort((a, b) => b.total - a.total);
  }

  // ─── Totais do período filtrado (cards de topo) ──────
  get totalSaldoInicial(): number { return this.registros.reduce((s, r) => s + (r.saldoInicial || 0), 0); }
  get totalSaldoFinal(): number   { return this.registros.reduce((s, r) => s + (r.saldoFinal || 0), 0); }
  get totalEntradas(): number     { return this.registros.reduce((s, r) => s + (r.totalEntradas || 0), 0); }
  get totalSaidas(): number       { return this.registros.reduce((s, r) => s + (r.totalSaidas || 0), 0); }

  // ─── Actions ──────────────────────────────────────────
  imprimir() {
    this.loading = true;
    const params: any = {};
    if (this.dataInicio) params.dataInicio = this.formatDateToIso(this.dataInicio);
    if (this.dataFim)    params.dataFim    = this.formatDateToIso(this.dataFim);

    this.relatoriosService.gerarRelatorio('caixa', params).subscribe({
      next: (blob) => { this.loading = false; this.relatoriosService.abrirBlobEmNovaAba(blob); },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o relatório.' });
      }
    });
  }

  imprimirRegistro(r: RegistroFechamento) {
    this.loading = true;
    const params: any = { dataInicio: r.dataAberturaRaw || '', dataFim: r.dataFechamentoRaw || '', usuarioNome: r.responsavel };
    this.relatoriosService.gerarRelatorio('caixa', params).subscribe({
      next: (blob) => { this.loading = false; this.relatoriosService.abrirBlobEmNovaAba(blob); },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o relatório do fechamento.' });
      }
    });
  }

  imprimirCupom() {
    this.messageService.add({ severity: 'info', summary: 'Cupom', detail: 'Enviando fechamento para a impressora térmica...' });
  }

  // ─── Init e Busca ─────────────────────────────────────
  ngOnInit() { this.buscar(); }

  buscar() {
    this.loading = true;
    this.service.listFechamentoCaixa({
      dataInicio: this.dataInicio ? this.formatDateToIso(this.dataInicio) : undefined,
      dataFim:    this.dataFim    ? this.formatDateToIso(this.dataFim)    : undefined,
      page: 0, size: 50
    }).subscribe({
      next: (page: any) => {
        const content = page.content || [];
        this.registros = content.map((item: any) => ({
          cod: item.id,
          dataReferente: this.formatDate(item.dataAbertura) || 'Atual',
          dataHoraOperacao: this.formatDateTime(item.dataFechamento),
          responsavel: item.responsavelNome || `Usuário ${item.usuarioResponsavel}`,
          saldoInicial: Number(item.saldoInicial || 0),
          saldoFinal:   Number(item.saldoFinal   || 0),
          totalEntradas: Number(item.totalEntradas || 0),
          totalSaidas:   Number(item.totalSaidas   || 0),
          situacao: item.situacao || 'FECHADO',
          dataAberturaRaw:  item.dataAbertura   ? item.dataAbertura.split('T')[0]   : '',
          dataFechamentoRaw: item.dataFechamento ? item.dataFechamento.split('T')[0] : ''
        }));
        this.totalRecords = page.totalElements || this.registros.length;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível buscar histórico de fechamentos.' });
      }
    });
  }

  visualizarDetalhes(registro: RegistroFechamento) {
    this.fechamentoSelecionado = registro;
    this.movimentacoesFechadas = [];
    this.activeTab = 0;
    this.detalhesDialogVisible = true;
    this.carregarDetalhesFechamento(registro);
  }

  carregarDetalhesFechamento(registro: RegistroFechamento) {
    this.loadingDetalhes = true;
    this.service.listFluxoCaixa({
      dataInicio: registro.dataAberturaRaw || '',
      dataFim:    registro.dataFechamentoRaw || '',
      includeClosed: true,
      size: 1000
    }).subscribe({
      next: (page: any) => {
        const content = page.content || [];
        // Ordena por data ascendente para calcular saldo progressivo
        content.sort((a: any, b: any) => {
          const da = new Date(a.dataMovimento || 0).getTime();
          const db = new Date(b.dataMovimento || 0).getTime();
          if (da === db) return (a.id || 0) - (b.id || 0);
          return da - db;
        });

        let saldoRunning = Number(registro.saldoInicial || 0);
        this.movimentacoesFechadas = content.map((r: any) => {
          const tipo = String(r.tipoMovimentacao || '').toUpperCase();
          const valor = Number(r.valor || 0);
          const entrada = tipo === 'ENTRADA' ? valor : 0;
          const saida   = tipo === 'SAIDA'   ? valor : 0;
          saldoRunning += entrada - saida;
          return {
            id: Number(r.id || 0),
            tipo,
            data: this.formatDate(r.dataMovimento),
            descricao: r.descricao || '',
            conta: r.contaBancariaNome || '',
            centroCusto: r.centroCustoNome || '',
            formaPagamento: r.formaPagamentoNome || 'Não especificado',
            entrada,
            saida,
            saldoAcumulado: saldoRunning
          } as LinhaMov;
        });
        this.loadingDetalhes = false;
      },
      error: () => {
        this.loadingDetalhes = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar detalhamento do caixa.' });
      }
    });
  }

  // ─── Utilitários ──────────────────────────────────────
  private formatDateToIso(d: Date): string {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

  formatDate(dateStr: string): string {
    if (!dateStr) return '';
    try { return new Date(dateStr).toLocaleDateString('pt-BR'); } catch { return dateStr; }
  }

  formatDateTime(dateStr: string): string {
    if (!dateStr) return '-';
    try {
      const d = new Date(dateStr);
      return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    } catch { return dateStr; }
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0);
  }

  getTipoSeverity(tipo: string): 'success' | 'danger' | 'info' | 'secondary' {
    if (tipo === 'ENTRADA') return 'success';
    if (tipo === 'SAIDA')   return 'danger';
    return 'info';
  }

  getTipoLabel(tipo: string): string {
    if (tipo === 'ENTRADA')       return 'Entrada';
    if (tipo === 'SAIDA')         return 'Saída';
    if (tipo === 'TRANSFERENCIA') return 'Transferência';
    return tipo;
  }

  getSituacaoSeverity(sit: string): 'success' | 'danger' | 'warn' | 'secondary' {
    if (sit === 'FECHADO') return 'danger';
    if (sit === 'ABERTO')  return 'success';
    return 'secondary';
  }
}
