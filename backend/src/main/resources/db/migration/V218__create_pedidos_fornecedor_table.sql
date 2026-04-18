CREATE TABLE pedidos_fornecedor (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    numero_pedido BIGINT NOT NULL,
    responsavel VARCHAR(255) NOT NULL,
    data_previsao DATE,
    numero_nf VARCHAR(100),
    observacao TEXT,
    descricao_interna TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_pedido_fornecedor_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_pedido_fornecedor_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT uq_pedido_empresa_numero UNIQUE (empresa_id, numero_pedido),
    CONSTRAINT chk_status_pedido_fornecedor CHECK (status IN ('PENDENTE', 'RECEBIDO', 'CANCELADO'))
);

CREATE INDEX idx_pedidos_fornecedor_empresa ON pedidos_fornecedor(empresa_id);
CREATE INDEX idx_pedidos_fornecedor_fornecedor ON pedidos_fornecedor(fornecedor_id);

CREATE SEQUENCE seq_pedido_fornecedor_numero START 1 INCREMENT 1;
