import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-produtos',
  standalone: true,
  imports: [CommonModule, FormsModule, PanelModule, ButtonModule],
  templateUrl: './relatorio-produtos.html',
  styleUrls: ['./relatorio-produtos.scss'],
})
export class RelatorioProdutos {
  private relatoriosService = inject(RelatoriosService);
  modoImpressao = false;
  reduzirDescricao = false;
  ordenarPorOptions = ['Nome', 'Código', 'Preço', 'Estoque'];
  criterioOptions = ['Ascendente', 'Descendente'];
  ordenarPor = this.ordenarPorOptions[0];
  criterio = this.criterioOptions[0];
  dataInicial: string | null = null;
  dataFinal: string | null = null;
  busca = '';
  page = 1;
  totalPages = 1;
  total = 0;
  pageItems: any[] = [];
  filtered: any[] = [];

  gerarRelatorio() {
    console.log('Gerando relatório de produtos...');
    this.relatoriosService.gerarRelatorio('produtos', {}).subscribe({
      next: blob => this.relatoriosService.downloadBlob(blob, 'relatorio-produtos.pdf'),
      error: err => console.error(err),
    });
  }

  abrirImpressao() {
    this.modoImpressao = true;
    this.filtered = this.pageItems;
  }

  voltarLista() {
    this.modoImpressao = false;
  }

  imprimir() {
    this.gerarRelatorio();
  }

  buscar() {
    const dados = [
      { codigo: 'PRD-001', descricao: 'Filtro de óleo', preco: 29.9, precoCusto: 18.0, endereco: 'A1', estoqueAtual: 12, estoqueMinimo: 3, codigoOriginal: 'FO-123', codigoFab: 'FAB-321', setor: 'Motor' },
      { codigo: 'PRD-002', descricao: 'Pastilha de freio', preco: 89.9, precoCusto: 60.0, endereco: 'B2', estoqueAtual: 0, estoqueMinimo: 2, codigoOriginal: 'PF-456', codigoFab: 'FAB-654', setor: 'Freio' }
    ];
    this.pageItems = dados;
    this.filtered = dados;
    this.total = dados.length;
    this.page = 1;
    this.totalPages = 1;
  }

  anterior() {
    if (this.page > 1) this.page--;
  }

  proxima() {
    if (this.page < this.totalPages) this.page++;
  }

  get totalCompraEstoque(): number {
    return this.filtered.reduce((acc, i) => acc + (i.precoCusto || 0) * (i.estoqueAtual || 0), 0);
  }

  get totalVendaEstoque(): number {
    return this.filtered.reduce((acc, i) => acc + (i.preco || 0) * (i.estoqueAtual || 0), 0);
  }
}
