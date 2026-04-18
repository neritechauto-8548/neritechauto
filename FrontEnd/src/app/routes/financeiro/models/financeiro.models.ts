export enum StatusTitulo {
    ABERTO = 'ABERTO',
    PAGO = 'PAGO',
    VENCIDO = 'VENCIDO',
    PROTESTADO = 'PROTESTADO',
    CANCELADO = 'CANCELADO'
}

export enum TipoTitulo {
    DUPLICATA = 'DUPLICATA',
    PROMISSORIA = 'PROMISSORIA',
    CHEQUE = 'CHEQUE',
    CONVENIO = 'CONVENIO',
    BOLETO = 'BOLETO',
    OS = 'OS',
    OUTROS = 'OUTROS'
}

export interface ContasPagarRequest {
    descricao: string;
    fornecedorId: number;
    numeroDocumento?: string;
    dataEmissao: string; // yyyy-MM-dd
    dataVencimento: string; // yyyy-MM-dd
    dataPagamento?: string; // yyyy-MM-dd
    dataAgendamento?: string; // yyyy-MM-dd
    valorOriginal: number;
    valorPago?: number;
    valorJuros?: number;
    valorMulta?: number;
    valorDesconto?: number;
    saldoDevedor?: number;
    status?: StatusTitulo;
    tipoTitulo?: TipoTitulo;
    formaPagamentoId?: number;
    contaBancariaId?: number;
    centroCustoId?: number;
    planoContasId?: number;
    codigoBarras?: string;
    observacoes?: string;
}

export interface ContasPagarResponse {
    id: number;
    empresaId: number;
    descricao: string;
    fornecedorId: number;
    numeroDocumento?: string;
    dataEmissao: string;
    dataVencimento: string;
    dataPagamento?: string;
    dataAgendamento?: string;
    valorOriginal: number;
    valorPago?: number;
    valorJuros?: number;
    valorMulta?: number;
    valorDesconto?: number;
    saldoDevedor?: number;
    status: StatusTitulo;
    tipoTitulo: TipoTitulo;
    formaPagamentoId?: number;
    formaPagamentoNome?: string;
    contaBancariaId?: number;
    contaBancariaNome?: string;
    centroCustoId?: number;
    centroCustoNome?: string;
    planoContasId?: number;
    planoContasNome?: string;
    codigoBarras?: string;
    observacoes?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface ContasReceberRequest {
    descricao: string;
    clienteId: number;
    faturaId?: number;
    dataEmissao: string; // yyyy-MM-dd
    dataVencimento: string; // yyyy-MM-dd
    dataRecebimento?: string; // yyyy-MM-dd
    valorOriginal: number;
    valorRecebido?: number;
    valorJuros?: number;
    valorMulta?: number;
    valorDesconto?: number;
    saldoDevedor?: number;
    status?: StatusTitulo;
    tipoTitulo?: TipoTitulo;
    formaPagamentoId?: number;
    contaBancariaId?: number;
    centroCustoId?: number;
    planoContasId?: number;
    observacoes?: string;
}

export interface ContasReceberResponse {
    id: number;
    empresaId: number;
    descricao: string;
    clienteId: number;
    faturaId?: number;
    faturaNumero?: string;
    dataEmissao: string;
    dataVencimento: string;
    dataRecebimento?: string;
    valorOriginal: number;
    valorRecebido?: number;
    valorJuros?: number;
    valorMulta?: number;
    valorDesconto?: number;
    saldoDevedor?: number;
    status: StatusTitulo;
    tipoTitulo: TipoTitulo;
    formaPagamentoId?: number;
    formaPagamentoNome?: string;
    contaBancariaId?: number;
    contaBancariaNome?: string;
    centroCustoId?: number;
    centroCustoNome?: string;
    planoContasId?: number;
    planoContasNome?: string;
    observacoes?: string;
    createdAt?: string;
    updatedAt?: string;
}
