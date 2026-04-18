import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { FinanceiroService } from '../financeiro.service';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { DatePickerModule } from 'primeng/datepicker';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TextareaModule } from 'primeng/textarea';
import { MatIconModule } from '@angular/material/icon';
import { MessageService, ConfirmationService } from 'primeng/api';

interface LinhaCaixa {
  id: number;
  tipo: 'ENTRADA' | 'SAIDA' | 'TRANSFERENCIA';
  data: string;
  dataFmt: string;
  descricao: string;
  conta: string;
  centroCusto: string;
  entrada: number;
  saida: number;
  saldo: number;
}

@Component({
  standalone: true,
  selector: 'app-caixa',
  templateUrl: './caixa.html',
  imports: [
    CommonModule, FormsModule, ButtonModule, SelectModule,
    InputTextModule, InputNumberModule, DialogModule, DatePickerModule,
    ToastModule, ConfirmDialogModule, TextareaModule, MatIconModule
  ],
  providers: [MessageService, ConfirmationService]
})
export class CaixaComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly finService = inject(FinanceiroService);
  private readonly messageService = inject(MessageService);
  private readonly confirmService = inject(ConfirmationService);

  // ─── Estado ───────────────────────────────────────────────
  loading = false;
  linhas: LinhaCaixa[] = [];
  filtroTexto = '';

  // Filtros
  dataInicial = new Date().toISOString().substring(0, 10);
  dataFinal   = new Date().toISOString().substring(0, 10);
  contaSelecionada: number | 'TODAS' = 'TODAS';
  centroCustoSelecionado: number | 'TODOS' = 'TODOS';

  // Auxiliares
  contaOptions: { label: string; value: number | 'TODAS' }[] = [
    { label: 'Todas as contas', value: 'TODAS' }
  ];
  centroCustoOptions: { label: string; value: number | 'TODOS' }[] = [
    { label: 'Todos os centros', value: 'TODOS' }
  ];
  contasTransf: { label: string; value: number }[] = [];

  // ─── Totais e Resumos ─────────────────────────────────────
  saldoInicial = 0;
  totalEntrada = 0;
  totalSaida = 0;
  saldoFinal = 0;

  // ─── Lançamento manual ────────────────────────────────────
  novoLancDialog = false;
  salvandoLanc = false;
  lanc = this.emptyLanc();

  emptyLanc() {
    return {
      tipo: 'ENTRADA' as 'ENTRADA' | 'SAIDA',
      descricao: '',
      valor: 0,
      data: new Date(),
      contaBancariaId: null as number | null,
      centroCustoId: null as number | null,
      observacoes: '',
    };
  }

  abrirNovoLanc() {
    this.lanc = this.emptyLanc();
    this.novoLancDialog = true;
  }

  salvarLanc() {
    if (!this.lanc.descricao || !this.lanc.valor) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha descrição e valor.' });
      return;
    }
    this.salvandoLanc = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;
    this.finService.createFluxoCaixa({
      dataMovimento: toISO(this.lanc.data),
      descricao: this.lanc.descricao,
      tipoMovimentacao: this.lanc.tipo,
      valor: this.lanc.valor,
      contaBancariaId: this.lanc.contaBancariaId ?? undefined,
      centroCustoId: this.lanc.centroCustoId ?? undefined,
      observacoes: this.lanc.observacoes || undefined,
    }).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Lançado!', detail: 'Movimentação registrada.' });
        this.novoLancDialog = false;
        this.salvandoLanc = false;
        this.buscar();
      },
      error: (e) => {
        this.salvandoLanc = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: e?.error?.message || 'Não foi possível lançar.' });
      }
    });
  }

  // ─── Transferência ────────────────────────────────────────
  transferenciaDialog = false;
  salvandoTransf = false;
  transfOrigem: number | null = null;
  transfDestino: number | null = null;
  transfValor = 0;
  transfDescricao = '';

  abrirTransferencia() {
    this.transfOrigem = null;
    this.transfDestino = null;
    this.transfValor = 0;
    this.transfDescricao = '';
    this.transferenciaDialog = true;
  }

  fazerTransferencia() {
    if (!this.transfOrigem || !this.transfDestino || this.transfValor <= 0) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Informe conta origem, destino e valor > 0.' });
      return;
    }
    if (this.transfOrigem === this.transfDestino) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Conta origem e destino devem ser diferentes.' });
      return;
    }
    this.salvandoTransf = true;
    const hoje = new Date().toISOString().split('T')[0];
    const origemNome = this.contasTransf.find(c => c.value === this.transfOrigem)?.label || 'Origem';
    const destinoNome = this.contasTransf.find(c => c.value === this.transfDestino)?.label || 'Destino';
    const desc = this.transfDescricao || `Transferência: ${origemNome} → ${destinoNome}`;

    this.finService.createFluxoCaixa({
      dataMovimento: hoje, descricao: desc,
      tipoMovimentacao: 'SAIDA', valor: this.transfValor,
      contaBancariaId: this.transfOrigem, observacoes: 'Transferência saída'
    }).subscribe({
      next: () => {
        this.finService.createFluxoCaixa({
          dataMovimento: hoje, descricao: desc,
          tipoMovimentacao: 'ENTRADA', valor: this.transfValor,
          contaBancariaId: this.transfDestino!, observacoes: 'Transferência entrada'
        }).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Transferência realizada!' });
            this.transferenciaDialog = false;
            this.salvandoTransf = false;
            this.buscar();
          },
          error: () => {
            this.salvandoTransf = false;
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao registrar entrada da transferência.' });
          }
        });
      },
      error: () => {
        this.salvandoTransf = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao registrar saída da transferência.' });
      }
    });
  }

  // ─── Fechamento de Caixa ──────────────────────────────────
  perguntaFechDialog = false;
  fechamentoDialog = false;
  salvandoFech = false;
  fechData = new Date().toISOString().substring(0, 10);
  fechHora = new Date().toTimeString().substring(0, 5);

  fecharCaixa() { this.perguntaFechDialog = true; }

  confirmarFechamento() {
    this.perguntaFechDialog = false;
    this.fechData = new Date().toISOString().substring(0, 10);
    this.fechHora = new Date().toTimeString().substring(0, 5);
    this.fechamentoDialog = true;
  }

  salvarFechamento() {
    this.salvandoFech = true;
    const req = {
      dataAbertura:   `${this.dataInicial}T00:00:00`,
      dataFechamento: `${this.fechData}T${this.fechHora}:00`,
      saldoInicial:   this.saldoInicial,
      saldoFinal:     this.saldoFinal,
      totalEntradas:  this.totalEntrada,
      totalSaidas:    this.totalSaida,
      observacoes:    `Fechamento — Período: ${this.dataInicial} a ${this.dataFinal}`,
      situacao:       'FECHADO',
    };
    this.finService.createFechamentoCaixa(req).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Caixa fechado!', detail: 'O fechamento foi registrado com sucesso.' });
        this.fechamentoDialog = false;
        this.salvandoFech = false;
        this.buscar();
      },
      error: (e) => {
        this.salvandoFech = false;
        this.messageService.add({ severity: 'error', summary: 'Erro ao fechar', detail: e?.error?.message || 'Verifique os dados.' });
      }
    });
  }

  // ─── Busca ────────────────────────────────────────────────
  buscar() {
    this.loading = true;
    const params: any = {
      page: 0, size: 200,
      dataInicio: this.dataInicial,
      dataFim:    this.dataFinal,
    };
    if (typeof this.contaSelecionada === 'number')      params.contaBancariaId = this.contaSelecionada;
    if (typeof this.centroCustoSelecionado === 'number') params.centroCustoId   = this.centroCustoSelecionado;

    this.finService.listFluxoCaixa(params).subscribe({
      next: (resp) => {
        let content = resp?.content || [];

        // Ordena ascendente (do mais antigo pro mais novo) para calcular saldo progressivo
        content.sort((a: any, b: any) => {
          const d1 = new Date(a.dataMovimento).getTime() || 0;
          const d2 = new Date(b.dataMovimento).getTime() || 0;
          if (d1 === d2) return (a.id || 0) - (b.id ||0);
          return d1 - d2;
        });

        // Pega o primeiro saldo acumulado para base de cálculo (ou 0 se não vier)
        this.saldoInicial = content.length
          ? Number(content[0]?.saldoAcumulado || 0) - (String(content[0]?.tipoMovimentacao).toUpperCase() === 'ENTRADA' ? Number(content[0]?.valor || 0) : -Number(content[0]?.valor || 0))
          : 0;

        this.linhas = content.map((r: any) => {
          const tipo = String(r.tipoMovimentacao || '').toUpperCase() as LinhaCaixa['tipo'];
          const valor = Number(r.valor || 0);

          let entrada = 0, saida = 0;
          if (tipo === 'ENTRADA') entrada = valor;
          if (tipo === 'SAIDA') saida = valor;

          let saldoLinha = entrada - saida;

          const fmtDate = (d: string) => {
            if (!d) return '-';
            const [y, m, day] = d.split('-');
            return `${day}/${m}/${y}`;
          };
          return {
            id: Number(r.id || 0),
            tipo,
            data: r.dataMovimento || '',
            dataFmt: fmtDate(r.dataMovimento),
            descricao: r.descricao || '',
            conta: r.contaBancariaNome || '',
            centroCusto: r.centroCustoNome || '',
            entrada,
            saida,
            saldo: saldoLinha,
          } as LinhaCaixa;
        });

        // Após iterar tudo, o último saldoRunning é nosso saldo da filtragem
        // Mas podemos recalcular totals p ter ctz p os cards
        this.totalEntrada = this.linhas.reduce((acc, curr) => acc + curr.entrada, 0);
        this.totalSaida = this.linhas.reduce((acc, curr) => acc + curr.saida, 0);
        this.saldoFinal = this.saldoInicial + this.totalEntrada - this.totalSaida;

        this.loading = false;
      },
      error: () => {
        this.linhas = [];
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar o fluxo de caixa.' });
      }
    });
  }

  get linhasFiltradas(): LinhaCaixa[] {
    if (!this.filtroTexto) return this.linhas;
    const t = this.filtroTexto.toLowerCase();
    return this.linhas.filter(l =>
      l.descricao.toLowerCase().includes(t) ||
      l.conta.toLowerCase().includes(t) ||
      l.dataFmt.includes(t)
    );
  }

  formatCurrency(v: number): string {
    return v.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }

  private carregarAuxiliares() {
    this.finService.listContasBancarias().subscribe({
      next: (page) => {
        const items = (page?.content || []).map((c: any) => ({
          label: c.nome || `${c.bancoNome} • ${c.agencia}/${c.conta}`,
          value: Number(c.id)
        }));
        this.contaOptions = [{ label: 'Todas as contas', value: 'TODAS' }, ...items];
        this.contasTransf = items;
      }
    });
    this.finService.listCentrosCusto().subscribe({
      next: (page) => {
        const items = (page?.content || []).map((c: any) => ({ label: c.nome, value: Number(c.id) }));
        this.centroCustoOptions = [{ label: 'Todos os centros', value: 'TODOS' }, ...items];
      }
    });
  }

  ngOnInit() {
    const qp = this.route.snapshot.queryParamMap;
    if (qp.get('de'))  this.dataInicial = qp.get('de')!;
    if (qp.get('ate')) this.dataFinal   = qp.get('ate')!;
    this.carregarAuxiliares();
    this.buscar();
  }
}
