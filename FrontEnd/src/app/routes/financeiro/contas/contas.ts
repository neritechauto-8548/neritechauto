import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { DatePickerModule } from 'primeng/datepicker';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { HotToastService } from '@ngxpert/hot-toast';
import { forkJoin } from 'rxjs';

import { FinanceiroService } from '../financeiro.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import { FornecedorService } from '../../fornecedor/fornecedor.service';
import { DepartamentoService } from '../../configuracoes/departamentos/departamento.service';
import { StatusTitulo, TipoTitulo, ContasPagarResponse, ContasReceberResponse } from '../models/financeiro.models';

interface UnifiedRow {
  id: number;
  tipo: 'Receita' | 'Despesa';
  numero: string;
  descricao: string;
  ref: string;
  venc: string;
  pagamento?: string;
  valor: number;
  valorPg?: number;
  pago: boolean;
  status: StatusTitulo;
  contaBancariaId?: number;
  centroCustoId?: number;
  planoContasId?: number;
  formaPagamentoId?: number;
  __original: ContasPagarResponse | ContasReceberResponse;
}

@Component({
  standalone: true,
  selector: 'app-contas',
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    SplitButtonModule,
    SelectModule,
    InputTextModule,
    CheckboxModule,
    DialogModule,
    TableModule,
    TagModule,
    TooltipModule,
    DatePickerModule,
    InputNumberModule,
    SelectButtonModule,
    TextareaModule,
    AutoCompleteModule
  ],
  templateUrl: './contas.html',
  styleUrls: ['./contas.scss'],
})
export class ContasComponent implements OnInit {
  private service = inject(FinanceiroService);
  private clientesService = inject(ClientesService);
  private fornecedorService = inject(FornecedorService);
  private departamentoService = inject(DepartamentoService);
  private toast = inject(HotToastService);

  loading = false;
  rows: UnifiedRow[] = [];
  selectedRows: UnifiedRow[] = [];

  // Filtros
  pageTab: 'contas' | 'config' = 'contas';
  filtroTipo = 'Todas';
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
  vencInicio: Date | null = null;
  vencFim: Date | null = null;
  buscarPor = 'conta';
  filtroValor: any = 'TODOS';

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

  // Dialogs
  incluirDialogVisible = false;
  quitarConfirmVisible = false;

  // Totais
  totalReceber = 0;
  totalPagar = 0;
  totalRecebido = 0;
  totalPago = 0;

  // Listas Auxiliares
  departamentos: any[] = [];
  planosConta: any[] = [];
  contasBancarias: any[] = [];
  formasPagamento: any[] = [];

  // Autocomplete
  suggestionsClientes: any[] = [];
  suggestionsFornecedores: any[] = [];

  // Form
  novaConta: any = this.getEmptyConta();
  entidadeSelecionada: any = null; // Objeto selecionado no Autocomplete

