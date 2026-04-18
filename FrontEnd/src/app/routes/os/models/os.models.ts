// ========== ENUMS ==========

export enum TipoOS {
  MANUTENCAO = 'MANUTENCAO',
  REPARO = 'REPARO',
  REVISAO = 'REVISAO',
  DIAGNOSTICO = 'DIAGNOSTICO',
  ORCAMENTO = 'ORCAMENTO',
  GARANTIA = 'GARANTIA',
  RECALL = 'RECALL'
}

export enum PrioridadeOS {
  BAIXA = 'BAIXA',
  NORMAL = 'NORMAL',
  ALTA = 'ALTA',
  URGENTE = 'URGENTE'
}

export enum MetodoAprovacao {
  PRESENCIAL = 'PRESENCIAL',
  TELEFONE = 'TELEFONE',
  WHATSAPP = 'WHATSAPP',
  EMAIL = 'EMAIL',
  APP = 'APP'
}

export enum NivelCombustivel {
  VAZIO = 'VAZIO',
  RESERVA = 'RESERVA',
  UM_QUARTO = 'UM_QUARTO',
  MEIO = 'MEIO',
  TRES_QUARTOS = 'TRES_QUARTOS',
  CHEIO = 'CHEIO'
}

// ========== INTERFACES - PAGINAÇÃO ==========

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // current page index (0-based)
  size: number;   // page size
  pageable?: {
    pageNumber: number;
    pageSize: number;
  };
  last?: boolean;
}

// ========== INTERFACES - STATUS OS ==========

export interface StatusOSRequest {
  empresaId: number;
  nome: string;
  descricao?: string;
  codigo: string;
  corIdentificacao?: string;
  icone?: string;
  ordemExibicao?: number;
  permiteEdicao?: boolean;
  notificaCliente?: boolean;
  templateNotificacaoId?: number;
  exigeAprovacao?: boolean;
  finalizaOS?: boolean;
  cancelaOS?: boolean;
  ativo?: boolean;
}

export interface StatusOSResponse {
  id: number;
  empresaId: number;
  nome: string;
  descricao?: string;
  codigo: string;
  corIdentificacao?: string;
  icone?: string;
  ordemExibicao?: number;
  permiteEdicao?: boolean;
  notificaCliente?: boolean;
  templateNotificacaoId?: number;
  exigeAprovacao?: boolean;
  finalizaOS?: boolean;
  cancelaOS?: boolean;
  sistema?: boolean;
  ativo?: boolean;
  dataCadastro?: string;
  dataAtualizacao?: string;
  versao?: number;
}

// ========== INTERFACES - ORDEM DE SERVIÇO ==========

export interface OrdemServicoRequest {
  empresaId: number;
  numeroOS: string; // @NotBlank @Size(max = 20) - OBRIGATÓRIO
  clienteId?: number;
  veiculoId?: number;
  statusId?: number;
  tipoOS: TipoOS; // @NotNull - OBRIGATÓRIO
  prioridade?: PrioridadeOS;
  dataAbertura?: string; // ISO DateTime (LocalDateTime)
  dataPromessa?: string; // ISO DateTime (LocalDateTime)
  quilometragemEntrada?: number;
  nivelCombustivelEntrada?: NivelCombustivel;
  consultorResponsavelId?: number;
  mecanicoResponsavelId?: number;
  equipeExecucao?: any; // jsonb no backend
  problemaRelatado?: string;
  solucaoAplicada?: string;
  observacoesInternas?: string;
  observacoesCliente?: string;
  valorServicos?: number; // BigDecimal
  valorProdutos?: number; // BigDecimal
  valorDesconto?: number; // BigDecimal
  valorAcrescimo?: number; // BigDecimal
  valorTotal: number; // @NotNull BigDecimal - OBRIGATÓRIO
  formaPagamentoId?: number;
  condicaoPagamentoId?: number;
  valorEntrada?: number; // BigDecimal
  aprovadoCliente?: boolean;
  metodoAprovacao?: MetodoAprovacao;
  garantiaDias?: number;
}

export interface OrdemServicoResponse {
  id: number;
  empresaId: number;
  numeroOS: string;
  clienteId?: number;
  veiculoId?: number;
  statusId?: number;
  tipoOS: TipoOS;
  prioridade?: PrioridadeOS;
  dataAbertura?: string; // ISO DateTime
  dataPromessa?: string; // ISO DateTime
  dataInicioExecucao?: string; // ISO DateTime
  dataFimExecucao?: string; // ISO DateTime
  dataEntrega?: string; // ISO DateTime
  quilometragemEntrada?: number;
  quilometragemSaida?: number;
  nivelCombustivelEntrada?: NivelCombustivel;
  nivelCombustivelSaida?: NivelCombustivel;
  consultorResponsavelId?: number;
  mecanicoResponsavelId?: number;
  equipeExecucao?: any; // jsonb no backend
  problemaRelatado?: string;
  solucaoAplicada?: string;
  observacoesInternas?: string;
  observacoesCliente?: string;
  valorServicos?: number;
  valorProdutos?: number;
  valorDesconto?: number;
  valorAcrescimo?: number;
  valorTotal: number;
  formaPagamentoId?: number;
  condicaoPagamentoId?: number;
  valorEntrada?: number;
  valorFinanciado?: number;
  aprovadoCliente?: boolean;
  dataAprovacaoCliente?: string; // ISO DateTime
  metodoAprovacao?: MetodoAprovacao;
  garantiaDias?: number;
  dataVencimentoGarantia?: string; // YYYY-MM-DD (LocalDate)
  nfeEmitida?: boolean;
  numeroNFe?: string;
  notaAvaliacaoCliente?: number;
  tempoTotalExecucaoMinutos?: number;
  margemLucroRealizada?: number;
  dataCadastro?: string; // ISO DateTime
  dataAtualizacao?: string; // ISO DateTime
  versao?: number;

