-- Criação da tabela documentos_clientes com escopo de tenant (id_empresa)
CREATE TABLE IF NOT EXISTS documentos_clientes (
    id BIGSERIAL PRIMARY KEY,
    id_empresa BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,

    tipo_documento VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    arquivo_url VARCHAR(500) NOT NULL,
    arquivo_nome VARCHAR(255) NOT NULL,
    arquivo_tamanho BIGINT,
    arquivo_tipo VARCHAR(50),
    observacoes TEXT,

    -- Campos de auditoria herdados de BaseEntity
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER
);

-- FK para cliente
ALTER TABLE documentos_clientes
    ADD CONSTRAINT fk_documentos_clientes_cliente
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
    ON UPDATE NO ACTION ON DELETE RESTRICT;

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_documentos_clientes_id_empresa ON documentos_clientes(id_empresa);
CREATE INDEX IF NOT EXISTS idx_documentos_clientes_cliente_id ON documentos_clientes(cliente_id);
CREATE INDEX IF NOT EXISTS idx_documentos_clientes_tipo_documento ON documentos_clientes(tipo_documento);