import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { A11yModule } from '@angular/cdk/a11y';
import { Observable, Subscription } from 'rxjs';
import { RelatoriosService } from '../../relatorios/relatorios.service';
import { ComissaoFinanceiraService, ComissaoLinha, FiltrosComissoes, PlanoContaComissao } from './comissao-financeira.service';

@Component({
  standalone: true,
  selector: 'app-comissoes',
  templateUrl: './comissoes.html',
  imports: [CommonModule, FormsModule, RouterModule, A11yModule]
})
export class ComissoesComponent implements OnInit, OnDestroy {
  private service = inject(ComissaoFinanceiraService);
  private relatorios = inject(RelatoriosService);
  private listSub?: Subscription;
  registros: ComissaoLinha[] = [];
  selecionados = new Set<number>();
  lote = false;
  selecionar(id: number, marcado: boolean): void { if(marcado)this.selecionados.add(id);else this.selecionados.delete(id); }
  selecionarTodos(marcado: boolean): void { this.selecionados.clear(); if(marcado) this.registros.filter(x => !x.pago).forEach(x => this.selecionados.add(x.id)); }
  get totalSelecionaveis(): number { return this.registros.filter(x => !x.pago).length; }
  get todosSelecionados(): boolean { return this.totalSelecionaveis>0 && this.registros.filter(x => !x.pago).every(x => this.selecionados.has(x.id)); }
  abrirQuitacaoLote(): void {
    const selecionada = this.registros.find(x => this.selecionados.has(x.id));
    if(!selecionada || this.busy) return;
    this.abrirQuitacao(selecionada); this.lote = true;
  }
  loading = false;
  busy = false;
  pdfLoading = false;
  error = '';
  notice = '';
  busca = '';
  filtroAberto = false;
  filtros: FiltrosComissoes = {};
  rascunho: { os: string; paga: string; cliente: string; placa: string; inicio: string; fim: string } = {
    os: '', paga: '', cliente: '', placa: '', inicio: '', fim: ''
  };
  pagina = 0;
  total = 0;
  totalPaginas = 1;
  operacao: 'quitar' | 'estornar' | null = null;
  selecionada: ComissaoLinha | null = null;
  dataPagamento = '';
  observacao = '';
  operacaoErro = '';
  gerarContaPagar = false;
  liquidarContaPagar = false;
  planoContaId: number | null = null;
  tipoDocumento = '';
  planosContas: PlanoContaComissao[] = [];
  carregandoPlanos = false;
  get planoSelecionado(): boolean { return !this.gerarContaPagar || (!!this.planoContaId && !!this.tipoDocumento); }
  aoMarcarConta(): void { if (!this.gerarContaPagar) { this.planoContaId=null; this.tipoDocumento='';this.liquidarContaPagar=false; } }

  ngOnInit(): void { this.carregar(); }
  ngOnDestroy(): void { this.listSub?.unsubscribe(); }

