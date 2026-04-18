export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface FornecedorRequest {
  empresaId: number;
  tipoPessoa?: 'PF' | 'PJ';
  nome: string;
  nomeFantasia?: string;
  cnpj?: string;
  cpf?: string;
  rg?: string;
  ie?: string;
  contato?: string;
  telefones?: string[];
  email?: string;
  site?: string;
  endereco?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  cep?: string;
  banco?: string;
  conta?: string;
  agencia?: string;
  obs?: string;
  ativo?: boolean;
}

export interface FornecedorResponse extends FornecedorRequest {
  id: number;
}
