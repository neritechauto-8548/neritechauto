-- Documentos de clientes: armazenamento binário no banco, isolado por empresa.
-- Limite da aplicação: 5 MB por arquivo; formatos PDF, JPEG e PNG.
CREATE TABLE documentos_clientes (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    cliente_id BIGINT NOT NULL REFERENCES cliente(id) ON DELETE CASCADE,
    tipo_documento VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),
    arquivo_nome VARCHAR(180) NOT NULL,
    arquivo_tipo VARCHAR(80) NOT NULL,
    arquivo_tamanho BIGINT NOT NULL,
    arquivo_conteudo BYTEA NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_documentos_clientes_arquivo_tamanho CHECK (arquivo_tamanho > 0 AND arquivo_tamanho <= 5242880)
);
CREATE INDEX idx_documentos_clientes_empresa_cliente
    ON documentos_clientes (empresa_id, cliente_id);
