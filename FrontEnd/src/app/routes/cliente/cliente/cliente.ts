import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PageHeader } from '@shared';
import { MessageService, MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { NgxPermissionsService } from 'ngx-permissions';

import {
  StatusCliente,
  StatusClienteLabels,
  TipoCliente,
  TipoClienteLabels,
  getStatusClienteOptions,
  getTipoClienteOptions,
} from '../models/cliente.models';
import { ClienteListResponseDTO, ClientesService, Page } from './cliente.service';

interface ClientListRow {
  id: number;
  nome: string;
  documento: string;
  tipo: TipoCliente;
  status: StatusCliente;
  contato: string;
}

@Component({
  selector: 'cliente',
  standalone: true,
  templateUrl: './cliente.html',
  styleUrl: './cliente.scss',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    PageHeader,
    SelectModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    SkeletonModule,
    MenuModule,
  ],
})
export class Cliente implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly messageService = inject(MessageService);
  private readonly permissionsService = inject(NgxPermissionsService);

  searchTerm = '';
  selectedTipo: TipoCliente | null = null;
  selectedStatus: StatusCliente | null = null;
  isLoading = false;
  loadError = false;

  readonly tipoOptions = [{ label: 'Todos os tipos', value: null }, ...getTipoClienteOptions()];
  readonly statusOptions = [{ label: 'Todos os status', value: null }, ...getStatusClienteOptions()];

  clients: ClientListRow[] = [];
  backendPage: Page<ClienteListResponseDTO> | null = null;

  rows = 10;
  first = 0;
  activeMenuItems: MenuItem[] = [];

  ngOnInit() {
    const query = this.route.snapshot.queryParamMap;
    const status = query.get('status') as StatusCliente | null;
    const tipo = query.get('tipo') as TipoCliente | null;
    const page = Number(query.get('page'));

    if (status && Object.values(StatusCliente).includes(status)) {
      this.selectedStatus = status;
    }
    if (tipo && Object.values(TipoCliente).includes(tipo)) {
      this.selectedTipo = tipo;
    }
    if (Number.isInteger(page) && page > 0) {
      this.first = (page - 1) * this.rows;
    }

    this.fetchPage();
  }

  get totalRecords() {
    return this.backendPage?.totalElements ?? 0;
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.clients.length, this.totalRecords);
  }

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  get totalPages() {
    return Math.max(1, this.backendPage?.totalPages ?? Math.ceil(this.totalRecords / this.rows));
  }

  get canCreateCliente() {
    return Boolean(this.permissionsService.getPermission('CLIENTE_CRIAR'));
  }

  get canEditCliente() {
    return Boolean(this.permissionsService.getPermission('CLIENTE_EDITAR'));
  }

  get canCreateVehicle() {
    return Boolean(this.permissionsService.getPermission('VEICULO_CRIAR'));
  }

  onSearch() {
    this.first = 0;
    this.syncNonSensitiveQueryState();
    this.fetchPage();
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedTipo = null;
    this.selectedStatus = null;
    this.first = 0;
    this.syncNonSensitiveQueryState();
    this.fetchPage();
  }

  onFilterChange() {
    this.first = 0;
    this.syncNonSensitiveQueryState();
    this.fetchPage();
  }

  goPrev() {
    if (this.first === 0 || this.isLoading) {
      return;
    }
    this.first = Math.max(0, this.first - this.rows);
    this.syncNonSensitiveQueryState();
    this.fetchPage();
  }

  goNext() {
    if (this.first + this.rows >= this.totalRecords || this.isLoading) {
      return;
    }
    this.first += this.rows;
    this.syncNonSensitiveQueryState();
    this.fetchPage();
  }

  cadastrarCliente() {
    if (!this.canCreateCliente) {
      this.warnPermission();
      return;
    }
    this.router.navigate(['/clientes/novo']);
  }

  navigateToEdit(row: ClientListRow) {
    if (!this.canEditCliente) {
      this.warnPermission();
      return;
    }
    this.router.navigate(['/clientes', row.id, 'editar']);
  }

  navigateToAddVeiculo(row: ClientListRow) {
    if (!this.canCreateVehicle) {
      this.warnPermission();
      return;
    }
    this.router.navigate(['/veiculos/cadastro'], { queryParams: { clienteId: row.id } });
  }

  toggleMenu(row: ClientListRow, event: Event, menu: { toggle: (event: Event) => void }) {
    this.activeMenuItems = this.menuItemsFor(row);
    menu.toggle(event);
  }

  menuItemsFor(row: ClientListRow): MenuItem[] {
    const items: MenuItem[] = [];

    if (this.canEditCliente) {
      items.push({
        label: 'Editar cliente',
        icon: 'pi pi-pencil',
        command: () => this.navigateToEdit(row),
      });
    }

    if (this.canCreateVehicle) {
      items.push({
        label: 'Cadastrar veículo',
        icon: 'pi pi-car',
        command: () => this.navigateToAddVeiculo(row),
      });
    }

    return items;
  }

  hasRowActions() {
    return this.canEditCliente || this.canCreateVehicle;
  }

  getTipoClienteLabel(tipo: TipoCliente) {
    return TipoClienteLabels[tipo] || 'Cliente';
  }

  getStatusLabel(status: StatusCliente) {
    return StatusClienteLabels[status] || status;
  }

  getStatusClass(status: StatusCliente) {
    switch (status) {
      case StatusCliente.ATIVO:
        return 'status-badge--success';
      case StatusCliente.BLOQUEADO:
        return 'status-badge--danger';
      default:
        return 'status-badge--neutral';
    }
  }

  private fetchPage() {
    const pageIndex = Math.floor(this.first / this.rows);
    const filters: Record<string, string | number> = {
      page: pageIndex,
      size: this.rows,
      sort: 'nomeCompleto,asc',
    };

    this.applySearchFilter(filters);

    if (this.selectedTipo) {
      filters['tipoCliente'] = this.selectedTipo;
    }
    if (this.selectedStatus) {
      filters['status'] = this.selectedStatus;
    }

    this.isLoading = true;
    this.loadError = false;

    this.clientesService.list(filters).subscribe({
      next: response => {
        this.backendPage = response;
        this.clients = (response.content || []).map(dto => this.mapToRow(dto));
        this.isLoading = false;
      },
      error: error => {
        this.clients = [];
        this.backendPage = null;
        this.isLoading = false;
        this.loadError = true;

        if (error?.status !== 403) {
          this.messageService.add({
            severity: 'error',
            summary: 'Não foi possível carregar os clientes',
            detail: 'Tente novamente. Nenhum dado de outro contexto será exibido.',
          });
        }
      },
    });
  }

  private applySearchFilter(filters: Record<string, string | number>) {
    const term = this.searchTerm.trim();
    if (!term) {
      return;
    }

    const digits = term.replace(/\D/g, '');
    if (digits.length === 11) {
      filters['cpf'] = digits;
      return;
    }
    if (digits.length === 14) {
      filters['cnpj'] = digits;
      return;
    }

    // O contrato atual do backend ainda não possui `q`; nomeCompleto também cobre
    // nome, razão social e nome fantasia na Specification tenant-scoped.
    filters['nomeCompleto'] = term;
  }

  private mapToRow(dto: ClienteListResponseDTO): ClientListRow {
    return {
      id: dto.id,
      nome: dto.displayName || `Cliente #${dto.id}`,
      documento: dto.maskedTaxId || 'Não informado',
      tipo: dto.type,
      status: dto.status,
      contato: dto.primaryContactSummary || '',
    };
  }

  private syncNonSensitiveQueryState() {
    this.router.navigate([], {
      relativeTo: this.route,
      replaceUrl: true,
      queryParams: {
        tipo: this.selectedTipo || null,
        status: this.selectedStatus || null,
        page: this.currentPage > 1 ? this.currentPage : null,
      },
      queryParamsHandling: 'merge',
    });
  }

  private warnPermission() {
    this.messageService.add({
      severity: 'warn',
      summary: 'Acesso restrito',
      detail: 'Seu perfil não possui permissão para esta ação.',
    });
  }
}
