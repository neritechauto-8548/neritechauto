
export enum TipoOrcamento {
  DRAFT = 'DRAFT',
  FORMAL = 'FORMAL'
}

export enum StatusOrcamento {
  PENDENTE = 'PENDENTE',
  ENVIADO = 'ENVIADO',
  APROVADO = 'APROVADO',
  REJEITADO = 'REJEITADO',
  CANCELADO = 'CANCELADO',
  EXPIRADO = 'EXPIRADO'
}

export enum MetodoEnvio {
  EMAIL = 'EMAIL',
  WHATSAPP = 'WHATSAPP',
  IMPRESSO = 'IMPRESSO'
}

export interface OrcamentoRequest {
  ordemServicoId: number;
  numeroOrcamento?: string;
  versao?: number;
  tipoOrcamento?: TipoOrcamento;
  valorServicos?: number;
  valorProdutos?: number;
  valorMaoObra?: number;
  valorDesconto?: number;
  valorAcrescimo?: number;
  valorTotal: number;
  prazoValidadeDias?: number;
  dataVencimento?: string;
  prazoExecucaoDias?: number;
  dataPrevistaConclusao?: string;
  condicoesPagamento?: string;
  observacoesComerciais?: string;
  termosCondicoes?: string;
  status?: StatusOrcamento;
  metodoEnvio?: MetodoEnvio;
  responsavelOrcamentoId?: number;
}

export interface OrcamentoResponse {
  id: number;
  ordemServicoId: number;
  numeroOrcamento: string;
  versao: number;
  tipoOrcamento: TipoOrcamento;
  valorServicos: number;
  valorProdutos: number;
  valorMaoObra: number;
  valorDesconto: number;
  valorAcrescimo: number;
  valorTotal: number;
  prazoValidadeDias: number;
  dataVencimento: string;
  prazoExecucaoDias: number;
  dataPrevistaConclusao: string;
  condicoesPagamento: string;
  observacoesComerciais: string;
  termosCondicoes: string;
  status: StatusOrcamento;
  dataEnvioCliente: string;
  dataRespostaCliente: string;
  metodoEnvio: MetodoEnvio;
  aprovadoPorCliente: string;
  documentoCliente: string;
  ipAprovacao: string;
  dataAprovacao: string;
  motivoRejeicao: string;
  responsavelOrcamentoId: number;
  dataCadastro: string;
  dataAtualizacao: string;
  versaoRegistro: number;
}
