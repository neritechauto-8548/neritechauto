CREATE TABLE itens_pedido_fornecedor (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade NUMERIC(10,2) NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2),
    empresa_id BIGINT NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_itens_pedido_fornecedor_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos_fornecedor(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_pedido_fornecedor_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_itens_pedido_forn_pedido ON itens_pedido_fornecedor(pedido_id);
CREATE INDEX idx_itens_pedido_forn_empresa ON itens_pedido_fornecedor(empresa_id);
