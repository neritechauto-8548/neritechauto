import { Component, inject, OnInit, OnDestroy } from '@angular/core';
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
import { Subscription } from 'rxjs';
import { CadastroColaborador } from './cadastro-colaborador/cadastro-colaborador';

@Component({
  selector: 'colaboradores',
  standalone: true,
  templateUrl: './colaboradores.html',
  styleUrls: ['./colaboradores.scss'],
  imports: [
    CommonModule, FormsModule, RouterModule,
    MatIconModule, MatMenuModule, MatButtonModule, CadastroColaborador
  ],
})
export class Colaboradores implements OnInit, OnDestroy {
  private readonly router    = inject(Router);
  private readonly toast     = inject(HotToastService);
  private readonly service   = inject(FuncionarioService);
  private readonly storage = inject(LocalStorageService);


  // As colunas e sua ordem são uma preferência visual local, isolada por empresa.
  employeeDrawerOpen = false;
  drawerEmployeeId?: number;
  columnPanelOpen = false;
  filterDrawerOpen = false;
  readonly columnOptions = [
    { key: 'sexo', label: 'Gênero' },
    { key: 'matricula', label: 'Nº Registro' },
    { key: 'cargo', label: 'Função' },
    { key: 'status', label: 'Situação' }
  ];
  selectedColumnKeys: string[] = ['sexo', 'matricula', 'cargo'];
  draftColumnKeys: string[] = [];
  get selectedColumns() {
    return this.selectedColumnKeys.map(key => this.columnOptions.find(option => option.key === key))
      .filter((option): option is { key: string; label: string } => !!option);
  }
  get visibleColumnCount() { return this.selectedColumnKeys.length + 2; }

  private get columnsStorageKey(): string {
    return 'neritech.funcionarios.colunas.' + String(empresaIdAtual(this.storage) ?? 'sem-empresa');
  }

  private restoreColumns(): void {
    try {
      const saved: unknown = JSON.parse(localStorage.getItem(this.columnsStorageKey) || 'null');
      if (Array.isArray(saved) && saved.every(key => typeof key === 'string')) {
        const allowed = saved.filter((key: string) => this.columnOptions.some(option => option.key === key));
        this.selectedColumnKeys = Array.from(new Set(allowed));
      } else if (saved && typeof saved === 'object') {
        // Compatibilidade com preferências publicadas no primeiro lote.
        const old = saved as Record<string, unknown>;
        this.selectedColumnKeys = ['sexo', 'matricula', 'cargo', 'status']
          .filter(key => key === 'sexo' || old[key] === true);
      }
    } catch {
      this.selectedColumnKeys = ['sexo', 'matricula', 'cargo'];
    }
  }

  openColumnPanel(): void {
    this.draftColumnKeys = [...this.selectedColumnKeys];
    this.columnPanelOpen = true;
  }

  isColumnSelected(key: string): boolean { return this.draftColumnKeys.includes(key); }
  toggleColumn(key: string, selected: boolean): void {
    if (!this.columnOptions.some(option => option.key === key)) return;
    this.draftColumnKeys = selected
      ? [...this.draftColumnKeys.filter(k => k !== key), key]
      : this.draftColumnKeys.filter(k => k !== key);
  }

  moveColumn(key: string, distance: number): void {
    const idx = this.draftColumnKeys.indexOf(key);
    const target = idx + distance;
    if (idx < 0 || target < 0 || target >= this.draftColumnKeys.length) return;
    const reordered = [...this.draftColumnKeys];
    [reordered[idx], reordered[target]] = [reordered[target], reordered[idx]];
    this.draftColumnKeys = reordered;
  }

  restoreDefaultColumns(): void { this.draftColumnKeys = ['sexo', 'matricula', 'cargo']; }

  saveColumns(): void {
    try {
      localStorage.setItem(this.columnsStorageKey, JSON.stringify(this.draftColumnKeys));
      this.selectedColumnKeys = [...this.draftColumnKeys];
      this.columnPanelOpen = false;
      this.toast.success('Colunas atualizadas');
    } catch {
      this.toast.error('Não foi possível salvar as preferências de colunas.');
    }
  }

  openEmployeeDrawer(employeeId?: number): void {
    this.drawerEmployeeId = employeeId;
    this.employeeDrawerOpen = true;
  }

  closeEmployeeDrawer(saved: boolean): void {
    this.employeeDrawerOpen = false;
    this.drawerEmployeeId = undefined;
    if (saved) this.loadData();
  }

  searchTerm = '';
  filters = { nome: '', funcao: '', registro: '', status: '' };
  draftFilters = { nome: '', funcao: '', registro: '', status: '' };
  readonly statuses = [
    { value: '', label: 'Todas as situações' },
    { value: 'ATIVO', label: 'Ativo' },
    { value: 'INATIVO', label: 'Inativo' },
    { value: 'AFASTADO', label: 'Afastado' },
    { value: 'DEMITIDO', label: 'Demitido' },
    { value: 'APOSENTADO', label: 'Aposentado' }
  ];
  get activeFilterCount(): number {
    return Object.values(this.filters).filter(value => !!value).length;
  }