  ngOnInit() {
      const hoje = new Date();
      this.vencInicio = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
      this.vencFim = new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0);
      this.carregarDados();
      this.carregarAuxiliares();
  }

  carregarAuxiliares() {
      forkJoin({
          cc: this.departamentoService.list({ size: 1000 }),
          pc: this.service.listPlanosConta(),
          cb: this.service.listContasBancarias(),
          fp: this.service.listFormasPagamento()
      }).subscribe({
          next: (res: any) => {
              const ccs = res.cc?.content || res.cc || [];
              this.departamentos = ccs.map((i: any) => ({ label: i.descricao, value: i.id }));

              const pcs = res.pc?.content || res.pc || [];
              this.planosConta = pcs.map((i: any) => ({ label: i.nome || i.descricao, value: i.id }));

              const cbs = res.cb?.content || res.cb || [];
              this.contasBancarias = cbs.map((i: any) => ({ 
                  label: i.nome || `${i.bancoNome} • ${i.agencia}/${i.conta}`, 
                  value: i.id 
              }));

              const fps = res.fp?.content || res.fp || [];
              this.formasPagamento = fps.map((i: any) => ({ label: i.nome, value: i.id }));
          },
          error: (err) => console.error('Erro ao carregar auxiliares', err)
      });
  }

  searchEntidade(event: any) {
    let query = event.query || '';
    if (typeof query === 'object') query = ''; // Segurança extra
    if (this.novaConta.tipo === 'Receita') {
        this.clientesService.list({ nome: query, page: 0, size: 10 }).subscribe({
            next: (res) => {
                console.log('Resposta completa clientes:', res);
                this.suggestionsClientes = res?.content || [];
                if (this.suggestionsClientes.length > 0) {
                    console.log('Primeiro cliente - campos disponíveis:', Object.keys(this.suggestionsClientes[0]));
                    console.log('Primeiro cliente - dados completos:', this.suggestionsClientes[0]);
                } else {
                    console.warn('Nenhum cliente encontrado para:', query);
                }
            },
            error: (err) => {
                console.error('Erro ao buscar clientes:', err);
                this.suggestionsClientes = [];
                this.toast.error('Erro ao buscar clientes');
            }
        });
    } else {
        this.fornecedorService.list({ nome: query, page: 0, size: 10 }).subscribe({
            next: (res) => {
                console.log('Resposta completa fornecedores:', res);
                this.suggestionsFornecedores = res?.content || [];
                if (this.suggestionsFornecedores.length > 0) {
                    console.log('Primeiro fornecedor - campos disponíveis:', Object.keys(this.suggestionsFornecedores[0]));
                    console.log('Primeiro fornecedor - dados completos:', this.suggestionsFornecedores[0]);
                } else {
                    console.warn('Nenhum fornecedor encontrado para:', query);
                }
            },
            error: (err) => {
                console.error('Erro ao buscar fornecedores:', err);
                this.suggestionsFornecedores = [];
                this.toast.error('Erro ao buscar fornecedores');
            }
        });
    }
  }

  getEmptyConta() {
      return {
          tipo: 'Despesa',
          descricao: '',
          valor: 0,
          vencimento: new Date(),
          dataEmissao: new Date(),
          centroCustoId: null,
          planoContaId: null,
          contaBancariaId: null,
          formaPagamentoId: null,
          observacoes: ''
      };
  }

  incluirConta() {
      this.novaConta = this.getEmptyConta();
      this.entidadeSelecionada = null;
      this.incluirDialogVisible = true;
  }

  salvarConta() {
      if (!this.novaConta.descricao || !this.novaConta.valor) {
          this.toast.warning('Preencha os campos obrigatórios.');
          return;
      }

      const commonPayload = {
          descricao: this.novaConta.descricao,
          valorOriginal: this.novaConta.valor,
          dataVencimento: this.novaConta.vencimento.toISOString().split('T')[0],
          dataEmissao: this.novaConta.dataEmissao.toISOString().split('T')[0],
          centroCustoId: this.novaConta.centroCustoId,
          planoContasId: this.novaConta.planoContaId,
          contaBancariaId: this.novaConta.contaBancariaId,
          formaPagamentoId: this.novaConta.formaPagamentoId,
          observacoes: this.novaConta.observacoes,
          status: StatusTitulo.ABERTO,
          tipoTitulo: TipoTitulo.OUTROS,
          numeroTitulo: `REC-${Date.now()}`
      };

      let req$: import('rxjs').Observable<any>;
      const entId = this.entidadeSelecionada ? this.entidadeSelecionada.id : null;
      if (this.novaConta.tipo === 'Despesa') {
          const payload = { ...commonPayload, fornecedorId: entId };
          req$ = this.service.createPagar(payload as any);
      } else {
          const payload = { ...commonPayload, clienteId: entId };
          req$ = this.service.createReceber(payload as any);
      }

      req$.subscribe({
          next: () => {
              this.toast.success('Lançamento salvo!');
              this.incluirDialogVisible = false;
              this.carregarDados();
          },
          error: (err: any) => {
              console.error(err);
              this.toast.error('Erro ao salvar lançamento.');
          }
      });
  }

  carregarDados() {
      this.loading = true;
      const empresaId = this.service['getTenantId']() || 1;
      forkJoin({
          pagar: this.service.listPagar({ empresaId, page: 0, size: 100 }),
          receber: this.service.listReceber({ empresaId, page: 0, size: 100 })
      }).subscribe({
          next: ({ pagar, receber }) => {
              const listaPagar: UnifiedRow[] = pagar.content.map((p: ContasPagarResponse) => ({
                  id: p.id,
                  tipo: 'Despesa',
                  numero: p.numeroDocumento || '-',
                  descricao: p.descricao,
                  ref: `Fornecedor #${p.fornecedorId}`,
                  venc: p.dataVencimento,
                  pagamento: p.dataPagamento,
                  valor: p.valorOriginal,
                  valorPg: p.valorPago,
                  pago: p.status === StatusTitulo.PAGO,
                  status: p.status,
                  contaBancariaId: p.contaBancariaId,
                  centroCustoId: p.centroCustoId,
                  planoContasId: p.planoContasId,
                  formaPagamentoId: p.formaPagamentoId,
                  __original: p
              }));

              const listaReceber: UnifiedRow[] = receber.content.map((r: ContasReceberResponse) => ({
                  id: r.id,
                  tipo: 'Receita',
                  numero: r.faturaNumero || '-',
                  descricao: r.descricao,
                  ref: r.faturaId ? `Fatura #${r.faturaId}` : `Cliente #${r.clienteId}`,
                  venc: r.dataVencimento,
                  pagamento: r.dataRecebimento,
                  valor: r.valorOriginal,
                  valorPg: r.valorRecebido,
                  pago: r.status === StatusTitulo.PAGO,
                  status: r.status,
                  contaBancariaId: r.contaBancariaId,
                  centroCustoId: r.centroCustoId,
                  planoContasId: r.planoContasId,
                  formaPagamentoId: r.formaPagamentoId,
                  __original: r
              }));

              this.rows = [...listaReceber, ...listaPagar].sort((a,b) => new Date(a.venc).getTime() - new Date(b.venc).getTime());
              this.calcularTotais();
              this.loading = false;
          },
          error: (err: any) => {
              console.error(err);
              this.toast.error('Erro ao carregar financeiro');
              this.loading = false;
          }
      });
  }

  calcularTotais() {
      this.totalReceber = this.rows.filter(r => r.tipo === 'Receita' && !r.pago).reduce((acc, r) => acc + r.valor, 0);
      this.totalPagar = this.rows.filter(r => r.tipo === 'Despesa' && !r.pago).reduce((acc, r) => acc + r.valor, 0);
      this.totalRecebido = this.rows.filter(r => r.tipo === 'Receita' && r.pago).reduce((acc, r) => acc + (r.valorPg || 0), 0);
      this.totalPago = this.rows.filter(r => r.tipo === 'Despesa' && r.pago).reduce((acc, r) => acc + (r.valorPg || 0), 0);
  }

  get filteredRows() {
      let result = this.rows;

      // 1. Filtro de Tipo
      if (this.filtroTipo !== 'Todas') {
          switch (this.filtroTipo) {
              case 'Todas Quitado':
                  result = result.filter(r => r.pago);
                  break;
              case 'Todas Aberto':
                  result = result.filter(r => !r.pago);
                  break;
              case 'Todas Receita':
                  result = result.filter(r => r.tipo === 'Receita');
                  break;
              case 'Todas Despesa':
                  result = result.filter(r => r.tipo === 'Despesa');
                  break;
              case 'Despesa Quitado':
                  result = result.filter(r => r.tipo === 'Despesa' && r.pago);
                  break;
              case 'Despesa Aberto':
                  result = result.filter(r => r.tipo === 'Despesa' && !r.pago);
                  break;
              case 'Receita Quitado':
                  result = result.filter(r => r.tipo === 'Receita' && r.pago);
                  break;
              case 'Receita Aberto':
                  result = result.filter(r => r.tipo === 'Receita' && !r.pago);
                  break;
              case 'Transferencias':
                  result = result.filter(r => 
                      r.descricao?.toLowerCase().includes('transferência') ||
                      r.ref?.toLowerCase().includes('transferência')
                  );
                  break;
          }
      }

      // 2. Filtros Avançados (Vencimento & Buscar Por / Filtro)
      // Filtro por data
      if (this.vencInicio) {
          const start = new Date(this.vencInicio);
          start.setHours(0, 0, 0, 0);
          result = result.filter(r => {
              if (!r.venc) return false;
              const d = new Date(r.venc + 'T00:00');
              return d >= start;
          });
      }
      if (this.vencFim) {
          const end = new Date(this.vencFim);
          end.setHours(23, 59, 59, 999);
          result = result.filter(r => {
              if (!r.venc) return false;
              const d = new Date(r.venc + 'T00:00');
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

  quitarContas() {
      if (!this.selectedRows.length) {
          this.toast.warning('Selecione ao menos um item.');
          return;
      }
      this.quitarConfirmVisible = true;
  }

  confirmarQuitar() {
      const requests = this.selectedRows.map(row => {
          if (row.tipo === 'Despesa') {
              const original = row.__original as ContasPagarResponse;
              const payload = { ...original, status: StatusTitulo.PAGO, dataPagamento: new Date().toISOString().split('T')[0], valorPago: original.valorOriginal };
              return this.service.updatePagar(row.id, payload as any);
          } else {
              const original = row.__original as ContasReceberResponse;
               const payload = { ...original, status: StatusTitulo.PAGO, dataRecebimento: new Date().toISOString().split('T')[0], valorRecebido: original.valorOriginal };
              return this.service.updateReceber(row.id, payload as any);
          }
      });

      forkJoin(requests).subscribe({
          next: () => {
              this.toast.success(`${this.selectedRows.length} contas quitadas!`);
              this.quitarConfirmVisible = false;
              this.selectedRows = [];
              this.carregarDados();
          },
          error: () => this.toast.error('Erro ao quitar contas.')
      });
  }

  getStatusSeverity(status: StatusTitulo): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' | undefined {
      switch(status) {
          case StatusTitulo.PAGO: return 'success';
          case StatusTitulo.ABERTO: return 'warn';
          case StatusTitulo.VENCIDO: return 'danger';
          default: return 'info';
      }
  }

  isVencido(row: UnifiedRow): boolean {
      if (row.pago) return false;
      const venc = new Date(row.venc);
      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0);
      return venc < hoje;
  }

  formatDate(dateStr: string): string {
      if (!dateStr) return '-';
      try {
          const date = new Date(dateStr);
          return date.toLocaleDateString('pt-BR');
      } catch {
          return dateStr;
      }
  }

  getDisplayName(item: any): string {
    if (!item) return '';
    return item.nome || item.nomeFantasia || item.razaoSocial || item.nomeCompleto || item.pessoaNome || `ID: ${item.id}`;
  }

  excluirConta(row: UnifiedRow) {
      if (!confirm(`Deseja realmente excluir a conta "${row.descricao}"?`)) return;

      const req$ = row.tipo === 'Despesa'
          ? this.service.deletePagar(row.id)
          : this.service.deleteReceber(row.id);

      req$.subscribe({
          next: () => {
              this.toast.success('Conta excluída com sucesso!');
              this.carregarDados();
          },
          error: (err: any) => {
              console.error(err);
              this.toast.error('Erro ao excluir conta.');
          }
      });
  }

  print(mode: string) {
      this.toast.info('Impressão não implementada');
  }
}
