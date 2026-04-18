-- V74: Create workflow_os table
CREATE SEQUENCE IF NOT EXISTS seq_workflow_os START WITH 1 INCREMENT BY 1;

CREATE TABLE workflow_os (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_workflow_os'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_os_aplicavel JSONB,
    ativo BOOLEAN DEFAULT TRUE,
    padrao BOOLEAN DEFAULT FALSE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_workflow_os_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_workflow_os_empresa ON workflow_os(empresa_id);
CREATE INDEX idx_workflow_os_ativo ON workflow_os(ativo);
COMMENT ON TABLE workflow_os IS 'Definições de workflows para ordens de serviço';
