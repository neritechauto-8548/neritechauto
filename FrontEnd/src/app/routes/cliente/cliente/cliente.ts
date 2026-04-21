import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { MenuItem, MessageService } from 'primeng/api';
import { ViewChild } from '@angular/core';
// PrimeNG modules for compatibility
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ClientesService, ClienteResponseDTO, Page } from './cliente.service';
import { ContatoClienteResponse, TipoContato } from '../models/cliente.models';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { LocalStorageService } from '@shared/services/storage.service';
import { ConfirmationService } from '@shared/services/confirmation.service';
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
    // Material
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    RouterModule,
  ],
})
export class Cliente implements OnInit {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly clientesService = inject(ClientesService);
  private readonly storage = inject(LocalStorageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly messageService = inject(MessageService);

  // Expose enums to template
  readonly TipoCliente = TipoCliente;
  readonly StatusCliente = StatusCliente;

  // Filtros
  searchTerm = '';
  isLoading = false;
  selectedTipo: TipoCliente | null = null;
  selectedStatus: StatusCliente | null = null;

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
  }[] = [];
  backendPage: Page<ClienteResponseDTO> | null = null;

  // Paginação
  rows = 5;
  first = 0;

  get totalRecords() {
    return this.backendPage?.totalElements ?? this.filtered.length;
  }
  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }
  jumpPageInput = '';

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
    return [
      { label: 'Visualizar / Editar Cliente', icon: 'person', routerLink: ['/cliente/editar', row.uuid] },
      { label: 'Cadastrar Agendamento / Alerta', icon: 'notifications', command: () => {} },
      { label: 'Visualizar / Editar Veículos', icon: 'directions_car', command: () => {} },
      { label: 'Cadastrar OS', icon: 'edit_note', command: () => {} },
      { label: 'Visualizar OS Veículo', icon: 'list', command: () => {} },
      { label: 'Histórico Veículo', icon: 'history', command: () => {} },
    ];
  }

  cadastrarCliente() {
    this.router.navigate(['/cliente/cadastro']);
  }

  navigateToEdit(row: { uuid?: string | number }) {
    const id = row?.uuid;
    if (id) {
      this.router.navigate(['/cliente/editar', id]);
    }
  }

  navigateToAddVeiculo(row: { id?: string | number; uuid?: string | number; nome?: string }) {
    const clienteId = row?.id || row?.uuid;
    if (clienteId) {
      this.router.navigate(['/veiculo/cadastro'], { queryParams: { clienteId } });
    }
  }




  // SplitButton gerencia abertura do menu; não precisamos do handler manual

  ngOnInit() {
    const qp = this.route.snapshot.queryParamMap;
    const statusParam = qp.get('status') as StatusCliente | null;
    const tipoParam = qp.get('tipo') as TipoCliente | null;
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
    };
  }

  private fetchPage() {
    // Backend usa 1-indexed parameters (page 1 é a primeira), então somamos 1
    const pageIndex = Math.floor(this.first / this.rows) + 1;
    this.isLoading = true;

    console.log(`[PAGINATION] first: ${this.first}, rows: ${this.rows}, pageIndex to API: ${pageIndex}`);

    const filters: any = { page: pageIndex, size: this.rows, sort: 'id,desc' }; // Restaurado para id,desc

    // Filtro de busca por nome
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.trim();
      filters.nomeCompleto = term;
      filters.razaoSocial = term;
      filters.nomeFantasia = term;
    }

    // Filtro por tipo de cliente
    if (this.selectedTipo) {
      filters.tipoCliente = this.selectedTipo;
    }

    // Filtro por status
    if (this.selectedStatus) {
      filters.status = this.selectedStatus;
    }

    // Console log the filters for debugging if needed
    // console.log('📤 Sending filters to API:', filters);


    this.clientesService.list(filters).subscribe({
      next: (res: Page<ClienteResponseDTO>) => {
        console.log('[API RESPONSE] Backend Page:', res);
        this.backendPage = res;
        this.clients = (res.content || []).map((d: ClienteResponseDTO) => this.mapToRow(d));

        this.isLoading = false;
        this.cdr.detectChanges(); // Forçar atualização da view

        // Após mapear clientes, buscar contatos para preencher a coluna "Contato"
        this.loadContatosForRows();
      },
      error: (err: unknown) => {
        this.isLoading = false;
        console.error('❌ Erro ao carregar clientes:', err);
        if (err && typeof err === 'object' && 'error' in err) {
          console.error('Error details:', (err as any).error);
        }
        if (err && typeof err === 'object' && 'status' in err) {
          console.error('HTTP Status:', (err as any).status);
        }
      },
    });
  }

  private loadContatosForRows() {
    const currentPageRows = this.clients;
    if (!currentPageRows || !currentPageRows.length) return;

    // Log do tenant/empresa para diagnosticar chamadas multi-tenant
    const tenantId = this.storage.has('tenantId') ? this.storage.get('tenantId') : null;
    console.debug('🔎 Buscando contatos para página atual', {
      rows: currentPageRows.map(r => ({ id: (r as any).id ?? r.uuid, uuid: r.uuid })),
      tenantId,
    });

    const requests = currentPageRows.map(row =>
      this.clientesService
        .listarContatos((row as any).id ?? row.uuid)
        .pipe(
          map((res: Page<ContatoClienteResponse>) => ({ id: (row as any).id ?? row.uuid, contatos: res.content || [] })),
          catchError(err => {
            console.error('Erro ao carregar contatos do cliente', (row as any).id ?? row.uuid, err);
            return of({ id: (row as any).id ?? row.uuid, contatos: [] as ContatoClienteResponse[] });
          })
        )
    );

    forkJoin(requests).subscribe(results => {
      const byId = new Map(results.map(r => [r.id, r.contatos]));
      this.clients = this.clients.map(c => {
        const key = (c as any).id ?? c.uuid;
        const contatos = byId.get(key) || [];
        return { ...c, contato: this.formatContatoLabel((c as any).email, contatos) };
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
    return value || emailFallback;
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
    const clean = value.replace(/\D/g, '');
    if (clean.length === 11) {
      return clean.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else if (clean.length === 14) {
      return clean.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    }
    return value;
  }
}
