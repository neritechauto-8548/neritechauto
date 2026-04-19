import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { TooltipModule } from 'primeng/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';
import { HotToastService } from '@ngxpert/hot-toast';

import { ConfirmationService } from '@shared/services/confirmation.service';
import { ItemInventarioService, ItemInventarioResponse, ItemInventarioRequest } from '../item-inventario.service';
import { InventarioService } from '../inventario.service';
import { ItemInventarioDialog } from '../item-inventario-dialog';

@Component({
  selector: 'itens-inventario-page',
  standalone: true,
  templateUrl: './itens-inventario-page.html',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    TooltipModule,
    MatIconModule,
    DynamicDialogModule
  ],
  providers: [DialogService, MessageService]
})
export class ItensInventarioPage implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly itemService = inject(ItemInventarioService);
  private readonly inventarioService = inject(InventarioService);
  private readonly dialogService = inject(DialogService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly toast = inject(HotToastService);

  inventarioId = 0;
  inventarioDescricao = 'Carregando...';

  itensRaw: ItemInventarioResponse[] = [];
  loading = false;

  // Custom Pagination / Search
  searchTerm = '';
  page = 0;
  size = 1000; // Load all for client-side pagination styling

  first = 0;
  rows = 15;

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.inventarioId = Number(params['id']);
      if (this.inventarioId) {
        this.loadCabecalho();
        this.loadItens();
      }
    });
  }

  loadCabecalho() {
    this.inventarioService.getById(this.inventarioId).subscribe({
      next: (inv) => {
        this.inventarioDescricao = `${inv.codigo} - ${inv.descricao}`;
      }
    });
  }

  loadItens() {
    this.loading = true;
    this.itemService.listPorInventario(this.inventarioId, { page: this.page, size: this.size, sort: 'dataContagem,desc' }).subscribe({
      next: resp => {
        this.itensRaw = resp.content || [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.toast.error('Erro ao carregar itens.');
      }
    });
  }

  // --- STATS GETTERS ---
  get totais() {
    let sis = 0;
    let btd = 0;
    let vSist = 0;
    let vDif = 0;

    this.itensRaw.forEach(i => {
      sis += i.quantidadeSistema || 0;
      btd += i.quantidadeContada || 0;
      vSist += i.valorTotalSistema || 0;
      vDif += i.diferencaValor || 0;
    });

    return {
      quantidadeSistema: sis,
      quantidadeContada: btd,
      valorSist: vSist,
      valorDif: vDif
    };
  }

  // --- FILTER & PAGINATION GETTERS ---
  get filteredItens() {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.itensRaw;
    return this.itensRaw.filter(i =>
      (i.produtoNome && i.produtoNome.toLowerCase().includes(term)) ||
      (i.localizacaoNome && i.localizacaoNome.toLowerCase().includes(term)) ||
      (i.loteNumero && i.loteNumero.toLowerCase().includes(term)) ||
      String(i.produtoId).includes(term)
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

  // --- ACTIONS ---
  openAddItem() {
    const ref = this.dialogService.open(ItemInventarioDialog, {
      header: 'Adicionar Peça Bipada',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: { inventarioId: this.inventarioId }
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result) this.salvarItemAPI(result, null);
      });
    }
  }

  editItem(item: ItemInventarioResponse) {
    const ref = this.dialogService.open(ItemInventarioDialog, {
      header: 'Editar Peça Bipada',
      width: '800px',
      closable: true,
      dismissableMask: true,
      data: { inventarioId: this.inventarioId, item }
    });
    if (ref) {
      ref.onClose.subscribe(result => {
        if (result) this.salvarItemAPI(result, item.id);
      });
    }
  }

  private salvarItemAPI(result: any, id: number | null) {
    const dto: ItemInventarioRequest = {
      inventarioId: Number(result.inventarioId),
      produtoId: Number(result.produtoId),
      localizacaoId: result.localizacaoId ? Number(result.localizacaoId) : undefined,
      loteNumero: result.loteNumero || '',
      quantidadeSistema: Number(result.quantidadeSistema),
      quantidadeContada: result.quantidadeContada !== null ? Number(result.quantidadeContada) : undefined,
      valorUnitario: result.valorUnitario !== null ? Number(result.valorUnitario) : undefined,
      status: result.status,
      motivoDiferenca: result.motivoDiferenca || '',
      observacoes: result.observacoes || '',
      usuarioContagem: result.usuarioContagem ? Number(result.usuarioContagem) : undefined,
      usuarioConferencia: result.usuarioConferencia ? Number(result.usuarioConferencia) : undefined,
      fotoComprovanteUrl: result.fotoComprovanteUrl || '',
    };

    const request = id ? this.itemService.update(id, dto) : this.itemService.create(dto);
    request.subscribe({
      next: () => {
        this.toast.success(id ? 'Peça atualizada' : 'Peça adicionada');
        this.loadItens();
        if (!id && result.continuar) {
          setTimeout(() => this.openAddItem(), 0);
        }
      }
    });
  }

  removeItem(item: ItemInventarioResponse) {
    this.confirmationService.confirm({
      title: 'Remover Peça Bipada',
      message: `Certeza que deseja excluir o apontamento da peça <span class="font-semibold">${item.produtoNome}</span>?`,
      confirmText: 'Excluir',
      cancelText: 'Cancelar',
      type: 'danger',
      icon: 'warning'
    }).subscribe(confirmed => {
      if (confirmed) {
        this.itemService.delete(item.id).subscribe({
          next: () => {
            this.toast.success('Apontamento excluído com sucesso');
            this.loadItens();
          }
        });
      }
    });
  }
}
