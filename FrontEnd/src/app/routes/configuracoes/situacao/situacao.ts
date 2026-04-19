import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { SituacaoItemDialog } from './situacao-item-dialog';
import { SituacaoService, SituacaoResponse } from './situacao.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationService as CustomConfirmationService } from '@shared/services/confirmation.service';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { InputTextModule } from 'primeng/inputtext';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';

@Component({
  selector: 'situacao',
  standalone: true,
  templateUrl: './situacao.html',
  styleUrls: ['./situacao.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    TagModule,
    DynamicDialogModule,
    ToastModule,
    MatIconModule,
    InputTextModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, PrimeNGConfirmationService],
})
export class Situacao implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly situacaoService = inject(SituacaoService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(CustomConfirmationService);
  private readonly messageService = inject(MessageService);

  dialogRef: DynamicDialogRef<SituacaoItemDialog> | null = null;
  itens: SituacaoResponse[] = [];
  loading = false;

  // Search & Filter
  searchTerm = '';

  // Pagination (Stripe UX Pattern)
  catFirst = 0;
  catRows = 10;

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.situacaoService.list({ size: 1000, sort: 'nmSituacao,asc' }).subscribe({
      next: (resp) => {
        this.itens = resp.content || [];
        this.loading = false;
        this.catFirst = 0; // reset pagination on load
      },
      error: (err) => {
        console.error('Erro ao buscar situações', err);
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: 'Não foi possível carregar as situações.'
        });
      }
    });
  }

  // --- Search & Pagination Logic ---

  get filteredSituacoes() {
    const term = this.searchTerm?.toLowerCase().trim();
    if (!term) return this.itens;
    return this.itens.filter(s => s.nmSituacao.toLowerCase().includes(term) || (s.dsSituacao && s.dsSituacao.toLowerCase().includes(term)));
  }

  get pagedSituacoes() {
    return this.filteredSituacoes.slice(this.catFirst, this.catFirst + this.catRows);
  }

  get catTotalRecords() {
    return this.filteredSituacoes.length;
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

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  goCatNext() {
    if (this.catFirst + this.catRows < this.catTotalRecords) {
      this.catFirst += this.catRows;
    }
  }

  // --- Dialog Actions ---

  openAddDialog() {
    const ref = this.dialogService.open(SituacaoItemDialog, {
      header: 'Nova Situação',
      width: '600px',
      closable: true,
      dismissableMask: true,
      styleClass: 'stripe-dialog'
    });

    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result?.nmSituacao) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          const payload = {
            empresaId: Number(tenantId),
            nmSituacao: result.nmSituacao,
            dsSituacao: result.dsSituacao || '',
            corSituacao: result.corSituacao || '#2563EB',
          };

          this.situacaoService.create(payload).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Situação criada com sucesso.' });
              this.load();
            },
            error: () => {
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao criar situação.' });
            }
          });
        }
      });
    }
  }

  openEditDialog(item: SituacaoResponse) {
    const ref = this.dialogService.open(SituacaoItemDialog, {
      header: 'Editar Situação',
      width: '600px',
      closable: true,
      dismissableMask: true,
      styleClass: 'stripe-dialog',
      data: { id: item.id, nmSituacao: item.nmSituacao, dsSituacao: item.dsSituacao, corSituacao: item.corSituacao },
    });

    if (ref) {
      ref.onClose.subscribe(result => {
        if (result?.nmSituacao) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          const payload = {
            empresaId: Number(tenantId),
            nmSituacao: result.nmSituacao,
            dsSituacao: result.dsSituacao || '',
            corSituacao: result.corSituacao || '#2563EB',
          };

          this.situacaoService.update(item.id, payload).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Situação atualizada com sucesso.' });
              this.load();
            },
            error: () => {
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao atualizar situação.' });
            }
          });
        }
      });
    }
  }

  delete(id: number) {
    this.confirmationService.confirm({
      title: 'Excluir Situação',
      message: 'Tem certeza que deseja excluir esta situação? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.situacaoService.delete(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Situação excluída com sucesso.' });
            this.load();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao excluir situação.' });
          }
        });
      }
    });
  }

  inicialLetter(nome: string): string {
    return nome ? nome.charAt(0).toUpperCase() : '?';
  }
}
