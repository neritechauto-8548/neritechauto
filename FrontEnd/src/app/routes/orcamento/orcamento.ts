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

  currentOS: OrdemServicoResponse | null = null;

  // ─── Paginação ─────────────────────────────────────
  get pagedData(): OrdemServicoResponse[] {
    return this.filteredData.slice(this.first, this.first + this.rows);
  }

  get totalRecords(): number {
    return this.filteredData.length;
  }

  get rangeStart(): number {
    return this.totalRecords === 0 ? 0 : this.first + 1;
  }

  get rangeEnd(): number {
    return Math.min(this.first + this.rows, this.totalRecords);
  }

  goPrev() { if (this.first > 0) this.first -= this.rows; }
  goNext() { if (this.first + this.rows < this.totalRecords) this.first += this.rows; }

  // ─── Busca ─────────────────────────────────────────
  onSearch() {
    this.first = 0;
    const t = this.termo.toLowerCase().trim();
    if (!t) {
      this.filteredData = [...this.orcamentos];
      return;
    }
    this.filteredData = this.orcamentos.filter(o =>
      (o.numeroOS || '').toLowerCase().includes(t) ||
      (o.nomeCliente || '').toLowerCase().includes(t) ||
      (o.placaVeiculo || '').toLowerCase().includes(t)
    );
  }

  ngOnInit() { this.carregar(); }

  carregar() {
    this.loading = true;
    this.osService.list({ size: 200 }).subscribe({
      next: (page) => {
        this.orcamentos = page.content.filter(os => os.tipoOS === TipoOS.ORCAMENTO);
        this.filteredData = [...this.orcamentos];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar os orçamentos.' });
      }
    });
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
