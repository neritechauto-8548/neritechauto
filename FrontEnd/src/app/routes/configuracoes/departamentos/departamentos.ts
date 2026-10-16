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
import { empresaIdAtual } from '@shared/utils/empresa-context';
import { HttpErrorResponse } from '@angular/common/http';
import { FuncionarioService } from '../colaboradores/funcionario.service';

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
  private readonly funcionarioService = inject(FuncionarioService);

  dialogRef: DynamicDialogRef<DepartamentoItemDialog> | null = null;
  loading = false;
  searchTerm = '';
  filtroAtivo = '';
  filtroTipo = '';
  loadError = '';

  itens: DepartamentoContabioResponse[] = [];
  responsaveis: Array<{ id: number; nomeCompleto: string }> = [];

  // --- PAGINATION (Stripe UX) ---
  catFirst = 0;
  catRows = 20;
  catTotal = 0;

  ngOnInit() {
    this.funcionarioService.list({ page: 0, size: 100, status: 'ATIVO' }).subscribe({ next: page => this.responsaveis = page.content ?? [] });
    this.load();
  }

  load() {
    this.loading = true;
    this.loadError = '';
    this.departamentosService.list({ page: Math.floor(this.catFirst / this.catRows), size: this.catRows,
      sort: 'ordem,asc', q: this.searchTerm.trim() || undefined,
      ativo: this.filtroAtivo === '' ? undefined : this.filtroAtivo === 'true',
      tipo: this.filtroTipo.trim() || undefined }).subscribe({
      next: (resp) => {
        this.itens = resp.content || [];
        this.catTotal = resp.totalElements || 0;
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.itens = [];
        this.loadError = err.status === 403 ? 'Você não tem permissão para administrar áreas.'
          : err.status === 0 ? 'Sem conexão. Verifique sua rede e tente novamente.' : 'Não foi possível carregar as áreas.';
      }
    });
  }

  get filteredItens() { return this.itens; }

  get pagedItens() {
    return this.filteredItens;
  }

  get catTotalRecords() {
    return this.catTotal;
  }

  get catRangeStart() {
    return this.catTotalRecords === 0 ? 0 : this.catFirst + 1;
  }

  get catRangeEnd() {
    return Math.min(this.catFirst + this.catRows, this.catTotalRecords);
  }

  onSearch() {
    this.catFirst = 0;
    this.load();
  }

  goCatNext() {
    this.catFirst = this.catFirst + this.catRows;
    this.load();
  }

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
    this.load();
  }

  incluir(): void {
    const ref = this.dialogService.open(DepartamentoItemDialog, {
      header: 'Nova Área',
      width: '700px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { responsaveis: this.responsaveis },
    });

    ref?.onClose?.subscribe((result: any) => {
      if (result) {
        if (result.error) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: result.error });
          return;
        }
        if (result.descricao?.trim()) {
          const tenantId = empresaIdAtual(this.storage);
          if (!tenantId) { this.loadError = 'Não foi possível identificar sua empresa. Entre novamente.'; return; }
          this.departamentosService.create({ empresaId: tenantId, descricao: result.descricao, codigo: result.codigo,
            detalhes: result.detalhes, tipoOperacao: result.tipoOperacao, ativo: result.ativo,
            supervisorId: result.supervisorId, ordem: result.ordem, tempoPadraoMinutos: result.tempoPadraoMinutos }).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Área criada com sucesso' });
              this.load();
            },
            error: (err: HttpErrorResponse) => this.mutationError(err)
          });
        }
      }
    });
  }

  editar(item: DepartamentoContabioResponse): void {
    const ref = this.dialogService.open(DepartamentoItemDialog, {
      header: 'Editar Área',
      width: '700px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { ...item, responsaveis: this.responsaveis },
    });

    ref?.onClose?.subscribe((result: any) => {
      if (result) {
        if (result.error) {
          this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: result.error });
          return;
        }
        if (result.descricao?.trim()) {
          const tenantId = empresaIdAtual(this.storage);
          if (!tenantId) { this.loadError = 'Não foi possível identificar sua empresa. Entre novamente.'; return; }
          const payload = { empresaId: tenantId, descricao: result.descricao, codigo: result.codigo,
            detalhes: result.detalhes, tipoOperacao: result.tipoOperacao, ativo: result.ativo,
            supervisorId: result.supervisorId, ordem: result.ordem, tempoPadraoMinutos: result.tempoPadraoMinutos, versao: item.versao };
          this.departamentosService.update(item.id, payload).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Área atualizada com sucesso' });
              this.load();
            },
            error: (err: HttpErrorResponse) => this.mutationError(err)
          });
        }
      }
    });
  }

  remover(item: DepartamentoContabioResponse): void {
    this.confirmationService.confirm({
      title: 'Inativar Área',
      message: `Inativar a área <span class="font-semibold text-slate-900">${item.descricao}</span>? Funcionários e cargos vinculados serão preservados.`,
      confirmText: 'Inativar',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.departamentosService.delete(item.id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Área inativada; vínculos preservados' });
            this.load();
          },
          error: (err: HttpErrorResponse) => this.mutationError(err)
        });
      }
    });
  }

  private mutationError(err: HttpErrorResponse): void {
    const detail = err.status === 403 ? 'Operação não autorizada.' : err.status === 409
      ? (err.error?.message || 'Conflito: revise os dados ou recarregue a área.')
      : err.status === 0 ? 'Sem conexão. Verifique sua rede.' : 'Não foi possível concluir a operação.';
    this.messageService.add({ severity: 'error', summary: 'Erro', detail });
  }
}
