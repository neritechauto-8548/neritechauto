// ========== ENUMS ==========

export enum StatusVeiculo {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO',
  VENDIDO = 'VENDIDO',
  SINISTRO = 'SINISTRO',
  BLOQUEADO = 'BLOQUEADO'
}

export enum CategoriaVeiculo {
  HATCH = 'HATCH',
  HATCHBACK = 'HATCHBACK',
  SEDAN = 'SEDAN',
  SUV = 'SUV',
  PICKUP = 'PICKUP',
  VAN = 'VAN',
  CONVERSIVEL = 'CONVERSIVEL',
  COUPE = 'COUPE',
  WAGON = 'WAGON',
  OUTROS = 'OUTROS'
}

export enum SegmentoVeiculo {
  COMPACTO = 'COMPACTO',
  MEDIO = 'MEDIO',
  GRANDE = 'GRANDE',
  LUXO = 'LUXO',
  ESPORTIVO = 'ESPORTIVO',
  UTILITARIO = 'UTILITARIO'
}

export enum TipoTracao {
  DIANTEIRA = 'DIANTEIRA',
  TRASEIRA = 'TRASEIRA',
  _4X4 = '_4X4',
  AWD = 'AWD'
}

export enum TipoFoto {
  EXTERNA_FRENTE = 'EXTERNA_FRENTE',
  EXTERNA_TRAS = 'EXTERNA_TRAS',
  EXTERNA_LATERAL_ESQUERDA = 'EXTERNA_LATERAL_ESQUERDA',
  EXTERNA_LATERAL_DIREITA = 'EXTERNA_LATERAL_DIREITA',
  INTERNA_PAINEL = 'INTERNA_PAINEL',
  INTERNA_BANCOS = 'INTERNA_BANCOS',
  MOTOR = 'MOTOR',
  PORTA_MALAS = 'PORTA_MALAS',
  DEFEITO = 'DEFEITO',
  OUTROS = 'OUTROS'
}

export enum TipoDocumentoVeiculo {
  CRLV = 'CRLV',
  CRV = 'CRV',
  SEGURO = 'SEGURO',
  IPVA = 'IPVA',
  DPVAT = 'DPVAT',
  LICENCIAMENTO = 'LICENCIAMENTO',
  VISTORIA = 'VISTORIA',
  OUTROS = 'OUTROS'
}

