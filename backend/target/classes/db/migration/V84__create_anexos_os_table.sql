-- V84: Create anexos_os table
CREATE SEQUENCE IF NOT EXISTS seq_anexos_os START WITH 1 INCREMENT BY 1;

CREATE TABLE anexos_os (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_anexos_os'),
    ordem_servico_id BIGINT NOT NULL,
    tipo_anexo VARCHAR(20),
    nome_arquivo VARCHAR(255) NOT NULL,
    descricao TEXT,
    arquivo_url VARCHAR(500) NOT NULL,
    arquivo_tamanho BIGINT,
    tipo_mime VARCHAR(100),
    hash_arquivo VARCHAR(128),
    versao INT DEFAULT 1,
    categoria VARCHAR(100),
    tags VARCHAR(255),
    visivel_cliente BOOLEAN DEFAULT TRUE,
    requer_aprovacao BOOLEAN DEFAULT FALSE,
    aprovado BOOLEAN DEFAULT FALSE,
    data_aprovacao TIMESTAMP,
    aprovado_por BIGINT,
    usuario_upload_id BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao_registro INT DEFAULT 0,
    
    CONSTRAINT fk_anexos_os_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT chk_anexos_os_tipo CHECK (tipo_anexo IN ('DOCUMENTO', 'MANUAL', 'CERTIFICADO', 'GARANTIA', 'NOTA_FISCAL', 'COMPROVANTE', 'OUTROS'))
);

CREATE INDEX idx_anexos_os_os ON anexos_os(ordem_servico_id);
CREATE INDEX idx_anexos_os_tipo ON anexos_os(tipo_anexo);
COMMENT ON TABLE anexos_os IS 'Anexos das ordens de serviço';
