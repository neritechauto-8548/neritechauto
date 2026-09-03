import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DataTableShell, DataViewState, NeriTechIcon, NeriTechIconName, PageHeader } from '@shared';
import { MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { NgxPermissionsService } from 'ngx-permissions';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';

import {
  OrcamentoListItem,
  OrcamentoListResponse,
  OrcamentoListService,
} from './orcamento-list.service';

interface SelectOption<T> {
  label: string;
  value: T;
}

interface SummaryCard {
  label: string;
  icon: NeriTechIconName;
  hint: string;
}

@Component({
  selector: 'app-orcamento',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PageHeader,
    NeriTechIcon,
    DataTableShell,
    DataViewState,
    InputTextModule,
    SelectModule,
    SkeletonModule,
    ToastModule,
  ],
  templateUrl: './orcamento.html',
  styleUrl: './orcamento.scss',
  providers: [MessageService],
})
export class OrcamentoComponent implements OnInit, OnDestroy {
  private readonly listService = inject(OrcamentoListService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly messageService = inject(MessageService);
  private readonly permissions = inject(NgxPermissionsService);
  private readonly searchChanges = new Subject<string>();
  private readonly destroy$ = new Subject<void>();

  readonly statusOptions: SelectOption<string | null>[] = [
    { label: 'Todos os status', value: null },
    { label: 'Rascunho', value: 'RASCUNHO' },
    { label: 'Enviado', value: 'ENVIADO' },
    { label: 'Aguardando aprovação', value: 'AGUARDANDO_APROVACAO' },
    { label: 'Aprovado', value: 'APROVADO' },
    { label: 'Aprovação parcial', value: 'PARCIAL' },
    { label: 'Recusado', value: 'RECUSADO' },
    { label: 'Expirado', value: 'EXPIRADO' },
    { label: 'Convertido', value: 'CONVERTIDO' },
    { label: 'Cancelado', value: 'CANCELADO' },
  ];
  readonly sortOptions: SelectOption<string>[] = [
    { label: 'Atualizados recentemente', value: 'updatedAt,desc' },
    { label: 'Mais recentes', value: 'createdAt,desc' },
    { label: 'Número crescente', value: 'numero,asc' },
    { label: 'Maior valor', value: 'total,desc' },
  ];
  readonly summaryCards: SummaryCard[] = [
    { label: 'Aguardando aprovação', icon: 'clipboard-list', hint: 'Contrato agregado pendente' },
    { label: 'Prontos para converter', icon: 'receipt', hint: 'Elegibilidade ainda não materializada' },
    { label: 'Expiram em breve', icon: 'calendar', hint: 'Validade canônica pendente' },
    { label: 'Valor aguardando decisão', icon: 'chart-bar', hint: 'Sem cálculo local ou estimado' },
  ];

  searchTerm = '';
  selectedStatus: string | null = null;
  selectedSort = 'updatedAt,desc';
  items: OrcamentoListItem[] = [];
  response: OrcamentoListResponse | null = null;
  isLoading = false;
  loadError = false;
  forbidden = false;
  first = 0;
  readonly rows = 25;

  ngOnInit() {
    const query = this.route.snapshot.queryParamMap;
    const status = query.get('status');
    const sort = query.get('sort');
    const page = Number(query.get('page'));
    if (status && this.statusOptions.some(option => option.value === status)) {
      this.selectedStatus = status;
    }
    if (sort && this.sortOptions.some(option => option.value === sort)) this.selectedSort = sort;
    if (Number.isInteger(page) && page > 0) this.first = (page - 1) * this.rows;

    this.searchChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.first = 0;
      this.load();
    });
    this.load();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get totalRecords() { return this.response?.totalElements ?? 0; }
  get totalPages() { return Math.max(1, this.response?.totalPages ?? 0); }
  get currentPage() { return Math.floor(this.first / this.rows) + 1; }
  get rangeStart() { return this.totalRecords === 0 ? 0 : this.first + 1; }
  get rangeEnd() { return Math.min(this.first + this.items.length, this.totalRecords); }
  get hasActiveFilters() { return Boolean(this.searchTerm.trim() || this.selectedStatus); }
  get canCreateBudget() { return Boolean(this.permissions.getPermission('OS_INCLUIR')); }

