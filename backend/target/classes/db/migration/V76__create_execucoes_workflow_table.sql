-- V76: Create execucoes_workflow table
CREATE SEQUENCE IF NOT EXISTS seq_execucoes_workflow START WITH 1 INCREMENT BY 1;

CREATE TABLE execucoes_workflow (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_execucoes_workflow'),
    ordem_servico_id BIGINT NOT NULL,
    etapa_workflow_id BIGINT NOT NULL,
    status VARCHAR(20),
    data_inicio TIMESTAMP,
    data_fim TIMESTAMP,
    tempo_execucao_real_minutos INT,
    usuario_executor_id BIGINT,
    usuario_aprovador_id BIGINT,
    data_aprovacao TIMESTAMP,
    motivo_rejeicao TEXT,
    motivo_pulo TEXT,
    observacoes_execucao TEXT,
    checklist_executado JSONB,
    fotos_anexadas JSONB,
    documentos_anexados JSONB,
    proximo_responsavel_id BIGINT,
    data_notificacao_cliente TIMESTAMP,
    cliente_notificado BOOLEAN DEFAULT FALSE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_execucoes_workflow_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_execucoes_workflow_etapa FOREIGN KEY (etapa_workflow_id) REFERENCES etapas_workflow(id),
    CONSTRAINT chk_execucoes_workflow_status CHECK (status IN ('PENDENTE', 'EM_EXECUCAO', 'CONCLUIDA', 'APROVADA', 'REJEITADA', 'PULADA', 'CANCELADA'))
);

CREATE INDEX idx_execucoes_workflow_os ON execucoes_workflow(ordem_servico_id);
CREATE INDEX idx_execucoes_workflow_etapa ON execucoes_workflow(etapa_workflow_id);
CREATE INDEX idx_execucoes_workflow_status ON execucoes_workflow(status);
COMMENT ON TABLE execucoes_workflow IS 'Execuções de etapas de workflow';
