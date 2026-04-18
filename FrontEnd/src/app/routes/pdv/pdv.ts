import { Component, inject, OnInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PanelModule } from 'primeng/panel';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { CardModule } from 'primeng/card';
import { HotToastService } from '@ngxpert/hot-toast';
import { ProdutoService } from '../produtos-servicos/produto.service';
import { FinanceiroService } from '../financeiro/financeiro.service';
import { PdvService, ItemCarrinho } from './pdv.service';

@Component({
  standalone: true,
  selector: 'app-pdv',
  templateUrl: './pdv.html',
  styleUrls: ['./pdv.scss'],
  imports: [
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    InputTextModule,
    TableModule,
    DialogModule,
    InputNumberModule,
    SelectModule,
    CardModule
  ]
})
export class PdvComponent implements OnInit {
  private produtoService = inject(ProdutoService);
  private financeiroService = inject(FinanceiroService);
  private pdvService = inject(PdvService);
  private toast = inject(HotToastService);

  // Estado
  produtos: any[] = []; // Vitrine
  produtosFiltrados: any[] = [];
  filtroBusca = '';

  carrinho: ItemCarrinho[] = [];

  // Pagamento
  modalPagamentoVisible = false;
  formaPagamentoSelecionada: number | null = null;
  formasPagamento: any[] = [];
  desconto = 0;

  loading = false;

  ngOnInit() {
      this.carregarProdutos();
      this.carregarFormasPagamento();
  }

  carregarProdutos() {
      this.produtoService.list({ page: 0, size: 100 }).subscribe(page => {
          this.produtos = page.content;
          this.filtrarProdutos();
      });
  }

  carregarFormasPagamento() {
      this.financeiroService.listFormasPagamento().subscribe(res => {
          this.formasPagamento = res.content.map((fp: any) => ({ label: fp.nome, value: fp.id }));
      });
  }

  filtrarProdutos() {
      if (!this.filtroBusca) {
          this.produtosFiltrados = this.produtos;
      } else {
          const termo = this.filtroBusca.toLowerCase();
          this.produtosFiltrados = this.produtos.filter(p =>
             p.nome.toLowerCase().includes(termo) ||
             (p.codigoBarras && p.codigoBarras.includes(termo))
          );
      }
  }

  adicionarAoCarrinho(produto: any) {
      const existente = this.carrinho.find(i => i.produto.id === produto.id);
      if (existente) {
          existente.quantidade++;
          existente.valorTotal = existente.quantidade * existente.valorUnitario;
      } else {
          this.carrinho.push({
              produto,
              quantidade: 1,
              valorUnitario: produto.precoVenda,
              valorTotal: produto.precoVenda
          });
      }
      this.toast.success('Adicionado');
  }

  removerDoCarrinho(index: number) {
      this.carrinho.splice(index, 1);
  }

  limparCarrinho() {
      this.carrinho = [];
  }

  getTotalCarrinho() {
      return this.carrinho.reduce((acc, i) => acc + i.valorTotal, 0);
  }

  getTotalFinal() {
      return Math.max(0, this.getTotalCarrinho() - this.desconto);
  }

  abrirPagamento() {
      if (!this.carrinho.length) {
          this.toast.warning('Carrinho vazio');
          return;
      }
      this.modalPagamentoVisible = true;
  }

  finalizarVenda() {
      if (!this.formaPagamentoSelecionada) {
          this.toast.warning('Selecione a forma de pagamento');
          return;
      }

      this.loading = true;
      this.pdvService.finalizarVenda(this.carrinho, this.formaPagamentoSelecionada, this.desconto)
        .subscribe({
            next: () => {
                this.toast.success('Venda Realizada com Sucesso!');
                this.modalPagamentoVisible = false;
                this.limparCarrinho();
                this.desconto = 0;
                this.formaPagamentoSelecionada = null;
                this.carregarProdutos(); // Recarrega estoque
                this.loading = false;
            },
            error: (err) => {
                console.error(err);
                this.toast.error('Erro ao finalizar venda');
                this.loading = false;
            }
        });
  }
}
