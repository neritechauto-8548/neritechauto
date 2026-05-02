// --- Empresa ---
export interface Empresa {
  id?: number;
  nomeFantasia: string;
  razaoSocial: string;
  cnpj: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  email?: string;
  telefone?: string;
  site?: string;
  dataAbertura?: string;
  observacoes?: string;
  ativo?: boolean;
}

// --- Endereço da Empresa ---
export interface EnderecoEmpresa {
  id?: number;
  empresaId: number;
  tipoEndereco?: 'MATRIZ' | 'FILIAL' | 'ADMINISTRATIVO' | 'DEPOSITO';
  cep: string;
  logradouro: string;
  numero: string;
  complemento?: string;
  bairro: string;
  cidade: string;
  estado: string;
  pais?: string;
  principal?: boolean;
  ativo?: boolean;
}

// --- Configuração Empresa (dados cadastrais / Receita Federal) ---
export interface ConfiguracaoEmpresa {
  id?: number;
  empresaId: number;
  regimeTributario?: 'SIMPLES_NACIONAL' | 'LUCRO_PRESUMIDO' | 'LUCRO_REAL';
  codigoCnaePrincipal?: string;
  codigoCnaeSecundario?: string;
  capitalSocial?: number;
  anexoSimples?: 'I' | 'II' | 'III' | 'IV' | 'V';
  porteEmpresa?: 'MEI' | 'ME' | 'EPP' | 'MEDIO' | 'GRANDE';
  tipoEstabelecimento?: 'MATRIZ' | 'FILIAL';
  situacaoCadastral?: 'ATIVA' | 'SUSPENSA' | 'INAPTA' | 'BAIXADA' | 'NULA';
  dataSituacaoCadastral?: string;
  motivoSituacaoCadastral?: string;
  situacaoEspecial?: string;
  dataSituacaoEspecial?: string;
}

// --- Configuração Fiscal ---
export interface ConfiguracaoFiscal {
  id?: number;
  empresaId: number;
  regimeTributario?: 'SIMPLES_NACIONAL' | 'LUCRO_PRESUMIDO' | 'LUCRO_REAL';
  anexoSimples?: string;
  aliquotaIss?: number;
  aliquotaIcms?: number;
  aliquotaPis?: number;
  aliquotaCofins?: number;
  aliquotaCsll?: number;
  aliquotaIrpj?: number;
  codigoMunicipioPrestacao?: string;
  cfopVendaDentroEstado?: string;
  cfopVendaForaEstado?: string;
  cfopServico?: string;
  serieNfe?: number;
  numeracaoNfe?: number;
  ambienteNfe?: 'HOMOLOGACAO' | 'PRODUCAO';
  certificadoDigitalA1?: string;
  senhaCertificado?: string;
  validadeCertificado?: string;
}

// --- Configuração Oficina (horários e regras operacionais) ---
export interface ConfiguracaoOficina {
  id?: number;
  empresaId: number;
  horarioAberturaSegSex?: string;
  horarioFechamentoSegSex?: string;
  horarioAberturaSabado?: string;
  horarioFechamentoSabado?: string;
  funcionaDomingo?: boolean;
  horarioAberturaDomingo?: string;
  horarioFechamentoDomingo?: string;
  tempoAgendamentoPadrao?: number;
  antecedenciaMinimaAgendamento?: number;
  permiteAgendamentoOnline?: boolean;
  enviaLembreteAgendamento?: boolean;
  tempoLembreteHoras?: number;
  margemLucroPadrao?: number;
  diasGarantiaPadrao?: number;
  moeda?: string;
  formatoData?: string;
  formatoHora?: string;
  timezone?: string;
}

// --- Configuração Email ---
export interface ConfiguracaoEmail {
  id?: number;
  empresaId: number;
  provedorServico: 'SMTP' | 'SMTP_CUSTOMIZADO' | 'AWS_SES' | 'SENDGRID' | 'MAILGUN';
  servidorSmtp?: string;
  portaSmtp?: number;
  usuarioSmtp?: string;
  senhaSmtp?: string;
  criptografia?: 'NONE' | 'SSL' | 'TLS';
  remetenteNome?: string;
  remetenteEmail?: string;
  emailResposta?: string;
  emailCopiaOculta?: string;
  apiKey?: string;
  apiSecret?: string;
  dominioAutorizado?: string;
  limiteEnviosDia?: number;
  webhookEntrega?: string;
  webhookAbertura?: string;
  webhookClique?: string;
  assinaturaPadrao?: string;
  templateCabecalho?: string;
  templateRodape?: string;
  ativo?: boolean;
  observacoes?: string;
}

// --- Configuração WhatsApp ---
export interface ConfiguracaoWhatsapp {
  id?: number;
  empresaId: number;
  tipoIntegracao?: 'WHATSAPP_BUSINESS_API' | 'WEBHOOK' | 'CHATBOT' | 'TERCEIROS';
  provedorServico?: string;
  numeroWhatsapp: string;
  tokenAcesso?: string;
  webhookUrl?: string;
  webhookToken?: string;
  phoneNumberId?: string;
  businessAccountId?: string;
  appId?: string;
  appSecret?: string;
  horarioAtendimentoInicio?: string;
  horarioAtendimentoFim?: string;
  mensagemForaHorario?: string;
  mensagemBoasVindas?: string;
  chatbotAtivo?: boolean;
  integracaoAtiva?: boolean;
  ambiente?: 'HOMOLOGACAO' | 'PRODUCAO';
  observacoes?: string;
}

// --- Configuração SMS ---
export interface ConfiguracaoSms {
  id?: number;
  empresaId: number;
  provedorServico: 'TWILIO' | 'ZENVIA' | 'INFOBIP' | 'SINCH' | 'NEXMO' | 'TOTALVOICE';
  apiEndpoint?: string;
  apiKey?: string;
  apiSecret?: string;
  usuarioApi?: string;
  senhaApi?: string;
  remetentePadrao?: string;
  limiteCaracteres?: number;
  custoPorSms?: number;
  creditosDisponiveis?: number;
  webhookEntrega?: string;
  webhookResposta?: string;
  ativo?: boolean;
  ambiente?: 'HOMOLOGACAO' | 'PRODUCAO';
  observacoes?: string;
}
