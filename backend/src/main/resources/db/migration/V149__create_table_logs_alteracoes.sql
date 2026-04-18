-- V149: Create logs_alteracoes table
-- Description: Audit logs for data changes

CREATE SEQUENCE IF NOT EXISTS seq_logs_alteracoes START WITH 1 INCREMENT BY 1;

CREATE TABLE logs_alteracoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_logs_alteracoes'),
    empresa_id BIGINT,
    tabela_afetada VARCHAR(100) NOT NULL,
    registro_id BIGINT NOT NULL,
    operacao VARCHAR(10) CHECK (operacao IN ('INSERT', 'UPDATE', 'DELETE', 'SELECT')),
    campos_alterados TEXT, -- JSON
    valores_antigos TEXT, -- JSON
    valores_novos TEXT, -- JSON
    usuario_responsavel BIGINT NOT NULL,
    ip_origem VARCHAR(45),
    user_agent TEXT,
    motivo_alteracao TEXT,
    contexto_operacao VARCHAR(255),
    dados_adicionais TEXT, -- JSON
    checkpoint_antes TEXT, -- JSON
    checkpoint_depois TEXT, -- JSON
    reversivel BOOLEAN DEFAULT FALSE,
    comando_reversao TEXT,
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    auditado BOOLEAN DEFAULT FALSE,
    data_auditoria TIMESTAMP,
    auditor_responsavel BIGINT,
    observacoes_auditoria TEXT,

    CONSTRAINT fk_logs_alteracoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
    -- CONSTRAINT fk_logs_alteracoes_usuario FOREIGN KEY (usuario_responsavel) REFERENCES usuarios(id)
);

CREATE INDEX idx_logs_alteracoes_empresa ON logs_alteracoes(empresa_id);
CREATE INDEX idx_logs_alteracoes_tabela_registro ON logs_alteracoes(tabela_afetada, registro_id);
CREATE INDEX idx_logs_alteracoes_usuario ON logs_alteracoes(usuario_responsavel);
CREATE INDEX idx_logs_alteracoes_data ON logs_alteracoes(data_alteracao);

COMMENT ON TABLE logs_alteracoes IS 'Auditoria de alterações de dados (CDC)';
COMMENT ON COLUMN logs_alteracoes.campos_alterados IS 'JSON com lista de campos modificados';
