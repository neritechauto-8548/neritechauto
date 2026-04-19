import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { MatIconModule } from '@angular/material/icon';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { LocalizacaoItemDialog } from './localizacao-item-dialog';
import { LocalizacaoService, LocalizacaoResponse } from './localizacao.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { Page } from '../categoria/categoria-produto.service';

interface LocalizacaoItem {
  id: number;
  descricao: string;
}

@Component({
  selector: 'localizacao',
  standalone: true,
  templateUrl: './localizacao.html',
  styleUrls: ['./localizacao.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    TagModule,
    DynamicDialogModule,
    ToastModule,
    ConfirmDialogModule,
    InputTextModule,
    MatIconModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, ConfirmationService, { provide: PrimeNGConfirmationService, useClass: PrimeNGConfirmationService }],
})
export class Localizacao implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly localizacaoService = inject(LocalizacaoService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  dialogRef: DynamicDialogRef<LocalizacaoItemDialog> | null = null;
  loading = false;

  itens: LocalizacaoResponse[] = [];

  // --- PAGINATION / SEARCH LOGIC ---
  searchTerm = '';
  catFirst = 0;
  catRows = 10;

  get filteredItens() {
    if (!this.searchTerm) return this.itens;
    const term = this.searchTerm.toLowerCase();
    return this.itens.filter(l =>
      l.descricao && l.descricao.toLowerCase().includes(term)
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

  goCatNext() {
    this.catFirst = this.catFirst + this.catRows;
  }

  goCatPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  onSearch() {
    this.catFirst = 0;
  }

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.localizacaoService.list({ page: 0, size: 1000, sort: 'descricao,asc' }).subscribe({
      next: (resp: Page<LocalizacaoResponse>) => {
        this.itens = resp.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao listar localizações', err);
        this.loading = false;
      }
    });
  }

  openAddDialog() {
    const ref = this.dialogService.open(LocalizacaoItemDialog, {
      header: 'Nova Localização',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });
    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result?.descricao) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';
          this.localizacaoService.create({ empresaId: Number(tenantId), descricao: result.descricao }).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização criada com sucesso' });
              this.load();
            },
            error: (err) => {
               console.error('Erro ao criar localização', err);
               this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao criar localização' });
            }
          });
        }
      });
    }
  }

  openEditDialog(item: LocalizacaoResponse) {
    const ref = this.dialogService.open(LocalizacaoItemDialog, {
      header: 'Editar Localização',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: item
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result?.descricao) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';
          const payload = { empresaId: Number(tenantId), descricao: result.descricao };
          this.localizacaoService.update(item.id, payload).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização atualizada com sucesso' });
              this.load();
            },
            error: (err) => {
              console.error('Erro ao atualizar localização', err);
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar localização' });
            }
          });
        }
      });
    }
  }

  remove(itemId: number) {
    this.confirmationService.confirm({
      title: 'Excluir Localização',
      message: 'Tem certeza que deseja excluir esta localização? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Definitivamente',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.localizacaoService.delete(itemId).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Localização apagada com sucesso' });
            this.load();
          },
          error: (err) => {
            console.error('Erro ao excluir localização', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir localização' });
          }
        });
      }
    });
  }


}
