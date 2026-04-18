-- V151: Create exportacoes table
-- Description: Data export requests and history

CREATE SEQUENCE IF NOT EXISTS seq_exportacoes START WITH 1 INCREMENT BY 1;

CREATE TABLE exportacoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_exportacoes'),
    empresa_id BIGINT,
    usuario_solicitante BIGINT NOT NULL,
    tipo_exportacao VARCHAR(100) NOT NULL,
    formato_arquivo VARCHAR(10) CHECK (formato_arquivo IN ('CSV', 'EXCEL', 'PDF', 'JSON', 'XML', 'TXT')),
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(500),
    tamanho_arquivo_bytes BIGINT,
    criterios_exportacao TEXT, -- JSON
    campos_exportados TEXT, -- JSON
    total_registros INT,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    duracao_segundos INT,
    status VARCHAR(20) CHECK (status IN ('PROCESSANDO', 'CONCLUIDA', 'FALHOU', 'CANCELADA')),
    progresso_percentual INT DEFAULT 0,
    erro_exportacao TEXT,
    url_download VARCHAR(500),
    data_expiracao_download TIMESTAMP,
    downloads_realizados INT DEFAULT 0,
    limite_downloads INT DEFAULT 5,
    senha_protegida BOOLEAN DEFAULT FALSE,
    notificar_conclusao BOOLEAN DEFAULT TRUE,
    notificacao_enviada BOOLEAN DEFAULT FALSE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_exportacoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
    -- CONSTRAINT fk_exportacoes_usuario FOREIGN KEY (usuario_solicitante) REFERENCES usuarios(id)
);

CREATE INDEX idx_exportacoes_empresa ON exportacoes(empresa_id);
CREATE INDEX idx_exportacoes_usuario ON exportacoes(usuario_solicitante);
CREATE INDEX idx_exportacoes_status ON exportacoes(status);

COMMENT ON TABLE exportacoes IS 'Histórico de exportações de dados';
