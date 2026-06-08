import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { AccordionModule } from 'primeng/accordion';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { MatIconModule } from '@angular/material/icon';

import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { PageHeader, ConfirmationDialogComponent } from '@shared/components';

import { ChecklistItemDialog } from './checklist-item-dialog';
import { ChecklistModeloDialog } from './checklist-modelo-dialog';
import { ChecklistService, ChecklistRequest } from './checklist.service';
import { ItChecklistOsService, ItChecklistOsRequest, ItChecklistOsResponse } from './it-checklist-os.service';

@Component({
  selector: 'app-checklist',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    AccordionModule,
    DynamicDialogModule,
    ToastModule,
    MatIconModule,
    ConfirmationDialogComponent
  ],
  providers: [DialogService, MessageService, ConfirmationService],
  templateUrl: './checklist.html',
  styleUrls: ['./checklist.scss'],
})
export class Checklist implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly checklistService = inject(ChecklistService);
  private readonly itChecklistService = inject(ItChecklistOsService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  modelos: { id: number; titulo: string; itens: ItChecklistOsResponse[]; loading?: boolean; loaded?: boolean }[] = [];
  loading = false;

  ngOnInit() {
    this.carregarChecklists();
  }

  get tenantId(): string | number {
    let tid = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tid || typeof tid === 'object') tid = '1';
    return tid;
  }

  carregarChecklists(): void {
    this.loading = true;
    this.checklistService.list({ page: 0, size: 1000, sort: 'dsChecklist,asc' }).subscribe({
      next: page => {
        this.modelos = (page.content || []).map(c => ({
          id: c.id,
          titulo: c.dsChecklist,
          itens: [],
          loading: true,
          loaded: false
        }));
        this.loading = false;

        // Carrega imediatamente os itens de cada checklist
        this.modelos.forEach(modelo => {
          this.itChecklistService.listPorChecklist(modelo.id).subscribe({
            next: itens => {
              modelo.itens = itens || [];
              modelo.loading = false;
              modelo.loaded = true;
              this.modelos = [...this.modelos];
            },
            error: err => {
              console.error(`Erro ao carregar itens para o checklist ${modelo.id}:`, err);
              modelo.loading = false;
              modelo.loaded = true;
              this.modelos = [...this.modelos];
            }
          });
        });
      },
      error: (err) => {
        console.error('Erro ao listar checklists', err);
        this.loading = false;
      }
    });
  }

  novoChecklist(): void {
    const ref = this.dialogService.open(ChecklistModeloDialog, {
      header: 'Novo Checklist',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((data: { titulo: string } | undefined) => {
      const value = data?.titulo?.trim();
      if (value) {
        const dto: ChecklistRequest = { empresaId: Number(this.tenantId), dsChecklist: value };
        this.checklistService.create(dto).subscribe({
          next: created => {
            this.modelos = [{ id: created.id, titulo: created.dsChecklist, itens: [], loaded: true }, ...this.modelos];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Checklist criado com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao criar checklist', err);
          }
        });
      }
    });
  }

  editarModelo(index: number): void {
    const modelo = this.modelos[index];
    const ref = this.dialogService.open(ChecklistModeloDialog, {
      header: 'Editar Checklist',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { titulo: modelo.titulo },
    });

    ref?.onClose?.subscribe((data: { titulo: string } | undefined) => {
      const value = data?.titulo?.trim();
      if (value) {
        const dto: ChecklistRequest = { empresaId: Number(this.tenantId), dsChecklist: value };
        this.checklistService.update(modelo.id, dto).subscribe({
          next: updated => {
            this.modelos[index] = { ...modelo, titulo: updated.dsChecklist };
            this.modelos = [...this.modelos];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Checklist atualizado com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao atualizar checklist', err);
          }
        });
      }
    });
  }

  removerModelo(index: number): void {
    const modelo = this.modelos[index];
    this.confirmationService.confirm({
      title: 'Excluir Checklist',
      message: `Tem certeza que deseja excluir o checklist <span class="font-semibold text-slate-900">${modelo.titulo}</span>? Todos os itens vinculados também serão removidos.`,
      confirmText: 'Excluir Definitivamente',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.checklistService.delete(modelo.id).subscribe({
          next: () => {
            this.modelos = this.modelos.filter((_, i) => i !== index);
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Checklist removido com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao excluir checklist', err);
          }
        });
      }
    });
  }

  adicionarItem(modeloIndex: number): void {
    const ref = this.dialogService.open(ChecklistItemDialog, {
      header: 'Adicionar Item',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
    });

    ref?.onClose?.subscribe((descricao: string | undefined) => {
      const value = descricao?.trim();
      if (value) {
        const modelo = this.modelos[modeloIndex];
        const dto: ItChecklistOsRequest = { checkListId: modelo.id, dsItChecklist: value };
        this.itChecklistService.create(dto).subscribe({
          next: created => {
            modelo.itens = [...(modelo.itens || []), created];
            this.modelos = [...this.modelos];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Item adicionado com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao adicionar item', err);
          }
        });
      }
    });
  }

  editarItem(modeloIndex: number, itemIndex: number): void {
    const modelo = this.modelos[modeloIndex];
    const atual = modelo.itens[itemIndex];
    const ref = this.dialogService.open(ChecklistItemDialog, {
      header: 'Editar Item',
      width: '450px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: { descricao: atual?.dsItChecklist || '' },
    });

    ref?.onClose?.subscribe((descricao: string | undefined) => {
      const value = descricao?.trim();
      if (value) {
        this.itChecklistService.update(atual.id, { checkListId: modelo.id, dsItChecklist: value }).subscribe({
          next: updated => {
            modelo.itens[itemIndex] = updated;
            modelo.itens = [...modelo.itens];
            this.modelos = [...this.modelos];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Item atualizado com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao atualizar item', err);
          }
        });
      }
    });
  }

  removerItem(modeloIndex: number, itemIndex: number): void {
    const modelo = this.modelos[modeloIndex];
    const item = modelo.itens[itemIndex];
    this.confirmationService.confirm({
      title: 'Excluir Item',
      message: `Tem certeza que deseja excluir o item <span class="font-semibold text-slate-900">${item.dsItChecklist}</span>?`,
      confirmText: 'Excluir Item',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'delete'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.itChecklistService.delete(item.id).subscribe({
          next: () => {
            modelo.itens = modelo.itens.filter((_, i) => i !== itemIndex);
            this.modelos = [...this.modelos];
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Item removido com sucesso' });
          },
          error: (err) => {
            console.error('Erro ao excluir item', err);
          }
        });
      }
    });
  }

  abrir(index: any): void {
    const idx = typeof index === 'string' ? parseInt(index, 10) : index;
    const modelo = this.modelos[idx];
    if (!modelo || modelo.loaded) {
      return;
    }
    modelo.loading = true;
    this.itChecklistService.listPorChecklist(modelo.id).subscribe({
      next: itens => {
        modelo.itens = itens || [];
        modelo.loading = false;
        modelo.loaded = true;
        this.modelos = [...this.modelos];
      },
      error: () => {
        modelo.loading = false;
      }
    });
  }

  getItemText(item: ItChecklistOsResponse): string {
    return item?.dsItChecklist || '';
  }
}
