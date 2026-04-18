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
  nome: string;
  precoBase: number;
  custo: number;
  instrucoesExecucao?: string;
  ativo?: boolean;
}

export interface ServicoResponse {
  id: number;
  empresaId: number;
  nome: string;
  precoBase: number;
  custo: number;
  instrucoesExecucao?: string;
  ativo: boolean;
  dataCadastro?: string;
  dataAtualizacao?: string;
}

export type CreateServicoPayload = ServicoRequest;
