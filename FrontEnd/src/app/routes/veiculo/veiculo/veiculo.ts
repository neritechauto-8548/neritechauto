import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { MenuItem, MessageService } from 'primeng/api';
import { SkeletonModule } from 'primeng/skeleton';
import { MenuModule } from 'primeng/menu';
import { NgxPermissionsService } from 'ngx-permissions';

import { VeiculoService } from './veiculo.service';
import { ClientesNav } from '../../cliente/clientes-nav';
import { CadastroVeiculo } from '../cadastro-veiculo/cadastro-veiculo';
import { environment } from '@env/environment';
import { VeiculoResponse, StatusVeiculo } from '../models/veiculo.models';

@Component({
  selector: 'veiculo',
  standalone: true,
  templateUrl: './veiculo.html',
  styleUrls: ['./veiculo.scss'],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    TooltipModule,
    ToastModule,
    SkeletonModule,
    MenuModule,
    RouterModule,
    ClientesNav,
    CadastroVeiculo,
  ],
  providers: [MessageService]
})
export class Veiculo implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly veiculoService = inject(VeiculoService);
  private readonly messageService = inject(MessageService);
  private readonly permissionsService = inject(NgxPermissionsService);

  // Estado
  loading = false;
  error: string | null = null;
  searchTerm = '';
  readonly isUxPreview = environment.uxPreview;
  filterOpen = false;
  cadastroOpen = false;
  draftCliente = '';
  draftMarca = '';
  draftModelo = '';
  draftPlaca = '';
  filterCliente = '';
  filterMarca = '';
  filterModelo = '';
  filterPlaca = '';
  get canCreateVehicle(): boolean { return this.isUxPreview || !!this.permissionsService.getPermission('VEICULO_CRIAR'); }
  openCreate(): void {
    if (this.canCreateVehicle) this.cadastroOpen = true;
  }
  onCreated(): void { this.cadastroOpen = false; this.loadVehicles(this.filterByCustomerId); }
  applyFilter(): void {
    this.filterCliente = this.draftCliente.trim().toLocaleLowerCase('pt-BR');
    this.filterMarca = this.draftMarca.trim().toLocaleLowerCase('pt-BR');
    this.filterModelo = this.draftModelo.trim().toLocaleLowerCase('pt-BR');
    this.filterPlaca = this.draftPlaca.trim().toLocaleLowerCase('pt-BR');
    this.first = 0; this.filterOpen = false;
  }
  resetFilters(): void {
    this.draftCliente = this.draftMarca = this.draftModelo = this.draftPlaca = '';
    this.filterCliente = this.filterMarca = this.filterModelo = this.filterPlaca = '';
    this.first = 0;
  }
  filterByCustomerId?: number;

  // Dados
  vehicles: VeiculoResponse[] = [];

  // Paginação
  rows = 10;
  first = 0;

  get totalRecords() {
    return this.filtered.length;
  }

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  jumpPageInput = '';

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  ngOnInit() {
    const clienteIdParam = this.route.snapshot.queryParamMap.get('clienteId');
    const clienteId = clienteIdParam ? Number(clienteIdParam) : null;
    this.filterByCustomerId = Number.isFinite(clienteId) && clienteId !== null ? clienteId : undefined;
    this.loadVehicles(this.filterByCustomerId);
  }

  loadVehicles(clienteId?: number) {
    this.loading = true;
    this.error = null;
    this.veiculoService.list(clienteId).subscribe({
      next: (res: any) => {
        let list: VeiculoResponse[] = [];
        if (Array.isArray(res)) {
          list = res;
        } else if (res && Array.isArray(res.content)) {
          list = res.content;
        } else if (res && Array.isArray(res.items)) {
          list = res.items;
        } else if (res && res.data && Array.isArray(res.data)) {
          list = res.data;
        }
        this.vehicles = list;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar veículos:', err);
        this.error = 'Erro ao carregar veículos. Tente novamente.';
        this.loading = false;
      }
    });
  }

  // Filtrados
  get filtered() {
    const term = this.searchTerm.trim().toLowerCase();
    const source = Array.isArray(this.vehicles) ? this.vehicles : [];

    return source.filter(v => {
      const fields = [v.placa, v.marcaNome, v.modeloNome, v.clienteNome, v.chassi].map(x => String(x || '').toLocaleLowerCase('pt-BR'));
      const keyword = !term || fields.some(value => value.includes(term));
      return keyword && (!this.filterCliente || fields[3].includes(this.filterCliente)) &&
        (!this.filterMarca || fields[1].includes(this.filterMarca)) &&
        (!this.filterModelo || fields[2].includes(this.filterModelo)) &&
        (!this.filterPlaca || fields[0].includes(this.filterPlaca));
    });
  }

  get pagedData() {
    const data = Array.isArray(this.filtered) ? this.filtered : [];
    return data.slice(this.first, this.first + this.rows);
  }

  onSearch() {
    this.first = 0;
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first = this.first + this.rows;
    }
  }

  jumpToPage() {
    const page = Number(this.jumpPageInput);
    if (!isNaN(page) && page >= 1) {
      const maxPage = Math.max(1, Math.ceil(this.totalRecords / this.rows));
      const clamped = Math.min(page, maxPage);
      this.first = (clamped - 1) * this.rows;
    }
  }

  // Menu popup
  activeRow: any = null;
  activeMenuItems: MenuItem[] = [];

  toggleMenu(row: any, event: Event, menu: any) {
    this.currentVeiculo = row;
    this.activeMenuItems = this.menuItemsFor(row);
    menu.toggle(event);
  }

  menuItemsFor(row: any): MenuItem[] {
    const items: MenuItem[] = [];
    if (this.permissionsService.getPermission('VEICULO_EDITAR')) {
      items.push({
        label: 'Editar Veículo',
        icon: 'pi pi-pencil',
        routerLink: ['/veiculo/editar', row.id]
      });
    }
    items.push({
      label: 'Visualizar Cliente',
      icon: 'pi pi-user',
      routerLink: ['/cliente/editar', row.clienteId]
    });
    items.push({
      label: 'Nova OS',
      icon: 'pi pi-file-edit',
      routerLink: ['/os/cadastro'],
      queryParams: { veiculoId: row.id }
    });
    return items;
  }

  cadastrarVeiculo() {
    if (!this.canCreateVehicle) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.'
      });
      return;
    }
    this.openCreate();
  }

  editarVeiculo(veiculo: VeiculoResponse) {
    if (!this.permissionsService.getPermission('VEICULO_EDITAR')) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Atenção',
        detail: 'Seu perfil não possui permissão para realizar esta operação.'
      });
      return;
    }
    this.router.navigate(['/veiculo/editar', veiculo.id]);
  }

  // Helpers
  getStatusBadgeClass(status: StatusVeiculo | string | undefined | null): string {
    if (!status) return 'nt-badge nt-badge--neutral';

    switch (status) {
      case StatusVeiculo.ATIVO:
      case 'ATIVO': return 'nt-badge nt-badge--success';
      case StatusVeiculo.INATIVO:
      case 'INATIVO': return 'nt-badge nt-badge--neutral';
      case StatusVeiculo.VENDIDO:
      case 'VENDIDO': return 'nt-badge nt-badge--info';
      case StatusVeiculo.SINISTRO:
      case 'SINISTRO':
      case StatusVeiculo.BLOQUEADO:
      case 'BLOQUEADO': return 'nt-badge nt-badge--danger';
      default: return 'nt-badge nt-badge--neutral';
    }
  }

  formatPlaca(placa: string): string {
    return placa || '';
  }

  // Menu helper
  currentVeiculo: VeiculoResponse | null = null;
  setVeiculo(veiculo: VeiculoResponse) {
    this.currentVeiculo = veiculo;
  }
}
