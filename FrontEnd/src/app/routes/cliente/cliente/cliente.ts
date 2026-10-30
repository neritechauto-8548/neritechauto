import { Component, inject, OnInit, ChangeDetectorRef, DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
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
import { ClientesNav } from '../clientes-nav';
import { ContatoClienteResponse, TipoContato } from '../models/cliente.models';
import { forkJoin, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
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
    ClientesNav,
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
  private readonly destroyRef = inject(DestroyRef);
  private readonly searchChanges = new Subject<string>();

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
  // Advanced filters use tenant-scoped server queries.
  advancedOpen = false;
  advancedDocument = '';
  advancedName = '';
  advancedFrom = '';
  advancedTo = '';
  advancedError = '';

  // Browser-only column preferences. Never alter server-side permissions.
  columnsOpen = false;
  readonly columnOptions = [
    { key: 'cliente', label: 'Cliente' }, { key: 'cpf', label: 'CPF/CNPJ' },
    { key: 'categoria', label: 'Categoria' }, { key: 'tipo', label: 'Tipo de pessoa' },
    { key: 'criacao', label: 'Criação' }, { key: 'contato', label: 'Contato' },
    { key: 'veiculos', label: 'Veículos' }, { key: 'situacao', label: 'Situação' },
  ];
  selectedColumns: string[] = ['cliente','cpf','categoria','tipo','criacao'];
  draftColumns: string[] = [];
  columnsSearch = '';
  labelsOpen = false;
  labelsColumns = 3;
  labelsFontSize = 9;
  labelsMargin = 10;
  labelsShowDocument = true;
  labelsWithBorder = true;

  openColumns(): void { this.draftColumns = [...this.selectedColumns]; this.columnsSearch = ''; this.columnsOpen = true; }
  toggleColumn(key: string): void {
    if (key === 'cliente') return;
    this.draftColumns = this.draftColumns.includes(key)
      ? this.draftColumns.filter(value => value !== key) : [...this.draftColumns, key];
  }
  shiftColumn(key: string, delta: number): void {
    const index = this.draftColumns.indexOf(key);
    const next = index + delta;
    if (key === 'cliente' || next < 1 || next >= this.draftColumns.length) return;
    [this.draftColumns[index], this.draftColumns[next]] = [this.draftColumns[next], this.draftColumns[index]];
  }
  restoreColumns(): void { this.draftColumns = ['cliente','cpf','categoria','tipo','criacao']; }
  saveColumns(): void {
    this.selectedColumns = [...this.draftColumns];
    try { window.localStorage.setItem('neritech-clients-column-order', JSON.stringify(this.selectedColumns)); } catch {}
    this.columnsOpen = false;
  }
  get filteredColumnOptions() {
    const term = this.columnsSearch.toLocaleLowerCase('pt-BR').trim();
    return this.columnOptions.filter(col => col.label.toLocaleLowerCase('pt-BR').includes(term));
  }
  columnLabel(key: string): string { return this.columnOptions.find(c => c.key === key)?.label || key; }
  get labelRows() { return this.pagedData; }
  openLabels(): void { this.labelsOpen = true; }
  printLabels(): void {
    if (!this.labelRows.length) return;
    // Isolated print document; escape all customer-supplied text to avoid HTML injection.
    const escape = (value: string) => String(value).replace(/[&<>"']/g, char =>
      ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'} as Record<string,string>)[char]);
    const columns = Math.min(4, Math.max(1, Number(this.labelsColumns) || 3));
    const font = Math.min(18, Math.max(7, Number(this.labelsFontSize) || 9));
    const margin = Math.min(30, Math.max(0, Number(this.labelsMargin) || 10));
    const cells = this.labelRows.map(item =>
      '<article><strong>'+escape(item.nome)+'</strong>' +
      (this.labelsShowDocument && item.cpfCnpj ? '<span>'+escape(this.formatCpfCnpj(item.cpfCnpj))+'</span>' : '') +
      '</article>').join('');
    const markup = '<!doctype html><html lang="pt-BR"><head><meta charset="utf-8"><title>Etiquetas NERITECH AUTO</title>' +
      '<style>@page{size:A4 portrait;margin:'+margin+'mm}body{margin:0;font-family:Arial,sans-serif}' +
      'main{display:grid;grid-template-columns:repeat('+columns+',minmax(0,1fr));gap:3mm}' +
      'article{box-sizing:border-box;min-height:20mm;padding:3mm;border:'+
        (this.labelsWithBorder ? '1px solid #b9c0c9' : '1px solid transparent')+
        ';font-size:'+font+'pt;break-inside:avoid;overflow-wrap:anywhere}' +
      'strong,span{display:block}span{margin-top:2mm;font-size:.9em}</style></head><body><main>'+cells+'</main></body></html>';
    const iframe = document.createElement('iframe');
    iframe.setAttribute('title', 'Impressão de etiquetas');
    iframe.style.position = 'fixed'; iframe.style.width = '0'; iframe.style.height = '0';
    iframe.style.border = '0'; iframe.style.opacity = '0';
    iframe.onload = () => {
      const preview = iframe.contentWindow;
      if (!preview) return;
      // Print only when the isolated document has loaded, then release resources.
      preview.addEventListener('afterprint', () => iframe.remove(), { once: true });
      preview.focus();
      preview.print();
    };
    iframe.srcdoc = markup;
    document.body.appendChild(iframe);
  }
  downloadCsvTemplate(): void {
    const header = 'tipoCliente;nomeCompleto;razaoSocial;nomeFantasia;cpf;cnpj;email;status\r\n';
    const blob = new Blob(['\uFEFF' + header], { type: 'text/csv;charset=utf-8' });
    const objectUrl = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = objectUrl; link.download = 'neritech-modelo-clientes.csv'; link.click();
    setTimeout(() => URL.revokeObjectURL(objectUrl), 0);
  }
  applyAdvancedFilters(): void {
    if (this.advancedFrom && this.advancedTo && this.advancedFrom > this.advancedTo) {
      this.advancedError = 'A data inicial deve ser anterior ou igual à data final.';
      return;
    }
    this.advancedError = '';
    this.advancedOpen = false;
    this.first = 0;
    this.fetchPage();
  }
  resetAdvancedFilters(): void {
    this.advancedDocument = ''; this.advancedName = '';
    this.advancedFrom = ''; this.advancedTo = ''; this.advancedError = '';
    this.selectedTipo = null; this.selectedStatus = null;
    this.first = 0; this.fetchPage();
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
    email: string;
    categoria: string;
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

  onSearchTermChange(value: string): void {
    this.searchChanges.next(value);
  }

  onSearch(): void {
    this.first = 0;
    this.fetchPage();
  }

  atualizar(): void {
    this.fetchPage();
  }

  get canCreateClient(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('CLIENTE_CRIAR');
  }

  get canOpenClient(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('CLIENTE_EDITAR') || !!this.permissionsService.getPermission('GERAL_USUARIO');
  }

  get canOpenVehicles(): boolean {
    return this.isUxPreview || !!this.permissionsService.getPermission('GERAL_USUARIO');
  }

  abrirVeiculos(row: { id?: number; uuid: number | string }): void {
    if (!this.canOpenVehicles) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito', detail: 'Você não tem permissão para consultar veículos.' });
      return;
    }
    const clienteId = row.id ?? row.uuid;
    this.router.navigate(['/veiculo'], { queryParams: { clienteId } });
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
    this.advancedDocument = '';
    this.advancedName = '';
    this.advancedFrom = '';
    this.advancedTo = '';
    this.first = 0;
    this.fetchPage();
  }

  get hasFilters(): boolean {
    return !!this.searchTerm.trim() || !!this.selectedTipo || !!this.selectedStatus ||
      !!this.advancedDocument.trim() || !!this.advancedName.trim() || !!this.advancedFrom || !!this.advancedTo;
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
    if (this.canOpenClient) {
      items.push({ label: this.permissionsService.getPermission('CLIENTE_EDITAR') || this.isUxPreview ? 'Consultar / editar cliente' : 'Consultar cliente',
        routerLink: ['/cliente/editar', row.uuid], queryParams: this.listQueryParams() });
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
    if (!this.canOpenClient) {
      this.messageService.add({ severity: 'warn', summary: 'Acesso restrito', detail: 'Seu perfil não possui permissão para consultar este cadastro.' });
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
    this.searchChanges.pipe(
      map(value => value.trim()),
      debounceTime(350),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef),
    ).subscribe(() => this.onSearch());

    const qp = this.route.snapshot.queryParamMap;
    const statusParam = qp.get('status') as StatusCliente | null;
    const tipoParam = qp.get('tipo') as TipoCliente | null;
    this.searchTerm = qp.get('busca') || '';
    this.advancedDocument = qp.get('doc') || '';
    this.advancedName = qp.get('nome') || '';
    this.advancedFrom = qp.get('de') || '';
    this.advancedTo = qp.get('ate') || '';
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
    try {
      const stored = JSON.parse(window.localStorage.getItem('neritech-clients-column-order') || 'null');
      const valid = new Set(this.columnOptions.map(c => c.key));
      if (Array.isArray(stored) && stored[0] === 'cliente' && stored.every(key => typeof key === 'string' && valid.has(key)) && new Set(stored).size === stored.length) this.selectedColumns = stored;
    } catch { /* Local preferences are optional. */ }
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
      categoria: dto.categoria || '',
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
    if (this.advancedDocument.trim()) filters.cpf = this.advancedDocument.trim();
    if (this.advancedName.trim()) filters.nomeCompleto = this.advancedName.trim();
    if (this.advancedFrom) filters.dataInicio = this.advancedFrom;
    if (this.advancedTo) filters.dataFim = this.advancedTo;

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
        // A UI apresenta a falha sem registrar dados de clientes no console.
        
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
      doc: this.advancedDocument || null,
      nome: this.advancedName || null,
      de: this.advancedFrom || null,
      ate: this.advancedTo || null,
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
