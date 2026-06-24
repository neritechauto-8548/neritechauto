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
    VENDA_BALCAO = 'VENDA_BALCAO',
    OUTROS = 'OUTROS'
}

export interface DashboardFinanceiroDTO {
    totalAReceber: number;
    recebidoHoje: number;
    atrasados: number;
    ticketMedio: number;
    recebimentosPrevistosMes: number;
    inadimplencia: number; // Percentual
    contasVencendoHoje: number;
}

export interface RecebimentoTituloDTO {
    id: number;
    dataRecebimento: string;
    valorRecebido: number;
    valorJuros: number;
    valorMulta: number;
    valorDesconto: number;
    formaPagamentoId?: number;
    formaPagamentoNome?: string;
    contaBancariaId?: number;
    contaBancariaNome?: string;
    observacoes?: string;
}

export interface AnexoTituloDTO {
    id: number;
    nomeArquivo: string;
    tipoArquivo: string;
    tamanhoBytes: number;
    caminhoLocal: string;
    observacoes?: string;
    dataCadastro: string;
}

export interface LogAlteracaoDTO {
    id: number;
    operacao: string;
    camposAlterados: string;
    usuarioResponsavelNome: string;
    dataAlteracao: string;
    motivoAlteracao?: string;
}

export interface ContasPagarRequest {
    descricao: string;
    fornecedorId?: number;
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
    numeroTitulo?: string;
    codigoBarras?: string;
    observacoes?: string;
}

export interface ContasPagarResponse {
    id: number;
    empresaId: number;
    descricao: string;
    fornecedorId?: number;
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
    numeroTitulo?: string;
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
    numeroTitulo?: string;
}

export interface ContasReceberResponse {
  id: number;
  empresaId: number;
  descricao: string;
  clienteId: number;
  clienteNome?: string;
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
  numeroTitulo?: string;
  recebimentos?: any[];
  anexos?: any[];
  historico?: any[];
  createdAt?: string;
  updatedAt?: string;
}
