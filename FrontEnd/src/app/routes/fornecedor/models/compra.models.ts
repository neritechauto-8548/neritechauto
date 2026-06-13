export interface ItemCompra {
    produto: any;
    quantidade: number;
    valorUnitario: number;
    valorTotal: number;
}

export interface PedidoCompraRequest {
    fornecedorId: number;
    itens: ItemCompra[];
    formaPagamentoId: number;
    condicaoPagamentoId?: number; // Opcional
    observacoes?: string;
}

export interface ItemPedidoFornecedorRequest {
    produtoId: number;
    quantidade: number;
    precoUnitario: number;
}

export interface ItemPedidoFornecedorResponse {
    id: number;
    produtoId: number;
    nomeProduto: string;
    codigoInternoProduto?: string;
    quantidade: number;
    precoUnitario: number;
    subtotal: number;
}

export interface PedidoFornecedorRequest {
    empresaId: number;
    fornecedorId: number;
    responsavel: string;
    dataPrevisao?: string | null; // ISO date string YYYY-MM-DD
    numeroNf?: string;
    observacao?: string;
    descricaoInterna?: string;
    itens?: ItemPedidoFornecedorRequest[];
}

export interface PedidoFornecedorResponse {
    id: number;
    empresaId: number;
    fornecedorId: number;
    nomeFornecedor: string;
    numeroPedido: number;
    responsavel: string;
    dataPrevisao?: string;
    numeroNf?: string;
    observacao?: string;
    descricaoInterna?: string;
    status: 'PENDENTE' | 'ENVIADO' | 'RECEBIDO' | 'CANCELADO';
    dataCadastro: string;
    itens?: ItemPedidoFornecedorResponse[];
}


