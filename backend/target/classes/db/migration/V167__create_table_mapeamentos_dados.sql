-- V167: Create mapeamentos_dados table
-- Description: Data mapping rules

CREATE SEQUENCE IF NOT EXISTS seq_mapeamentos_dados START WITH 1 INCREMENT BY 1;

CREATE TABLE mapeamentos_dados (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_mapeamentos_dados'),
    integracao_id BIGINT,
    entidade_origem VARCHAR(100) NOT NULL,
    campo_origem VARCHAR(100) NOT NULL,
    entidade_destino VARCHAR(100) NOT NULL,
    campo_destino VARCHAR(100) NOT NULL,
    tipo_transformacao VARCHAR(20) CHECK (tipo_transformacao IN ('DIRETO', 'FORMATACAO', 'CONVERSAO', 'CALCULO', 'LOOKUP', 'CONDICIONAL')),
    regra_transformacao TEXT,
    valor_padrao TEXT,
    obrigatorio BOOLEAN DEFAULT FALSE,
    validacao_formato VARCHAR(255),
    validacao_regex VARCHAR(255),
    erro_transformacao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    ordem_processamento INT DEFAULT 0,
    observacoes TEXT,
    criado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_mapeamentos_dados_integracao FOREIGN KEY (integracao_id) REFERENCES integracoes_ativas(id)
);

CREATE INDEX idx_mapeamentos_dados_integracao ON mapeamentos_dados(integracao_id);

COMMENT ON TABLE mapeamentos_dados IS 'Regras de mapeamento de dados entre sistemas';
