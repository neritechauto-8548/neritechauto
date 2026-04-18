import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { LocalStorageService } from '@shared/services/storage.service';
import { PanelModule } from 'primeng/panel';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService as PrimeNGConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService as CustomConfirmationService } from '@shared/services/confirmation.service';
import { MatIconModule } from '@angular/material/icon';
import { InputTextModule } from 'primeng/inputtext';

import { SetoresItemDialog } from './setores-item-dialog';
import { SetorService, SetorResponse } from './setor.service';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';

@Component({
  selector: 'setores',
  standalone: true,
  templateUrl: './setores.html',
  styleUrls: ['./setores.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    PanelModule,
    DynamicDialogModule,
    MatIconModule,
    InputTextModule,
    ToastModule,
    PageHeader,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, PrimeNGConfirmationService],
})
export class Setores implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly setoresService = inject(SetorService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(CustomConfirmationService);
  private readonly messageService = inject(MessageService);

  dialogRef: DynamicDialogRef<any> | null = null;
  loading = false;
  searchTerm = '';

  setores: SetorResponse[] = [];
  catFirst = 0;
  catRows = 10;

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.setoresService.list({ size: 1000, sort: 'nome,asc' }).subscribe({
      next: (resp) => {
        this.setores = resp.content || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao listar setores', err);
        this.loading = false;
      }
    });
  }

  get filteredSetores() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.setores;
    return this.setores.filter(s => s.nome.toLowerCase().includes(term));
  }

  get pagedSetores() {
    return this.filteredSetores.slice(this.catFirst, this.catFirst + this.catRows);
  }

  get catTotalRecords() {
    return this.filteredSetores.length;
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

  goPrev() {
    this.catFirst = Math.max(0, this.catFirst - this.catRows);
  }

  goNext() {
    if (this.catFirst + this.catRows < this.catTotalRecords) {
      this.catFirst = this.catFirst + this.catRows;
    }
  }

  inicialLetter(nome: string): string {
    return nome ? nome.charAt(0).toUpperCase() : '?';
  }

  openAddDialog() {
    const ref = this.dialogService.open(SetoresItemDialog, {
      header: 'Incluindo Novo Setor',
      width: '800px',
      closable: true,
      dismissableMask: true,
    });
    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result?.nome) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          result.empresaId = Number(tenantId);
          // O dialog retorna 'nome' ao inves de descricao agora.
          this.setoresService.create({ empresaId: Number(tenantId), nome: result.nome, ativo: result.ativo ?? true }).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Setor cadastrado com sucesso!' });
              this.load();
            },
            error: (err) => {
              console.error('Erro ao criar setor', err);
              this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar setor.' });
            }
          });
        }
      });
    }
  }

  openEditDialog(setor: SetorResponse) {
    const ref = this.dialogService.open(SetoresItemDialog, {
      header: 'Editando Setor',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: setor,
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result?.nome) {
          let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
          if (!tenantId || typeof tenantId === 'object') tenantId = '1';

          const payload = {
            empresaId: Number(tenantId),
            nome: result.nome,
            ativo: result.ativo ?? true
          };

          this.setoresService.update(setor.id, payload).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Setor atualizado com sucesso!' });
              this.load();
            },
            error: (err) => {
              console.error('Erro ao atualizar setor', err);
               this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar setor.' });
            }
          });
        }
      });
    }
  }

  delete(id: number) {
    this.confirmationService.confirm({
      title: 'Excluir Setor',
      message: 'Tem certeza que deseja excluir este setor? Esta ação não pode ser desfeita.',
      confirmText: 'Excluir Setor',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.setoresService.delete(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Setor apagado com sucesso!' });
            this.load();
          },
          error: (err) => {
            console.error('Erro ao excluir setor', err);
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir setor.' });
          }
        });
      }
    });
  }
}
