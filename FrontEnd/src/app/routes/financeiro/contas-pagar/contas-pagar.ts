import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { DatePickerModule } from 'primeng/datepicker';
import { InputNumberModule } from 'primeng/inputnumber';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SelectModule } from 'primeng/select';
import { TabsModule } from 'primeng/tabs';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '../../../shared/services/confirmation.service';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { forkJoin } from 'rxjs';

import { FinanceiroService } from '../financeiro.service';
import { FornecedorService } from '../../fornecedor/fornecedor.service';
import { DepartamentoService } from '../../configuracoes/departamentos/departamento.service';
import { RelatoriosService } from '../../relatorios/relatorios.service';
import {
  ContasPagarRequest,
  ContasPagarResponse,
  StatusTitulo,
  TipoTitulo
} from '../models/financeiro.models';

@Component({
  standalone: true,
  selector: 'app-contas-pagar',
  templateUrl: './contas-pagar.html',
  imports: [
    CommonModule, FormsModule, ButtonModule, InputTextModule, TableModule,
    TagModule, DialogModule, DatePickerModule, InputNumberModule, TextareaModule,
    AutoCompleteModule, ToastModule, SelectModule, ConfirmationDialogComponent,
    TabsModule, CheckboxModule
  ],
  providers: [MessageService, ConfirmationService]
})
export class ContasPagarComponent implements OnInit {
  private service = inject(FinanceiroService);
  private fornecedorService = inject(FornecedorService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private departamentoService = inject(DepartamentoService);
  private relatoriosService = inject(RelatoriosService);

  loading = false;
  salvando = false;
  rows: ContasPagarResponse[] = [];
  selectedRows: ContasPagarResponse[] = [];
  termo = '';

  // Paginação
  page = 0;
  pageSize = 15;
  totalElements = 0;

  // Totais
  totalAberto = 0;
  totalVencido = 0;
  totalPago = 0;

  // Dialogs
  dialogVisible = false;
  dialogDetalhesVisible = false;
  quitarDialogVisible = false;
  quitarLoteDialogVisible = false;
  editandoId: number | null = null;
  detalhesRowSelected: ContasPagarResponse | null = null;
  quitarRowSelected!: ContasPagarResponse;
  formPagamento: any = {};
  valorTotalQuitarLote = 0;
  contaQuitarLoteId: number | null = null;

  // Filtros Avançados
  filtroTipo = 'Todas';
  vencInicio: Date | null = null;
  vencFim: Date | null = null;
  buscarPor = 'conta';
  filtroValor: any = 'TODOS';

  tiposFiltro = [
    { label: 'Todas', value: 'Todas' },
    { label: 'Todas Quitado', value: 'Todas Quitado' },
    { label: 'Todas Aberto', value: 'Todas Aberto' },
    { label: 'Todas Receita', value: 'Todas Receita' },
    { label: 'Todas Despesa', value: 'Todas Despesa' },
    { label: 'Despesa Quitado', value: 'Despesa Quitado' },
    { label: 'Despesa Aberto', value: 'Despesa Aberto' },
    { label: 'Receita Quitado', value: 'Receita Quitado' },
    { label: 'Receita Aberto', value: 'Receita Aberto' },
    { label: 'Transferencias', value: 'Transferencias' }
  ];

  buscarPorOptions = [
    { label: 'Caixa/Conta', value: 'conta' },
    { label: 'Departamento', value: 'centroCusto' },
    { label: 'Plano de Contas', value: 'planoContas' },
    { label: 'Forma de Pagamento', value: 'formaPagamento' }
  ];

  get filtroValorOptions() {
      const defaultOption = [{ label: 'TODOS', value: 'TODOS' }];
      switch (this.buscarPor) {
          case 'conta':
              return [...defaultOption, ...this.contasBancarias];
          case 'centroCusto':
              return [...defaultOption, ...this.departamentos];
          case 'planoContas':
              return [...defaultOption, ...this.planosConta];
          case 'formaPagamento':
              return [...defaultOption, ...this.formasPagamento];
          default:
              return defaultOption;
      }
  }

  onBuscarPorChange() {
      this.filtroValor = 'TODOS';
  }

  // Auxiliares
  formasPagamento: any[] = [];
  contasBancarias: any[] = [];
  departamentos: any[] = [];
  planosConta: any[] = [];

  // Autocomplete fornecedor
  fornecedoresFiltrados: any[] = [];
  private _fornecedorNome = '';
  get fornecedorNome(): any { return this._fornecedorNome; }
  set fornecedorNome(val: any) {
    if (val && typeof val === 'object') {
      this._fornecedorNome = val.nomeFantasia || val.razaoSocial || '';
      this.form.fornecedorId = val.id;
      this.form.fornecedorObj = val;
    } else {
      this._fornecedorNome = val || '';
    }
  }

  tiposDocumento = Object.values(TipoTitulo).map(v => ({ label: v, value: v }));

  form: any = this.emptyForm();

  emptyForm() {
    return {
      descricao: '',
      fornecedorId: null as number | null,
      fornecedorObj: null as any,
      dataEmissao: new Date(),
      dataVencimento: new Date(),
      valorOriginal: 0,
      tipoTitulo: TipoTitulo.OUTROS,
      observacoes: '',
      quitarAgora: false,
      formaPagamentoId: null as number | null,
      contaBancariaId: null as number | null,
      centroCustoId: null as number | null,
      planoContasId: null as number | null,
      numeroTitulo: '',
      numeroDocumento: '',
    };
  }

  ngOnInit() {
    const hoje = new Date();
    this.vencInicio = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.vencFim = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);
    this.carregar();
    this.carregarAuxiliares();
  }

