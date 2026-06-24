import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, Subject, debounceTime, distinctUntilChanged } from 'rxjs';
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
import { SelectModule } from 'primeng/select';
import { TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';
import { SplitButtonModule } from 'primeng/splitbutton';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '../../../shared/services/confirmation.service';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';

import { FinanceiroService } from '../financeiro.service';
import { RelatoriosService } from '../../relatorios/relatorios.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { DepartamentoService } from '../../configuracoes/departamentos/departamento.service';
import {
  ContasReceberRequest,
  ContasReceberResponse,
  DashboardFinanceiroDTO,
  StatusTitulo,
  TipoTitulo
} from '../models/financeiro.models';

@Component({
  standalone: true,
  selector: 'app-contas-receber',
  templateUrl: './contas-receber.html',
  imports: [
    CommonModule, FormsModule, ButtonModule, InputTextModule, TableModule,
    TagModule, DialogModule, DatePickerModule, InputNumberModule, TextareaModule,
    AutoCompleteModule, ToastModule, SelectModule, ConfirmationDialogComponent,
    TabsModule, DrawerModule, SplitButtonModule, CheckboxModule
  ],
  providers: [MessageService, ConfirmationService]
})
export class ContasReceberComponent implements OnInit {
  private service = inject(FinanceiroService);
  private clientesService = inject(ClientesService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private departamentoService = inject(DepartamentoService);
  private relatoriosService = inject(RelatoriosService);

  loading = false;
  salvando = false;
  rows: ContasReceberResponse[] = [];
  selectedRows: ContasReceberResponse[] = [];
  termo = '';
  private readonly searchSubject = new Subject<string>();

  printMenuOptions = [
    { label: 'Relatório PDF', icon: 'pi pi-file-pdf', command: () => this.imprimir('pdf') },
    { label: 'Exportar Excel', icon: 'pi pi-file-excel', command: () => this.imprimir('excel') }
  ];

  // Paginação
  page = 0;
  pageSize = 15;
  totalElements = 0;

  // Totais
  totalAberto = 0;
  totalVencido = 0;
  totalRecebido = 0;
  
  dashboard: DashboardFinanceiroDTO | null = null;

  // Dialogs
  dialogVisible = false;
  receberDialogVisible = false;
  dialogDetalhesVisible = false;
  quitarLoteDialogVisible = false;
  editandoId: number | null = null;
  receberRowSelected!: ContasReceberResponse;
  detalhesRowSelected: ContasReceberResponse | null = null;
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

  isFirstLazyLoad = true;

  ngOnInit() {
    const hoje = new Date();
    this.vencInicio = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.vencFim = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.page = 0;
      this.carregar();
    });

    this.carregar();
    this.carregarAuxiliares();
  }

  onTermoChange() {
    this.searchSubject.next(this.termo);
  }

  onFiltroChange() {
    this.page = 0;
    this.carregar();
  }
  
  valorTotalQuitarLote = 0;
  contaQuitarLoteId: number | null = null;

  // Auxiliares
  formasPagamento: any[] = [];
  contasBancarias: any[] = [];
  departamentos: any[] = [];
  planosConta: any[] = [];

  // Autocomplete cliente
  clientesFiltrados: any[] = [];
  private _clienteNome = '';
  get clienteNome(): any { return this._clienteNome; }
  set clienteNome(val: any) {
    if (val && typeof val === 'object') {
      this._clienteNome = val.nome || val.nomeCompleto || val.pessoaNome || '';
      this.form.clienteId = val.id;
      this.form.clienteObj = val;
    } else {
      this._clienteNome = val || '';
    }
  }

  tiposDocumento = Object.values(TipoTitulo).map(v => ({ label: v, value: v }));
  form: any = this.emptyForm();
  formRecebimento: any = {};

  emptyForm() {
    return {
      descricao: '',
      clienteId: null as number | null,
      clienteObj: null as any,
      dataEmissao: new Date(),
      dataVencimento: new Date(),
      valorOriginal: 0,
      tipoTitulo: TipoTitulo.OUTROS,
      observacoes: '',
      receberAgora: false,
      formaPagamentoId: null as number | null,
      contaBancariaId: null as number | null,
      centroCustoId: null as number | null,
      planoContasId: null as number | null,
      numeroTitulo: '',
    };
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
    this.service.getDashboardReceber().subscribe(d => this.dashboard = d);

    const query: any = {
      page: this.page,
      size: this.pageSize,
      sort: 'dataVencimento,asc'
    };

    if (this.termo?.trim()) {
      query.termo = this.termo.trim();
    }

    const formatDateISO = (d: Date | null) => {
      if (!d) return undefined;
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    };

    if (this.vencInicio) {
      query.dataInicio = formatDateISO(this.vencInicio);
    }
    if (this.vencFim) {
      query.dataFim = formatDateISO(this.vencFim);
    }

    if (this.filtroTipo && this.filtroTipo !== 'Todas') {
      query.status = this.filtroTipo;
    }

    if (this.buscarPor && this.filtroValor && this.filtroValor !== 'TODOS') {
      const filterId = Number(this.filtroValor);
      switch (this.buscarPor) {
        case 'conta':
          query.contaBancariaId = filterId;
          break;
        case 'centroCusto':
          query.centroCustoId = filterId;
          break;
        case 'planoContas':
          query.planoContasId = filterId;
          break;
        case 'formaPagamento':
          query.formaPagamentoId = filterId;
          break;
      }
    }

    this.service.listReceber(query).subscribe({
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
    this.totalRecebido = this.rows.filter(r => r.status === StatusTitulo.PAGO).reduce((s, r) => s + (r.valorRecebido || 0), 0);
  }

  get rowsFiltradas() {
    return this.rows;
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
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione a conta bancária para o recebimento.' });
      return;
    }
    
    this.loading = true;
    const requests = this.selectedRows.map(row => {
      const payload = {
        descricao: row.descricao,
        clienteId: row.clienteId,
        dataEmissao: row.dataEmissao,
        dataVencimento: row.dataVencimento,
        valorOriginal: row.valorOriginal,
        tipoTitulo: row.tipoTitulo,
        centroCustoId: row.centroCustoId,
        planoContasId: row.planoContasId,
        dataRecebimento: new Date().toISOString().split('T')[0],
        valorRecebido: row.valorOriginal,
        formaPagamentoId: row.formaPagamentoId || 1,
        contaBancariaId: this.contaQuitarLoteId,
        status: 'PAGO',
        observacoes: row.observacoes || 'Quitação em lote'
      };
      return this.service.receberTitulo(row.id, payload);
    });

    forkJoin(requests).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: `${this.selectedRows.length} contas baixadas!` });
        this.selectedRows = [];
        this.quitarLoteDialogVisible = false;
        this.carregar();
      },
      error: (err: any) => {
        console.error(err);
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao baixar uma ou mais contas.' });
      }
    });
  }

  abrirNovo() {
    this.editandoId = null;
    this.form = this.emptyForm();
    this.clienteNome = '';
    this.dialogVisible = true;
  }

  abrirEditar(row: ContasReceberResponse) {
    this.editandoId = row.id;
    this.form = {
      descricao: row.descricao,
      clienteId: row.clienteId,
      clienteObj: null,
      dataEmissao: row.dataEmissao ? new Date(row.dataEmissao + 'T00:00') : new Date(),
      dataVencimento: row.dataVencimento ? new Date(row.dataVencimento + 'T00:00') : new Date(),
      valorOriginal: row.valorOriginal,
      tipoTitulo: row.tipoTitulo,
      observacoes: row.observacoes || '',
      receberAgora: false,
      formaPagamentoId: row.formaPagamentoId,
      contaBancariaId: row.contaBancariaId,
      centroCustoId: row.centroCustoId,
      planoContasId: row.planoContasId,
      numeroTitulo: row.numeroTitulo || '',
    };
    this._clienteNome = `Cliente #${row.clienteId}`;
    this.dialogVisible = true;
  }

  abrirDetalhes(row: ContasReceberResponse) {
    this.service.getReceberById(row.id).subscribe({
      next: (res) => {
        this.detalhesRowSelected = res;
        this.dialogDetalhesVisible = true;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar detalhes.' })
    });
  }

  getAnexoIcon(tipo: string): string {
    if (tipo?.includes('pdf')) return 'pi-file-pdf';
    if (tipo?.includes('image')) return 'pi-image';
    return 'pi-file';
  }

  formatBytes(bytes: number): string {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  onFileSelected(event: any, contaId: number) {
    const file = event.target.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Arquivo excede limite de 5MB' });
        return;
      }
      this.service.uploadAnexo(contaId, file).subscribe({
        next: (res) => {
          this.messageService.add({ severity: 'success', summary: 'Anexo adicionado' });
          if (this.detalhesRowSelected) {
            this.detalhesRowSelected.anexos = [...(this.detalhesRowSelected.anexos || []), res];
          }
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao fazer upload do anexo' })
      });
    }
  }

  baixarAnexo(contaId: number, anexo: any) {
    this.service.downloadAnexo(contaId, anexo.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = anexo.nomeArquivo;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao baixar anexo' })
    });
  }

  filtrarClientes(event: any) {
    this.clientesService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
      page => this.clientesFiltrados = page.content
    );
  }

  salvar() {
    if (!this.form.descricao || !this.form.valorOriginal) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Descrição e Valor.' });
      return;
    }
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasReceberRequest = {
      descricao: this.form.descricao,
      clienteId: this.form.clienteId || null,
      dataEmissao: toISO(this.form.dataEmissao),
      dataVencimento: toISO(this.form.dataVencimento),
      valorOriginal: this.form.valorOriginal,
      tipoTitulo: this.form.tipoTitulo,
      observacoes: this.form.observacoes || undefined,
      status: this.form.receberAgora ? StatusTitulo.PAGO : StatusTitulo.ABERTO,
      dataRecebimento: this.form.receberAgora ? toISO(new Date()) : undefined,
      valorRecebido: this.form.receberAgora ? this.form.valorOriginal : undefined,
      formaPagamentoId: this.form.formaPagamentoId || undefined,
      contaBancariaId: this.form.contaBancariaId || undefined,
      centroCustoId: this.form.centroCustoId || undefined,
      planoContasId: this.form.planoContasId || undefined,
      numeroTitulo: this.form.numeroTitulo || undefined,
    };

    const op$ = this.editandoId
      ? this.service.updateReceber(this.editandoId, dto)
      : this.service.createReceber(dto);

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

  receber(row: ContasReceberResponse) {
    this.receberRowSelected = row;

    // Calcular juros e multa sugeridos
    const hoje = new Date(); hoje.setHours(0,0,0,0);
    const vencimento = new Date(row.dataVencimento + 'T00:00');
    let juros = 0;
    let multa = 0;

    if (vencimento < hoje) {
      // Multa padrão de 2%
      multa = row.valorOriginal * 0.02;
      // Juros padrão de 0.033% ao dia (aproximadamente 1% ao mês)
      const diffTime = Math.abs(hoje.getTime() - vencimento.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      juros = row.valorOriginal * 0.00033 * diffDays;
    }

    this.formRecebimento = {
      id: row.id,
      descricao: row.descricao,
      valorOriginal: row.valorOriginal,
      valorJuros: Number(juros.toFixed(2)),
      valorMulta: Number(multa.toFixed(2)),
      valorDesconto: 0,
      valorTotal: Number((row.valorOriginal + juros + multa).toFixed(2)),
      dataRecebimento: new Date(),
      formaPagamentoId: row.formaPagamentoId || (this.formasPagamento.length > 0 ? this.formasPagamento[0].value : null),
      contaBancariaId: row.contaBancariaId || (this.contasBancarias.length > 0 ? this.contasBancarias[0].value : null),
    };

    this.receberDialogVisible = true;
  }

  recalcularTotalRecebimento() {
    const original = this.formRecebimento.valorOriginal || 0;
    const juros = this.formRecebimento.valorJuros || 0;
    const multa = this.formRecebimento.valorMulta || 0;
    const desconto = this.formRecebimento.valorDesconto || 0;
    this.formRecebimento.valorTotal = Number((original + juros + multa - desconto).toFixed(2));
  }

  salvarRecebimento() {
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasReceberRequest = {
      descricao: this.receberRowSelected.descricao,
      clienteId: this.receberRowSelected.clienteId,
      dataEmissao: this.receberRowSelected.dataEmissao,
      dataVencimento: this.receberRowSelected.dataVencimento,
      valorOriginal: this.receberRowSelected.valorOriginal,
      tipoTitulo: this.receberRowSelected.tipoTitulo,
      observacoes: this.receberRowSelected.observacoes,
      status: StatusTitulo.PAGO,
      dataRecebimento: toISO(this.formRecebimento.dataRecebimento),
      valorRecebido: this.formRecebimento.valorTotal,
      valorJuros: this.formRecebimento.valorJuros,
      valorMulta: this.formRecebimento.valorMulta,
      valorDesconto: this.formRecebimento.valorDesconto,
      formaPagamentoId: this.formRecebimento.formaPagamentoId,
      contaBancariaId: this.formRecebimento.contaBancariaId,
      centroCustoId: this.receberRowSelected.centroCustoId,
      planoContasId: this.receberRowSelected.planoContasId,
    };

    this.service.updateReceber(this.formRecebimento.id, dto).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Recebido!', detail: 'Conta marcada como recebida.' });
        this.receberDialogVisible = false;
        this.salvando = false;
        this.carregar();
      },
      error: () => {
        this.salvando = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível registrar o recebimento.' });
      }
    });
  }

  desfazerQuitacao(row: ContasReceberResponse) {
    this.confirmationService.confirm({
      title: 'Confirmar Estorno',
      message: `Deseja realmente desfazer a quitação de "<strong>${row.descricao}</strong>"? O título voltará para o status original de pendente.`,
      confirmText: 'Confirmar',
      cancelText: 'Cancelar',
      type: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.desfazerQuitacao(row.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Quitação desfeita!' });
            this.carregar();
          },
          error: (err: any) => {
            console.error(err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível desfazer a quitação.' });
          }
        });
      }
    });
  }

  excluir(row: ContasReceberResponse) {
    this.confirmationService.confirm({
      title: 'Confirmar Exclusão',
      message: `Deseja realmente excluir "<strong>${row.descricao}</strong>"? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.deleteReceber(row.id).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Excluído!' }); this.carregar(); },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir.' })
        });
      }
    });
  }

  getStatusLabel(status: StatusTitulo, venc?: string): string {
    if (status === StatusTitulo.PAGO) return 'Recebido';
    if (status === StatusTitulo.CANCELADO) return 'Cancelado';
    if (venc) {
      const v = new Date(venc + 'T00:00'); const h = new Date(); h.setHours(0,0,0,0);
      if (v < h) return 'Vencido';
    }
    return 'A receber';
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
    if (this.isFirstLazyLoad) {
      this.isFirstLazyLoad = false;
      return;
    }
    this.carregar();
  }
}
