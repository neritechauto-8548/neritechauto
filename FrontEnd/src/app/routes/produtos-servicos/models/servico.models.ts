// ========== PAGINAÇÃO ==========
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

// ========== SERVIÇO ==========
export interface ServicoRequest {
  codigo?: string;
  nome: string;
  areaId: number;
  unidade: string;
  tempoPrevistoHoras: number;
  precoBase: number;
  custo: number;
  aliquotaIss?: number;
  codigoServicoMunicipal?: string;
  instrucoesExecucao?: string;
  ativo?: boolean;
}

export interface ServicoResponse {
  id: number;
  empresaId: number;
  codigo?: string;
  nome: string;
  areaId: number;
  unidade: string;
  tempoPrevistoHoras: number;
  precoBase: number;
  custo: number;
  aliquotaIss?: number;
  codigoServicoMunicipal?: string;
  instrucoesExecucao?: string;
  ativo: boolean;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

export type CreateServicoPayload = ServicoRequest;
