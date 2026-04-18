-- V59__create_itens_inventario_table.sql
CREATE SEQUENCE itens_inventario_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_inventario (
    id BIGINT PRIMARY KEY DEFAULT nextval('itens_inventario_seq'),
    inventario_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    localizacao_id BIGINT,
    lote_numero VARCHAR(50),
    quantidade_sistema DECIMAL(10,2) NOT NULL,
    quantidade_contada DECIMAL(10,2),
    diferenca DECIMAL(10,2),
    valor_unitario DECIMAL(10,4),
    valor_total_sistema DECIMAL(15,2),
    valor_total_contado DECIMAL(15,2),
    diferenca_valor DECIMAL(15,2),
    status VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'CONTADO', 'CONFERIDO', 'AJUSTADO')),
    motivo_diferenca TEXT,
    observacoes TEXT,
    usuario_contagem BIGINT,
    data_contagem TIMESTAMP,
    usuario_conferencia BIGINT,
    data_conferencia TIMESTAMP,
    foto_comprovante_url VARCHAR(500),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_item_inventario FOREIGN KEY (inventario_id) REFERENCES inventarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_item_localizacao FOREIGN KEY (localizacao_id) REFERENCES localizacoes_estoque(id)
);

CREATE INDEX idx_itens_inventario_inventario ON itens_inventario(inventario_id);
CREATE INDEX idx_itens_inventario_produto ON itens_inventario(produto_id);
CREATE INDEX idx_itens_inventario_status ON itens_inventario(status);
