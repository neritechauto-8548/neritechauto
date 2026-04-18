-- V159: Create certificados_digitais table
-- Description: Digital certificates management

CREATE SEQUENCE IF NOT EXISTS seq_certificados_digitais START WITH 1 INCREMENT BY 1;

CREATE TABLE certificados_digitais (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_certificados_digitais'),
    empresa_id BIGINT,
    tipo_certificado VARCHAR(2) CHECK (tipo_certificado IN ('A1', 'A3')),
    nome_certificado VARCHAR(255) NOT NULL,
    numero_serie VARCHAR(100),
    emissor VARCHAR(255),
    titular VARCHAR(255),
    cpf_cnpj_titular VARCHAR(18) NOT NULL,
    data_emissao DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    arquivo_certificado TEXT, -- LONGTEXT
    senha_certificado VARCHAR(255),
    token_serie VARCHAR(100),
    pin_token VARCHAR(20),
    caminho_biblioteca VARCHAR(500),
    ativo BOOLEAN DEFAULT TRUE,
    principal BOOLEAN DEFAULT FALSE,
    usado_nfe BOOLEAN DEFAULT TRUE,
    usado_nfce BOOLEAN DEFAULT FALSE,
    usado_cte BOOLEAN DEFAULT FALSE,
    usado_mdfe BOOLEAN DEFAULT FALSE,
    data_instalacao TIMESTAMP,
    ultima_utilizacao TIMESTAMP,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,

    CONSTRAINT fk_certificados_digitais_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_certificados_digitais_empresa ON certificados_digitais(empresa_id);
CREATE INDEX idx_certificados_digitais_vencimento ON certificados_digitais(data_vencimento);

COMMENT ON TABLE certificados_digitais IS 'Certificados digitais (A1, A3) para emissão fiscal';
