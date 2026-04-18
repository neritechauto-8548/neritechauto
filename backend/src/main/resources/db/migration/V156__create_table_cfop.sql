-- V156: Create cfop table
-- Description: Fiscal Operations Codes

CREATE SEQUENCE IF NOT EXISTS seq_cfop START WITH 1 INCREMENT BY 1;

CREATE TABLE cfop (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_cfop'),
    codigo VARCHAR(4) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    aplicacao TEXT,
    dentro_estado BOOLEAN DEFAULT TRUE,
    fora_estado BOOLEAN DEFAULT FALSE,
    exterior BOOLEAN DEFAULT FALSE,
    tipo_operacao VARCHAR(20) CHECK (tipo_operacao IN ('ENTRADA', 'SAIDA')),
    gera_credito_icms BOOLEAN DEFAULT FALSE,
    gera_debito_icms BOOLEAN DEFAULT FALSE,
    movimenta_estoque BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_cfop_codigo UNIQUE (codigo)
);

CREATE INDEX idx_cfop_codigo ON cfop(codigo);

COMMENT ON TABLE cfop IS 'Código Fiscal de Operações e Prestações';
