-- V148: Create logs_sistema table
-- Description: System application logs

CREATE SEQUENCE IF NOT EXISTS seq_logs_sistema START WITH 1 INCREMENT BY 1;

CREATE TABLE logs_sistema (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_logs_sistema'),
    empresa_id BIGINT, -- Nullable as some system logs might not be tied to a specific company
    nivel_log VARCHAR(10) CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    categoria VARCHAR(50) NOT NULL,
    modulo VARCHAR(50) NOT NULL,
    funcao VARCHAR(100),
    mensagem TEXT NOT NULL,
    stack_trace TEXT,
    dados_contexto TEXT, -- JSON
    usuario_id BIGINT,
    sessao_id VARCHAR(100),
    ip_origem VARCHAR(45),
    user_agent TEXT,
    url_requisicao VARCHAR(500),
    metodo_http VARCHAR(10),
    tempo_resposta_ms INT,
    memoria_utilizada_mb DECIMAL(8,2),
    cpu_utilizada_percent DECIMAL(5,2),
    exception_class VARCHAR(255),
    exception_message TEXT,
    correlacao_id VARCHAR(100),
    thread_id VARCHAR(50),
    servidor VARCHAR(100),
    versao_aplicacao VARCHAR(20),
    data_ocorrencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolvido BOOLEAN DEFAULT FALSE,
    data_resolucao TIMESTAMP,
    responsavel_resolucao BIGINT,
    observacoes_resolucao TEXT,

    CONSTRAINT fk_logs_sistema_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    -- TODO: Uncomment when usuarios table is created/confirmed
    -- CONSTRAINT fk_logs_sistema_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    CONSTRAINT fk_logs_sistema_responsavel FOREIGN KEY (responsavel_resolucao) REFERENCES funcionarios(id) -- Assuming responsible is an employee
);

CREATE INDEX idx_logs_sistema_empresa ON logs_sistema(empresa_id);
CREATE INDEX idx_logs_sistema_nivel ON logs_sistema(nivel_log);
CREATE INDEX idx_logs_sistema_data ON logs_sistema(data_ocorrencia);
CREATE INDEX idx_logs_sistema_correlacao ON logs_sistema(correlacao_id);

COMMENT ON TABLE logs_sistema IS 'Logs técnicos e de aplicação do sistema';
COMMENT ON COLUMN logs_sistema.dados_contexto IS 'JSON com dados contextuais do log';
