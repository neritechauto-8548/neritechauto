import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { VeiculoService } from './veiculo.service';
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
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    RouterModule
  ],
  providers: [MessageService]
})
export class Veiculo implements OnInit {
  private readonly router = inject(Router);
  private readonly veiculoService = inject(VeiculoService);
  private readonly messageService = inject(MessageService);

  // Estado
  loading = false;
  error: string | null = null;
  searchTerm = '';

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
    this.loadVehicles();
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

    if (!term) return source;

    return source.filter(v =>
      (v.placa || '').toLowerCase().includes(term) ||
      (v.marcaNome || '').toLowerCase().includes(term) ||
      (v.modeloNome || '').toLowerCase().includes(term) ||
      (v.clienteNome || '').toLowerCase().includes(term)
    );
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

  cadastrarVeiculo() {
    this.router.navigate(['/veiculo/cadastro']);
  }

  editarVeiculo(veiculo: VeiculoResponse) {
    this.router.navigate(['/veiculo/editar', veiculo.id]);
  }

  // Helpers
  getStatusBadgeClass(status: StatusVeiculo | string | undefined | null): string {
    if (!status) return 'bg-gray-100 text-gray-700 ring-gray-600/20';

    switch (status) {
      case StatusVeiculo.ATIVO:
      case 'ATIVO': return 'bg-green-100 text-green-700 ring-green-600/20';
      case StatusVeiculo.INATIVO:
      case 'INATIVO': return 'bg-gray-100 text-gray-700 ring-gray-600/20';
      case StatusVeiculo.VENDIDO:
      case 'VENDIDO': return 'bg-blue-100 text-blue-700 ring-blue-600/20';
      case StatusVeiculo.SINISTRO:
      case 'SINISTRO':
      case StatusVeiculo.BLOQUEADO:
      case 'BLOQUEADO': return 'bg-red-100 text-red-700 ring-red-600/20';
      default: return 'bg-gray-100 text-gray-700 ring-gray-600/20';
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
