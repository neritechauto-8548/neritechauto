import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { PageHeader } from '@shared';
// PrimeNG
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
// Angular Material
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { FornecedorService } from '../fornecedor.service';

@Component({
  selector: 'fornecedor',
  standalone: true,
  templateUrl: './fornecedor.html',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    PageHeader,
    SelectModule,
    InputTextModule,
    ButtonModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
  ],
})
export class Fornecedor implements OnInit {
  private readonly router = inject(Router);
  private readonly location = inject(Location);
  private readonly fornecedorService = inject(FornecedorService);

  filterOptions = [
    { label: 'Razão Social', value: 'razaoSocial' },
    { label: 'Nome Fantasia', value: 'nomeFantasia' },
    { label: 'CNPJ', value: 'cnpj' },
    { label: 'CPF', value: 'cpf' },
  ];

  filterType: 'razaoSocial' | 'nomeFantasia' | 'cnpj' | 'cpf' = 'razaoSocial';
  searchTerm = '';

  items: any[] = [];

  rows = 10;
  first = 0;
  totalRecords = 0;

  ngOnInit() {
    this.buscar();
  }

  buscar() {
    const page = Math.floor(this.first / this.rows);
    const size = this.rows;
    // Basic filter query placeholder; usually parsed in the Backend Pageable API
    const sort = 'id,desc';

    this.fornecedorService.list({ page, size, sort }).subscribe({
      next: (res: any) => {
        this.items = (res?.content || []).map((f: any) => this.mapFornecedor(f));
        this.totalRecords = res?.totalElements || this.items.length;
      },
      error: () => {
        this.items = [];
        this.totalRecords = 0;
      },
    });
  }

  private mapFornecedor(f: any) {
    return {
      id: f.id,
      codigo: f.id,
      nomeFantasia: f.nomeFantasia,
      razaoSocial: f.razaoSocial,
      documento: f.cnpj || f.cpf || 'Não inform.',
      telefone: f.telefonePrincipal || f.celularPrincipal || 'Sem Contato',
      email: f.emailPrincipal || f.emailContato || '-',
      cidadeEstado: (f.cidade && f.estado) ? `${f.cidade} - ${f.estado}` : (f.cidade || f.estado || 'Endereço n/a'),
      classificacao: f.classificacao, // PRODUTO, SERVICO, AMBOS
      ativo: f.ativo !== false
    };
  }

  get currentPage() {
    return Math.floor(this.first / this.rows) + 1;
  }

  get totalPages() {
    return Math.ceil(this.totalRecords / this.rows) || 1;
  }

  onSearch() {
    this.first = 0;
    this.buscar();
  }

  goPrev() {
    this.first = Math.max(0, this.first - this.rows);
    this.buscar();
  }

  goNext() {
    const next = this.first + this.rows;
    if (next < this.totalRecords) {
      this.first = next;
      this.buscar();
    }
  }

  // Ações da barra superior
  imprimir() {
    this.router.navigate(['/fornecedor/relatorio']);
  }

  cadastrarFornecedor() {
    this.router.navigate(['/fornecedor/novo']);
  }

  // Ações por linha
  goEditarFornecedor(row: any) {
    this.router.navigate(['/fornecedor/editar', row.id || row.codigo]);
  }
}
