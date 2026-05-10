import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MessageService } from 'primeng/api';

import { OrdemServicoService } from '../os/ordem-servico.service';
import { OrdemServicoResponse, TipoOS } from '../os/models/os.models';

@Component({
  selector: 'app-orcamento',
  standalone: true,
  imports: [
    CommonModule, FormsModule, RouterModule,
    ButtonModule, InputTextModule, ToastModule,
    MatIconModule, MatMenuModule, MatButtonModule
  ],
  templateUrl: './orcamento.html',
  providers: [MessageService]
})
export class OrcamentoComponent implements OnInit {
  private osService = inject(OrdemServicoService);
  private router = inject(Router);
  private messageService = inject(MessageService);

  orcamentos: OrdemServicoResponse[] = [];
  filteredData: OrdemServicoResponse[] = [];
  loading = false;
  termo = '';

  // Paginação — padrão veículo
  first = 0;
  rows = 10;
  totalItemsBackend = 0;

  currentOS: OrdemServicoResponse | null = null;

  get totalRecords(): number {
    return this.totalItemsBackend;
  }

  get pagedData(): OrdemServicoResponse[] {
    return this.filteredData; // Já vem paginado do backend
  }

  get rangeStart(): number {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd(): number {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  ngOnInit() { this.carregar(); }

  onSearch() {
    this.first = 0;
    this.carregar();
  }

  carregar() {
    this.loading = true;
    const page = Math.floor(this.first / this.rows);
    this.osService.list({ 
      page, 
      size: this.rows, 
      sort: 'dataAbertura,desc', 
      tipo: 'ORCAMENTO', 
      search: this.termo 
    }).subscribe({
      next: (res) => {
        this.orcamentos = res.content || [];
        this.filteredData = [...this.orcamentos];
        this.totalItemsBackend = res.totalElements || 0;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar os orçamentos.' });
      }
    });
  }

  goPrev() { 
    if (this.first >= this.rows) {
      this.first -= this.rows; 
      this.carregar();
    }
  }
  
  goNext() { 
    if (this.first + this.rows < this.totalItemsBackend) {
      this.first += this.rows; 
      this.carregar();
    }
  }

  setCurrent(os: OrdemServicoResponse) {
    this.currentOS = os;
  }

  novo() {
    this.router.navigate(['/os/cadastro'], { queryParams: { tipo: 'ORCAMENTO' } });
  }

  editar(id: number) {
    this.router.navigate(['/os/cadastro', id]);
  }

  visualizar(id: number) {
    this.router.navigate(['/os/visualizar-os', id]);
  }

  excluir(id: number, numero?: string) {
    const ok = confirm(`Deseja excluir o orçamento${numero ? ' ' + numero : ''}? Esta ação não pode ser desfeita.`);
    if (!ok) return;
    this.osService.delete(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Excluído!', detail: 'Orçamento removido com sucesso.' });
        this.carregar();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir.' })
    });
  }

  // ─── Helpers ─────────────────────────────────────
  getStatusBadgeClass(status?: string): string {
    const s = (status || '').toUpperCase();
    if (s.includes('APROVADO') || s.includes('CONCLUIDO') || s.includes('FINALIZADO'))
      return 'bg-emerald-50 text-emerald-700 border-emerald-200';
    if (s.includes('CANCELADO') || s.includes('REJEITADO'))
      return 'bg-red-50 text-red-600 border-red-200';
    if (s.includes('ANDAMENTO') || s.includes('EXECUCAO'))
      return 'bg-blue-50 text-blue-700 border-blue-200';
    return 'bg-amber-50 text-amber-700 border-amber-200';
  }

  formatDate(s?: string): string {
    if (!s) return '—';
    return new Date(s).toLocaleDateString('pt-BR');
  }

  formatCurrency(v?: number | any): string {
    return (Number(v) || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
  }
}
