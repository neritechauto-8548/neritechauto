import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { environment } from '../../../../environments/environment';

import { MenuItem, MessageService } from 'primeng/api';
import { ViewChild } from '@angular/core';
// PrimeNG modules for compatibility
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { SkeletonModule } from 'primeng/skeleton';
import { MenuModule } from 'primeng/menu';
import { DialogModule } from 'primeng/dialog';
import { ClientesService, ClienteResponseDTO, Page } from './cliente.service';
import { CadastroCliente } from '../cadastro-cliente/cadastro-cliente';
import { ContatoClienteResponse, TipoContato } from '../models/cliente.models';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { NgxPermissionsService } from 'ngx-permissions';
import { VeiculoService } from '../../veiculo/veiculo/veiculo.service';
import {
  TipoCliente,
  StatusCliente,
  getTipoClienteOptions,
  getStatusClienteOptions,
  StatusClienteLabels,
  TipoClienteLabels
} from '../models/cliente.models';

@Component({
  selector: 'cliente',
  standalone: true,
  templateUrl: './cliente.html',
  styleUrls: ['./cliente.scss'],
  imports: [
    CommonModule,
    FormsModule,
    // PrimeNG
    SelectModule,
    InputTextModule,
    ButtonModule,
    TagModule,
    TooltipModule,
    ToastModule,
    SkeletonModule,
    MenuModule,
    DialogModule,
    CadastroCliente,
    RouterModule,
  ],
})
export class Cliente implements OnInit {
  readonly isUxPreview = environment.uxPreview;
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly messageService = inject(MessageService);
  private readonly permissionsService = inject(NgxPermissionsService);
  private readonly veiculosService = inject(VeiculoService);

  // Expose enums to template
  readonly TipoCliente = TipoCliente;
  readonly StatusCliente = StatusCliente;

  // Filtros
  searchTerm = '';
  isLoading = false;
  loadError = false;
  loadErrorMessage = 'Tente novamente em instantes.';
  private requestVersion = 0;

  // Menu popup
  activeRow: any = null;
  activeMenuItems: MenuItem[] = [];

  toggleMenu(row: any, event: Event, menu: any) {
    this.activeRow = row;
    this.activeMenuItems = this.menuItemsFor(row);
    menu.toggle(event);
  }
  selectedTipo: TipoCliente | null = null;
  selectedStatus: StatusCliente | null = null;
  sort = 'nomeCompleto,asc';
  readonly pageSizes = [10, 25, 50];
  readonly sortOptions = [
    { label: 'Nome: A–Z', value: 'nomeCompleto,asc' },
    { label: 'Nome: Z–A', value: 'nomeCompleto,desc' },
    { label: 'Cadastro mais recente', value: 'dataCadastro,desc' },
    { label: 'Status', value: 'status,asc' },
  ];

  // Opções para dropdowns
  tipoOptions = [{ label: 'Todos', value: null }, ...getTipoClienteOptions()];
  get tipoOptionsFiltered() {
    return getTipoClienteOptions();
  }

  statusOptions = [{ label: 'Todos', value: null }, ...getStatusClienteOptions()];

  // Labels
  StatusClienteLabels = StatusClienteLabels;
  TipoClienteLabels = TipoClienteLabels;

  // Dados carregados do backend
  clients: {
    uuid: string | number;
    id?: number;
    nome: string;
    cpfCnpj: string;
    tipo: TipoCliente;
    status: StatusCliente;
    contato: string;
    dataCadastro?: string;
    quantidadeVeiculos: number | null;
  }[] = [];
  backendPage: Page<ClienteResponseDTO> | null = null;

  // Paginação
  rows = 10;
  first = 0;

