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
}

export interface ProdutoResponse extends ProdutoRequest {
  id: number;
  categoriaNome?: string;
  unidadeMedidaSigla?: string;
}

