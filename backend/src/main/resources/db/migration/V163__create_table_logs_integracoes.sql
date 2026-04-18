-- V163: Create logs_integracoes table
-- Description: Integration logs

CREATE SEQUENCE IF NOT EXISTS seq_logs_integracoes START WITH 1 INCREMENT BY 1;

CREATE TABLE logs_integracoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_logs_integracoes'),
    integracao_id BIGINT,
    tipo_operacao VARCHAR(20) CHECK (tipo_operacao IN ('ENVIO', 'RECEBIMENTO', 'SINCRONIZACAO', 'WEBHOOK', 'AUTENTICACAO')),
    metodo_http VARCHAR(10),
    url_requisicao VARCHAR(500),
    headers_requisicao TEXT, -- JSON
    payload_requisicao TEXT, -- LONGTEXT
    status_http INT,
    tempo_resposta_ms INT,
    headers_resposta TEXT, -- JSON
    payload_resposta TEXT, -- LONGTEXT
    sucesso BOOLEAN NOT NULL,
    codigo_erro VARCHAR(50),
    mensagem_erro TEXT,
    stack_trace TEXT, -- LONGTEXT
    dados_processados INT,
    dados_erro INT,
    tentativa_numero INT DEFAULT 1,
    reprocessado BOOLEAN DEFAULT FALSE,
    data_reprocessamento TIMESTAMP,
    contexto_adicional TEXT, -- JSON
    usuario_origem BIGINT,
    ip_origem VARCHAR(45),
    user_agent TEXT,
    correlacao_id VARCHAR(100),
    data_requisicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_logs_integracoes_integracao FOREIGN KEY (integracao_id) REFERENCES integracoes_ativas(id)
);

CREATE INDEX idx_logs_integracoes_integracao ON logs_integracoes(integracao_id);
CREATE INDEX idx_logs_integracoes_data ON logs_integracoes(data_requisicao);
CREATE INDEX idx_logs_integracoes_correlacao ON logs_integracoes(correlacao_id);

COMMENT ON TABLE logs_integracoes IS 'Logs detalhados de integrações';
