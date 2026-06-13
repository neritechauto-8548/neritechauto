// ========== INTERFACES - PAGINAÇÃO ==========

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first?: boolean;
  last?: boolean;
}

// ========== ENUMS ==========

export enum OrigemProduto {
  NACIONAL = '0',
  ESTRANGEIRA_IMPORTACAO_DIRETA = '1',
  ESTRANGEIRA_ADQUIRIDA_NO_MERCADO_INTERNO = '2'
}

// ========== INTERFACES - FISCAL ==========

export interface ProdutoFiscal {
  ceanTrib?: string;
  cfop?: string;
  ncm?: string;
  cest?: string;
  unComercial?: string;
  qtdComercial?: number;
  valorUnitComercial?: number;
  unTrib?: string;
  qtdTrib?: number;
  valorUnitTributavel?: number;
  totalSeguro?: number;
  desconto?: number;
  totalFrete?: number;
  outrasDespesas?: number;
  valorTotalBruto?: number;
  exTipi?: string;
  indicadorEscalaRelevante?: string;
  cnpjFabricante?: string;
  codigoBeneficioFiscal?: string;
  valorBrutoCompoeTotal?: boolean;
  pedidoCompra?: string;
  itemPedidoCompra?: number;
  numeroFci?: string;
  impostoFederalAprox?: number;
  impostoEstadualAprox?: number;

  ipiSitTrib?: string;
  ipiClasseEnquadramento?: string;
  ipiCodEnquadramento?: string;
  ipiCnpjProdutor?: string;
  ipiCodSeloControle?: string;
  ipiQtdSelo?: number;
  ipiTipoCalculo?: string;
  ipiBaseCalc?: number;
  ipiAliquota?: number;
  ipiValorUnidTrib?: number;
  ipiValor?: number;

  icmsSitTrib?: string;
  icmsOrigem?: string;
  icmsModBc?: string;
  icmsRedBc?: number;
  icmsBaseCalc?: number;
  icmsAliquota?: number;
  icmsMotivoDesoneracao?: string;
  icmsValorDesoneracao?: number;
  icmsValor?: number;
  icmsBcStUfOrigem?: number;
  icmsRetidoAntes?: number;
  icmsBcStUfDest?: number;
  icmsStUfDestino?: number;
  icmsAliqCalcCredito?: number;
  icmsValorCredito?: number;
  icmsModBcSt?: string;
  icmsMargemValorAdicionado?: number;
  icmsPrecoUnitPautaSt?: number;
  icmsRedBcSt?: number;
  icmsBaseCalcSt?: number;
  icmsAliquotaSt?: number;
  icmsValorSt?: number;
  icmsPercBcOpPropria?: number;
  icmsUfPgtoSt?: string;
  icmsPercFcp?: number;
  icmsValorFcp?: number;
  icmsBcFcp?: number;
  icmsValorBcRetSt?: number;
  icmsPercFcpRetSt?: number;
  icmsVbCalcFcpRetAntSt?: number;
  icmsPercFcpRetAntSt?: number;
  icmsValorBcFcpUfDest?: number;

  pisSitTrib?: string;
  pisTipoCalculo?: string;
  pisBaseCalc?: number;
  pisAliquota?: number;
  pisQv?: number;
  pisRv?: number;
  pisRvbc?: number;
  pisValorUnidTrib?: number;
  pisTipoCalculoSt?: string;
  pisAliquotaSt?: number;
  pisValorUnidTribSt?: number;

  cofinsSitTrib?: string;
  cofinsBaseCalc?: number;
  cofinsAliquota?: number;
  cofinsValorUnidTrib?: number;
  cofinsValor?: number;
  cofinsTipoCalculoSt?: string;
  cofinsAliquotaSt?: number;
  cofinsValorUnidTribSt?: number;

  importValorBc?: number;
  importValorDespAduaneiras?: number;
  importValorImposto?: number;
  importValorIof?: number;

  combCodAnp?: string;
  combPercGlp?: number;
  combCodAutorizacaoCodif?: string;
  combUfConsumo?: string;
  combBcCide?: number;
  combAliquotaCide?: number;
  combValorCide?: number;
  combDescAnp?: string;
  combPercGlpDerivPetroleo?: number;
  combPercGasNac?: number;
  combPercGasImp?: number;
  combValorPartida?: number;

  ibsSitTrib?: string;
  ibsClassificacaoTrib?: string;
  ibsAliquotaUf?: number;
  ibsPercDiferimentoUf?: number;
  ibsPercRedAliquotaUf?: number;
  ibsAliquotaMunicipio?: number;
  cbsAliquota?: number;
  cbsPercDiferimento?: number;
  cbsPercRedAliquota?: number;
}

// ========== INTERFACES - DEPENDÊNCIAS ==========

export interface CategoriaProdutoResponse {
  id: number;
  nome: string;
  descricao?: string;
  ativo: boolean;
}

export interface UnidadeMedidaResponse {
  id: number;
  nome: string;
  sigla: string;
  ativo: boolean;
}

// ========== INTERFACES - PRODUTO ==========

export interface ProdutoRequest {
  empresaId: number;
  categoriaId?: number;
  unidadeMedidaId?: number;

  // Identificação
  codigoInterno: string;
  codigoBarras?: string;
  codigoFabricante?: string;
  nome: string;
  descricao?: string;
  descricaoDetalhada?: string;
  marca?: string;
  modelo?: string;
  aplicacao?: string;
  especificacoesTecnicas?: string;

  // Logística
  pesoLiquido?: number;
  pesoBruto?: number;
  dimensoesComprimento?: number;
  dimensoesLargura?: number;
  dimensoesAltura?: number;

  // Preço
  precoCompra?: number;
  precoCusto: number;
  precoVenda: number;
  margemLucroPercentual?: number;

  // Estoque
  estoqueMinimo?: number;
  estoqueMaximo?: number;
  pontoReposicao?: number;
  controlaLote?: boolean;
  controlaValidade?: boolean;
  diasValidade?: number;

  // Fiscal
  ncm?: string;
  cest?: string;
  origemProduto?: string; // Usar string para simplificar select, ou enum

  // Outros
  fotoPrincipalUrl?: string;
  garantiaMeses?: number;
  observacoes?: string;
  ativo?: boolean;
  destaque?: boolean;
  promocional?: boolean;
  pontosFidelidade?: number;
  comissaoVendaPercentual?: number;
  quantidadeEstoque?: number;
  
  enderecoEstoque?: string;
  setor?: string;
  dataVencimento?: string; // LocalDate is string in TS (ISO format yyyy-MM-dd)
  codigoSubstituto1?: string;
  codigoSubstituto2?: string;
  descontoFornecedorPercentual?: number;
  
  dadosFiscais?: ProdutoFiscal;
}

export interface ProdutoResponse extends ProdutoRequest {
  id: number;
  categoriaNome?: string;
  unidadeMedidaSigla?: string;
}