  carregarAuxiliares() {
    this.service.listFormasPagamento().subscribe({
      next: res => {
        const items = res?.content || res || [];
        this.formasPagamento = items.map((x: any) => ({ label: x.nome, value: x.id }));
      },
      error: err => console.error('Erro formas pagamento', err)
    });
    this.service.listContasBancarias().subscribe({
      next: res => {
        const items = res?.content || res || [];
        this.contasBancarias = items.map((x: any) => ({
          label: x.nome || `${x.bancoNome} • ${x.agencia}/${x.conta}`,
          value: x.id
        }));
      },
      error: err => console.error('Erro contas bancarias', err)
    });
    this.departamentoService.list({ size: 1000 }).subscribe({
      next: res => {
        const items = res?.content || res || [];
        this.departamentos = items.map((x: any) => ({ label: x.descricao, value: x.id }));
      },
      error: err => console.error('Erro departamentos', err)
    });
    this.service.listPlanosConta().subscribe({
      next: res => {
        const items = res?.content || res || [];
        this.planosConta = items.map((x: any) => ({ label: x.nome || x.descricao, value: x.id }));
      },
      error: err => console.error('Erro planos conta', err)
    });
  }

  carregar() {
    this.loading = true;
    this.service.listPagar({ page: this.page, size: this.pageSize, sort: 'dataVencimento,asc' }).subscribe({
      next: (res) => {
        this.rows = res.content || [];
        this.totalElements = res.totalElements || 0;
        this.calcularTotais();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar contas.' });
      }
    });
  }

  calcularTotais() {
    const hoje = new Date(); hoje.setHours(0,0,0,0);
    this.totalAberto = this.rows.filter(r => r.status === StatusTitulo.ABERTO).reduce((s, r) => s + r.valorOriginal, 0);
    this.totalVencido = this.rows.filter(r => r.status === StatusTitulo.VENCIDO || (r.status === StatusTitulo.ABERTO && new Date(r.dataVencimento) < hoje)).reduce((s, r) => s + r.valorOriginal, 0);
    this.totalPago = this.rows.filter(r => r.status === StatusTitulo.PAGO).reduce((s, r) => s + (r.valorPago || 0), 0);
  }

