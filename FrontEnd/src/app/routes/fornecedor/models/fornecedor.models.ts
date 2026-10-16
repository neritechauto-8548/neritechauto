export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface FornecedorRequest {
  empresaId: number;
  tipoPessoa?: 'FISICA' | 'JURIDICA';
  nomeFantasia?: string;
  razaoSocial?: string;
  cpf?: string | null;
  cnpj?: string | null;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  emailPrincipal?: string;
  telefonePrincipal?: string;
  celularPrincipal?: string;
  website?: string;
  nomeContato?: string;
  cargoContato?: string;
  emailContato?: string;
  telefoneContato?: string;
  enderecoCompleto?: string;
  cep?: string;
  cidade?: string;
  estado?: string;
  prazoPagamentoDias?: number | null;
  limiteCredito?: number | null;
  descontoPadrao?: number | null;
  condicoesEspeciais?: string;
  classificacao?: 'A' | 'B' | 'C' | 'D' | 'E';
  observacoes?: string;
  ativo?: boolean;
  versao?: number;
  // Compatibilidade de consumidores legados; não é enviado como campo canônico.
  nome?: string;
}

export interface FornecedorResponse extends FornecedorRequest {
  id: number;
}

export interface HistoricoFornecedor {
  campo: string;
  anterior: string;
  novo: string;
  autor: number;
  data: string;
  versao: number;
}