  rows = 20;
  first = 0;
  totalRecords = 0;
  colaboradores: FuncionarioResponse[] = [];
  loading = false;
  loadError = '';
  sortField = 'nomeCompleto';
  sortDirection: 'asc' | 'desc' = 'asc';
  private listSubscription?: Subscription;

  ngOnInit(): void { this.restoreColumns(); this.loadData(); }
  ngOnDestroy(): void { this.listSubscription?.unsubscribe(); }

  loadData(): void {
    this.listSubscription?.unsubscribe();
    this.loading = true;
    this.loadError = '';
    const params: Record<string, string | number> = {
      page: Math.floor(this.first / this.rows),
      size: this.rows,
      sort: this.sortField + ',' + this.sortDirection
    };
    if (this.searchTerm.trim()) params['nomeCompleto'] = this.searchTerm.trim();
    if (this.filters.nome) params['nome'] = this.filters.nome;
    if (this.filters.funcao) params['funcao'] = this.filters.funcao;
    if (this.filters.registro) params['registro'] = this.filters.registro;
    if (this.filters.status) params['status'] = this.filters.status;

    this.listSubscription = this.service.list(params).subscribe({
      next: data => {
        this.colaboradores = data.content ?? [];
        this.totalRecords = data.totalElements ?? 0;
        this.loading = false;
      },
      error: error => {
        this.loading = false;
        this.colaboradores = [];
        this.totalRecords = 0;
        this.loadError = error?.status === 403
          ? 'Você não tem permissão para consultar funcionários.'
          : 'Não foi possível carregar os funcionários.';
      }
    });
  }

  onSearch(): void { this.first = 0; this.loadData(); }
  openFilters(): void {
    this.draftFilters = { ...this.filters };
    this.filterDrawerOpen = true;
  }
  closeFilters(): void { this.filterDrawerOpen = false; }
  applyFilters(): void {
    this.filters = {
      nome: this.draftFilters.nome.trim(),
      funcao: this.draftFilters.funcao.trim(),
      registro: this.draftFilters.registro.trim(),
      status: this.draftFilters.status
    };
    this.filterDrawerOpen = false;
    this.first = 0;
    this.loadData();
  }
  resetFilters(): void {
    this.filters = { nome: '', funcao: '', registro: '', status: '' };
    this.draftFilters = { ...this.filters };
    this.first = 0;
    this.loadData();
  }

  changeSort(field: 'nomeCompleto' | 'sexo' | 'matricula' | 'status'): void {
    if (this.sortField === field) this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    else { this.sortField = field; this.sortDirection = 'asc'; }
    this.first = 0;
    this.loadData();
  }

  goPrev(): void {
    if (this.first >= this.rows) { this.first -= this.rows; this.loadData(); }
  }
  goNext(): void {
    if (this.first + this.rows < this.totalRecords) { this.first += this.rows; this.loadData(); }
  }

  get currentPage() { return Math.floor(this.first / this.rows) + 1; }
  get maxPage() { return Math.max(1, Math.ceil(this.totalRecords / this.rows)); }
  get rangeStart(): number { return this.totalRecords === 0 ? 0 : this.first + 1; }
  get rangeEnd(): number { return Math.min(this.first + this.rows, this.totalRecords); }

  genderLabel(sexo: string | undefined): string {
    return ({ M: 'Masculino', F: 'Feminino', O: 'Outro' } as Record<string, string>)[sexo ?? '']
      || 'Não informado';
  }

  exportCurrentPage(): void {
    if (!this.colaboradores.length) return;
    // Exporta exclusivamente o resultado da página carregada, sem sugerir relatório completo.
    const header = ['Funcionário', 'Gênero', 'Nº Registro', 'Função', 'Situação'];
    const rows = this.colaboradores.map(row => [
      row.nomeCompleto, this.genderLabel(row.sexo), row.matricula,
      row.cargoNome || 'Não informado', row.status || 'Não informado'
    ]);
    const safeCsvField = (value: unknown) => {
      let text = String(value ?? '');
      if (/^[\s]*[=+\-@\t\r]/.test(text)) text = "'" + text;
      return '"' + text.replace(/"/g, '""') + '"';
    };
    const csv = [header, ...rows].map(values => values.map(safeCsvField).join(';')).join('\r\n');
    const href = URL.createObjectURL(new Blob(['\uFEFF', csv], { type: 'text/csv;charset=utf-8' }));
    const a = document.createElement('a');
    a.href = href;
    a.download = 'neritech-funcionarios-pagina-' + this.currentPage + '.csv';
    a.click();
    URL.revokeObjectURL(href);
  }

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