  get rowsFiltradas() {
    let result = this.rows;
    if (this.termo) {
      const t = this.termo.toLowerCase();
      result = result.filter(r =>
        r.descricao?.toLowerCase().includes(t) ||
        r.numeroDocumento?.toLowerCase().includes(t)
      );
    }
    
    // 1. Filtro de Tipo
    if (this.filtroTipo !== 'Todas') {
        switch (this.filtroTipo) {
            case 'Todas Quitado':
            case 'Despesa Quitado':
                result = result.filter(r => r.status === StatusTitulo.PAGO);
                break;
            case 'Todas Aberto':
            case 'Despesa Aberto':
                result = result.filter(r => r.status !== StatusTitulo.PAGO && r.status !== StatusTitulo.CANCELADO);
                break;
            case 'Todas Despesa':
                break;
            case 'Todas Receita':
            case 'Receita Quitado':
            case 'Receita Aberto':
                result = [];
                break;
            case 'Transferencias':
                result = result.filter(r => 
                    r.descricao?.toLowerCase().includes('transferência') ||
                    (r.observacoes && r.observacoes.toLowerCase().includes('transferência'))
                );
                break;
        }
    }

    // 2. Filtros Avançados
    // Filtro por data
    if (this.vencInicio) {
        const start = new Date(this.vencInicio);
        start.setHours(0, 0, 0, 0);
        result = result.filter(r => {
            if (!r.dataVencimento) return false;
            const d = new Date(r.dataVencimento + 'T00:00');
            return d >= start;
        });
    }
    if (this.vencFim) {
        const end = new Date(this.vencFim);
        end.setHours(23, 59, 59, 999);
        result = result.filter(r => {
            if (!r.dataVencimento) return false;
            const d = new Date(r.dataVencimento + 'T00:00');
            return d <= end;
        });
    }

    // Filtro por Caixa, Centro de Custo, Plano de Contas, Forma de Pagamento
    if (this.buscarPor && this.filtroValor && this.filtroValor !== 'TODOS') {
        const filterId = Number(this.filtroValor);
        switch (this.buscarPor) {
            case 'conta':
                result = result.filter(r => r.contaBancariaId === filterId);
                break;
            case 'centroCusto':
                result = result.filter(r => r.centroCustoId === filterId);
                break;
            case 'planoContas':
                result = result.filter(r => r.planoContasId === filterId);
                break;
            case 'formaPagamento':
                result = result.filter(r => r.formaPagamentoId === filterId);
                break;
        }
    }

    return result;
  }

  imprimir(tipo: string) {
    if (tipo === 'pdf') {
      let dataInicio = '';
      let dataFim = '';
      if (this.vencInicio) {
        const d = new Date(this.vencInicio);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        dataInicio = `${year}-${month}-${day}`;
      }
      if (this.vencFim) {
        const d = new Date(this.vencFim);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        dataFim = `${year}-${month}-${day}`;
      }

      this.messageService.add({
        severity: 'info',
        summary: 'Impressão',
        detail: 'Gerando relatório financeiro...'
      });

      this.relatoriosService.gerarRelatorio('financeiro', { dataInicio, dataFim }).subscribe({
        next: (blob) => {
          this.relatoriosService.abrirBlobEmNovaAba(blob);
        },
        error: (err) => {
          console.error(err);
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao gerar relatório.' });
        }
      });
    } else {
      this.messageService.add({
        severity: 'info',
        summary: 'Impressão',
        detail: `Exportando em formato ${tipo.toUpperCase()}...`
      });
    }
  }

  abrirNovo() {
    this.editandoId = null;
    this.form = this.emptyForm();
    this.fornecedorNome = '';
    this.dialogVisible = true;
  }

  abrirEditar(row: ContasPagarResponse) {
    this.editandoId = row.id;
    this.form = {
      descricao: row.descricao,
      fornecedorId: row.fornecedorId,
      fornecedorObj: null,
      numeroDocumento: row.numeroDocumento || '',
      dataEmissao: row.dataEmissao ? new Date(row.dataEmissao + 'T00:00') : new Date(),
      dataVencimento: row.dataVencimento ? new Date(row.dataVencimento + 'T00:00') : new Date(),
      valorOriginal: row.valorOriginal,
      tipoTitulo: row.tipoTitulo,
      observacoes: row.observacoes || '',
      quitarAgora: false,
      formaPagamentoId: row.formaPagamentoId,
      contaBancariaId: row.contaBancariaId,
      centroCustoId: row.centroCustoId,
      planoContasId: row.planoContasId,
      numeroTitulo: row.numeroTitulo || '',
    };
    this._fornecedorNome = row.fornecedorId ? `Fornecedor #${row.fornecedorId}` : '';
    this.dialogVisible = true;
  }

