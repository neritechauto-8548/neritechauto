import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { InputNumberModule } from 'primeng/inputnumber';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { HotToastService } from '@ngxpert/hot-toast';
import { MatIconModule } from '@angular/material/icon';

import { ProdutoService } from '../../produtos-servicos/produto.service';
import { FornecedorService } from '../fornecedor.service';
import { FinanceiroService } from '../../financeiro/financeiro.service';
import { PedidoCompraService } from '../pedido-compra.service';
import { ItemCompra, PedidoCompraRequest } from '../models/compra.models';

@Component({
  standalone: true,
  selector: 'app-pedido-compra',
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    TableModule,
    InputNumberModule,
    AutoCompleteModule,
    SelectModule,
    InputTextModule,
    TextareaModule,
    MatIconModule
  ],
  templateUrl: './pedido-compra.html'
})
export class PedidoCompraComponent implements OnInit {
  private produtoService = inject(ProdutoService);
  private fornecedorService = inject(FornecedorService);
  private financeiroService = inject(FinanceiroService);
  private compraService = inject(PedidoCompraService);
  private toast = inject(HotToastService);

  // Dados
  fornecedoresFiltrados: any[] = [];
  produtosFiltrados: any[] = [];
  formasPagamento: any[] = [];

  // Form Header
  fornecedorSelecionado: any = null;
  observacoes = '';
  formaPagamentoId: number | null = null;

  // Form Item
  produtoSelecionado: any = null;
  qtd = 1;
  custoUnitario = 0;

  // Carrinho
  itens: ItemCompra[] = [];
  loading = false;

  ngOnInit() {
      this.carregarFormasPagamento();
  }

  carregarFormasPagamento() {
      this.financeiroService.listFormasPagamento().subscribe(res => {
          this.formasPagamento = res.content.map((f: any) => ({ label: f.nome, value: f.id }));
      });
  }

  filtrarFornecedores(event: any) {
      this.fornecedorService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
          page => this.fornecedoresFiltrados = page.content
      );
  }

  filtrarProdutos(event: any) {
      this.produtoService.list({ nome: event.query, page: 0, size: 10 }).subscribe(
          page => this.produtosFiltrados = page.content
      );
  }

  onProdutoSelect(event: any) {
    if (event.value) {
        this.custoUnitario = event.value.precoCusto || 0;
    }
  }

  adicionarItem() {
      if (!this.produtoSelecionado || this.qtd <= 0 || this.custoUnitario <= 0) {
          this.toast.warning('Preencha os dados do item corretamente.');
          return;
      }

      const item: ItemCompra = {
          produto: this.produtoSelecionado,
          quantidade: this.qtd,
          valorUnitario: this.custoUnitario,
          valorTotal: this.qtd * this.custoUnitario
      };

      this.itens.push(item);
      this.toast.success('Item adicionado');

      // Reset item form
      this.produtoSelecionado = null;
      this.qtd = 1;
      this.custoUnitario = 0;
  }

  removerItem(index: number) {
      this.itens.splice(index, 1);
  }

  getTotal() {
      return this.itens.reduce((acc, i) => acc + i.valorTotal, 0);
  }

  finalizarPedido() {
      if (!this.fornecedorSelecionado) {
          this.toast.warning('Selecione um Fornecedor.');
          return;
      }
      if (this.itens.length === 0) {
          this.toast.warning('Adicione produtos ao pedido.');
          return;
      }

      this.loading = true;
      const req: PedidoCompraRequest = {
          fornecedorId: this.fornecedorSelecionado.id,
          itens: this.itens,
          formaPagamentoId: this.formaPagamentoId || 1, // Default Dinheiro se nulo
          observacoes: this.observacoes
      };

      this.compraService.finalizarCompra(req).subscribe({
          next: () => {
              this.toast.success('Compra registrada! Estoque atualizado.');
              this.loading = false;
              this.limparTela();
          },
          error: (err) => {
              console.error(err);
              this.toast.error('Erro ao finalizar compra.');
              this.loading = false;
          }
      });
  }

  limparTela() {
      this.fornecedorSelecionado = null;
      this.itens = [];
      this.observacoes = '';
      this.formaPagamentoId = null;
  }
}
