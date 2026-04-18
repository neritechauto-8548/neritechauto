-- V164: Create webhooks table
-- Description: Outbound webhooks configuration

CREATE SEQUENCE IF NOT EXISTS seq_webhooks START WITH 1 INCREMENT BY 1;

CREATE TABLE webhooks (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_webhooks'),
    empresa_id BIGINT,
    nome_webhook VARCHAR(100) NOT NULL,
    url_destino VARCHAR(500) NOT NULL,
    eventos_interesse TEXT, -- JSON
    metodo_http VARCHAR(10) DEFAULT 'POST' CHECK (metodo_http IN ('POST', 'PUT', 'PATCH')),
    headers_customizados TEXT, -- JSON
    formato_payload VARCHAR(20) CHECK (formato_payload IN ('JSON', 'XML', 'FORM_URLENCODED')),
    template_payload TEXT,
    autenticacao_tipo VARCHAR(20) CHECK (autenticacao_tipo IN ('NONE', 'API_KEY', 'BEARER', 'BASIC', 'CUSTOM')),
    autenticacao_dados TEXT, -- JSON
    secret_token VARCHAR(255),
    ssl_verificacao BOOLEAN DEFAULT TRUE,
    timeout_segundos INT DEFAULT 30,
    max_tentativas INT DEFAULT 3,
    intervalo_retry_segundos INT DEFAULT 60,
    ativo BOOLEAN DEFAULT TRUE,
    eventos_processados BIGINT DEFAULT 0,
    eventos_sucesso BIGINT DEFAULT 0,
    eventos_erro BIGINT DEFAULT 0,
    ultima_execucao TIMESTAMP,
    proximo_retry TIMESTAMP,
    status_atual VARCHAR(20) CHECK (status_atual IN ('ATIVO', 'ERRO', 'DESABILITADO')),
    observacoes TEXT,
    configurado_por BIGINT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_webhooks_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_webhooks_empresa ON webhooks(empresa_id);

COMMENT ON TABLE webhooks IS 'Configuração de webhooks de saída';
