-- V154: Create regimes_tributarios table
-- Description: Tax regimes configuration

CREATE SEQUENCE IF NOT EXISTS seq_regimes_tributarios START WITH 1 INCREMENT BY 1;

CREATE TABLE regimes_tributarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_regimes_tributarios'),
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    aliquotas_padrao TEXT, -- JSON
    obrigacoes_acessorias TEXT, -- JSON
    limites_faturamento TEXT, -- JSON
    anexos_aplicaveis TEXT, -- JSON
    ativo BOOLEAN DEFAULT TRUE,
    data_vigencia_inicio DATE NOT NULL,
    data_vigencia_fim DATE,
    observacoes TEXT,

    CONSTRAINT uk_regimes_tributarios_codigo UNIQUE (codigo)
);

COMMENT ON TABLE regimes_tributarios IS 'Regimes tributários (Simples Nacional, Lucro Presumido, Lucro Real)';
