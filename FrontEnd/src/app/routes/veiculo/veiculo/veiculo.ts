import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PageHeader } from '@shared';
import { MenuItem, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { NgxPermissionsService } from 'ngx-permissions';

import {
  StatusVeiculo,
  StatusVeiculoLabels,
  VeiculoResponse,
  getStatusVeiculoOptions,
} from '../models/veiculo.models';
import { VeiculoService } from './veiculo.service';

@Component({
  selector: 'veiculo',
  standalone: true,
  templateUrl: './veiculo.html',
  styleUrls: ['./veiculo.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    InputTextModule,
    ButtonModule,
    SelectModule,
    ToastModule,
    SkeletonModule,
    MenuModule,
  ],
  providers: [MessageService],
})
export class Veiculo implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly veiculoService = inject(VeiculoService);
  private readonly messageService = inject(MessageService);
  private readonly permissionsService = inject(NgxPermissionsService);

  readonly statusOptions = [{ label: 'Todos os status', value: null }, ...getStatusVeiculoOptions()];

  loading = false;
  loadError = false;
  searchTerm = '';
  selectedStatus: StatusVeiculo | null = null;
  vehicles: VeiculoResponse[] = [];
  rows = 10;
  first = 0;
  activeMenuItems: MenuItem[] = [];

  ngOnInit() {
    const query = this.route.snapshot.queryParamMap;
    const status = query.get('status') as StatusVeiculo | null;
    const page = Number(query.get('page'));

    if (status && Object.values(StatusVeiculo).includes(status)) this.selectedStatus = status;
    if (Number.isInteger(page) && page > 0) this.first = (page - 1) * this.rows;
    this.loadVehicles();
  }

  get canCreateVehicle() { return Boolean(this.permissionsService.getPermission('VEICULO_CRIAR')); }
  get canEditVehicle() { return Boolean(this.permissionsService.getPermission('VEICULO_EDITAR')); }
  get canCreateBudget() { return Boolean(this.permissionsService.getPermission('OS_INCLUIR')); }
  get canSchedule() { return Boolean(this.permissionsService.getPermission('GERAL_AGENDAMENTO_EDITAR')); }

  get filtered() {
    const term = this.normalizeSearch(this.searchTerm);
    return this.vehicles.filter(vehicle => {
      if (this.selectedStatus && vehicle.status !== this.selectedStatus) return false;
      if (!term) return true;
      return [vehicle.placa, vehicle.marcaNome, vehicle.modeloNome, vehicle.clienteNome]
        .filter(Boolean)
        .some(value => this.normalizeSearch(String(value)).includes(term));
    });
  }

  get pagedData() { return this.filtered.slice(this.first, this.first + this.rows); }
  get totalRecords() { return this.filtered.length; }
  get rangeStart() { return this.totalRecords === 0 ? 0 : this.first + 1; }
  get rangeEnd() { return Math.min(this.first + this.rows, this.totalRecords); }
  get currentPage() { return Math.floor(this.first / this.rows) + 1; }
  get totalPages() { return Math.max(1, Math.ceil(this.totalRecords / this.rows)); }
  get hasActiveFilters() { return Boolean(this.searchTerm.trim() || this.selectedStatus); }

  loadVehicles() {
    this.loading = true;
    this.loadError = false;
    this.veiculoService.list().subscribe({
      next: response => {
        this.vehicles = Array.isArray(response) ? response : [];
        this.loading = false;
        this.clampPage();
      },
      error: () => {
        this.vehicles = [];
        this.loading = false;
        this.loadError = true;
        this.messageService.add({
          severity: 'error',
          summary: 'Não foi possível carregar os veículos',
          detail: 'Tente novamente. A consulta foi interrompida sem simular dados.',
        });
      },
    });
  }

  onSearch() {
    this.first = 0;
    this.syncNonSensitiveQueryState();
  }

  onFilterChange() {
    this.first = 0;
    this.syncNonSensitiveQueryState();
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedStatus = null;
    this.first = 0;
    this.syncNonSensitiveQueryState();
  }

  goPrev() {
    if (this.first === 0 || this.loading) return;
    this.first = Math.max(0, this.first - this.rows);
    this.syncNonSensitiveQueryState();
  }

  goNext() {
    if (this.first + this.rows >= this.totalRecords || this.loading) return;
    this.first += this.rows;
    this.syncNonSensitiveQueryState();
  }

  cadastrarVeiculo() {
    if (!this.canCreateVehicle) return this.warnPermission();
    this.router.navigate(['/veiculos/novo']);
  }

  editarVeiculo(vehicle: VeiculoResponse) {
    if (!this.canEditVehicle) return this.warnPermission();
    this.router.navigate(['/veiculos', vehicle.id, 'editar']);
  }

  abrirPassaporte(vehicle: VeiculoResponse) {
    this.router.navigate(['/veiculos', vehicle.id]);
  }

  abrirCliente(vehicle: VeiculoResponse) {
    if (!vehicle.clienteId) return;
    this.router.navigate(['/clientes', vehicle.clienteId]);
  }

  criarOrcamento(vehicle: VeiculoResponse) {
    if (!this.canCreateBudget || !vehicle.clienteId) return this.warnPermission();
    this.router.navigate(['/orcamentos/novo'], {
      queryParams: { clienteId: vehicle.clienteId, veiculoId: vehicle.id },
    });
  }

  agendar(vehicle: VeiculoResponse) {
    if (!this.canSchedule || !vehicle.clienteId) return this.warnPermission();
    this.router.navigate(['/agenda/novo'], {
      queryParams: { clienteId: vehicle.clienteId, veiculoId: vehicle.id },
    });
  }

  toggleMenu(vehicle: VeiculoResponse, event: Event, menu: { toggle: (event: Event) => void }) {
    this.activeMenuItems = this.menuItemsFor(vehicle);
    menu.toggle(event);
  }

  menuItemsFor(vehicle: VeiculoResponse): MenuItem[] {
    const items: MenuItem[] = [
      {
        label: 'Abrir passaporte',
        icon: 'pi pi-id-card',
        command: () => this.abrirPassaporte(vehicle),
      },
    ];
    if (this.canEditVehicle) {
      items.push({ label: 'Editar veículo', icon: 'pi pi-pencil', command: () => this.editarVeiculo(vehicle) });
    }
    if (vehicle.clienteId) {
      items.push({ label: 'Abrir cliente', icon: 'pi pi-user', command: () => this.abrirCliente(vehicle) });
    }
    if (this.canCreateBudget && vehicle.clienteId && vehicle.status === StatusVeiculo.ATIVO) {
      items.push({ label: 'Novo orçamento', icon: 'pi pi-file-edit', command: () => this.criarOrcamento(vehicle) });
    }
    if (this.canSchedule && vehicle.clienteId && vehicle.status === StatusVeiculo.ATIVO) {
      items.push({ label: 'Agendar serviço', icon: 'pi pi-calendar', command: () => this.agendar(vehicle) });
    }
    return items;
  }

  vehicleName(vehicle: VeiculoResponse) {
    const name = [vehicle.marcaNome, vehicle.modeloNome].filter(Boolean).join(' ');
    return name || `Veículo #${vehicle.id}`;
  }

  yearLabel(vehicle: VeiculoResponse) {
    if (vehicle.anoFabricacao && vehicle.anoModelo) return `${vehicle.anoFabricacao}/${vehicle.anoModelo}`;
    return String(vehicle.anoModelo || vehicle.anoFabricacao || 'Não informado');
  }

  odometerLabel(value?: number) {
    return value == null ? 'Não informado' : `${new Intl.NumberFormat('pt-BR').format(value)} km`;
  }

  statusLabel(status?: StatusVeiculo) {
    return status ? StatusVeiculoLabels[status] || status : 'Não informado';
  }

  statusClass(status?: StatusVeiculo) {
    switch (status) {
      case StatusVeiculo.ATIVO:
        return 'status-badge--success';
      case StatusVeiculo.BLOQUEADO:
      case StatusVeiculo.SINISTRO:
        return 'status-badge--danger';
      case StatusVeiculo.VENDIDO:
        return 'status-badge--info';
      default:
        return 'status-badge--neutral';
    }
  }

  private normalizeSearch(value: string) {
    return value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/[\s-]/g, '').toLowerCase();
  }

  private clampPage() {
    const lastPageStart = Math.max(0, (this.totalPages - 1) * this.rows);
    if (this.first > lastPageStart) this.first = lastPageStart;
  }

  private syncNonSensitiveQueryState() {
    this.router.navigate([], {
      relativeTo: this.route,
      replaceUrl: true,
      queryParams: {
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
