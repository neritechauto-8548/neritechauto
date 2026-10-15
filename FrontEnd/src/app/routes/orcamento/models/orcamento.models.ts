
export enum TipoOrcamento {
  SIMPLES = 'SIMPLES',
  DETALHADO = 'DETALHADO',
  COMPARATIVO = 'COMPARATIVO',
  /** @deprecated Use SIMPLES; serializa para o enum aceito pelo backend. */
  DRAFT = 'SIMPLES',
  /** @deprecated Use DETALHADO; mantém cadastro de OS legado funcional. */
  FORMAL = 'DETALHADO'
}

export enum StatusOrcamento {
  RASCUNHO = 'RASCUNHO',
  /** @deprecated Use RASCUNHO; mantém clientes legados sem enviar PENDENTE inválido. */
  PENDENTE = 'RASCUNHO',
  ENVIADO = 'ENVIADO',
  APROVADO = 'APROVADO',
  REJEITADO = 'REJEITADO',
  CANCELADO = 'CANCELADO',
  EXPIRADO = 'EXPIRADO'
}

export enum MetodoEnvio {
  EMAIL = 'EMAIL',
  WHATSAPP = 'WHATSAPP',
  PRESENCIAL = 'PRESENCIAL',
  POSTAL = 'POSTAL',
  /** @deprecated Use PRESENCIAL; IMPRESSO não é valor válido na API. */
  IMPRESSO = 'PRESENCIAL'
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
