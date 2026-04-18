-- V82: Create fotos_servicos table
CREATE SEQUENCE IF NOT EXISTS seq_fotos_servicos START WITH 1 INCREMENT BY 1;

CREATE TABLE fotos_servicos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_fotos_servicos'),
    ordem_servico_id BIGINT,
    etapa_workflow_id BIGINT,
    diagnostico_id BIGINT,
    tipo_foto VARCHAR(20),
    descricao VARCHAR(255),
    arquivo_url VARCHAR(500) NOT NULL,
    arquivo_nome VARCHAR(255),
    arquivo_tamanho BIGINT,
    largura INT,
    altura INT,
    localizacao_gps VARCHAR(50),
    data_foto TIMESTAMP,
    usuario_upload_id BIGINT,
    visivel_cliente BOOLEAN DEFAULT TRUE,
    ordem_exibicao INT DEFAULT 0,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_fotos_servicos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_fotos_servicos_etapa FOREIGN KEY (etapa_workflow_id) REFERENCES etapas_workflow(id),
    CONSTRAINT fk_fotos_servicos_diagnostico FOREIGN KEY (diagnostico_id) REFERENCES diagnosticos(id),
    CONSTRAINT chk_fotos_servicos_tipo CHECK (tipo_foto IN ('ANTES', 'DURANTE', 'DEPOIS', 'PROBLEMA', 'SOLUCAO', 'EVIDENCIA', 'PROGRESSO', 'OUTROS'))
);

CREATE INDEX idx_fotos_servicos_os ON fotos_servicos(ordem_servico_id);
CREATE INDEX idx_fotos_servicos_diagnostico ON fotos_servicos(diagnostico_id);
COMMENT ON TABLE fotos_servicos IS 'Fotos dos serviços executados';
