export interface UsuarioRequest {
  nomeCompleto: string;
  email: string;
  senha?: string;
  ativo: boolean;
  bloqueado: boolean;
  funcoesIds: number[];
  cargo?: string;
  departamento?: string;
  telefone?: string;
}

export interface UsuarioResponse {
  id: number;
  empresaId: number;
  nomeCompleto: string;
  email: string;
  ativo: boolean;
  bloqueado: boolean;
  ultimoAcesso?: string;
  funcoes: string[];
  cargo?: string;
  departamento?: string;
  telefone?: string;
  avatarUrl?: string;
}

export interface Permissao {
    id: number;
    nome: string;
    descricao: string;
}
