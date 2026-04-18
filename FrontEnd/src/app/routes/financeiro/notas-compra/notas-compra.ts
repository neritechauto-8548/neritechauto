import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { DatePickerModule } from 'primeng/datepicker';
import { InputNumberModule } from 'primeng/inputnumber';
import { TextareaModule } from 'primeng/textarea';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from '../../../shared/services/confirmation.service';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';

import { FornecedorService } from '../../fornecedor/fornecedor.service';

export interface NotaCompra {
  id?: number;
  numeroNota: string;
  serie?: string;
  fornecedorId: number;
  fornecedorNome: string;
  fornecedorCnpj?: string;
  dataEmissao: string;
  dataEntrada: string;
  valorTotal: number;
  chaveNfe?: string;
  observacoes?: string;
  status?: string;
}

@Component({
  standalone: true,
  selector: 'app-notas-compra',
  templateUrl: './notas-compra.html',
  imports: [
    CommonModule, FormsModule, ButtonModule, InputTextModule, SelectModule,
    DialogModule, DatePickerModule, InputNumberModule, TextareaModule,
    AutoCompleteModule, ToastModule, MatIconModule, ConfirmationDialogComponent
  ],
  providers: [MessageService, ConfirmationService]
})
export class NotasCompraComponent implements OnInit {
  private fornecedorService = inject(FornecedorService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);

  loading = false;
  salvando = false;

  // Lista de notas
  notas: NotaCompra[] = [];
  filtroTexto = '';
  filtroCampo = 'NUMERO DA NOTA FISCAL';

  campoOptions: { label: string; value: string }[] = [
    { label: 'Número da Nota Fiscal', value: 'NUMERO DA NOTA FISCAL' },
    { label: 'CNPJ Fornecedor', value: 'CNPJ FORNECEDOR' },
    { label: 'Data de Emissão', value: 'DATA EMISSAO' },
    { label: 'Data de Entrada', value: 'DATA ENTRADA' },
  ];

  // Dialog Nova Nota
  dialogVisible = false;
  form: any = this.emptyForm();

  // Dialog Detalhe
  detalheVisible = false;
  notaSelecionada: NotaCompra | null = null;

  // Autocomplete fornecedor
  fornecedoresFiltrados: any[] = [];
  private _fornecedorNome = '';
  get fornecedorNome(): any { return this._fornecedorNome; }
  set fornecedorNome(val: any) {
    if (val && typeof val === 'object') {
      this._fornecedorNome = val.nomeFantasia || val.razaoSocial || '';
      this.form.fornecedorId = val.id;
      this.form.fornecedorCnpj = val.cnpj;
      this.form.fornecedorNome = this._fornecedorNome;
    } else {
      this._fornecedorNome = val || '';
    }
  }

  // Totais computados
  get totalNotas(): number { return this.notas.length; }
  get totalItens(): number { return this.notas.reduce((s, n) => s + (n.status !== 'CANCELADO' ? 1 : 0), 0); }
  get totalComprado(): number { return this.notas.filter(n => n.status !== 'CANCELADO').reduce((s, n) => s + n.valorTotal, 0); }
  get totalFornecedores(): number { return new Set(this.notas.map(n => n.fornecedorId)).size; }

  get notasFiltradas(): NotaCompra[] {
    if (!this.filtroTexto.trim()) return this.notas;
    const t = this.filtroTexto.toLowerCase();
    return this.notas.filter(n => {
      switch (this.filtroCampo) {
        case 'CNPJ FORNECEDOR': return n.fornecedorCnpj?.toLowerCase().includes(t);
        case 'DATA EMISSAO': return n.dataEmissao?.toLowerCase().includes(t);
        case 'DATA ENTRADA': return n.dataEntrada?.toLowerCase().includes(t);
        default: // NUMERO DA NOTA FISCAL ou nome
          return n.numeroNota?.toLowerCase().includes(t) || n.fornecedorNome?.toLowerCase().includes(t);
      }
    });
  }

  ngOnInit() {
    // Futuramente: carregar notas do backend
    // this.carregar();
  }

  emptyForm() {
    return {
      numeroNota: '',
      serie: '',
      fornecedorId: null as number | null,
      fornecedorNome: '',
      fornecedorCnpj: '',
      dataEmissao: new Date(),
      dataEntrada: new Date(),
      valorTotal: 0,
      chaveNfe: '',
      observacoes: '',
    };
  }

  abrirNovo() {
    this.form = this.emptyForm();
    this._fornecedorNome = '';
    this.dialogVisible = true;
  }

  filtrarFornecedores(event: any) {
    this.fornecedorService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
      page => this.fornecedoresFiltrados = page.content
    );
  }

  salvar() {
    if (!this.form.numeroNota || !this.form.fornecedorId || !this.form.valorTotal) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Preencha Nº da NF, Fornecedor e Valor.' });
      return;
    }
    this.salvando = true;
    const toISO = (d: Date) => d instanceof Date ? d.toISOString().split('T')[0] : d;

    const nova: NotaCompra = {
      id: Date.now(),
      numeroNota: this.form.numeroNota,
      serie: this.form.serie || undefined,
      fornecedorId: this.form.fornecedorId,
      fornecedorNome: this.form.fornecedorNome,
      fornecedorCnpj: this.form.fornecedorCnpj || undefined,
      dataEmissao: toISO(this.form.dataEmissao),
      dataEntrada: toISO(this.form.dataEntrada),
      valorTotal: this.form.valorTotal,
      chaveNfe: this.form.chaveNfe || undefined,
      observacoes: this.form.observacoes || undefined,
      status: 'ATIVO',
    };

    // Simula salvamento (substituir por chamada de API quando backend estiver pronto)
    setTimeout(() => {
      this.notas.unshift(nova);
      this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Nota incluída com sucesso!' });
      this.dialogVisible = false;
      this.salvando = false;
    }, 400);
  }

  verDetalhe(nota: NotaCompra) {
    this.notaSelecionada = nota;
    this.detalheVisible = true;
  }

  imprimir() {
    this.messageService.add({ severity: 'info', summary: 'Imprimir', detail: 'Funcionalidade de impressão em desenvolvimento.' });
  }

  imprimirNota(nota: NotaCompra) {
    this.messageService.add({ severity: 'info', summary: 'Imprimir', detail: `Imprimindo NF ${nota.numeroNota}...` });
  }

  excluir(nota: NotaCompra) {
    this.confirmationService.confirm({
      title: 'Confirmar Exclusão',
      message: `Deseja realmente excluir a nota <strong>${nota.numeroNota}</strong>? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.notas = this.notas.filter(n => n.id !== nota.id);
        this.messageService.add({ severity: 'success', summary: 'Excluída!', detail: `Nota ${nota.numeroNota} removida.` });
      }
    });
  }

  formatDate(s: string): string {
    if (!s) return '-';
    const [y, m, d] = s.split('-');
    return `${d}/${m}/${y}`;
  }

  formatCurrency(v: number): string {
    return v?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) ?? '-';
  }
}
