-- V160: Create configuracoes_nfe table
-- Description: NFe configuration per company

CREATE SEQUENCE IF NOT EXISTS seq_configuracoes_nfe START WITH 1 INCREMENT BY 1;

CREATE TABLE configuracoes_nfe (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_configuracoes_nfe'),
    empresa_id BIGINT,
    certificado_digital_id BIGINT NOT NULL,
    ambiente VARCHAR(20) DEFAULT 'HOMOLOGACAO' CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO')),
    uf_emitente CHAR(2) NOT NULL,
    serie_nfe INT DEFAULT 1,
    numeracao_atual BIGINT DEFAULT 1,
    serie_nfce INT DEFAULT 1,
    numeracao_nfce_atual BIGINT DEFAULT 1,
    formato_impressao_danfe VARCHAR(20) DEFAULT 'RETRATO' CHECK (formato_impressao_danfe IN ('RETRATO', 'PAISAGEM')),
    tipo_impressao VARCHAR(10) DEFAULT 'PDF' CHECK (tipo_impressao IN ('PDF', 'HTML')),
    logo_danfe_url VARCHAR(500),
    exibir_logo_danfe BOOLEAN DEFAULT TRUE,
    margem_superior DECIMAL(4,2) DEFAULT 0.8,
    margem_inferior DECIMAL(4,2) DEFAULT 0.8,
    margem_esquerda DECIMAL(4,2) DEFAULT 0.6,
    margem_direita DECIMAL(4,2) DEFAULT 0.6,
    salvar_xml_enviado BOOLEAN DEFAULT TRUE,
    salvar_xml_retorno BOOLEAN DEFAULT TRUE,
    pasta_xml VARCHAR(500),
    email_xml_automatico BOOLEAN DEFAULT FALSE,
    assunto_email_xml VARCHAR(255),
    corpo_email_xml TEXT,
    timeout_envio INT DEFAULT 30,
    tentativas_envio INT DEFAULT 3,
    contingencia_automatica BOOLEAN DEFAULT TRUE,
    justificativa_contingencia VARCHAR(255),
    observacoes_padrao TEXT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    configurado_por BIGINT,
    ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_configuracoes_nfe_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_configuracoes_nfe_certificado FOREIGN KEY (certificado_digital_id) REFERENCES certificados_digitais(id)
);

CREATE INDEX idx_configuracoes_nfe_empresa ON configuracoes_nfe(empresa_id);

COMMENT ON TABLE configuracoes_nfe IS 'Configurações de emissão de NFe/NFCe';