  abrirDetalhes(row: ContasPagarResponse) {
    this.service.getPagarById(row.id).subscribe({
      next: (res) => {
        this.detalhesRowSelected = res;
        this.dialogDetalhesVisible = true;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar detalhes.' })
    });
  }

  filtrarFornecedores(event: any) {
    this.fornecedorService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
      page => this.fornecedoresFiltrados = page.content
    );
  }

  salvar() {
    if (!this.form.descricao || !this.form.valorOriginal) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Descrição e Valor.' });
      return;
    }
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasPagarRequest = {
      descricao: this.form.descricao,
      fornecedorId: this.form.fornecedorId || undefined,
      numeroDocumento: this.form.numeroDocumento || undefined,
      dataEmissao: toISO(this.form.dataEmissao),
      dataVencimento: toISO(this.form.dataVencimento),
      valorOriginal: this.form.valorOriginal,
      tipoTitulo: this.form.tipoTitulo,
      observacoes: this.form.observacoes || undefined,
      status: this.form.quitarAgora ? StatusTitulo.PAGO : StatusTitulo.ABERTO,
      dataPagamento: this.form.quitarAgora ? toISO(new Date()) : undefined,
      valorPago: this.form.quitarAgora ? this.form.valorOriginal : undefined,
      formaPagamentoId: this.form.formaPagamentoId || undefined,
      contaBancariaId: this.form.contaBancariaId || undefined,
      centroCustoId: this.form.centroCustoId || undefined,
      planoContasId: this.form.planoContasId || undefined,
      numeroTitulo: this.form.numeroTitulo || undefined,
    };

    const op$ = this.editandoId
      ? this.service.updatePagar(this.editandoId, dto)
      : this.service.createPagar(dto);

