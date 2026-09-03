import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DataTableShell, DataViewState, NeriTechIcon, PageHeader } from '@shared';
import { MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { NgxPermissionsService } from 'ngx-permissions';

import { OrdemServicoResponse } from '../models/os.models';
import { OrdemServicoService } from '../ordem-servico.service';

interface OrdemServicoListRow {
  id: number;
  numeroOS: string;
  cliente: string;
  placa: string;
  veiculo: string;
  dataAbertura?: string;
  statusNome: string;
  valorTotal: number;
}

@Component({
  selector: 'ordem-servico',
  standalone: true,
  templateUrl: './ordem-servico.html',
  styleUrl: './ordem-servico.scss',
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    NeriTechIcon,
    DataTableShell,
    DataViewState,
    InputTextModule,
    SkeletonModule,
    ToastModule,
  ],
  providers: [MessageService],
})
export class OrdemServico implements OnInit {
  private readonly router = inject(Router);
  private readonly osService = inject(OrdemServicoService);
  private readonly messageService = inject(MessageService);
  private readonly permissions = inject(NgxPermissionsService);

  search = '';
  orders: OrdemServicoListRow[] = [];
  loading = false;
  loadError = false;
  forbidden = false;
  totalItems = 0;
  first = 0;
  readonly rows = 25;

  ngOnInit() {
    this.load();
  }

  get canCreateOS() {
    return Boolean(this.permissions.getPermission('OS_INCLUIR'));
  }

  get canEditOS() {
    return Boolean(this.permissions.getPermission('OS_EDITAR'));
  }

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  get totalPages() {
    return Math.max(1, Math.ceil(this.totalItems / this.rows));
  }

  get rangeStart() {
    return this.totalItems === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.orders.length, this.totalItems);
  }

  get hasSearch() {
    return Boolean(this.search.trim());
  }

  onBuscar() {
    this.first = 0;
    this.load();
  }

  clearSearch() {
    if (!this.search) return;
    this.search = '';
    this.first = 0;
    this.load();
  }

  refresh() {
    this.load();
  }

  goPrev() {
    if (this.first === 0 || this.loading) return;
    this.first = Math.max(0, this.first - this.rows);
    this.load();
  }

  goNext() {
    if (this.first + this.rows >= this.totalItems || this.loading) return;
    this.first += this.rows;
    this.load();
  }

  onCadastrar() {
    if (!this.canCreateOS) {
      this.warnPermission('criar ordens de serviço');
      return;
    }
    this.router.navigate(['/os/cadastro']);
  }

  onCadastrarOrcamento() {
    if (!this.canCreateOS) {
      this.warnPermission('criar orçamentos');
      return;
    }
    this.router.navigate(['/orcamentos/novo']);
  }

  visualizarOS(row: OrdemServicoListRow) {
    this.router.navigate(['/ordens-servico', row.id], {
      state: { returnUrl: this.router.url },
    });
  }

  editarOS(row: OrdemServicoListRow) {
    if (!this.canEditOS) {
      this.warnPermission('editar ordens de serviço');
      return;
    }
    this.router.navigate(['/os/cadastro', row.id], {
      state: { returnUrl: this.router.url },
    });
  }

  statusClass(statusNome: string) {
    const status = (statusNome || '').toLowerCase();
    if (status.includes('conclu') || status.includes('finaliz') || status.includes('entreg')) {
      return 'status-badge--success';
    }
    if (status.includes('cancel')) return 'status-badge--danger';
    if (status.includes('andamento') || status.includes('execu') || status.includes('aguard')) {
      return 'status-badge--warning';
    }
    if (status.includes('aberta') || status.includes('aberto')) return 'status-badge--info';
    return 'status-badge--neutral';
  }

  private load() {
    this.loading = true;
    this.loadError = false;
    this.forbidden = false;

    const page = Math.floor(this.first / this.rows);

    try {
      this.osService.list({
        page,
        size: this.rows,
        sort: 'numeroOS,desc',
        search: this.search.trim() || undefined,
        tipo: 'SERVICO',
      }).subscribe({
        next: response => {
          this.orders = (response?.content || []).map(item => this.mapOS(item));
          this.totalItems = response?.totalElements ?? this.orders.length;
          this.loading = false;
        },
        error: error => this.handleLoadError(error),
      });
    } catch (error) {
      this.handleLoadError(error);
    }
  }

  private handleLoadError(error: any) {
    this.orders = [];
    this.totalItems = 0;
    this.loading = false;
    this.loadError = true;
    this.forbidden = error?.status === 403;

    if (!this.forbidden) {
      this.messageService.add({
        severity: 'error',
        summary: 'Não foi possível carregar as ordens de serviço',
        detail: 'A busca foi preservada. Tente novamente.',
      });
    }
  }

  private mapOS(order: OrdemServicoResponse): OrdemServicoListRow {
    return {
      id: order.id,
      numeroOS: order.numeroOS || `#${order.id}`,
      cliente: order.nomeCliente || 'Cliente não disponível',
      placa: order.placaVeiculo || '',
      veiculo: order.nomeVeiculo || '',
      dataAbertura: order.dataAbertura,
      statusNome: order.statusNome || 'Sem status',
      valorTotal: Number(order.valorTotal ?? 0),
    };
  }

  private warnPermission(operation: string) {
    this.messageService.add({
      severity: 'warn',
      summary: 'Acesso restrito',
      detail: `Seu perfil não possui permissão para ${operation}.`,
    });
  }
}
