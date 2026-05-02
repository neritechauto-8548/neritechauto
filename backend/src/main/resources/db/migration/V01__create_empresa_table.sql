-- V01__create_empresa_table.sql
CREATE SCHEMA IF NOT EXISTS public;

CREATE SEQUENCE empresa_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS empresa (
    id BIGINT PRIMARY KEY DEFAULT nextval('empresa_seq'),
    nome_fantasia VARCHAR(255),
    razao_social VARCHAR(255),
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    inscricao_estadual VARCHAR(30),
    inscricao_municipal VARCHAR(30),
    email VARCHAR(120),
    telefone VARCHAR(30),
    site VARCHAR(120),
    data_abertura DATE,
    observacoes VARCHAR(2000),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0
);

-- Índices e restrições
CREATE INDEX IF NOT EXISTS idx_empresa_cnpj ON empresa(cnpj);
CREATE INDEX IF NOT EXISTS idx_empresa_ativo ON empresa(ativo);
CREATE UNIQUE INDEX IF NOT EXISTS uk_empresa_cnpj ON empresa(cnpj);
CREATE INDEX IF NOT EXISTS idx_empresa_razao_social ON empresa(razao_social);
CREATE INDEX IF NOT EXISTS idx_empresa_nome_fantasia ON empresa(nome_fantasia);

