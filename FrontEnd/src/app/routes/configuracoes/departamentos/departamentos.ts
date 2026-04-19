import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';
import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';

import { DepartamentoItemDialog } from './departamento-item-dialog';
import { DepartamentoService, DepartamentoContabioResponse } from './departamento.service';
import { Page } from '../categoria/categoria-produto.service';

@Component({
  selector: 'app-departamentos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    DynamicDialogModule,
    ToastModule,
    ConfirmDialogModule,
    MatIconModule,
    ConfirmationDialogComponent
  ],
  providers: [
    DialogService,
    MessageService,
    ConfirmationService,
    { provide: PrimeNGConfirmationService, useClass: PrimeNGConfirmationService }
  ],
  templateUrl: './departamentos.html',
  styleUrls: ['./departamentos.scss'],
})
export class Departamentos implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly departamentosService = inject(DepartamentoService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  dialogRef: DynamicDialogRef<DepartamentoItemDialog> | null = null;
  loading = false;
  searchTerm = '';

  itens: DepartamentoContabioResponse[] = [];

  // --- PAGINATION (Stripe UX) ---
  catFirst = 0;
  catRows = 10;

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.departamentosService.list({ page: 0, size: 1000, sort: 'descricao,asc' }).subscribe({
      next: (resp) => {
        this.itens = resp.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao listar departamentos', err);
        this.loading = false;
      }
    });
  }

  get filteredItens() {
    if (!this.searchTerm) return this.itens;
    const term = this.searchTerm.toLowerCase();
    return this.itens.filter(i =>
      i.descricao && i.descricao.toLowerCase().includes(term)
    );
  }

  get pagedItens() {
    return this.filteredItens.slice(this.catFirst, this.catFirst + this.catRows);
  }

  get catTotalRecords() {
    return this.filteredItens.length;
  }

  get catRangeStart() {
    return this.catTotalRecords === 0 ? 0 : this.catFirst + 1;
  }

  get catRangeEnd() {
    return Math.min(this.catFirst + this.catRows, this.catTotalRecords);
  }

  onSearch() {
    this.catFirst = 0;
  }

  goCatNext() {
    this.catFirst = this.catFirst + this.catRows;
  }

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  incluir(): void {
    const ref = this.dialogService.open(DepartamentoItemDialog, {
      header: 'Novo Departamento',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((result: { descricao: string } | undefined) => {
      if (result && result.descricao?.trim()) {
        let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
        if (!tenantId || typeof tenantId === 'object') tenantId = '1';
        this.departamentosService.create({ empresaId: Number(tenantId), descricao: result.descricao }).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Departamento criado com sucesso' });
            this.load();
          },
          error: (err) => {
            console.error('Erro ao criar departamento', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar departamento' });
          }
        });
      }
    });
  }

  editar(item: DepartamentoContabioResponse): void {
    const ref = this.dialogService.open(DepartamentoItemDialog, {
      header: 'Editar Departamento',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { descricao: item.descricao },
    });

    ref?.onClose?.subscribe((result: { descricao: string } | undefined) => {
      if (result && result.descricao?.trim()) {
        let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
        if (!tenantId || typeof tenantId === 'object') tenantId = '1';
        const payload = { empresaId: Number(tenantId), descricao: result.descricao };
        this.departamentosService.update(item.id, payload).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Departamento atualizado com sucesso' });
            this.load();
          },
          error: (err) => {
            console.error('Erro ao atualizar departamento', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar departamento' });
          }
        });
      }
    });
  }

  remover(item: DepartamentoContabioResponse): void {
    this.confirmationService.confirm({
      title: 'Excluir Departamento',
      message: `Tem certeza que deseja excluir o departamento <span class="font-semibold text-slate-900">${item.descricao}</span>? Esta ação não pode ser desfeita.`,
      confirmText: 'Excluir Definitivamente',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.departamentosService.delete(item.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Departamento apagado com sucesso' });
            this.load();
          },
          error: (err) => {
            console.error('Erro ao excluir departamento', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir departamento' });
          }
        });
      }
    });
  }
}
