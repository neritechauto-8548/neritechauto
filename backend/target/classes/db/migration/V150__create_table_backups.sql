-- V150: Create backups table
-- Description: Database and system backups registry

CREATE SEQUENCE IF NOT EXISTS seq_backups START WITH 1 INCREMENT BY 1;

CREATE TABLE backups (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_backups'),
    empresa_id BIGINT,
    tipo_backup VARCHAR(20) CHECK (tipo_backup IN ('COMPLETO', 'INCREMENTAL', 'DIFERENCIAL', 'TRANSACIONAL')),
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(500) NOT NULL,
    tamanho_arquivo_bytes BIGINT,
    hash_arquivo VARCHAR(128),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    duracao_minutos INT,
    status VARCHAR(20) CHECK (status IN ('EM_ANDAMENTO', 'CONCLUIDO', 'FALHOU', 'CANCELADO')),
    automatico BOOLEAN DEFAULT TRUE,
    tabelas_incluidas TEXT, -- JSON
    total_registros BIGINT,
    compressao_utilizada VARCHAR(50),
    taxa_compressao DECIMAL(5,2),
    senha_protegido BOOLEAN DEFAULT FALSE,
    criptografado BOOLEAN DEFAULT FALSE,
    algoritmo_criptografia VARCHAR(50),
    local_armazenamento VARCHAR(20) CHECK (local_armazenamento IN ('LOCAL', 'NUVEM', 'EXTERNO')),
    provedor_nuvem VARCHAR(50),
    url_download VARCHAR(500),
    data_expiracao TIMESTAMP,
    testado_restauracao BOOLEAN DEFAULT FALSE,
    data_teste_restauracao TIMESTAMP,
    resultado_teste TEXT,
    erro_backup TEXT,
    observacoes TEXT,
    executado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_backups_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_backups_empresa ON backups(empresa_id);
CREATE INDEX idx_backups_data ON backups(data_inicio);
CREATE INDEX idx_backups_status ON backups(status);

COMMENT ON TABLE backups IS 'Registro de backups do sistema';
