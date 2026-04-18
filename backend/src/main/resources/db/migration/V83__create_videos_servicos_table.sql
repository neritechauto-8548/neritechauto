-- V83: Create videos_servicos table
CREATE SEQUENCE IF NOT EXISTS seq_videos_servicos START WITH 1 INCREMENT BY 1;

CREATE TABLE videos_servicos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_videos_servicos'),
    ordem_servico_id BIGINT,
    diagnostico_id BIGINT,
    tipo_video VARCHAR(20),
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    arquivo_url VARCHAR(500) NOT NULL,
    arquivo_nome VARCHAR(255),
    arquivo_tamanho BIGINT,
    duracao_segundos INT,
    formato VARCHAR(10),
    qualidade VARCHAR(10),
    thumbnail_url VARCHAR(500),
    data_video TIMESTAMP,
    usuario_upload_id BIGINT,
    visivel_cliente BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_videos_servicos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_videos_servicos_diagnostico FOREIGN KEY (diagnostico_id) REFERENCES diagnosticos(id),
    CONSTRAINT chk_videos_servicos_tipo CHECK (tipo_video IN ('EXPLICATIVO', 'DEMONSTRATIVO', 'PROBLEMA', 'TESTE', 'OUTROS'))
);

CREATE INDEX idx_videos_servicos_os ON videos_servicos(ordem_servico_id);
CREATE INDEX idx_videos_servicos_diagnostico ON videos_servicos(diagnostico_id);
COMMENT ON TABLE videos_servicos IS 'Vídeos dos serviços executados';