export enum StatusDocumentoVeiculo {
  REGULAR = 'REGULAR',
  VENCIDO = 'VENCIDO',
  PENDENTE = 'PENDENTE',
  ISENTO = 'ISENTO'
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

// ========== INTERFACES - VEÍCULO ==========

export interface VeiculoRequest {
  clienteId: number;
  marcaId?: number;
  modeloId?: number;
  anoModeloId?: number;
  combustivelId?: number;
  placa: string;
  renavam?: string;
  chassi?: string;
  numeroMotor?: string;
  corExterna?: string;
  quilometragemAtual?: number;
  quilometragemCadastro?: number;
  dataUltimaRevisao?: string; // YYYY-MM-DD
  proximaRevisaoKm?: number;
  proximaRevisaoData?: string; // YYYY-MM-DD
  status?: StatusVeiculo;
  observacoes?: string;
}

export interface VeiculoResponse extends VeiculoRequest {
  id: number;
  clienteNome?: string;
  marcaNome?: string;
  modeloNome?: string;
  anoFabricacao?: number;
  anoModelo?: number;
  combustivelNome?: string;
}

export interface TipoCombustivelResponse {
  id: number;
  nome: string;
}

// ========== INTERFACES - MARCA ==========

export interface MarcaVeiculoRequest {
  nome: string;
  logoUrl?: string;
  website?: string;
}

export interface MarcaVeiculoResponse extends MarcaVeiculoRequest {
  id: number;
  empresaId: number;
  ativo: boolean;
  createdDate?: string;
  lastModifiedDate?: string;
  createdBy?: number;
  lastModifiedBy?: number;
  version?: number;
}

// ========== INTERFACES - MODELO ==========

export interface ModeloVeiculoRequest {
  marcaId: number;
  nome: string;
  categoria: CategoriaVeiculo;
  segmento: SegmentoVeiculo;
  tipoTracao: TipoTracao;
  numeroPortas?: number;
  numeroLugares?: number;
}

export interface ModeloVeiculoResponse extends ModeloVeiculoRequest {
  id: number;
  marcaNome?: string;
}

// ========== INTERFACES - ANO MODELO ==========

export interface AnoModeloResponse {
  id: number;
  anoFabricacao: number;
  anoModelo: number;
  descricao?: string;
}

// ========== INTERFACES - FOTO ==========

export interface FotoVeiculoRequest {
  veiculoId: number;
  tipoFoto?: TipoFoto;
  descricao?: string;
  arquivoUrl: string;
  arquivoNome?: string;
  arquivoTamanho?: number;
  largura?: number;
  altura?: number;
  principal?: boolean;
  ordemExibicao?: number;
  dataFoto?: string; // ISO DateTime
  usuarioUpload?: number;
}

export interface FotoVeiculoResponse extends FotoVeiculoRequest {
  id: number;
}

// ========== INTERFACES - DOCUMENTO ==========

export interface DocumentoVeiculoRequest {
  veiculoId: number;
  tipoDocumento?: TipoDocumentoVeiculo;
  numeroDocumento?: string;
  orgaoEmissor?: string;
  dataEmissao?: string; // YYYY-MM-DD
  dataVencimento?: string; // YYYY-MM-DD
  status?: StatusDocumentoVeiculo;
  arquivoUrl?: string;
  arquivoNome?: string;
  arquivoTamanho?: number;
  observacoes?: string;
}

export interface DocumentoVeiculoResponse extends DocumentoVeiculoRequest {
  id: number;
}

// ========== LABELS E HELPERS ==========

export const StatusVeiculoLabels: Record<StatusVeiculo, string> = {
  [StatusVeiculo.ATIVO]: 'Ativo',
  [StatusVeiculo.INATIVO]: 'Inativo',
  [StatusVeiculo.VENDIDO]: 'Vendido',
  [StatusVeiculo.SINISTRO]: 'Sinistrado',
  [StatusVeiculo.BLOQUEADO]: 'Bloqueado'
};

export const CategoriaVeiculoLabels: Record<CategoriaVeiculo, string> = {
  [CategoriaVeiculo.HATCH]: 'Hatch',
  [CategoriaVeiculo.HATCHBACK]: 'Hatchback',
  [CategoriaVeiculo.SEDAN]: 'Sedan',
  [CategoriaVeiculo.SUV]: 'SUV',
  [CategoriaVeiculo.PICKUP]: 'Pickup',
  [CategoriaVeiculo.VAN]: 'Van',
  [CategoriaVeiculo.CONVERSIVEL]: 'Conversível',
  [CategoriaVeiculo.COUPE]: 'Coupé',
  [CategoriaVeiculo.WAGON]: 'Wagon',
  [CategoriaVeiculo.OUTROS]: 'Outros'
};

export const SegmentoVeiculoLabels: Record<SegmentoVeiculo, string> = {
  [SegmentoVeiculo.COMPACTO]: 'Compacto',
  [SegmentoVeiculo.MEDIO]: 'Médio',
  [SegmentoVeiculo.GRANDE]: 'Grande',
  [SegmentoVeiculo.LUXO]: 'Luxo',
  [SegmentoVeiculo.ESPORTIVO]: 'Esportivo',
  [SegmentoVeiculo.UTILITARIO]: 'Utilitário'
};

export const TipoTracaoLabels: Record<TipoTracao, string> = {
  [TipoTracao.DIANTEIRA]: 'Tração Dianteira',
  [TipoTracao.TRASEIRA]: 'Tração Traseira',
  [TipoTracao._4X4]: 'Tração 4x4',
  [TipoTracao.AWD]: 'All Wheel Drive'
};

export const TipoFotoLabels: Record<TipoFoto, string> = {
  [TipoFoto.EXTERNA_FRENTE]: 'Foto Externa Frontal',
  [TipoFoto.EXTERNA_TRAS]: 'Foto Externa Traseira',
  [TipoFoto.EXTERNA_LATERAL_ESQUERDA]: 'Foto Externa Lateral Esquerda',
  [TipoFoto.EXTERNA_LATERAL_DIREITA]: 'Foto Externa Lateral Direita',
  [TipoFoto.INTERNA_PAINEL]: 'Foto Interna do Painel',
  [TipoFoto.INTERNA_BANCOS]: 'Foto Interna dos Bancos',
  [TipoFoto.MOTOR]: 'Foto do Motor',
  [TipoFoto.PORTA_MALAS]: 'Foto do Porta-Malas',
  [TipoFoto.DEFEITO]: 'Foto de Defeito',
  [TipoFoto.OUTROS]: 'Outros'
};

export const TipoDocumentoVeiculoLabels: Record<TipoDocumentoVeiculo, string> = {
  [TipoDocumentoVeiculo.CRLV]: 'CRLV',
  [TipoDocumentoVeiculo.CRV]: 'CRV',
  [TipoDocumentoVeiculo.SEGURO]: 'Seguro',
  [TipoDocumentoVeiculo.IPVA]: 'IPVA',
  [TipoDocumentoVeiculo.DPVAT]: 'DPVAT',
  [TipoDocumentoVeiculo.LICENCIAMENTO]: 'Licenciamento',
  [TipoDocumentoVeiculo.VISTORIA]: 'Vistoria',
  [TipoDocumentoVeiculo.OUTROS]: 'Outros'
};

export const StatusDocumentoVeiculoLabels: Record<StatusDocumentoVeiculo, string> = {
  [StatusDocumentoVeiculo.REGULAR]: 'Regular',
  [StatusDocumentoVeiculo.VENCIDO]: 'Vencido',
  [StatusDocumentoVeiculo.PENDENTE]: 'Pendente',
  [StatusDocumentoVeiculo.ISENTO]: 'Isento'
};

// Helper para obter opções de select
export const getStatusVeiculoOptions = () =>
  Object.values(StatusVeiculo).map(value => ({ label: StatusVeiculoLabels[value], value }));

export const getCategoriaVeiculoOptions = () =>
  Object.values(CategoriaVeiculo).map(value => ({ label: CategoriaVeiculoLabels[value], value }));

export const getSegmentoVeiculoOptions = () =>
  Object.values(SegmentoVeiculo).map(value => ({ label: SegmentoVeiculoLabels[value], value }));

export const getTipoTracaoOptions = () =>
  Object.values(TipoTracao).map(value => ({ label: TipoTracaoLabels[value], value }));

export const getTipoFotoOptions = () =>
  Object.values(TipoFoto).map(value => ({ label: TipoFotoLabels[value], value }));

export const getTipoDocumentoVeiculoOptions = () =>
  Object.values(TipoDocumentoVeiculo).map(value => ({ label: TipoDocumentoVeiculoLabels[value], value }));

export const getStatusDocumentoVeiculoOptions = () =>
  Object.values(StatusDocumentoVeiculo).map(value => ({ label: StatusDocumentoVeiculoLabels[value], value }));
