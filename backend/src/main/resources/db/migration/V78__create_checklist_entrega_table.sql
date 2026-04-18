-- V78: Create checklist_entrega table
CREATE SEQUENCE IF NOT EXISTS seq_checklist_entrega START WITH 1 INCREMENT BY 1;

CREATE TABLE checklist_entrega (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_checklist_entrega'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_veiculo_aplicavel JSONB,
    itens_checklist JSONB,
    obrigatorio BOOLEAN DEFAULT TRUE,
    permite_fotos BOOLEAN DEFAULT TRUE,
    exige_assinatura_cliente BOOLEAN DEFAULT TRUE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_checklist_entrega_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_checklist_entrega_empresa ON checklist_entrega(empresa_id);
COMMENT ON TABLE checklist_entrega IS 'Checklists de entrega de veículos';
