-- Criação da tabela contatos_clientes com escopo de tenant (id_empresa)
CREATE TABLE IF NOT EXISTS contatos_clientes (
    id BIGSERIAL PRIMARY KEY,
    id_empresa BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,

    tipo_contato VARCHAR(20) NOT NULL,
    contato VARCHAR(255) NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,

    -- Campos de auditoria herdados de BaseEntity
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER
);

-- FK para cliente
ALTER TABLE contatos_clientes
    ADD CONSTRAINT fk_contatos_clientes_cliente
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
    ON UPDATE NO ACTION ON DELETE RESTRICT;

-- Índices de performance
CREATE INDEX IF NOT EXISTS idx_contatos_clientes_id_empresa ON contatos_clientes(id_empresa);
CREATE INDEX IF NOT EXISTS idx_contatos_clientes_cliente_id ON contatos_clientes(cliente_id);
CREATE INDEX IF NOT EXISTS idx_contatos_clientes_tipo_contato ON contatos_clientes(tipo_contato);
CREATE INDEX IF NOT EXISTS idx_contatos_clientes_ativo ON contatos_clientes(ativo);

-- Regra: apenas um contato principal por cliente
CREATE UNIQUE INDEX IF NOT EXISTS ux_contatos_clientes_principal_true
    ON contatos_clientes(cliente_id)
    WHERE principal = TRUE;