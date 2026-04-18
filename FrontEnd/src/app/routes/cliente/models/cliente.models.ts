// ========== ENUMS ==========

export enum TipoCliente {
  PESSOA_FISICA = 'PESSOA_FISICA',
  PESSOA_JURIDICA = 'PESSOA_JURIDICA'
}

export enum StatusCliente {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO',
  BLOQUEADO = 'BLOQUEADO'
}

export enum Sexo {
  M = 'M',
  F = 'F'
}

export enum EstadoCivil {
  SOLTEIRO = 'SOLTEIRO',
  CASADO = 'CASADO',
  DIVORCIADO = 'DIVORCIADO',
  VIUVO = 'VIUVO',
  UNIAO_ESTAVEL = 'UNIAO_ESTAVEL'
}

export enum OrigemCliente {
  SITE = 'SITE',
  TELEFONE = 'TELEFONE',
  INDICACAO = 'INDICACAO',
  REDES_SOCIAIS = 'REDES_SOCIAIS',
  OUTROS = 'OUTROS'
}

export enum TipoEndereco {
  RESIDENCIAL = 'RESIDENCIAL',
  COMERCIAL = 'COMERCIAL',
  COBRANCA = 'COBRANCA',
  ENTREGA = 'ENTREGA',
  OUTROS = 'OUTROS'
}

export enum TipoContato {
  TELEFONE_FIXO = 'TELEFONE_FIXO',
  CELULAR = 'CELULAR',
  WHATSAPP = 'WHATSAPP',
  TELEGRAM = 'TELEGRAM',
  OUTROS = 'OUTROS'
}

export enum TipoDocumento {
  CPF = 'CPF',
  RG = 'RG',
  CNH = 'CNH',
  CNPJ = 'CNPJ',
  CONTRATO_SOCIAL = 'CONTRATO_SOCIAL',
  COMPROVANTE_RESIDENCIA = 'COMPROVANTE_RESIDENCIA',
  OUTROS = 'OUTROS'
}

// ========== INTERFACES - PAGINAÇÃO ==========

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // current page index (0-based)
  size: number;   // page size
}

// ========== INTERFACES - CLIENTE ==========

export interface ClienteRequest {
  tipoCliente: TipoCliente;
  nomeCompleto?: string;
  email?: string;
  nomeFantasia?: string;
  razaoSocial?: string;
  cpf?: string;
  cnpj?: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  dataNascimento?: string; // YYYY-MM-DD
  sexo?: Sexo;
  estadoCivil?: EstadoCivil;
  profissao?: string;
  origemCliente?: OrigemCliente;
  detalhesOrigem?: string;
  status?: StatusCliente;
  dataBloqueio?: string; // ISO datetime
  motivoBloqueio?: string;
  observacoesGerais?: string;
}

export interface ClienteResponse extends ClienteRequest {
  id: number;
  empresaId: number;
}

// ========== INTERFACES - ENDEREÇO ==========

export interface EnderecoClienteRequest {
  cep: string;
  logradouro: string;
  numero: string;
  complemento?: string;
  bairro: string;
  cidade: string;
  estado: string; // UF (2 letras)
  pais?: string;
}

export interface EnderecoClienteResponse extends EnderecoClienteRequest {
  id: number;
  clienteId: number;
}

// ========== INTERFACES - CONTATO ==========

export interface ContatoClienteRequest {
  tipoContato: TipoContato;
  valor: string; // Obrigatório (UI)
}

export interface ContatoClienteResponse extends ContatoClienteRequest {
  id: number;
  clienteId: number;
}

// ========== INTERFACES - DOCUMENTO ==========

export interface DocumentoClienteRequest {
  tipoDocumento: TipoDocumento;
  descricao?: string;
  numeroDocumento?: string;
  dataEmissao?: string; // YYYY-MM-DD
  dataValidade?: string; // YYYY-MM-DD
  orgaoEmissor?: string;
  observacoes?: string;
}

export interface DocumentoClienteResponse extends DocumentoClienteRequest {
  id: number;
  clienteId: number;
  arquivoNome?: string;
  arquivoTipo?: string;
  arquivoTamanho?: number;
  arquivoCaminho?: string;
}

