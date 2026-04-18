import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { NfeService } from '../../financeiro/nfe.service';

@Component({
  selector: 'app-nfe-lista',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    TagModule,
    InputTextModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule
  ],
  providers: [DatePipe],
  templateUrl: './nfe-lista.html',

})
export class NfeLista implements OnInit {
  private readonly nfeService = inject(NfeService);
  private readonly datePipe = inject(DatePipe);

  // Filtros
  vendaDe = '';
  vendaAte = '';
  searchTerm = '';
  loading = false;
  empresaId = 1;

  // Lista de NF-e
  notas: any[] = [];
  todasNotasBanco: any[] = [];

  // Paginação Custom Tailwind
  page = 1;
  pageSize = 10;
  totalItems = 0;
  pageCount = 1;

  ngOnInit(): void {
    const hoje = new Date();
    const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.vendaDe = this.datePipe.transform(primeiroDia, 'yyyy-MM-dd') || '';
    this.vendaAte = this.datePipe.transform(hoje, 'yyyy-MM-dd') || '';
    this.buscar();
  }

  // Barra de ações
  imprimir(nfe?: any) {}
  imprimirComNcm(nfe?: any) {}
  exportarCsv() {}
  loteNotasEmitidas() {}
  transmitirNfe(nfe: any) {
    if(!nfe || !nfe.id) return;
    this.nfeService.transmitir(nfe.id, this.empresaId).subscribe({
        next: () => this.buscar(),
        error: (err) => console.error(err)
    });
  }

  buscar(): void {
    this.loading = true;
    this.nfeService.list({ empresaId: this.empresaId, page: this.page - 1, size: 500 }).subscribe({
      next: (res) => {
        const content = res.content || [];
        this.todasNotasBanco = content.map((n: any) => ({
             id: n.id,
             numero: n.numeroNfe || 'S/N',
             tipo: n.tipoOperacao === 'SAIDA' ? 'Saída' : 'Entrada',
             clienteNome: n.faturaNumero ? `Fatura #${n.faturaNumero}` : 'Consumidor',
             statusRaw: n.status,
             statusLabel: this.traduzirStatus(n.status),
             totalProdutos: n.valorTotalProdutos || 0,
             desconto: 0,
             totalNota: n.valorTotalNota || 0,
             data: this.datePipe.transform(n.dataEmissao, 'dd/MM/yyyy HH:mm') || 'Não Emitida'
        }));
        this.loading = false;
        this.atualizarPaginas();
      },
      error: (err) => {
        console.error('Erro buscar NFe', err);
        this.loading = false;
        this.todasNotasBanco = [];
        this.atualizarPaginas();
      }
    });
  }

  get pageItems() {
    let listData = this.todasNotasBanco;

    if (this.searchTerm?.trim()) {
        const term = this.searchTerm.toLowerCase();
        listData = listData.filter(n =>
           String(n.numero).includes(term) ||
           (n.clienteNome && n.clienteNome.toLowerCase().includes(term))
        );
    }

    this.totalItems = listData.length;
    this.pageCount = Math.max(1, Math.ceil(this.totalItems / this.pageSize));
    if (this.page > this.pageCount) this.page = 1;

    const start = (this.page - 1) * this.pageSize;
    return listData.slice(start, start + this.pageSize);
  }

  atualizarPaginas() {
      // Usado para forcar refresh do get
  }

  prevPage(): void { if (this.page > 1) this.page--; }
  nextPage(): void { if (this.page < this.pageCount) this.page++; }

  traduzirStatus(s: string): string {
      switch(s) {
          case 'AUTORIZADA': return 'Autorizada';
          case 'REJEITADA': return 'Rejeitada';
          case 'DENEGADA': return 'Denegada';
          case 'CANCELADA': return 'Cancelada';
          case 'EM_PROCESSAMENTO': return 'Processando';
          default: return 'Pendente';
      }
  }

  statusSeverity(s: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' {
    switch(s) {
       case 'AUTORIZADA': return 'success';
       case 'REJEITADA':
       case 'DENEGADA':
       case 'CANCELADA': return 'danger';
       case 'EM_PROCESSAMENTO': return 'warn';
       default: return 'info';
    }
  }
}
