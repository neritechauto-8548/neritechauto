import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService as PrimeConfirmationService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { MatIconModule } from '@angular/material/icon';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';
import { ConfirmationService } from '@shared/services/confirmation.service';

import { ContasItemDialog } from './contas-item-dialog';
import { ContaBancariaService, ContaBancariaRequest, ContaBancariaResponse } from './conta-bancaria.service';
import { Page } from '../categoria/categoria-produto.service';

@Component({
  selector: 'app-contas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    DynamicDialogModule,
    ToastModule,
    InputTextModule,
    MatIconModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, ConfirmationService, { provide: PrimeConfirmationService, useClass: PrimeConfirmationService }],
  templateUrl: './contas.html',
  styleUrls: ['./contas.scss'],
})
export class Contas {
  private dialog = inject(DialogService);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  dialogRef: DynamicDialogRef<ContasItemDialog> | null = null;
  private service = inject(ContaBancariaService);

  contas: ContaBancariaResponse[] = [];

  // --- PAGINATION / SEARCH LOGIC ---
  searchTerm = '';
  catFirst = 0;
  catRows = 10;

  get filteredContas() {
    if (!this.searchTerm) return this.contas;
    const term = this.searchTerm.toLowerCase();
    return this.contas.filter(c =>
      (c.bancoNome && c.bancoNome.toLowerCase().includes(term)) ||
      (c.conta && c.conta.toLowerCase().includes(term)) ||
      (c.titularConta && c.titularConta.toLowerCase().includes(term))
    );
  }

  get pagedContas() {
    return this.filteredContas.slice(this.catFirst, this.catFirst + this.catRows);
  }

  get catTotalRecords() {
    return this.filteredContas.length;
  }

  get catRangeStart() {
    return this.catTotalRecords === 0 ? 0 : this.catFirst + 1;
  }

  get catRangeEnd() {
    return Math.min(this.catFirst + this.catRows, this.catTotalRecords);
  }

  goCatNext() {
    this.catFirst = this.catFirst + this.catRows;
  }

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  onSearch() {
    this.catFirst = 0;
  }

  constructor() {
    this.load();
  }

  displayName(c: ContaBancariaResponse) {
    const cod = c.bancoCodigo || '';
    const nome = c.bancoNome || '';
    const ag = c.agencia || '';
    const ct = c.conta || '';
    return [cod, nome, ag && `${ag}/${ct}`].filter(Boolean).join(' - ');
  }

  load() {
    this.service.list({ page: 0, size: 1000, sort: 'bancoNome,asc' }).subscribe({
      next: (page: Page<ContaBancariaResponse>) => this.contas = page.content || [],
      error: () => {}
    });
  }

  incluir() {
    const ref = this.dialog.open(ContasItemDialog, {
      header: 'Nova Conta',
      width: '40rem',
      styleClass: 'modern-dialog',
      data: {
        bancoCodigo: '',
        bancoNome: '',
        agencia: '',
        conta: '',
        digitoConta: '',
        tipoConta: 'CORRENTE',
        titularConta: '',
        cpfCnpjTitular: '',
        saldoAtual: null,
        limiteChequeEspecial: null,
        snAtivo: true,
        ativo: true,
      },
    });

    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        const r = result || {};
        const tipoRaw = String(r.tipoConta || '').trim().toUpperCase();
        const tipoConta =
          tipoRaw.includes('POUP') ? 'POUPANCA' :
          tipoRaw.includes('CORR') ? 'CORRENTE' :
          tipoRaw.includes('INV') ? 'INVESTIMENTO' : tipoRaw;
        const dto: ContaBancariaRequest = {
          bancoCodigo: String(r.bancoCodigo || '').trim(),
          bancoNome: String(r.bancoNome || '').trim(),
          agencia: String(r.agencia || '').trim(),
          conta: String(r.conta || '').trim(),
          digitoConta: String(r.digitoConta || '').trim() || undefined,
          tipoConta,
          titularConta: String(r.titularConta || '').trim(),
          cpfCnpjTitular: String(r.cpfCnpjTitular || '').trim(),
          saldoAtual: r.valorSaldoInicial != null ? Number(r.valorSaldoInicial) : (r.saldoAtual != null ? Number(r.saldoAtual) : undefined),
          limiteChequeEspecial: r.limiteChequeEspecial != null ? Number(r.limiteChequeEspecial) : undefined,
          ativo: r.snAtivo !== false,
        };
        if (!dto.bancoCodigo || !dto.bancoNome || !dto.agencia || !dto.conta || !dto.tipoConta || !dto.titularConta || !dto.cpfCnpjTitular) return;
        this.service.create(dto).subscribe({
          next: (created: ContaBancariaResponse) => {
            this.contas = [...this.contas, created];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Conta bancária cadastrada!' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um erro ao cadastrar a conta.' });
          }
        });
      });
    }
  }

  remover(item: ContaBancariaResponse) {
    this.confirmationService.confirm({
      title: 'Excluir Conta Bancária',
      message: `Tem certeza que deseja excluir a conta <span class="font-semibold text-slate-900">${item.bancoNome}</span>? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir Definitivamente',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.service.delete(item.id).subscribe({
          next: () => {
            this.contas = this.contas.filter(c => c.id !== item.id);
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Conta bancária excluída com sucesso!' });
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir a conta bancária.' });
          }
        });
      }
    });
  }

  visualizar(item: ContaBancariaResponse) {
    this.dialog.open(ContasItemDialog, {
      header: 'Detalhes da conta',
      width: '40rem',
      styleClass: 'modern-dialog',
      data: { ...item, readonly: true },
    });
  }

  editar(item: ContaBancariaResponse) {
    const ref = this.dialog.open(ContasItemDialog, {
      header: 'Editar conta',
      width: '40rem',
      styleClass: 'modern-dialog',
      data: { ...item },
    });
    ref?.onClose?.subscribe(result => {
      const r = result || {};
      const tipoRaw = String(r.tipoConta || '').trim().toUpperCase();
      const tipoConta =
        tipoRaw.includes('POUP') ? 'POUPANCA' :
        tipoRaw.includes('CORR') ? 'CORRENTE' :
        tipoRaw.includes('INV') ? 'INVESTIMENTO' : tipoRaw;
      const dto: ContaBancariaRequest = {
        bancoCodigo: String(r.bancoCodigo || '').trim(),
        bancoNome: String(r.bancoNome || '').trim(),
        agencia: String(r.agencia || '').trim(),
        conta: String(r.conta || '').trim(),
        digitoConta: String(r.digitoConta || '').trim() || undefined,
        tipoConta,
        titularConta: String(r.titularConta || '').trim(),
        cpfCnpjTitular: String(r.cpfCnpjTitular || '').trim(),
        saldoAtual: r.valorSaldoInicial != null ? Number(r.valorSaldoInicial) : (r.saldoAtual != null ? Number(r.saldoAtual) : undefined),
        limiteChequeEspecial: r.limiteChequeEspecial != null ? Number(r.limiteChequeEspecial) : undefined,
        ativo: r.snAtivo !== false,
      };
      if (!dto.bancoCodigo || !dto.bancoNome || !dto.agencia || !dto.conta || !dto.tipoConta || !dto.titularConta || !dto.cpfCnpjTitular) return;
      this.service.update(item.id, dto).subscribe({
        next: (updated: ContaBancariaResponse) => {
          this.contas = this.contas.map(c => c.id === updated.id ? updated : c);
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Conta bancária atualizada!' });
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Ocorreu um erro ao atualizar a conta.' });
        }
      });
    });
  }
}
