import { Injectable, inject } from '@angular/core';
import { forkJoin, map, switchMap, Observable, of } from 'rxjs';
import { ProdutoService } from '../produtos-servicos/produto.service';
import { FinanceiroService } from '../financeiro/financeiro.service';
import { StatusTitulo, TipoTitulo } from '../financeiro/models/financeiro.models';

export interface ItemCarrinho {
  produto: any;
  quantidade: number;
  valorUnitario: number;
  valorTotal: number;
}

@Injectable({
  providedIn: 'root'
})
export class PdvService {
  private produtoService = inject(ProdutoService);
  private financeiroService = inject(FinanceiroService);

  // Orquestração de Venda
  finalizarVenda(itens: ItemCarrinho[], formaPagamentoId: number, desconto = 0): Observable<any> {
    const totalVenda = itens.reduce((acc, i) => acc + i.valorTotal, 0) - desconto;

    // 1. Atualizar Estoque de cada item
    const updateEstoque$ = itens.map(item => {
       const novaQtd = (item.produto.estoqueAtual || 0) - item.quantidade;
       // Mock de update: Reenviar o produto com estoque atualizado
       // Idealmente backend teria endpoint /baixa-estoque, mas usaremos update completo
       const produtoAtualizado = { ...item.produto, estoqueAtual: novaQtd };
       return this.produtoService.update(item.produto.id, produtoAtualizado);
    });

    // 2. Criar Receita no Financeiro
    const receita: any = {
        descricao: `Venda Balcão - ${(new Date()).toLocaleString()}`,
        valorOriginal: totalVenda,
        valorPago: totalVenda, // Já nasce pago no PDV
        dataVencimento: new Date().toISOString().split('T')[0],
        dataEmissao: new Date().toISOString().split('T')[0],
        dataRecebimento: new Date().toISOString().split('T')[0], // Pago hoje
        status: StatusTitulo.PAGO,
        tipoTitulo: TipoTitulo.OUTROS, // Venda
        formaPagamentoId,
        clienteId: 1, // Consumidor Final (Default)
        planoContasId: null, // Opcional ou default
        centroCustoId: null  // Opcional ou default
    };

    const criarReceita$ = this.financeiroService.createReceber(receita);

    // Executa estoque em paralelo, depois cria receita
    return forkJoin(updateEstoque$).pipe(
        switchMap(() => criarReceita$)
    );
  }
}
