-- V165: Create tokens_api table
-- Description: API tokens management

CREATE SEQUENCE IF NOT EXISTS seq_tokens_api START WITH 1 INCREMENT BY 1;

CREATE TABLE tokens_api (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_tokens_api'),
    empresa_id BIGINT,
    integracao_id BIGINT,
    nome_token VARCHAR(100) NOT NULL,
    tipo_token VARCHAR(20) CHECK (tipo_token IN ('ACCESS_TOKEN', 'REFRESH_TOKEN', 'API_KEY', 'CLIENT_SECRET')),
    valor_token TEXT NOT NULL,
    escopo_permissoes TEXT, -- JSON
    data_criacao TIMESTAMP NOT NULL,
    data_expiracao TIMESTAMP,
    renovacao_automatica BOOLEAN DEFAULT FALSE,
    token_renovacao TEXT,
    endpoint_renovacao VARCHAR(500),
    status VARCHAR(20) CHECK (status IN ('ATIVO', 'EXPIRADO', 'REVOGADO', 'SUSPENSO')),
    ultimo_uso TIMESTAMP,
    total_usos BIGINT DEFAULT 0,
    limite_uso_diario INT,
    usos_hoje INT DEFAULT 0,
    ip_restricoes TEXT, -- JSON
    observacoes TEXT,
    criado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tokens_api_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_tokens_api_integracao FOREIGN KEY (integracao_id) REFERENCES integracoes_ativas(id)
);

CREATE INDEX idx_tokens_api_empresa ON tokens_api(empresa_id);
CREATE INDEX idx_tokens_api_integracao ON tokens_api(integracao_id);

COMMENT ON TABLE tokens_api IS 'Gerenciamento de tokens de API';
