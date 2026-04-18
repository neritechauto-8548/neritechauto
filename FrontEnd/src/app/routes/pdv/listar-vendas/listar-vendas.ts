import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { OrdemServicoService } from '../../os/ordem-servico.service';
import { ClientesService } from '../../cliente/cliente/cliente.service';

@Component({
  selector: 'app-pdv-listar-vendas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule
  ],
  providers: [DatePipe],
  templateUrl: './listar-vendas.html',
  styleUrls: ['./listar-vendas.scss'],
})
export class ListarVendasPDV implements OnInit {
  private readonly router = inject(Router);
  private readonly osService = inject(OrdemServicoService);
  private readonly clienteService = inject(ClientesService);
  private readonly datePipe = inject(DatePipe);
  vendaDe = '';
  vendaAte = '';
  operadorSelecionado = 'Todos';
  operadores = ['Todos', 'ALEXANDRE ROMULO ALBUQUERQUE NERI'];

  vendas: any[] = [];
  searchTerm = '';
  loading = false;
  empresaId = 1;

  page = 1;
  pageSize = 10;
  totalItems = 0;
  pageCount = 1;
  gotoPage = 1;

  ngOnInit(): void {
    // Inicializa datas com o mês corrente
    const hoje = new Date();
    const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
    this.vendaDe = this.datePipe.transform(primeiroDia, 'yyyy-MM-dd') || '';
    this.vendaAte = this.datePipe.transform(hoje, 'yyyy-MM-dd') || '';

    this.buscar();
  }

  imprimir(): void {
    window.print();
  }

  abrirPDV(): void {
    this.router.navigate(['/pdv/venda-balcao']);
  }

  buscar(): void {
    this.loading = true;
    // O service atual lista todas as OS. Iremos puxar e filtrar no front (ou apenas mostrar as últimas).
    // Num cenário ideal haveria ?tipoOS=VENDA_PRODUTO direto na query da API
    this.osService.list({ page: this.page - 1, size: 50 }).subscribe({
      next: (res: any) => {
         // Filtra apenas VENDAS e mapeia para interface da tela
         const contentArray = res.content || [];
         const filtradas = contentArray.filter((o: any) => o.tipoOS === 'VENDA_PRODUTO');

         this.vendas = filtradas.map((os: any) => ({
             id: os.id,
             pdv: os.numeroOS || os.id,
             data: this.datePipe.transform(os.dataAbertura, 'dd/MM/yyyy HH:mm'),
             clienteId: os.clienteId,
             clienteNome: 'Carregando...', // Popula em seguida
             operador: os.consultorResponsavelId || 'Operador',
             total: os.valorTotal || 0,
             status: os.status?.nome || 'Concluída',
             osOriginal: os
         }));

         this.totalItems = this.vendas.length;
         this.pageCount = Math.ceil(this.totalItems / this.pageSize) || 1;

         // Busca nomes dos clientes para enriquecer a tabela
         this.vendas.forEach(v => {
            if(v.clienteId) {
               this.clienteService.getById(v.clienteId).subscribe({
                  next: (clienteData: any) => v.clienteNome = clienteData.nomeCompleto || clienteData.razaoSocial || 'Cliente',
                  error: () => v.clienteNome = 'Cliente não informado'
               });
            } else {
               v.clienteNome = 'Cliente não informado';
            }
         });

         this.loading = false;
      },
      error: (err: any) => {
         console.error('Erro ao buscar vendas:', err);
         this.loading = false;
      }
    });
  }

  get pageItems() {
    let listData = this.vendas;

    // Filtro global em tempo real (Padrão Stripe Client View)
    if (this.searchTerm?.trim()) {
        const term = this.searchTerm.toLowerCase();
        listData = listData.filter(v =>
           String(v.pdv).includes(term) ||
           (v.clienteNome && v.clienteNome.toLowerCase().includes(term)) ||
           (v.operador && v.operador.toLowerCase().includes(term))
        );
    }

    // Atualiza contadores de acordo com a busca
    this.totalItems = listData.length;
    this.pageCount = Math.max(1, Math.ceil(this.totalItems / this.pageSize));
    if (this.page > this.pageCount) this.page = 1;

    const start = (this.page - 1) * this.pageSize;
    return listData.slice(start, start + this.pageSize);
  }

  prevPage(): void { if (this.page > 1) { this.page--; this.gotoPage = this.page; } }
  nextPage(): void { if (this.page < this.pageCount) { this.page++; this.gotoPage = this.page; } }
  goToPage(n?: number): void {
    const target = Number(n || this.gotoPage);
    if (!Number.isNaN(target) && target >= 1 && target <= this.pageCount) {
      this.page = target; this.gotoPage = this.page;
    }
  }

  verEditarCliente(v: any): void {
     if (v.clienteId) this.router.navigate(['/cliente/cadastro', { id: v.clienteId }]);
  }

  continuarVenda(v: any): void {
     this.router.navigate(['/pdv/venda-balcao', { id: v.id }]);
  }

  visualizarVenda(v: any): void {
     this.router.navigate(['/pdv/venda-balcao', { id: v.id }]);
  }
}
