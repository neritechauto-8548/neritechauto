import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { HotToastService } from '@ngxpert/hot-toast';
import { FuncionarioService, FuncionarioResponse } from './funcionario.service';

@Component({
  selector: 'colaboradores',
  standalone: true,
  templateUrl: './colaboradores.html',
  styleUrls: ['./colaboradores.scss'],
  imports: [
    CommonModule, FormsModule, RouterModule,
    MatIconModule, MatMenuModule, MatButtonModule
  ],
})
export class Colaboradores implements OnInit {
  private readonly router    = inject(Router);
  private readonly toast     = inject(HotToastService);
  private readonly service   = inject(FuncionarioService);

  searchTerm   = '';
  statusFilter = '';

  rows  = 10;
  first = 0;
  totalRecords = 0;
  colaboradores: FuncionarioResponse[] = [];

  ngOnInit() { this.loadData(); }

  loadData() {
    const page = Math.floor(this.first / this.rows);
    const params: any = { page, size: this.rows };
    if (this.searchTerm)   params['nomeCompleto'] = this.searchTerm;
    if (this.statusFilter) params['status']       = this.statusFilter;

    this.service.list(params).subscribe({
      next: (data) => {
        this.colaboradores  = data.content ?? [];
        this.totalRecords   = data.totalElements ?? 0;
      },
      error: () => this.toast.error('Erro ao carregar colaboradores'),
    });
  }

  onSearch() { this.first = 0; this.loadData(); }

  goPrev() {
    if (this.first - this.rows >= 0) { this.first -= this.rows; this.loadData(); }
  }
  goNext() {
    if (this.first + this.rows < this.totalRecords) { this.first += this.rows; this.loadData(); }
  }

  get currentPage() { return Math.floor(this.first / this.rows) + 1; }
  get maxPage()     { return Math.max(1, Math.ceil(this.totalRecords / this.rows)); }

  get rangeStart(): number { return this.totalRecords === 0 ? 0 : this.first + 1; }
  get rangeEnd(): number   { return Math.min(this.first + this.rows, this.totalRecords); }

  getInitials(nome: string | undefined): string {
    if (!nome) return '??';
    const parts = nome.trim().split(/\s+/);
    if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
  }

  excluir(id: number) {
    if (!confirm('Deseja realmente excluir este colaborador? Esta ação não pode ser desfeita.')) return;
    this.service.delete(id).subscribe({
      next: () => { this.toast.success('Colaborador excluído'); this.loadData(); },
      error: () => this.toast.error('Erro ao excluir colaborador'),
    });
  }

  getBadgeClass(status: string | undefined): string {
    const base = 'inline-flex items-center px-2 py-0.5 rounded-full text-xs font-semibold';
    const map: Record<string, string> = {
      ATIVO:      `${base} bg-emerald-100 text-emerald-700`,
      INATIVO:    `${base} bg-slate-100 text-slate-600`,
      AFASTADO:   `${base} bg-yellow-100 text-yellow-700`,
      DEMITIDO:   `${base} bg-red-100 text-red-700`,
      APOSENTADO: `${base} bg-blue-100 text-blue-700`,
    };
    return (status ? map[status] : undefined) ?? `${base} bg-slate-100 text-slate-600`;
  }
}
