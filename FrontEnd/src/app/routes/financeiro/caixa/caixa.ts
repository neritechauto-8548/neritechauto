import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { FinanceiroService } from '../financeiro.service';
import { DepartamentoService } from '../../configuracoes/departamentos/departamento.service';
import { AuthService } from '../../../core/authentication/auth.service';
import { EmpresaService } from '../../configuracoes/empresa/services/empresa.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { DatePickerModule } from 'primeng/datepicker';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TextareaModule } from 'primeng/textarea';
import { MessageService, ConfirmationService } from 'primeng/api';
import { RelatoriosService } from '../../relatorios/relatorios.service';

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
  dataVencimentoFmt?: string;
  dataPagamentoFmt?: string;
  formaPagamento: string;
}

@Component({
  standalone: true,
  selector: 'app-caixa',
  templateUrl: './caixa.html',
  imports: [
    CommonModule, FormsModule, ButtonModule, SelectModule,
    InputTextModule, InputNumberModule, DialogModule, DatePickerModule,
    ToastModule, ConfirmDialogModule, TextareaModule
  ],
  providers: [MessageService, ConfirmationService]
})
export class CaixaComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly finService = inject(FinanceiroService);
  private readonly departamentoService = inject(DepartamentoService);
  private readonly messageService = inject(MessageService);
  private readonly confirmService = inject(ConfirmationService);
  private readonly authService = inject(AuthService);
  private readonly empresaService = inject(EmpresaService);
  private readonly storage = inject(LocalStorageService);
  private readonly relatoriosService = inject(RelatoriosService);

  // ─── Estado ───────────────────────────────────────────────
  loading = false;
  pdfLoading = false;
  linhas: LinhaCaixa[] = [];
  filtroTexto = '';
  entradasPorForma: { forma: string; total: number }[] = [];
  saidasPorForma: { forma: string; total: number }[] = [];
  caixaFechado = false;
  ultimoFechamentoData = '';

  // Informações da Empresa & Usuário para Relatório Impresso
  empresaInfo: any = null;
  empresaEndereco = '';
  usuarioAtual = '';
  dataGeracao = new Date();

  // Filtros
  dataInicial = '';
  dataFinal   = '';
  contaSelecionada: number | 'TODAS' = 'TODAS';
  centroCustoSelecionado: number | 'TODOS' = 'TODOS';
  contasBancariasCompletas: any[] = [];

  // Auxiliares
  contaOptions: { label: string; value: number | 'TODAS' }[] = [
    { label: 'Todas as contas', value: 'TODAS' }
  ];
  centroCustoOptions: { label: string; value: number | 'TODOS' }[] = [
    { label: 'Todos os departamentos', value: 'TODOS' }
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

  getSaldoAtual(contaId: number | null | 'TODAS'): number {
    if (!contaId || contaId === 'TODAS') return 0;
    const conta = this.contasBancariasCompletas.find(c => Number(c.id) === Number(contaId));
    return conta ? Number(conta.saldoAtual || 0) : 0;
  }

  getSaldoFinalOrigem(): number {
    const atual = this.getSaldoAtual(this.transfOrigem);
    return atual - (this.transfValor || 0);
  }

  getSaldoFinalDestino(): number {
    const atual = this.getSaldoAtual(this.transfDestino);
    return atual + (this.transfValor || 0);
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
  sucessoFechDialog = false;
  salvandoFech = false;
  fechData = new Date().toISOString().substring(0, 10);
  fechHora = new Date().toTimeString().substring(0, 5);

  fecharCaixa() {
    this.fechData = new Date().toISOString().substring(0, 10);
    this.fechHora = new Date().toTimeString().substring(0, 5);
    this.fechamentoDialog = true; // Passo 1
  }

  salvarDataHoraFechamento() {
    this.fechamentoDialog = false;
    this.perguntaFechDialog = true; // Passo 2
  }

  salvarFechamentoFinal() {
    this.salvandoFech = true;
    const req = {
      dataAbertura:   `${this.dataInicial || new Date().toISOString().split('T')[0]}T00:00:00`,
      dataFechamento: `${this.fechData}T${this.fechHora}:00`,
      saldoInicial:   this.saldoInicial,
      saldoFinal:     this.saldoFinal,
      totalEntradas:  this.totalEntrada,
      totalSaidas:    this.totalSaida,
      observacoes:    `Fechamento — Período: ${this.dataInicial || 'Inicial'} a ${this.dataFinal || 'Final'}`,
      situacao:       'FECHADO',
    };
    this.finService.createFechamentoCaixa(req).subscribe({
      next: () => {
        this.perguntaFechDialog = false;
        this.salvandoFech = false;
        this.sucessoFechDialog = true; // Passo 3
        this.buscar();
      },
      error: (e) => {
        this.salvandoFech = false;
        this.messageService.add({ severity: 'error', summary: 'Erro ao fechar', detail: e?.error?.message || 'Verifique os dados.' });
      }
    });
  }

  verificarCaixaFechado() {
    this.finService.listFechamentoCaixa({ page: 0, size: 1, sort: 'dataFechamento,desc' }).subscribe({
      next: (page: any) => {
        const content = page?.content || [];
        if (content.length > 0) {
          const ultimo = content[0];
          this.ultimoFechamentoData = ultimo.dataHoraOperacao || ultimo.dataFechamento ? (ultimo.dataHoraOperacao || ultimo.dataFechamento).split('T')[0] : '';
          
          const dataFinalRef = this.dataFinal || new Date().toISOString().split('T')[0];
          this.caixaFechado = !!(this.ultimoFechamentoData && dataFinalRef <= this.ultimoFechamentoData);
        } else {
          this.caixaFechado = false;
        }
      },
      error: () => {
        this.caixaFechado = false;
      }
    });
  }

  // ─── Busca ────────────────────────────────────────────────
  buscar() {
    this.loading = true;
    const params: any = {
      page: 0, size: 200,
    };
    if (this.dataInicial) params.dataInicio = this.dataInicial;
    if (this.dataFinal)   params.dataFim    = this.dataFinal;
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
            if (!d) return '';
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
            dataVencimentoFmt: fmtDate(r.dataVencimento),
            dataPagamentoFmt: fmtDate(r.dataPagamento),
            formaPagamento: r.formaPagamentoNome || 'SEM ESPECIFICAR'
          } as LinhaCaixa;
        });

        // Após iterar tudo, o último saldoRunning é nosso saldo da filtragem
        // Mas podemos recalcular totals p ter ctz p os cards
        this.totalEntrada = this.linhas.reduce((acc, curr) => acc + curr.entrada, 0);
        this.totalSaida = this.linhas.reduce((acc, curr) => acc + curr.saida, 0);
        this.saldoFinal = this.saldoInicial + this.totalEntrada - this.totalSaida;

        // Grouping pay method totals for print report
        const entGroup: Record<string, number> = {};
        const saiGroup: Record<string, number> = {};
        this.linhas.forEach(l => {
          if (l.tipo === 'ENTRADA') {
            entGroup[l.formaPagamento] = (entGroup[l.formaPagamento] || 0) + l.entrada;
          } else if (l.tipo === 'SAIDA') {
            saiGroup[l.formaPagamento] = (saiGroup[l.formaPagamento] || 0) + l.saida;
          }
        });
        this.entradasPorForma = Object.keys(entGroup).map(k => ({ forma: k, total: entGroup[k] }));
        this.saidasPorForma = Object.keys(saiGroup).map(k => ({ forma: k, total: saiGroup[k] }));

        this.verificarCaixaFechado();
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

  formatNumber(v: number): string {
    return (v || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  imprimir() {
    this.pdfLoading = true;
    const params: any = {};
    if (this.dataInicial) params.dataInicio = this.dataInicial;
    if (this.dataFinal)   params.dataFim    = this.dataFinal;
    if (typeof this.contaSelecionada === 'number')      params.contaBancariaId = this.contaSelecionada;
    if (typeof this.centroCustoSelecionado === 'number') params.centroCustoId   = this.centroCustoSelecionado;
    params.usuarioNome = this.usuarioAtual || 'ALEXANDRE ROMULO ALBUQUERQUE NERI';

    this.relatoriosService.gerarRelatorio('caixa', params).subscribe({
      next: (blob) => {
        this.pdfLoading = false;
        this.relatoriosService.abrirBlobEmNovaAba(blob);
      },
      error: (err) => {
        this.pdfLoading = false;
        console.error('Erro ao gerar relatório de caixa', err);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível gerar o relatório.' });
      }
    });
  }

  getContaSelecionadaLabel(): string {
    if (this.contaSelecionada === 'TODAS') return 'Todas as contas';
    const c = this.contasBancariasCompletas.find(x => Number(x.id) === Number(this.contaSelecionada));
    return c ? `${c.bancoNome} • ${c.agencia}/${c.conta}` : 'Todas as contas';
  }

  private carregarAuxiliares() {
    this.finService.listContasBancarias().subscribe({
      next: (page) => {
        const rawItems = page?.content || [];
        this.contasBancariasCompletas = rawItems;
        const items = rawItems.map((c: any) => ({
          label: `${c.bancoNome} • ${c.agencia}/${c.conta}`,
          value: Number(c.id)
        }));
        this.contaOptions = [{ label: 'Todas as contas', value: 'TODAS' }, ...items];
        this.contasTransf = items;
      }
    });
    this.departamentoService.list({ size: 1000 }).subscribe({
      next: (page) => {
        const items = (page?.content || []).map((c: any) => ({ label: c.descricao, value: Number(c.id) }));
        this.centroCustoOptions = [{ label: 'Todos os departamentos', value: 'TODOS' }, ...items];
      }
    });
  }

  private carregarEmpresaEUsuario() {
    let tenantId = this.storage.get('tenantId');
    if (!tenantId || (typeof tenantId === 'object' && Object.keys(tenantId).length === 0)) {
      tenantId = this.storage.get('empresaId');
    }
    if (tenantId && typeof tenantId === 'object' && (tenantId as any).id) {
      tenantId = (tenantId as any).id;
    }
    const id = Number(tenantId || 1);

    this.empresaService.getEmpresa(id).subscribe({
      next: (emp) => {
        this.empresaInfo = emp;
      }
    });

    this.empresaService.getEnderecos(id).subscribe({
      next: (endrs) => {
        const principal = (endrs || []).find(e => e.principal) || (endrs || [])[0];
        if (principal) {
          const comp = principal.complemento ? `, ${principal.complemento}` : '';
          this.empresaEndereco = `${principal.logradouro}, N ${principal.numero}${comp}, ${principal.bairro}, ${principal.cidade} - ${principal.estado}`;
        }
      }
    });

    this.authService.user().subscribe({
      next: (u) => {
        this.usuarioAtual = u.name || '';
      }
    });
  }

  ngOnInit() {
    const qp = this.route.snapshot.queryParamMap;
    if (qp.get('de'))  this.dataInicial = qp.get('de')!;
    if (qp.get('ate')) this.dataFinal   = qp.get('ate')!;
    this.carregarAuxiliares();
    this.carregarEmpresaEUsuario();
    this.buscar();
  }
}
