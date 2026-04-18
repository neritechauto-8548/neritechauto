-- V161: Create sped_fiscal table
-- Description: SPED Fiscal generation history

CREATE SEQUENCE IF NOT EXISTS seq_sped_fiscal START WITH 1 INCREMENT BY 1;

CREATE TABLE sped_fiscal (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_sped_fiscal'),
    empresa_id BIGINT,
    periodo_referencia VARCHAR(6) NOT NULL,
    data_inicial DATE NOT NULL,
    data_final DATE NOT NULL,
    versao_layout VARCHAR(10) DEFAULT '016',
    finalidade_arquivo VARCHAR(1) DEFAULT '0' CHECK (finalidade_arquivo IN ('0', '1')),
    perfil_apresentacao VARCHAR(1) DEFAULT 'A' CHECK (perfil_apresentacao IN ('A', 'B', 'C')),
    atividade_preponderante VARCHAR(7),
    codigo_versao VARCHAR(6),
    nome_arquivo_gerado VARCHAR(255),
    caminho_arquivo VARCHAR(500),
    tamanho_arquivo_bytes BIGINT,
    total_registros INT,
    hash_arquivo VARCHAR(128),
    data_inicio_geracao TIMESTAMP,
    data_fim_geracao TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('EM_PROCESSAMENTO', 'GERADO', 'TRANSMITIDO', 'ERRO', 'VALIDADO')),
    erros_validacao TEXT, -- JSON
    protocolo_envio VARCHAR(50),
    data_transmissao TIMESTAMP,
    recibo_entrega VARCHAR(100),
    observacoes TEXT,
    gerado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sped_fiscal_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_sped_fiscal_empresa ON sped_fiscal(empresa_id);
CREATE INDEX idx_sped_fiscal_periodo ON sped_fiscal(periodo_referencia);

COMMENT ON TABLE sped_fiscal IS 'Histórico de geração do SPED Fiscal';
