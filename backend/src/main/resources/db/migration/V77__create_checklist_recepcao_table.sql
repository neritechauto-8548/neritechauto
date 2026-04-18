-- V77: Create checklist_recepcao table
CREATE SEQUENCE IF NOT EXISTS seq_checklist_recepcao START WITH 1 INCREMENT BY 1;

CREATE TABLE checklist_recepcao (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_checklist_recepcao'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_veiculo_aplicavel JSONB,
    itens_checklist JSONB,
    obrigatorio BOOLEAN DEFAULT TRUE,
    permite_fotos BOOLEAN DEFAULT TRUE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_checklist_recepcao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_checklist_recepcao_empresa ON checklist_recepcao(empresa_id);
COMMENT ON TABLE checklist_recepcao IS 'Checklists de recepção de veículos';
