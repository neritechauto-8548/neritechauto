import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-nfse-lista',
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
  templateUrl: './nfse-lista.html',

})
export class NfseListaComponent implements OnInit {
  private readonly datePipe = inject(DatePipe);

  dataInicio = '';
  dataFim = '';
  searchTerm = '';
  loading = false;

  notas: any[] = [];
  pageItems: any[] = [];

  page = 1;
  pageSize = 10;
  totalItems = 0;
  pageCount = 1;

  ngOnInit(): void {
    const hoje = new Date();
    const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.dataInicio = this.datePipe.transform(primeiroDia, 'yyyy-MM-dd') || '';
    this.dataFim = this.datePipe.transform(hoje, 'yyyy-MM-dd') || '';
    this.buscar();
  }

  imprimir(n?: any) {}
  exportarCsv() {}
  transmitirNfse(n: any) {}

  buscar() {
     this.loading = true;
     // Simulando delay de API vazia até plugar NeriTech NFSe Backend
     setTimeout(() => {
        this.pageItems = [];
        this.totalItems = 0;
        this.loading = false;
     }, 600);
  }

  prevPage(): void { if (this.page > 1) this.page--; }
  nextPage(): void { if (this.page < this.pageCount) this.page++; }

  statusSeverity(s: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' {
    switch(s) {
       case 'AUTORIZADA': return 'success';
       case 'REJEITADA':
       case 'CANCELADA': return 'danger';
       case 'EM_PROCESSAMENTO': return 'warn';
       default: return 'info';
    }
  }
}