// ========== LABELS E HELPERS ==========

export const TipoClienteLabels: Record<TipoCliente, string> = {
  [TipoCliente.PESSOA_FISICA]: 'Pessoa Física',
  [TipoCliente.PESSOA_JURIDICA]: 'Pessoa Jurídica'
};

export const StatusClienteLabels: Record<StatusCliente, string> = {
  [StatusCliente.ATIVO]: 'Ativo',
  [StatusCliente.INATIVO]: 'Inativo',
  [StatusCliente.BLOQUEADO]: 'Bloqueado'
};

export const SexoLabels: Record<Sexo, string> = {
  [Sexo.M]: 'Masculino',
  [Sexo.F]: 'Feminino'
};

export const EstadoCivilLabels: Record<EstadoCivil, string> = {
  [EstadoCivil.SOLTEIRO]: 'Solteiro(a)',
  [EstadoCivil.CASADO]: 'Casado(a)',
  [EstadoCivil.DIVORCIADO]: 'Divorciado(a)',
  [EstadoCivil.VIUVO]: 'Viúvo(a)',
  [EstadoCivil.UNIAO_ESTAVEL]: 'União Estável'
};

export const OrigemClienteLabels: Record<OrigemCliente, string> = {
  [OrigemCliente.SITE]: 'Site',
  [OrigemCliente.TELEFONE]: 'Telefone',
  [OrigemCliente.INDICACAO]: 'Indicação',
  [OrigemCliente.REDES_SOCIAIS]: 'Redes Sociais',
  [OrigemCliente.OUTROS]: 'Outros'
};

export const TipoEnderecoLabels: Record<TipoEndereco, string> = {
  [TipoEndereco.RESIDENCIAL]: 'Residencial',
  [TipoEndereco.COMERCIAL]: 'Comercial',
  [TipoEndereco.COBRANCA]: 'Cobrança',
  [TipoEndereco.ENTREGA]: 'Entrega',
  [TipoEndereco.OUTROS]: 'Outros'
};

export const TipoContatoLabels: Record<TipoContato, string> = {
  [TipoContato.TELEFONE_FIXO]: 'Telefone Fixo',
  [TipoContato.CELULAR]: 'Celular',
  [TipoContato.WHATSAPP]: 'WhatsApp',
  [TipoContato.TELEGRAM]: 'Telegram',
  [TipoContato.OUTROS]: 'Outros'
};

export const TipoDocumentoLabels: Record<TipoDocumento, string> = {
  [TipoDocumento.CPF]: 'CPF',
  [TipoDocumento.RG]: 'RG',
  [TipoDocumento.CNH]: 'CNH',
  [TipoDocumento.CNPJ]: 'CNPJ',
  [TipoDocumento.CONTRATO_SOCIAL]: 'Contrato Social',
  [TipoDocumento.COMPROVANTE_RESIDENCIA]: 'Comprovante de Residência',
  [TipoDocumento.OUTROS]: 'Outros'
};

// Helper para obter opções de select
export const getTipoClienteOptions = () =>
  Object.values(TipoCliente).map(value => ({ label: TipoClienteLabels[value], value }));

export const getStatusClienteOptions = () =>
  Object.values(StatusCliente).map(value => ({ label: StatusClienteLabels[value], value }));

export const getSexoOptions = () =>
  Object.values(Sexo).map(value => ({ label: SexoLabels[value], value }));

export const getEstadoCivilOptions = () =>
  Object.values(EstadoCivil).map(value => ({ label: EstadoCivilLabels[value], value }));

export const getOrigemClienteOptions = () =>
  Object.values(OrigemCliente).map(value => ({ label: OrigemClienteLabels[value], value }));

export const getTipoEnderecoOptions = () =>
  Object.values(TipoEndereco).map(value => ({ label: TipoEnderecoLabels[value], value }));

export const getTipoContatoOptions = () =>
  Object.values(TipoContato).map(value => ({ label: TipoContatoLabels[value], value }));

export const getTipoDocumentoOptions = () =>
  Object.values(TipoDocumento).map(value => ({ label: TipoDocumentoLabels[value], value }));
