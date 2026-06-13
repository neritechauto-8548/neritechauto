import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { PedidoFornecedorService } from '../pedido-fornecedor.service';
import { PedidoFornecedorResponse } from '../models/compra.models';

import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'pedido-fornecedor',
  standalone: true,
  templateUrl: './pedido-fornecedor.html',
  imports: [CommonModule, FormsModule, RouterModule, ButtonModule, InputTextModule, TableModule, MatMenuModule, MatButtonModule, MatIconModule, ToastModule, ConfirmDialogModule],
  providers: [MessageService, ConfirmationService]
})
export class PedidoFornecedor implements OnInit {
  private readonly router = inject(Router);
  private readonly pedidoService = inject(PedidoFornecedorService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);


  dataInicial = '';
  dataFinal = '';
  termo = '';

  pedidos: PedidoFornecedorResponse[] = [];
  loading = false;

  // Paginação
  rows = 10;
  first = 0;
  totalElements = 0;

  ngOnInit(): void {
    this.carregarPedidos();
  }

  carregarPedidos(termo?: string) {
    this.loading = true;
    const params: Record<string, any> = { page: Math.floor(this.first / this.rows), size: this.rows };
    if (termo) params['termo'] = termo;
    this.pedidoService.list(params).subscribe({
      next: (page) => {
        this.pedidos = page.content;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: () => {
        this.pedidos = [];
        this.loading = false;
      }
    });
  }

  get pagedData() {
    return this.pedidos;
  }

  get totalRecords() {
    return this.totalElements;
  }

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  get totalPages() {
    return Math.max(1, Math.ceil(this.totalRecords / this.rows));
  }

  jumpPageInput = '';

  imprimir() {
    this.router.navigate(['/fornecedor/pedidos/relatorio']);
  }

  cadastrarPedido() {
    this.router.navigate(['/fornecedor/pedidos/cadastro']);
  }

  visualizarPedido(id: number) {
    this.router.navigate(['/fornecedor/pedidos/visualizar', id]);
  }

  editarPedido(id: number) {
    this.router.navigate(['/fornecedor/pedidos/editar', id]);
  }

  buscar() {
    this.first = 0;
    this.carregarPedidos(this.termo.trim() || undefined);
  }

  onPage(event: { first: number; rows: number }) {
    this.first = event.first;
    this.rows = event.rows;
    this.carregarPedidos(this.termo.trim() || undefined);
  }

  anterior() {
    const prev = Math.max(0, this.first - this.rows);
    if (prev !== this.first) {
      this.first = prev;
      this.carregarPedidos(this.termo.trim() || undefined);
    }
  }

  proximo() {
    const next = this.first + this.rows;
    if (next < this.totalRecords) {
      this.first = next;
      this.carregarPedidos(this.termo.trim() || undefined);
    }
  }

  irParaPagina() {
    const n = parseInt(this.jumpPageInput, 10);
    if (!isNaN(n) && n > 0) {
      const idx = (n - 1) * this.rows;
      if (idx < this.totalRecords) {
        this.first = idx;
        this.carregarPedidos(this.termo.trim() || undefined);
      }
    }
  }

  formatarData(isoDate?: string): string {
    if (!isoDate) return '-';
    const [year, month, day] = isoDate.split('-');
    return `${day}/${month}/${year}`;
  }

  alterarStatus(pedido: PedidoFornecedorResponse, novoStatus: string) {
    let msg = '';
    let icon = '';
    let btnClass = '';
    
    if (novoStatus === 'RECEBIDO') {
        msg = `Tem certeza que deseja marcar o pedido #${pedido.numeroPedido} como entregue? Os produtos serão adicionados ao estoque.`;
        icon = 'pi pi-check-circle';
        btnClass = 'p-button-success';
    } else if (novoStatus === 'CANCELADO') {
        msg = `Tem certeza que deseja cancelar o pedido #${pedido.numeroPedido}?`;
        icon = 'pi pi-exclamation-triangle';
        btnClass = 'p-button-danger';
    }

    this.confirmationService.confirm({
      message: msg,
      header: 'Confirmar Ação',
      icon: icon,
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      acceptButtonStyleClass: btnClass,
      accept: () => {
        this.loading = true;
        this.pedidoService.updateStatus(pedido.id, novoStatus).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Status atualizado com sucesso.' });
            this.carregarPedidos();
          },
          error: (err) => {
            this.loading = false;
            const errorMsg = err?.error?.message || 'Erro ao atualizar status.';
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMsg });
          }
        });
      }
    });
  }
}

