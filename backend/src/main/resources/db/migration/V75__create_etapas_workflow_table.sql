-- V75: Create etapas_workflow table
CREATE SEQUENCE IF NOT EXISTS seq_etapas_workflow START WITH 1 INCREMENT BY 1;

CREATE TABLE etapas_workflow (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_etapas_workflow'),
    workflow_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    ordem_execucao INT NOT NULL,
    obrigatoria BOOLEAN DEFAULT TRUE,
    tempo_estimado_minutos INT,
    responsavel_tipo VARCHAR(20),
    responsavel_especifico_id BIGINT,
    especialidade_requerida VARCHAR(100),
    permite_execucao_paralela BOOLEAN DEFAULT FALSE,
    exige_aprovacao BOOLEAN DEFAULT FALSE,
    aprovador_id BIGINT,
    checklist_obrigatorio BOOLEAN DEFAULT FALSE,
    fotos_obrigatorias BOOLEAN DEFAULT FALSE,
    quantidade_fotos_minima INT DEFAULT 0,
    notifica_cliente BOOLEAN DEFAULT FALSE,
    template_notificacao_id BIGINT,
    proximas_etapas_automaticas JSONB,
    condicoes_pulo_etapa JSONB,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_etapas_workflow_workflow FOREIGN KEY (workflow_id) REFERENCES workflow_os(id) ON DELETE CASCADE,
    CONSTRAINT chk_etapas_workflow_responsavel CHECK (responsavel_tipo IN ('QUALQUER', 'CONSULTOR', 'MECANICO', 'SUPERVISOR', 'ESPECIFICO'))
);

CREATE INDEX idx_etapas_workflow_workflow ON etapas_workflow(workflow_id);
CREATE INDEX idx_etapas_workflow_ordem ON etapas_workflow(ordem_execucao);
COMMENT ON TABLE etapas_workflow IS 'Etapas dos workflows de ordens de serviço';