    op$.subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: this.editandoId ? 'Conta atualizada!' : 'Conta lançada!' });
        this.dialogVisible = false;
        this.salvando = false;
        this.carregar();
      },
      error: (e) => {
        this.salvando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: e?.error?.message || 'Erro ao salvar.' });
      }
    });
  }

  quitar(row: ContasPagarResponse) {
    this.quitarRowSelected = row;

    // Calcular juros e multa sugeridos
    const hoje = new Date(); hoje.setHours(0,0,0,0);
    const vencimento = new Date(row.dataVencimento + 'T00:00');
    let juros = 0;
    let multa = 0;

    if (vencimento < hoje) {
      // Multa padrão de 2%
      multa = row.valorOriginal * 0.02;
      // Juros padrão de 0.033% ao dia
      const diffTime = Math.abs(hoje.getTime() - vencimento.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      juros = row.valorOriginal * 0.00033 * diffDays;
    }

    this.formPagamento = {
      id: row.id,
      descricao: row.descricao,
      valorOriginal: row.valorOriginal,
      valorJuros: Number(juros.toFixed(2)),
      valorMulta: Number(multa.toFixed(2)),
      valorDesconto: 0,
      valorTotal: Number((row.valorOriginal + juros + multa).toFixed(2)),
      dataPagamento: new Date(),
      formaPagamentoId: row.formaPagamentoId || (this.formasPagamento.length > 0 ? this.formasPagamento[0].value : null),
      contaBancariaId: row.contaBancariaId || (this.contasBancarias.length > 0 ? this.contasBancarias[0].value : null),
    };

    this.quitarDialogVisible = true;
  }

  recalcularTotalPagamento() {
    const original = this.formPagamento.valorOriginal || 0;
    const juros = this.formPagamento.valorJuros || 0;
    const multa = this.formPagamento.valorMulta || 0;
    const desconto = this.formPagamento.valorDesconto || 0;
    this.formPagamento.valorTotal = Number((original + juros + multa - desconto).toFixed(2));
  }

  salvarPagamento() {
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasPagarRequest = {
      descricao: this.quitarRowSelected.descricao,
      fornecedorId: this.quitarRowSelected.fornecedorId,
      numeroDocumento: this.quitarRowSelected.numeroDocumento,
      dataEmissao: this.quitarRowSelected.dataEmissao,
      dataVencimento: this.quitarRowSelected.dataVencimento,
      valorOriginal: this.quitarRowSelected.valorOriginal,
      tipoTitulo: this.quitarRowSelected.tipoTitulo,
      observacoes: this.quitarRowSelected.observacoes,
      status: StatusTitulo.PAGO,
      dataPagamento: toISO(this.formPagamento.dataPagamento),
      valorPago: this.formPagamento.valorTotal,
      valorJuros: this.formPagamento.valorJuros,
      valorMulta: this.formPagamento.valorMulta,
      valorDesconto: this.formPagamento.valorDesconto,
      formaPagamentoId: this.formPagamento.formaPagamentoId,
      contaBancariaId: this.formPagamento.contaBancariaId,
      centroCustoId: this.quitarRowSelected.centroCustoId,
      planoContasId: this.quitarRowSelected.planoContasId,
      numeroTitulo: this.quitarRowSelected.numeroTitulo,
    };

    this.service.updatePagar(this.formPagamento.id, dto).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Conta marcada como paga.' });
        this.quitarDialogVisible = false;
        this.salvando = false;
        this.carregar();
      },
      error: () => {
        this.salvando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível registrar o pagamento.' });
      }
    });
  }

  quitarContas() {
    if (!this.selectedRows.length) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione ao menos um lançamento.' });
      return;
    }

    this.valorTotalQuitarLote = this.selectedRows.reduce((sum, row) => sum + row.valorOriginal, 0);
    this.contaQuitarLoteId = null;
    this.quitarLoteDialogVisible = true;
  }

  confirmarQuitar() {
    if (!this.contaQuitarLoteId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione a conta bancária para o pagamento.' });
      return;
    }
    
    this.loading = true;
    const requests = this.selectedRows.map(row => {
      const payload: ContasPagarRequest = {
        descricao: row.descricao,
        fornecedorId: row.fornecedorId,
        numeroDocumento: row.numeroDocumento,
        dataEmissao: row.dataEmissao,
        dataVencimento: row.dataVencimento,
        valorOriginal: row.valorOriginal,
        tipoTitulo: row.tipoTitulo,
        centroCustoId: row.centroCustoId,
        planoContasId: row.planoContasId,
        dataPagamento: new Date().toISOString().split('T')[0],
        valorPago: row.valorOriginal,
        formaPagamentoId: row.formaPagamentoId || 1,
        contaBancariaId: this.contaQuitarLoteId!,
        status: StatusTitulo.PAGO,
        observacoes: row.observacoes || 'Pagamento em lote',
        numeroTitulo: row.numeroTitulo,
      };
      return this.service.updatePagar(row.id, payload);
    });

    forkJoin(requests).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: `${this.selectedRows.length} contas pagas!` });
        this.selectedRows = [];
        this.quitarLoteDialogVisible = false;
        this.carregar();
      },
      error: (err: any) => {
        console.error(err);
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao pagar uma ou mais contas.' });
      }
    });
  }

  excluir(row: ContasPagarResponse) {
    this.confirmationService.confirm({
      title: 'Confirmar Exclusão',
      message: `Deseja realmente excluir "<strong>${row.descricao}</strong>"? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.deletePagar(row.id).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Excluído!' }); this.carregar(); },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir.' })
        });
      }
    });
  }

  getStatusLabel(status: StatusTitulo, venc?: string): string {
    if (status === StatusTitulo.PAGO) return 'Pago';
    if (status === StatusTitulo.CANCELADO) return 'Cancelado';
    if (venc) {
      const v = new Date(venc + 'T00:00'); const h = new Date(); h.setHours(0,0,0,0);
      if (v < h) return 'Vencido';
    }
    return 'Em aberto';
  }

  getStatusClass(status: StatusTitulo, venc?: string): string {
    if (status === StatusTitulo.PAGO) return 'status-pago';
    if (status === StatusTitulo.CANCELADO) return 'status-cancelado';
    if (venc) {
      const v = new Date(venc + 'T00:00'); const h = new Date(); h.setHours(0,0,0,0);
      if (v < h) return 'status-vencido';
    }
    return 'status-aberto';
  }

  formatDate(s: string): string {
    if (!s) return '-';
    const [y, m, d] = s.split('-');
    return `${d}/${m}/${y}`;
  }

  formatCurrency(v: number): string {
    return v?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '-';
  }

  onPage(event: any) {
    this.page = event.first / event.rows;
    this.pageSize = event.rows;
    this.carregar();
  }
}