  get totalRecords() {
    return this.backendPage?.totalElements ?? this.filtered.length;
  }
  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }
  jumpPageInput = '';
  isQuickFormOpen = false;

  // Filtrados (aplicado no frontend para dados já carregados)
  get filtered() {
    return this.clients;
  }
  // Removed incorrect pagedData getter for server-side pagination

  // Client-side pagination implementation
  get pagedData() {
    return this.filtered; // Agora a filtered sempre tem apenas a página atual
  }

  get rangeStart() {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }
  get rangeEnd() {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  onSearch() {
    this.first = 0;
    this.fetchPage();
  }

  onFilterChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  onPageSizeChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  onSortChange(): void {
    this.first = 0;
    this.fetchPage();
  }

  limparFiltros(): void {
    this.searchTerm = '';
    this.selectedTipo = null;
    this.selectedStatus = null;
    this.first = 0;
    this.fetchPage();
  }

  get hasFilters(): boolean {
    return !!this.searchTerm.trim() || !!this.selectedTipo || !!this.selectedStatus;
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
    this.fetchPage();
  }

  goNext() {
    if (this.first + this.rows < this.totalRecords) {
      this.first = this.first + this.rows;
      this.fetchPage();
    }
  }

  jumpToPage() {
    const page = Number(this.jumpPageInput);
    if (!isNaN(page) && page >= 1) {
      const maxPage = Math.max(1, Math.ceil(this.totalRecords / this.rows));
      const clamped = Math.min(page, maxPage);
      this.first = (clamped - 1) * this.rows;
      this.fetchPage();
    }
  }

  pendingRoute: any[] | string | null = null;
  onMenuNavigate(item: MenuItem) {
    if (item && item.routerLink) {
      this.pendingRoute = item.routerLink as any;
    }
  }
  onMenuClosed() {
    if (this.pendingRoute) {
      const route = this.pendingRoute;
      this.pendingRoute = null;
      if (Array.isArray(route)) {
        this.router.navigate(route);
      } else if (typeof route === 'string') {
        this.router.navigateByUrl(route);
      }
    }
  }

  menuItemsFor(row: any): MenuItem[] {
    const items: MenuItem[] = [];
    const clienteId = row.id ?? row.uuid;
    if (this.isUxPreview || this.permissionsService.getPermission('CLIENTE_EDITAR')) {
      items.push({ label: 'Visualizar / Editar Cliente', routerLink: ['/cliente/editar', row.uuid], queryParams: this.listQueryParams() });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('GERAL_USUARIO')) {
      items.push({ label: 'Visualizar / Editar Veículos', routerLink: ['/veiculo'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('VEICULO_CRIAR')) {
      items.push({ label: 'Adicionar veículo', routerLink: ['/veiculo/cadastro'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('AGENDAMENTO_CRIAR')) {
      items.push({ label: 'Cadastrar Agendamento / Alerta', routerLink: ['/agendamento/cadastro'], queryParams: { clienteId } });
    }
    if (this.isUxPreview || this.permissionsService.getPermission('OS_INCLUIR')) {
      items.push({ label: 'Cadastrar OS', routerLink: ['/os/cadastro'], queryParams: { clienteId } });
    }
    return items;
  }

  cadastrarCliente() {
    if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_CRIAR')) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Atenção', 
        detail: 'Seu perfil não possui permissão para realizar esta operação.' 
      });
      return;
    }
    this.router.navigate(['/cliente/cadastro'], { queryParams: this.listQueryParams() });
  }

  abrirCadastroRapido() {
    if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_CRIAR')) {
      this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Seu perfil não possui permissão para cadastrar clientes.' });
      return;
    }
    this.isQuickFormOpen = true;
  }

  onQuickClientSaved() {
    this.isQuickFormOpen = false;
    this.messageService.add({ severity: 'success', summary: 'Cliente salvo', detail: 'O cadastro e os dados associados foram salvos.' });
    this.fetchPage();
  }

  onQuickClientCanceled() {
    this.isQuickFormOpen = false;
  }

  navigateToEdit(row: { uuid?: string | number }) {
    if (!this.isUxPreview && !this.permissionsService.getPermission('CLIENTE_EDITAR')) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Atenção', 
        detail: 'Seu perfil não possui permissão para realizar esta operação.' 
      });
      return;
    }
    const id = row?.uuid;
    if (id) {
      this.router.navigate(['/cliente/editar', id], { queryParams: this.listQueryParams() });
    }
  }

  navigateToAddVeiculo(row: { id?: string | number; uuid?: string | number; nome?: string }) {
    if (!this.isUxPreview && !this.permissionsService.getPermission('VEICULO_CRIAR')) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Atenção', 
        detail: 'Seu perfil não possui permissão para realizar esta operação.' 
      });
      return;
    }
    const clienteId = row?.id || row?.uuid;
    if (clienteId) {
      this.router.navigate(['/veiculo/cadastro'], { queryParams: { clienteId } });
    }
  }




  ngOnInit() {
    const qp = this.route.snapshot.queryParamMap;
    const statusParam = qp.get('status') as StatusCliente | null;
    const tipoParam = qp.get('tipo') as TipoCliente | null;
    this.searchTerm = qp.get('busca') || '';
    const page = Number(qp.get('page'));
    const size = Number(qp.get('size'));
    this.first = Number.isInteger(page) && page >= 0 ? page * (this.pageSizes.includes(size) ? size : this.rows) : 0;
    if (this.pageSizes.includes(size)) this.rows = size;
    const sortParam = qp.get('sort');
    if (this.sortOptions.some(option => option.value === sortParam)) this.sort = sortParam!;
    if (statusParam && Object.values(StatusCliente).includes(statusParam)) {
      this.selectedStatus = statusParam;
    }
    if (tipoParam && Object.values(TipoCliente).includes(tipoParam)) {
      this.selectedTipo = tipoParam;
    }
    this.fetchPage();
  }

  private mapToRow(dto: ClienteResponseDTO) {
    const nome = dto.nomeCompleto || dto.nomeFantasia || dto.razaoSocial || '';
    const cpfCnpj = dto.cpf || dto.cnpj || '';
    const tipo = (dto.tipoCliente as TipoCliente) || TipoCliente.PESSOA_FISICA;
    const status = (dto.status as StatusCliente) || StatusCliente.ATIVO;

    return {
      uuid: (dto as any).id ?? (dto as any).uuid,
      id: (dto as any).id,
      nome,
      cpfCnpj,
      tipo,
      status,
      email: dto.email || '',
      contato: '', // Placeholder
      dataCadastro: dto.dataCadastro,
      quantidadeVeiculos: null,
    };
  }

  private fetchPage() {
    const requestId = ++this.requestVersion;
    this.loadError = false;
    // Spring Data Pageable usa paginação 0-indexed: a primeira página é page=0.
    const pageIndex = Math.floor(this.first / this.rows);
    this.isLoading = true;


    const filters: any = { page: pageIndex, size: this.rows, sort: this.sort };
    this.router.navigate([], { relativeTo: this.route, queryParams: this.listQueryParams(), replaceUrl: true });

    // Filtro de busca por nome ou documento
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.trim();
      // A busca é tratada no backend como OR entre nome/razão/nome fantasia/CPF/CNPJ.
      // Isso evita exigir que o mesmo termo exista simultaneamente em nome e documento.
      filters.busca = term;
    }

    // Filtro por tipo de cliente
    if (this.selectedTipo) {
      filters.tipoCliente = this.selectedTipo;
    }

    // Filtro por status
    if (this.selectedStatus) {
      filters.status = this.selectedStatus;
    }

    this.clientesService.list(filters).subscribe({
      next: (res: Page<ClienteResponseDTO>) => {
        if (requestId !== this.requestVersion) return;
        this.backendPage = res;
        this.clients = (res.content || []).map((d: ClienteResponseDTO) => this.mapToRow(d));

        this.isLoading = false;
        this.cdr.detectChanges(); // Forçar atualização da view

        // Enriquecer a página com os contatos e veículos disponíveis nos serviços existentes.
        this.loadContatosForRows(requestId);
      },
      error: (err: any) => {
        if (requestId !== this.requestVersion) return;
        this.isLoading = false;
        this.loadError = true;
        this.loadErrorMessage = err.status === 403 ? 'Seu perfil não tem permissão para consultar esta lista.' : 'Verifique sua conexão e tente novamente.';
        this.backendPage = null;
        this.clients = [];
        console.error('Erro ao carregar clientes:', err);
        
        if (err.status !== 403) {
          this.messageService.add({ 
            severity: 'error', 
            summary: 'Erro', 
            detail: 'Ocorreu um erro ao carregar os dados dos clientes.' 
          });
        }
      },
    });
  }

  private listQueryParams() {
    return {
      busca: this.searchTerm.trim() || null,
      tipo: this.selectedTipo,
      status: this.selectedStatus,
      page: this.first ? Math.floor(this.first / this.rows) : null,
      size: this.rows === 10 ? null : this.rows,
      sort: this.sort === 'nomeCompleto,asc' ? null : this.sort,
    };
  }

  formatDataCadastro(value?: string): string {
    if (!value) return '—';
    const [datePart] = value.split('T');
    const [year, month, day] = datePart.split('-').map(Number);
    const date = new Date(year, month - 1, day);
    return Number.isNaN(date.getTime()) ? '—' : new Intl.DateTimeFormat('pt-BR').format(date);
  }

  private loadContatosForRows(requestId: number) {
    const currentPageRows = this.clients;
    if (!currentPageRows || !currentPageRows.length) return;

    const requests = currentPageRows.map(row => {
      const id = row.id ?? row.uuid;
      return forkJoin({
        contatos: this.clientesService.listarContatos(id).pipe(
          map((res: Page<ContatoClienteResponse>) => res.content || []),
          catchError(() => of([] as ContatoClienteResponse[]))
        ),
        veiculos: this.veiculosService.list(Number(id)).pipe(catchError(() => of(null)))
      }).pipe(map(result => ({ id, ...result })));
    });

    forkJoin(requests).subscribe(results => {
      if (requestId !== this.requestVersion) return;
      const byId = new Map(results.map(r => [r.id, r]));
      this.clients = this.clients.map(c => {
        const key = (c as any).id ?? c.uuid;
        const result = byId.get(key);
        return {
          ...c,
          contato: this.formatContatoLabel((c as any).email, result?.contatos || []),
          quantidadeVeiculos: result?.veiculos?.length ?? null,
        };
      });
    });
  }

  private formatContatoLabel(emailFallback: string, contatos: ContatoClienteResponse[]): string {
    if (!contatos || !contatos.length) return emailFallback;
    // Preferência: CELULAR -> WHATSAPP -> TELEFONE_FIXO -> OUTROS
    const ordered = [
          ...contatos.filter(c => c.tipoContato === TipoContato.CELULAR),
          ...contatos.filter(c => c.tipoContato === TipoContato.WHATSAPP),
          ...contatos.filter(c => c.tipoContato === TipoContato.TELEFONE_FIXO),
          ...contatos.filter(c => c.tipoContato === TipoContato.OUTROS),
        ];

    const chosen = ordered[0] || contatos[0];
    const value = (chosen as any)?.valor ?? (chosen as any)?.contato ?? '';
    
    if (this.isPhoneType(chosen.tipoContato)) {
      return this.applyPhoneMask(value, chosen.tipoContato) || emailFallback;
    }

    return value || emailFallback;
  }

  private isPhoneType(tipo: any): boolean {
    return [TipoContato.TELEFONE_FIXO, TipoContato.CELULAR, TipoContato.WHATSAPP, TipoContato.TELEGRAM].includes(tipo);
  }

  private applyPhoneMask(digits: string, tipo: any): string {
    const d = this.stripNonDigits(digits);
    if (tipo === TipoContato.TELEFONE_FIXO && d.length >= 10) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 6)}-${d.slice(6, 10)}`;
    }
    if (d.length >= 11) {
       return `(${d.slice(0, 2)}) ${d.slice(2, 7)}-${d.slice(7, 11)}`;
    }
    return d; // Retorna apenas os dígitos se não bater com tamanho padrão
  }

  private stripNonDigits(v: string) {
    return (v || '').replace(/\D+/g, '');
  }

  // Helper para obter classe de severidade do badge de status
  getStatusSeverity(status: StatusCliente): 'success' | 'secondary' | 'danger' {
    switch (status) {
      case StatusCliente.ATIVO:
        return 'success';
      case StatusCliente.INATIVO:
        return 'secondary';
      case StatusCliente.BLOQUEADO:
        return 'danger';
      default:
        return 'secondary';
    }
  }

  // Helper para obter ícone do tipo de cliente
  getTipoIcon(tipo: TipoCliente): string {
    return tipo === TipoCliente.PESSOA_FISICA ? 'person' : 'business';
  }

  // Helper para obter label do tipo de cliente
  getTipoClienteLabel(tipo: TipoCliente): string {
    return TipoClienteLabels[tipo];
  }

  // Helper para obter label do status
  getStatusLabel(status: StatusCliente): string {
    return StatusClienteLabels[status];
  }

  // Helper para formatar CPF/CNPJ
  formatCpfCnpj(value: string): string {
    if (!value) return '';
    const clean = value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    if (clean.length === 11) {
      return clean.replace(/(.{3})(.{3})(.{3})(.{2})/, '$1.$2.$3-$4');
    } else if (clean.length === 14) {
      return clean.replace(/(.{2})(.{3})(.{3})(.{4})(.{2})/, '$1.$2.$3/$4-$5');
    }
    return value;
  }
}