  onSearchTermChange(value: string) {
    this.searchChanges.next(value.trim());
  }

  submitSearch() {
    this.first = 0;
    this.load();
  }

  onFilterChange() {
    this.first = 0;
    this.syncSafeQueryState();
    this.load();
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedStatus = null;
    this.first = 0;
    this.syncSafeQueryState();
    this.load();
  }

  goPrev() {
    if (this.first === 0 || this.isLoading) return;
    this.first = Math.max(0, this.first - this.rows);
    this.syncSafeQueryState();
    this.load();
  }

  goNext() {
    if (this.first + this.rows >= this.totalRecords || this.isLoading) return;
    this.first += this.rows;
    this.syncSafeQueryState();
    this.load();
  }

  novo() {
    if (!this.canCreateBudget) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Acesso restrito',
        detail: 'Seu perfil não possui permissão para criar orçamentos.',
      });
      return;
    }
    this.router.navigate(['/orcamentos/novo']);
  }

  abrir(item: OrcamentoListItem) {
    this.router.navigate(['/orcamentos', item.id], {
      state: { returnUrl: this.router.url },
    });
  }

  statusLabel(status: string) {
    return this.statusOptions.find(option => option.value === status)?.label
      || this.humanize(status);
  }

  statusClass(status: string) {
    switch (status) {
      case 'APROVADO':
      case 'CONVERTIDO':
        return 'status-badge--success';
      case 'RECUSADO':
      case 'CANCELADO':
      case 'EXPIRADO':
        return 'status-badge--danger';
      case 'ENVIADO':
      case 'AGUARDANDO_APROVACAO':
      case 'PARCIAL':
        return 'status-badge--warning';
      default:
        return 'status-badge--neutral';
    }
  }

  nextActionLabel(action: string) {
    const labels: Record<string, string> = {
      CONTINUAR_EDICAO: 'Continuar edição',
      ACOMPANHAR_APROVACAO: 'Acompanhar aprovação',
      CONVERTER_EM_OS: 'Converter em OS',
      REVISAR_DECISAO: 'Revisar decisão',
      REGISTRAR_FOLLOW_UP: 'Registrar follow-up',
      REVALIDAR: 'Revalidar proposta',
      ABRIR_OS: 'Abrir OS',
      CONSULTAR_HISTORICO: 'Consultar histórico',
      REVISAR_DETALHES: 'Revisar detalhes',
    };
    return labels[action] || this.humanize(action);
  }

  formatCurrency(money: OrcamentoListItem['total']) {
    const amount = Number(money?.amount ?? 0);
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: money?.currency || 'BRL' }).format(amount);
  }

  private load() {
    this.isLoading = true;
    this.loadError = false;
    this.forbidden = false;
    const page = Math.floor(this.first / this.rows);
    this.listService.list({
      q: this.searchTerm.trim() || undefined,
      status: this.selectedStatus || undefined,
      page,
      size: this.rows,
      sort: this.selectedSort,
    }).subscribe({
      next: response => {
        this.response = response;
        this.items = response.items || [];
        this.isLoading = false;
      },
      error: error => {
        this.response = null;
        this.items = [];
        this.isLoading = false;
        this.loadError = true;
        this.forbidden = error?.status === 403;
        if (!this.forbidden) {
          this.messageService.add({
            severity: 'error',
            summary: 'Não foi possível carregar os orçamentos',
            detail: 'Os filtros foram preservados. Tente novamente.',
          });
        }
      },
    });
  }

  private syncSafeQueryState() {
    this.router.navigate([], {
      relativeTo: this.route,
      replaceUrl: true,
      queryParams: {
        status: this.selectedStatus || null,
        sort: this.selectedSort !== 'updatedAt,desc' ? this.selectedSort : null,
        page: this.currentPage > 1 ? this.currentPage : null,
      },
      queryParamsHandling: 'merge',
    });
  }

  private humanize(value: string) {
    return value.toLowerCase().replaceAll('_', ' ').replace(/^./, char => char.toUpperCase());
  }
}