  // Campos de Visualização
  nomeCliente?: string;
  placaVeiculo?: string;
  nomeVeiculo?: string;
  statusNome?: string;
  statusCor?: string;
}

// ========== LABELS E HELPERS ==========

export const TipoOSLabels: Record<TipoOS, string> = {
  [TipoOS.MANUTENCAO]: 'Manutenção',
  [TipoOS.REPARO]: 'Reparo',
  [TipoOS.REVISAO]: 'Revisão',
  [TipoOS.DIAGNOSTICO]: 'Diagnóstico',
  [TipoOS.ORCAMENTO]: 'Orçamento',
  [TipoOS.GARANTIA]: 'Garantia',
  [TipoOS.RECALL]: 'Recall'
};

export const PrioridadeOSLabels: Record<PrioridadeOS, string> = {
  [PrioridadeOS.BAIXA]: 'Baixa',
  [PrioridadeOS.NORMAL]: 'Normal',
  [PrioridadeOS.ALTA]: 'Alta',
  [PrioridadeOS.URGENTE]: 'Urgente'
};

export const MetodoAprovacaoLabels: Record<MetodoAprovacao, string> = {
  [MetodoAprovacao.PRESENCIAL]: 'Presencial',
  [MetodoAprovacao.TELEFONE]: 'Telefone',
  [MetodoAprovacao.WHATSAPP]: 'WhatsApp',
  [MetodoAprovacao.EMAIL]: 'E-mail',
  [MetodoAprovacao.APP]: 'App'
};

export const NivelCombustivelLabels: Record<NivelCombustivel, string> = {
  [NivelCombustivel.VAZIO]: 'Vazio',
  [NivelCombustivel.RESERVA]: 'Reserva',
  [NivelCombustivel.UM_QUARTO]: '1/4',
  [NivelCombustivel.MEIO]: '1/2',
  [NivelCombustivel.TRES_QUARTOS]: '3/4',
  [NivelCombustivel.CHEIO]: 'Cheio'
};

// Helper para obter opções de select
export const getTipoOSOptions = () =>
  Object.values(TipoOS).map(value => ({ label: TipoOSLabels[value], value }));

export const getPrioridadeOSOptions = () =>
  Object.values(PrioridadeOS).map(value => ({ label: PrioridadeOSLabels[value], value }));

export const getMetodoAprovacaoOptions = () =>
  Object.values(MetodoAprovacao).map(value => ({ label: MetodoAprovacaoLabels[value], value }));

export const getNivelCombustivelOptions = () =>
  Object.values(NivelCombustivel).map(value => ({ label: NivelCombustivelLabels[value], value }));

// ========== INTERFACES - ITENS OS (PRODUTOS) ==========

export interface ItemOSProdutoRequest {
  ordemServicoId: number;
  produtoId?: number;
  descricao?: string;
  quantidade: number;
  valorUnitario: number;
  valorTotal: number;
  descontoPercentual?: number;
  descontoValor?: number;
  valorFinal: number;
  loteNumero?: string;
  localizacaoEstoqueId?: number;
  fornecedorId?: number;
  precoCusto?: number;
  garantiaMeses?: number;
  numeroSerie?: string;
  observacoes?: string;
  aprovadoCliente?: boolean;
}

export interface ItemOSProdutoResponse extends ItemOSProdutoRequest {
  id: number;
  nomeProduto?: string;
  codigoProduto?: string;
  // Campos de cálculo ou formatação extra se necessário
}

// ========== INTERFACES - ITENS OS (SERVIÇOS) ==========

export interface ItemOSServicoRequest {
  ordemServicoId: number;
  servicoId?: number;
  descricao?: string;
  quantidade?: number; // Backend usa BigDecimal, Frontend number
  valorUnitario: number;
  valorTotal: number;
  descontoPercentual?: number;
  descontoValor?: number;
  valorFinal: number;
  mecanicoExecutorId?: number;
  tempoExecucaoPrevisto?: number;
  statusExecucao?: string; // Enum StatusExecucao
  garantiaDias?: number;
  observacoes?: string;
  aprovadoCliente?: boolean;
  comissaoPercentual?: number;
  ordemExecucao?: number;
}

export interface ItemOSServicoResponse extends ItemOSServicoRequest {
  id: number;
  nomeServico?: string;
  codigoServico?: string;
}

// ========== INTERFACES - DIAGNÓSTICOS (SOLICITAÇÕES) ==========

export interface DiagnosticoRequest {
  ordemServicoId: number;
  problemaIdentificado?: string;
  causaProvavel?: string;
  solucaoRecomendada?: string;
  codigoErro?: string;
  sistemaVeiculo?: string;
  componenteEspecifico?: string;
  mecanicoDiagnosticoId?: number;
  testesRealizados?: string;
  evidenciasEncontradas?: string;
  impactoSeguranca?: string;
  impactoDirigibilidade?: string;
  urgencia?: string;
  ferramentasNecessarias?: string;
  pecasNecessarias?: string;
  tempoEstimadoReparo?: number;
  custoEstimado?: number;
  observacoes?: string;
  fotosDiagnostico?: string[];
  videosDiagnostico?: string[];
  aprovadoCliente?: boolean;
}

export interface DiagnosticoResponse extends DiagnosticoRequest {
  id: number;
  nomeResponsavel?: string;
}
