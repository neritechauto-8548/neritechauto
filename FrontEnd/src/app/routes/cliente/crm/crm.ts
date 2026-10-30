import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NeriTechIcon } from '@shared/components';
import { ClientesService } from '../cliente/cliente.service';
import { ClienteResponse, StatusClienteLabels } from '../models/cliente.models';

@Component({
  selector: 'app-cliente-crm',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, NeriTechIcon],
  templateUrl: './crm.html',
  styleUrl: './crm.scss',
})
export class Crm implements OnInit {
  private readonly clientesService = inject(ClientesService);
  private requestVersion = 0;
  readonly statusLabels = StatusClienteLabels;
  readonly pageSize = 10;

  opportunityTerm = '';
  opportunityCategory = 'todas';
  readonly opportunityCards = [
    { id:'aniversario', name:'Aniversariantes do mês', kind:'Clientes', color:'lilac', action:'Enviar parabéns' },
    { id:'semvenda', name:'Clientes sem movimento em venda', kind:'Clientes', color:'amber', action:'Reativar' },
    { id:'semos', name:'Clientes sem movimento em O.S', kind:'Clientes', color:'amber', action:'Reativar' },
    { id:'revisao', name:'Próximas revisões', kind:'Veículos', color:'cyan', action:'Lembrar revisão' },
    { id:'futuros', name:'Serviços futuros', kind:'Veículos', color:'blue', action:'Lembrar cliente' },
  ];
  get filteredOpportunities() {
    const query = this.opportunityTerm.trim().toLocaleLowerCase('pt-BR');
    return this.opportunityCards.filter(item =>
      (this.opportunityCategory === 'todas' || item.kind === this.opportunityCategory) &&
      item.name.toLocaleLowerCase('pt-BR').includes(query));
  }

  searchTerm = '';
  dataInicio = '';
  dataFim = '';
  page = 0;
  total = 0;
  clientes: ClienteResponse[] = [];
  loading = false;
  error = '';
  validationError = '';
  private appliedFilters: Record<string, string> = {};

  ngOnInit(): void {
    this.buscar();
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.total / this.pageSize));
  }

  get hasFilters(): boolean {
    return !!(this.searchTerm.trim() || this.dataInicio || this.dataFim);
  }

  nomeCliente(cliente: ClienteResponse): string {
    return cliente.nomeCompleto || cliente.nomeFantasia || cliente.razaoSocial || `Cliente #${cliente.id}`;
  }

  buscar(): void {
    if (this.dataInicio && this.dataFim && this.dataInicio > this.dataFim) {
      this.validationError = 'A data inicial deve ser anterior ou igual à data final.';
      return;
    }
    this.validationError = '';
    this.page = 0;
    this.appliedFilters = {
      ...(this.searchTerm.trim() ? { busca: this.searchTerm.trim() } : {}),
      ...(this.dataInicio ? { dataInicio: this.dataInicio } : {}),
      ...(this.dataFim ? { dataFim: this.dataFim } : {}),
    };
    this.loadPage();
  }

  limpar(): void {
    this.searchTerm = '';
    this.dataInicio = '';
    this.dataFim = '';
    this.buscar();
  }

  irParaPagina(page: number): void {
    if (page < 0 || page >= this.totalPages || page === this.page) return;
    this.page = page;
    this.loadPage();
  }

  private loadPage(): void {
    const version = ++this.requestVersion;
    this.loading = true;
    this.error = '';
    this.clientesService.list({
      ...this.appliedFilters,
      page: this.page,
      size: this.pageSize,
      sort: 'dataCadastro,desc',
    }).subscribe({
      next: response => {
        if (version !== this.requestVersion) return;
        this.clientes = response.content || [];
        this.total = response.totalElements || 0;
        this.loading = false;
      },
      error: error => {
        if (version !== this.requestVersion) return;
        this.clientes = [];
        this.total = 0;
        this.loading = false;
        this.error = error?.status === 403
          ? 'Seu perfil não tem permissão para consultar clientes.'
          : 'Não foi possível carregar os clientes. Tente novamente.';
      },
    });
  }
}
