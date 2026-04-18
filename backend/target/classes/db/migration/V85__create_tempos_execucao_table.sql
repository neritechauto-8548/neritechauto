-- V85: Create tempos_execucao table
CREATE SEQUENCE IF NOT EXISTS seq_tempos_execucao START WITH 1 INCREMENT BY 1;

CREATE TABLE tempos_execucao (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_tempos_execucao'),
    ordem_servico_id BIGINT,
    etapa_workflow_id BIGINT,
    servico_id BIGINT,
    funcionario_id BIGINT,
    tipo_tempo VARCHAR(20),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    duracao_minutos INT,
    descricao_atividade TEXT,
    produtivo BOOLEAN DEFAULT TRUE,
    motivo_improdutivo TEXT,
    observacoes TEXT,
    pausas_realizadas JSONB,
    equipamentos_utilizados JSONB,
    eficiencia_percentual DECIMAL(5,2),
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_tempos_execucao_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_tempos_execucao_etapa FOREIGN KEY (etapa_workflow_id) REFERENCES etapas_workflow(id),
    CONSTRAINT chk_tempos_execucao_tipo CHECK (tipo_tempo IN ('DIAGNOSTICO', 'EXECUCAO', 'TESTE', 'ESPERA', 'RETRABALHO'))
);

CREATE INDEX idx_tempos_execucao_os ON tempos_execucao(ordem_servico_id);
CREATE INDEX idx_tempos_execucao_funcionario ON tempos_execucao(funcionario_id);
CREATE INDEX idx_tempos_execucao_tipo ON tempos_execucao(tipo_tempo);
COMMENT ON TABLE tempos_execucao IS 'Registro de tempos de execução';
