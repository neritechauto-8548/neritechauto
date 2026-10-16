import { Injectable, inject } from '@angular/core';
import { forkJoin, Observable, switchMap, throwError } from 'rxjs';
import { FornecedorService } from './fornecedor.service';
import { ProdutoService } from '../produtos-servicos/produto.service';
import { FinanceiroService } from '../financeiro/financeiro.service';
import { ItemCompra, PedidoCompraRequest } from './models/compra.models';
import { StatusTitulo, TipoTitulo } from '../financeiro/models/financeiro.models';

@Injectable({
  providedIn: 'root'
})
export class PedidoCompraService {
  private produtoService = inject(ProdutoService);
  private financeiroService = inject(FinanceiroService);
  private fornecedorService = inject(FornecedorService);

  finalizarCompra(pedido: PedidoCompraRequest): Observable<any> {
    const totalCompra = pedido.itens.reduce((acc, i) => acc + i.valorTotal, 0);

    // 1. Atualizar Estoque (AUMENTAR)
    const updateEstoque$ = pedido.itens.map(item => {
       const novaQtd = (item.produto.estoqueAtual || 0) + item.quantidade;
       // Mock update: idealmente seria endpoint de entrada
       const produtoAtualizado = {
           ...item.produto,
           estoqueAtual: novaQtd,
           precoCusto: item.valorUnitario // Atualiza preço de custo com a nova compra
        };
       return this.produtoService.update(item.produto.id, produtoAtualizado);
    });

    // 2. Criar Despesa no Financeiro (Conta a Pagar)
    const despesa: any = {
        descricao: `Compra de Mercadoria - ${(new Date()).toLocaleString()}`,
        valorOriginal: totalCompra,
        valorPago: 0, // A pagar
        dataVencimento: new Date().toISOString().split('T')[0], // Hoje por default
        dataEmissao: new Date().toISOString().split('T')[0],
        status: StatusTitulo.ABERTO,
        tipoTitulo: TipoTitulo.OUTROS, // Compra
        formaPagamentoId: pedido.formaPagamentoId,
        fornecedorId: pedido.fornecedorId,
        observacoes: pedido.observacoes
    };

    const criarDespesa$ = this.financeiroService.createPagar(despesa);

    // Confirme que o cadastro mestre está ativo ANTES de qualquer mutação local.
    // O servidor de cada operação continua responsável pelo RBAC e isolamento.
    return this.fornecedorService.get(pedido.fornecedorId).pipe(
      switchMap(fornecedor => {
        if (fornecedor.ativo === false) {
          return throwError(() => new Error('Fornecedor inativo não pode gerar nova compra.'));
        }
        return forkJoin(updateEstoque$).pipe(switchMap(() => criarDespesa$));
      })
    );
  }
}