  carregar(): void {
    this.listSub?.unsubscribe();
    this.loading = true; this.error = '';
    const filters = { ...this.filtros, nome: this.busca.trim() || undefined };
    this.listSub = this.service.listar(filters, this.pagina, 20).subscribe({
      next: page => {
        this.registros = page.content ?? [];
        this.selecionados.clear();
        this.total = page.totalElements ?? 0;
        this.totalPaginas = Math.max(1, page.totalPages ?? 1);
        this.loading = false;
      },
      error: err => {
        this.registros = []; this.total = 0; this.loading = false;
        this.error = err.status === 403 ? 'Você não possui permissão para consultar comissões.'
          : 'Falha ao consultar comissões desta empresa.';
      }
    });
  }
  pesquisar(): void { this.pagina = 0; this.carregar(); }
  mudarPagina(delta: number): void {
    const nova = this.pagina + delta;
    if (nova < 0 || nova >= this.totalPaginas || this.loading) return;
    this.pagina = nova; this.carregar();
  }
  abrirFiltros(): void {
    this.rascunho = {
      os: this.filtros.numeroOs || '',
      paga: this.filtros.paga === undefined ? '' : String(this.filtros.paga),
      cliente: this.filtros.cliente || '', placa: this.filtros.placa || '',
      inicio: this.filtros.inicio || '', fim: this.filtros.fim || ''
    };
    this.filtroAberto = true;
  }
  aplicarFiltros(): void {
    const os = this.rascunho.os.trim();
    if (this.rascunho.inicio && this.rascunho.fim && this.rascunho.inicio > this.rascunho.fim) {
      this.error = 'Data inicial não pode ser posterior à final.'; return;
    }
    this.error = '';
    this.filtros = {
      numeroOs: os || undefined,
      paga: this.rascunho.paga === '' ? undefined : this.rascunho.paga === 'true',
      cliente: this.rascunho.cliente.trim() || undefined, placa: this.rascunho.placa.trim() || undefined,
      inicio: this.rascunho.inicio || undefined,
      fim: this.rascunho.fim || undefined
    };
    this.filtroAberto = false; this.pagina = 0; this.carregar();
  }
  limparFiltros(): void {
    this.rascunho = { os: '', paga: '', cliente: '', placa: '', inicio: '', fim: '' };
    this.filtros = {}; this.busca = ''; this.filtroAberto = false;
    this.pagina = 0; this.carregar();
  }
  aprovar(linha: ComissaoLinha): void {
    if (this.busy || !confirm('Aprovar a comissão de ' + linha.funcionarioNome + '? Verifique a apuração e a origem antes de confirmar.')) return;
    this.busy = true; this.notice = '';
    this.service.aprovar(linha.id).subscribe({
      next: () => { this.busy = false; this.notice = 'Comissão aprovada e registrada em auditoria.'; this.carregar(); },
      error: err => { this.busy = false; this.error = this.fail(err); }
    });
  }
  abrirQuitacao(linha: ComissaoLinha): void {
    this.selecionada = linha; this.operacao = 'quitar'; this.lote = false; this.observacao = '';
    this.dataPagamento = this.localDate(new Date()); this.operacaoErro = '';
    this.gerarContaPagar=false; this.liquidarContaPagar=false; this.planoContaId=null; this.tipoDocumento='';
    this.carregandoPlanos=true;
    this.service.listarPlanos().subscribe({next: p => {this.planosContas=p;this.carregandoPlanos=false;},
      error: () => {this.carregandoPlanos=false;this.operacaoErro='Não foi possível consultar o plano de contas.';} });
  }
  abrirEstorno(linha: ComissaoLinha): void {
    this.selecionada = linha; this.operacao = 'estornar'; this.lote = false; this.operacaoErro = '';
  }
  fecharOperacao(): void { if (!this.busy) { this.operacao = null; this.selecionada = null; } }
  confirmarOperacao(): void {
    if (!this.selecionada || !this.operacao || this.busy) return;
    if (this.operacao === 'quitar' && (!this.dataPagamento || this.dataPagamento > this.localDate(new Date()))) {
      this.operacaoErro = 'Informe a data real do pagamento (não futura).'; return;
    }
    if (this.operacao === 'quitar' && !this.planoSelecionado) {
      this.operacaoErro = 'Selecione plano de contas e tipo de documento.'; return;
    }
    if (this.operacao === 'quitar' && this.liquidarContaPagar) {
      this.operacaoErro = 'A liquidação automática exige pagamento financeiro comprovado; utilize Contas a Pagar.'; return;
    }
    this.busy = true; this.operacaoErro = '';
    const op: Observable<unknown> = this.operacao === 'quitar'
      ? (this.lote ? this.service.quitarLote([...this.selecionados], this.dataPagamento, this.observacao,
          { gerarContaPagar:this.gerarContaPagar, liquidarContaPagar:false,
            planoContaId:this.gerarContaPagar?(this.planoContaId||undefined):undefined,
            tipoDocumento:this.gerarContaPagar?this.tipoDocumento:undefined })
        : this.service.processarQuitacao(this.selecionada.id, this.dataPagamento, this.observacao,
          { gerarContaPagar:this.gerarContaPagar, liquidarContaPagar:false,
            planoContaId:this.gerarContaPagar?(this.planoContaId||undefined):undefined,
            tipoDocumento:this.gerarContaPagar?this.tipoDocumento:undefined }))
      : this.service.estornar(this.selecionada.id);
    op.subscribe({
      next: () => {
        const status = this.operacao;
        this.busy = false; this.fecharOperacao(); this.selecionados.clear();
        this.notice = status === 'quitar'
          ? (this.gerarContaPagar ? 'Comissão quitada e conta a pagar criada em aberto.' : 'Quitação registrada. Nenhuma conta a pagar nem transferência bancária foi criada.')
          : 'Quitação estornada e registrada em auditoria.';
        this.carregar();
      },
      error: (err: unknown) => { this.busy = false; this.operacaoErro = this.fail(err); }
    });
  }
  private fail(err: any): string {
    return err.status === 403 ? 'Operação não autorizada.'
      : err.status === 409 ? 'Conflito: confira aprovação, recebimento da fatura ou pagamento prévio.'
      : err.status === 422 ? 'Dados inválidos. Revise a operação.'
      : 'Não foi possível executar a operação.';
  }
  private localDate(d: Date): string {
    return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-'
      + String(d.getDate()).padStart(2, '0');
  }
  imprimirRelatorio(): void {
    if (this.pdfLoading) return;
    this.pdfLoading = true;
    const payload = { dataInicio: this.filtros.inicio, dataFim: this.filtros.fim };
    this.relatorios.gerarRelatorio('comissoes', payload).subscribe({
      next: blob => { this.pdfLoading = false; this.relatorios.abrirBlobEmNovaAba(blob); },
      error: () => { this.pdfLoading = false; this.error = 'O relatório PDF não está disponível no momento.'; }
    });
  }
  formatarMoeda(value: number | null | undefined): string {
    return Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
