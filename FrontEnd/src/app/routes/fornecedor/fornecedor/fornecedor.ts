import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
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
    const sort = 'id,desc';
    
    const params: Record<string, any> = { page, size, sort };
    if (this.searchTerm && this.searchTerm.trim() !== '') {
      params['search'] = this.searchTerm.trim();
    }

    this.fornecedorService.list(params).subscribe({
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

  formatCpf(cpf: string | null): string {
    if (!cpf) return '';
    let raw = cpf.replace(/\D/g, '');
    if (raw.length !== 11) return cpf;
    return `${raw.substring(0, 3)}.${raw.substring(3, 6)}.${raw.substring(6, 9)}-${raw.substring(9, 11)}`;
  }

  formatCnpj(cnpj: string | null): string {
    if (!cnpj) return '';
    let raw = cnpj.replace(/[^A-Za-z0-9]/g, '').toUpperCase();
    if (raw.length !== 14) return cnpj;
    return `${raw.substring(0, 2)}.${raw.substring(2, 5)}.${raw.substring(5, 8)}/${raw.substring(8, 12)}-${raw.substring(12, 14)}`;
  }

  formatPhone(phone: string | null): string {
    if (!phone) return '';
    let raw = phone.replace(/\D/g, '');
    if (raw.length === 10) {
      return `(${raw.substring(0, 2)}) ${raw.substring(2, 6)}-${raw.substring(6)}`;
    } else if (raw.length === 11) {
      return `(${raw.substring(0, 2)}) ${raw.substring(2, 7)}-${raw.substring(7)}`;
    }
    return phone;
  }

  private mapFornecedor(f: any) {
    return {
      id: f.id,
      codigo: f.id,
      nomeFantasia: f.nomeFantasia,
      razaoSocial: f.razaoSocial,
      documento: f.cnpj ? this.formatCnpj(f.cnpj) : (f.cpf ? this.formatCpf(f.cpf) : 'Não inform.'),
      telefone: f.telefonePrincipal ? this.formatPhone(f.telefonePrincipal) : (f.celularPrincipal ? this.formatPhone(f.celularPrincipal) : 'Sem Contato'),
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
  cadastrarFornecedor() {
    this.router.navigate(['/fornecedor/novo']);
  }

  // Ações por linha
  goEditarFornecedor(row: any) {
    this.router.navigate(['/fornecedor/editar', row.id || row.codigo]);
  }
}
