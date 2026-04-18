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
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '../../../shared/services/confirmation.service';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { forkJoin } from 'rxjs';

import { FinanceiroService } from '../financeiro.service';
import { FornecedorService } from '../../fornecedor/fornecedor.service';
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
    AutoCompleteModule, ToastModule, SelectModule, MatIconModule, ConfirmationDialogComponent
  ],
  providers: [MessageService, ConfirmationService]
})
export class ContasPagarComponent implements OnInit {
  private service = inject(FinanceiroService);
  private fornecedorService = inject(FornecedorService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  loading = false;
  salvando = false;
  rows: ContasPagarResponse[] = [];
  termo = '';

  // Paginação
  page = 0;
  pageSize = 15;
  totalElements = 0;

  // Totais
  totalAberto = 0;
  totalVencido = 0;
  totalPago = 0;

  // Dialog
  dialogVisible = false;
  editandoId: number | null = null;

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
      numeroDocumento: '',
      dataEmissao: new Date(),
      dataVencimento: new Date(),
      valorOriginal: 0,
      tipoTitulo: TipoTitulo.OUTROS,
      observacoes: '',
      quitarAgora: false,
    };
  }

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.service.listPagar({ page: this.page, size: this.pageSize }).subscribe({
      next: (res) => {
        this.rows = res.content;
        this.totalElements = res.totalElements;
        this.calcularTotais();
        this.loading = false;
      },
      error: () => { this.loading = false; this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar contas.' }); }
    });
  }

  calcularTotais() {
    const hoje = new Date(); hoje.setHours(0,0,0,0);
    this.totalAberto = this.rows.filter(r => r.status === StatusTitulo.ABERTO).reduce((s, r) => s + r.valorOriginal, 0);
    this.totalVencido = this.rows.filter(r => r.status === StatusTitulo.VENCIDO || (r.status === StatusTitulo.ABERTO && new Date(r.dataVencimento) < hoje)).reduce((s, r) => s + r.valorOriginal, 0);
    this.totalPago = this.rows.filter(r => r.status === StatusTitulo.PAGO).reduce((s, r) => s + (r.valorPago || 0), 0);
  }

  get rowsFiltradas() {
    if (!this.termo) return this.rows;
    const t = this.termo.toLowerCase();
    return this.rows.filter(r =>
      r.descricao?.toLowerCase().includes(t) ||
      r.numeroDocumento?.toLowerCase().includes(t)
    );
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
    };
    this._fornecedorNome = `Fornecedor #${row.fornecedorId}`;
    this.dialogVisible = true;
  }

  filtrarFornecedores(event: any) {
    this.fornecedorService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
      page => this.fornecedoresFiltrados = page.content
    );
  }

  salvar() {
    if (!this.form.descricao || !this.form.valorOriginal || !this.form.fornecedorId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Descrição, Fornecedor e Valor.' });
      return;
    }
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasPagarRequest = {
      descricao: this.form.descricao,
      fornecedorId: this.form.fornecedorId,
      numeroDocumento: this.form.numeroDocumento || undefined,
      dataEmissao: toISO(this.form.dataEmissao),
      dataVencimento: toISO(this.form.dataVencimento),
      valorOriginal: this.form.valorOriginal,
      tipoTitulo: this.form.tipoTitulo,
      observacoes: this.form.observacoes || undefined,
      status: this.form.quitarAgora ? StatusTitulo.PAGO : StatusTitulo.ABERTO,
      dataPagamento: this.form.quitarAgora ? toISO(new Date()) : undefined,
      valorPago: this.form.quitarAgora ? this.form.valorOriginal : undefined,
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
    this.confirmationService.confirm({
      title: 'Confirmar Pagamento',
      message: `Quitar "${row.descricao}" por <strong>${this.formatCurrency(row.valorOriginal)}</strong>?`,
      confirmText: 'Sim, quitar',
      cancelText: 'Cancelar',
      type: 'info'
    }).subscribe(confirmed => {
      if (confirmed) {
        const dto: ContasPagarRequest = {
          descricao: row.descricao,
          fornecedorId: row.fornecedorId,
          numeroDocumento: row.numeroDocumento,
          dataEmissao: row.dataEmissao,
          dataVencimento: row.dataVencimento,
          valorOriginal: row.valorOriginal,
          tipoTitulo: row.tipoTitulo,
          observacoes: row.observacoes,
          status: StatusTitulo.PAGO,
          dataPagamento: new Date().toISOString().split('T')[0],
          valorPago: row.valorOriginal,
        };
        this.service.updatePagar(row.id, dto).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Quitado!', detail: 'Conta marcada como paga.' }); this.carregar(); },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível quitar.' })
        });
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
