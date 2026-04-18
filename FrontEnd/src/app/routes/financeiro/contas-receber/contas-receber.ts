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
import { SelectModule } from 'primeng/select';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '../../../shared/services/confirmation.service';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';

import { FinanceiroService } from '../financeiro.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';
import {
  ContasReceberRequest,
  ContasReceberResponse,
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
    AutoCompleteModule, ToastModule, SelectModule, MatIconModule, ConfirmationDialogComponent
  ],
  providers: [MessageService, ConfirmationService]
})
export class ContasReceberComponent implements OnInit {
  private service = inject(FinanceiroService);
  private clientesService = inject(ClientesService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  loading = false;
  salvando = false;
  rows: ContasReceberResponse[] = [];
  termo = '';

  // Paginação
  page = 0;
  pageSize = 15;
  totalElements = 0;

  // Totais
  totalAberto = 0;
  totalVencido = 0;
  totalRecebido = 0;

  // Dialog
  dialogVisible = false;
  editandoId: number | null = null;

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
    };
  }

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.service.listReceber({ page: this.page, size: this.pageSize }).subscribe({
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
    this.totalRecebido = this.rows.filter(r => r.status === StatusTitulo.PAGO).reduce((s, r) => s + (r.valorRecebido || 0), 0);
  }

  get rowsFiltradas() {
    if (!this.termo) return this.rows;
    const t = this.termo.toLowerCase();
    return this.rows.filter(r =>
      r.descricao?.toLowerCase().includes(t) ||
      r.faturaNumero?.toLowerCase().includes(t)
    );
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
    };
    this._clienteNome = `Cliente #${row.clienteId}`;
    this.dialogVisible = true;
  }

  filtrarClientes(event: any) {
    this.clientesService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
      page => this.clientesFiltrados = page.content
    );
  }

  salvar() {
    if (!this.form.descricao || !this.form.valorOriginal || !this.form.clienteId) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Descrição, Cliente e Valor.' });
      return;
    }
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const dto: ContasReceberRequest = {
      descricao: this.form.descricao,
      clienteId: this.form.clienteId,
      dataEmissao: toISO(this.form.dataEmissao),
      dataVencimento: toISO(this.form.dataVencimento),
      valorOriginal: this.form.valorOriginal,
      tipoTitulo: this.form.tipoTitulo,
      observacoes: this.form.observacoes || undefined,
      status: this.form.receberAgora ? StatusTitulo.PAGO : StatusTitulo.ABERTO,
      dataRecebimento: this.form.receberAgora ? toISO(new Date()) : undefined,
      valorRecebido: this.form.receberAgora ? this.form.valorOriginal : undefined,
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
    this.confirmationService.confirm({
      title: 'Confirmar Recebimento',
      message: `Registrar recebimento de "${row.descricao}" — <strong>${this.formatCurrency(row.valorOriginal)}</strong>?`,
      confirmText: 'Sim, receber',
      cancelText: 'Cancelar',
      type: 'info'
    }).subscribe(confirmed => {
      if (confirmed) {
        const dto: ContasReceberRequest = {
          descricao: row.descricao,
          clienteId: row.clienteId,
          dataEmissao: row.dataEmissao,
          dataVencimento: row.dataVencimento,
          valorOriginal: row.valorOriginal,
          tipoTitulo: row.tipoTitulo,
          observacoes: row.observacoes,
          status: StatusTitulo.PAGO,
          dataRecebimento: new Date().toISOString().split('T')[0],
          valorRecebido: row.valorOriginal,
        };
        this.service.updateReceber(row.id, dto).subscribe({
          next: () => { this.messageService.add({ severity: 'success', summary: 'Recebido!', detail: 'Conta marcada como recebida.' }); this.carregar(); },
          error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível registrar.' })
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
    this.carregar();
  }
}
