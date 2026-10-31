import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { HotToastService } from '@ngxpert/hot-toast';
import { FuncionarioService, FuncionarioResponse } from './funcionario.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { empresaIdAtual } from '@shared/utils/empresa-context';

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
  private readonly storage = inject(LocalStorageService);

  // Preferências visuais locais por empresa; o campo de identificação é fixo.
  columnPanelOpen = false;
  readonly columnOptions = [
    { key: 'matricula', label: 'Nº Registro' },
    { key: 'cargo', label: 'Função' },
    { key: 'status', label: 'Situação' },
  ];
  visibleColumns: Record<string, boolean> = { matricula: true, cargo: true, status: true };
  private get columnsStorageKey(): string {
    return 'neritech.funcionarios.colunas.' + String(empresaIdAtual(this.storage) ?? 'sem-empresa');
  }

  private restoreColumns(): void {
    try {
      const saved = JSON.parse(localStorage.getItem(this.columnsStorageKey) || '{}');
      for (const option of this.columnOptions) {
        if (typeof saved[option.key] === 'boolean') this.visibleColumns[option.key] = saved[option.key];
      }
    } catch { this.visibleColumns = { matricula: true, cargo: true, status: true }; }
  }

  toggleColumn(key: string, visible: boolean): void {
    if (!this.columnOptions.some(option => option.key === key)) return;
    this.visibleColumns = { ...this.visibleColumns, [key]: visible };
  }

  saveColumns(): void {
    try {
      localStorage.setItem(this.columnsStorageKey, JSON.stringify(this.visibleColumns));
      this.columnPanelOpen = false;
      this.toast.success('Colunas atualizadas');
    } catch {
      this.toast.error('Não foi possível salvar as preferências de colunas.');
    }
  }

  restoreDefaultColumns(): void {
    this.visibleColumns = { matricula: true, cargo: true, status: true };
  }

  get visibleColumnCount(): number {
    return 2 + this.columnOptions.filter(option => this.visibleColumns[option.key]).length;
  }

  searchTerm   = '';
  statusFilter = '';

  rows  = 20;
  first = 0;
  totalRecords = 0;
  colaboradores: FuncionarioResponse[] = [];
  loading = false;
  loadError = '';

  ngOnInit() { this.restoreColumns(); this.loadData(); }

  loadData() {
    this.loading = true;
    this.loadError = '';
    const page = Math.floor(this.first / this.rows);
    const params: any = { page, size: this.rows };
    if (this.searchTerm)   params['nomeCompleto'] = this.searchTerm;
    if (this.statusFilter) params['status']       = this.statusFilter;

    this.service.list(params).subscribe({
      next: (data) => {
        this.colaboradores  = data.content ?? [];
        this.totalRecords   = data.totalElements ?? 0;
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        this.colaboradores = [];
        this.loadError = error?.status === 403 ? 'Você não tem permissão para consultar funcionários.' : 'Não foi possível carregar os funcionários.';
      },
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
    if (!confirm('Deseja inativar este colaborador? Os históricos de OS, agenda e comissões serão preservados.')) return;
    this.service.delete(id).subscribe({
      next: () => { this.toast.success('Colaborador inativado'); this.loadData(); },
      error: (error) => this.toast.error(error?.status === 409 ? 'Conflito ao inativar colaborador' : 'Erro ao inativar colaborador'),
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
