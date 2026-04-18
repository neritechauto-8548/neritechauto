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

export interface PedidoFornecedorRequest {
    empresaId: number;
    fornecedorId: number;
    responsavel: string;
    dataPrevisao?: string | null; // ISO date string YYYY-MM-DD
    numeroNf?: string;
    observacao?: string;
    descricaoInterna?: string;
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
    status: 'PENDENTE' | 'RECEBIDO' | 'CANCELADO';
    dataCadastro: string;
}
