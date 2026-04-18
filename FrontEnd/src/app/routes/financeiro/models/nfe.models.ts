export enum TipoOperacaoNfe {
    ENTRADA = 'ENTRADA',
    SAIDA = 'SAIDA'
}

export enum StatusNfe {
    EM_DIGITACAO = 'EM_DIGITACAO',
    TRANSMITIDA = 'TRANSMITIDA',
    AUTORIZADA = 'AUTORIZADA',
    REJEITADA = 'REJEITADA',
    CANCELADA = 'CANCELADA',
    DENEGADA = 'DENEGADA',
    INUTILIZADA = 'INUTILIZADA'
}

export enum AmbienteNfe {
    PRODUCAO = 'PRODUCAO',
    HOMOLOGACAO = 'HOMOLOGACAO'
}

export enum TipoEmissaoNfe {
    NORMAL = 'NORMAL',
    CONTINGENCIA = 'CONTINGENCIA'
}

export interface NfeRequest {
    faturaId?: number;
    chaveNfe?: string;
    numeroNfe?: string;
    serieNfe?: string;
    tipoOperacao: TipoOperacaoNfe;
    status?: StatusNfe;
    ambiente?: AmbienteNfe;
    tipoEmissao?: TipoEmissaoNfe;
    dataEmissao?: string;
    dataSaidaEntrada?: string;
    valorTotalNota: number;
    valorTotalProdutos: number;
    valorTotalServicos: number;
    xmlUrl?: string;
    danfeUrl?: string;
    protocoloAutorizacao?: string;
    mensagemRetorno?: string;
    observacoes?: string;
}

export interface NfeResponse {
    id: number;
    empresaId: number;
    faturaId?: number;
    chaveNfe?: string;
    numeroNfe?: string;
    serieNfe?: string;
    tipoOperacao: TipoOperacaoNfe;
    status: StatusNfe;
    ambiente: AmbienteNfe;
    tipoEmissao: TipoEmissaoNfe;
    dataEmissao: string;
    dataSaidaEntrada?: string;
    valorTotalNota: number;
    valorTotalProdutos: number;
    valorTotalServicos: number;
    xmlUrl?: string;
    danfeUrl?: string;
    protocoloAutorizacao?: string;
    mensagemRetorno?: string;
    observacoes?: string;
    createdAt?: string;
    updatedAt?: string;
}
