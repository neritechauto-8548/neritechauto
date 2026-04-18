-- V162: Create integracoes_ativas table
-- Description: External integrations configuration

CREATE SEQUENCE IF NOT EXISTS seq_integracoes_ativas START WITH 1 INCREMENT BY 1;

CREATE TABLE integracoes_ativas (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_integracoes_ativas'),
    empresa_id BIGINT,
    nome_integracao VARCHAR(100) NOT NULL,
    tipo_integracao VARCHAR(20) CHECK (tipo_integracao IN ('API_REST', 'WEBHOOK', 'FTP', 'EMAIL', 'ARQUIVO', 'DATABASE')),
    categoria VARCHAR(20) CHECK (categoria IN ('FISCAL', 'BANCARIA', 'PAGAMENTO', 'COMUNICACAO', 'ERP', 'CRM', 'OUTROS')),
    sistema_externo VARCHAR(100) NOT NULL,
    versao_api VARCHAR(20),
    url_base VARCHAR(500),
    endpoint_principal VARCHAR(500),
    metodo_autenticacao VARCHAR(20) CHECK (metodo_autenticacao IN ('API_KEY', 'OAUTH2', 'JWT', 'BASIC', 'BEARER', 'CUSTOM')),
    credenciais_acesso TEXT, -- JSON
    headers_padrao TEXT, -- JSON
    parametros_padrao TEXT, -- JSON
    timeout_requisicao INT DEFAULT 30,
    retry_attempts INT DEFAULT 3,
    intervalo_retry INT DEFAULT 5,
    rate_limit_por_minuto INT,
    ativa BOOLEAN DEFAULT TRUE,
    ambiente VARCHAR(20) CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO', 'DESENVOLVIMENTO')),
    logs_habilitados BOOLEAN DEFAULT TRUE,
    nivel_log VARCHAR(10) CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR')),
    webhook_url VARCHAR(500),
    webhook_eventos TEXT, -- JSON
    webhook_secret VARCHAR(255),
    mapeamento_campos TEXT, -- JSON
    transformacoes_dados TEXT, -- JSON
    validacoes_ativas TEXT, -- JSON
    frequencia_sincronizacao VARCHAR(20) CHECK (frequencia_sincronizacao IN ('TEMPO_REAL', 'MINUTO', 'HORA', 'DIA', 'MANUAL')),
    proximo_sync TIMESTAMP,
    ultima_sincronizacao TIMESTAMP,
    total_requisicoes BIGINT DEFAULT 0,
    total_erros BIGINT DEFAULT 0,
    uptime_percentual DECIMAL(5,2),
    observacoes TEXT,
    configurada_por BIGINT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_integracoes_ativas_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_integracoes_ativas_empresa ON integracoes_ativas(empresa_id);
CREATE INDEX idx_integracoes_ativas_sistema ON integracoes_ativas(sistema_externo);

COMMENT ON TABLE integracoes_ativas IS 'Configuração de integrações com sistemas externos';
