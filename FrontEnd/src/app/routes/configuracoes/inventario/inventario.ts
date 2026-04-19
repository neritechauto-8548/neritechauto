import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DynamicDialogModule, DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { InventarioItemDialog } from './inventario-item-dialog';
import { ItemInventarioDialog } from './item-inventario-dialog';
import { InventarioService, InventarioResponse, InventarioRequest } from './inventario.service';
import { ItemInventarioService, ItemInventarioRequest } from './item-inventario.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmationService } from '@shared/services/confirmation.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'inventario',
  standalone: true,
  templateUrl: './inventario.html',
  styleUrls: ['./inventario.scss'],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    DynamicDialogModule,
    MatIconModule,
    ToastModule,
    RouterModule
  ],
  providers: [DialogService, MessageService],
})
export class Inventario implements OnInit {
  private readonly dialogService = inject(DialogService);
  private readonly inventarioService = inject(InventarioService);
  private readonly itemService = inject(ItemInventarioService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly toast = inject(HotToastService);

  dialogRef: DynamicDialogRef | null = null;

  itens: InventarioResponse[] = [];

  // Custom Pagination / Search
  searchTerm = '';
  page = 0;
  size = 1000; // Load all for client-side pagination matching Categoria
  loading = false;

  // Client-side pagination state
  first = 0;
  rows = 10;

  ngOnInit() {
    this.load();
  }

  private getEmpresaId(): number {
    let tenantId = this.storage.has('tenantId') ? (this.storage.get('tenantId') as string | number) : '1';
    if (!tenantId || typeof tenantId === 'object') tenantId = '1';
    return Number(tenantId);
  }

  load() {
    this.loading = true;
    const empresaId = this.getEmpresaId();
    this.inventarioService.listPorEmpresa(empresaId, { page: this.page, size: this.size, sort: 'dataInicio,desc' }).subscribe({
      next: resp => {
        this.itens = resp.content || [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.toast.error('Erro ao carregar inventários.');
      }
    });
  }

  // --- STATS GETTERS ---
  get totalInventarios() {
    return this.itens.length;
  }

  get totalEmAndamento() {
    return this.itens.filter(i => i.status === 'EM_ANDAMENTO' || i.status === 'PLANEJADO').length;
  }

  get totalConcluidos() {
    return this.itens.filter(i => i.status === 'FINALIZADO' || i.status === 'CANCELADO').length;
  }

  // --- FINANCIAL STATS ---
  get somaValorSistema() {
    return this.filteredItens.reduce((acc, i) => acc + (i.valorTotalSistema || 0), 0);
  }

  get somaValorContado() {
    return this.filteredItens.reduce((acc, i) => acc + (i.valorTotalContado || 0), 0);
  }

  get somaDiferencaValor() {
    return this.filteredItens.reduce((acc, i) => acc + (i.diferencaValor || 0), 0);
  }

  // --- FILTER & PAGINATION GETTERS ---
  get filteredItens() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.itens;
    return this.itens.filter(i =>
      (i.descricao && i.descricao.toLowerCase().includes(term)) ||
      (i.codigo && i.codigo.toLowerCase().includes(term)) ||
      String(i.id).includes(term)
    );
  }

  get paginatedItens() {
    return this.filteredItens.slice(this.first, this.first + this.rows);
  }

  get totalRecords() {
    return this.filteredItens.length;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  onSearch() {
    this.first = 0;
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first += this.rows;
    }
  }

  // --- UTILS ---
  inicialLetter(nome: string): string {
    return nome ? nome.charAt(0).toUpperCase() : 'I';
  }

  // --- ACTIONS ---
  openAddDialog() {
    const ref = this.dialogService.open(InventarioItemDialog, {
      header: 'Novo Inventário',
      width: '800px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog'
    });
    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result?.codigo && result?.descricao && result?.dataInicio) {
          const empresaId = this.getEmpresaId();
          const payload: InventarioRequest = {
            empresaId,
            codigo: String(result.codigo),
            descricao: String(result.descricao),
            tipoInventario: result.tipoInventario || 'GERAL',
            dataInicio: String(result.dataInicio),
            dataFim: result.dataFim || '',
            status: result.status || 'PLANEJADO',
            observacoes: result.observacoes || '',
          };
          this.inventarioService.create(payload).subscribe({
            next: (created) => {
              this.toast.success('Inventário criado com sucesso');
              this.load();
              if (created?.id) {
                this.openAddItems(created.id);
              }
            },
            error: () => {}
          });
        } else if (result) {
          this.toast.error('Preencha Código, Descrição e Data de Início');
        }
      });
    }
  }

  openAddItems(inventarioId: number) {
    const ref = this.dialogService.open(ItemInventarioDialog, {
      header: 'Adicionar itens ao inventário',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: { inventarioId }
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result?.inventarioId && result?.produtoId && result?.quantidadeSistema !== undefined) {
          const dto: ItemInventarioRequest = {
            inventarioId: Number(result.inventarioId),
            produtoId: Number(result.produtoId),
            localizacaoId: result.localizacaoId ? Number(result.localizacaoId) : undefined,
            loteNumero: result.loteNumero || '',
            quantidadeSistema: Number(result.quantidadeSistema),
            quantidadeContada: result.quantidadeContada !== null && result.quantidadeContada !== undefined ? Number(result.quantidadeContada) : undefined,
            valorUnitario: result.valorUnitario !== null && result.valorUnitario !== undefined ? Number(result.valorUnitario) : undefined,
            status: result.status,
            motivoDiferenca: result.motivoDiferenca || '',
            observacoes: result.observacoes || '',
            usuarioContagem: result.usuarioContagem ? Number(result.usuarioContagem) : undefined,
            usuarioConferencia: result.usuarioConferencia ? Number(result.usuarioConferencia) : undefined,
            fotoComprovanteUrl: result.fotoComprovanteUrl || '',
          };
          this.itemService.create(dto).subscribe({
            next: () => {
              this.toast.success('Item adicionado ao inventário');
              if (result.continuar) {
                setTimeout(() => this.openAddItems(inventarioId), 0);
              }
            },
            error: () => {}
          });
        }
      });
    }
  }

  edit(row: InventarioResponse) {
    const ref = this.dialogService.open(InventarioItemDialog, {
      header: 'Editar Inventário',
      width: '800px',
      closable: true,
      dismissableMask: true,
      styleClass: 'modern-dialog',
      data: {
        codigo: row.codigo,
        descricao: row.descricao,
        tipoInventario: row.tipoInventario,
        dataInicio: row.dataInicio,
        dataFim: row.dataFim,
        status: row.status,
        observacoes: row.observacoes || '',
      }
    });
    if (ref) {
      this.dialogRef = ref;
      ref.onClose.subscribe(result => {
        if (result?.codigo && result?.descricao && result?.dataInicio) {
          const empresaId = this.getEmpresaId();
          const payload: InventarioRequest = {
            empresaId,
            codigo: String(result.codigo),
            descricao: String(result.descricao),
            tipoInventario: result.tipoInventario || 'GERAL',
            dataInicio: String(result.dataInicio),
            dataFim: result.dataFim || '',
            status: result.status,
            observacoes: result.observacoes || '',
          };
          this.inventarioService.update(row.id, payload).subscribe({
            next: () => {
              this.toast.success('Inventário atualizado com sucesso');
              this.load();
            },
            error: () => {}
          });
        }
      });
    }
  }

  remove(row: InventarioResponse) {
    this.confirmationService.confirm({
      title: 'Excluir Inventário',
      message: `Tem certeza que deseja excluir o inventário <span class="font-semibold text-slate-900">${row.codigo}</span>?`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.inventarioService.delete(row.id).subscribe({
          next: () => {
            this.toast.success('Inventário apagado com sucesso');
            this.load();
          },
          error: () => {}
        });
      }
    });
  }
}

