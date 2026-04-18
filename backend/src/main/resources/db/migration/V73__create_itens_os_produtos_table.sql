-- V73: Create itens_os_produtos table
-- Description: Product items for service orders

CREATE SEQUENCE IF NOT EXISTS seq_itens_os_produtos START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_os_produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_itens_os_produtos'),
    ordem_servico_id BIGINT NOT NULL,
    produto_id BIGINT,
    descricao TEXT,
    quantidade DECIMAL(10,2) NOT NULL,
    valor_unitario DECIMAL(10,4) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    desconto_percentual DECIMAL(5,2) DEFAULT 0,
    desconto_valor DECIMAL(10,2) DEFAULT 0,
    valor_final DECIMAL(10,2) NOT NULL,
    lote_numero VARCHAR(50),
    localizacao_estoque_id BIGINT,
    quantidade_reservada DECIMAL(10,2),
    quantidade_utilizada DECIMAL(10,2),
    data_reserva TIMESTAMP,
    data_utilizacao TIMESTAMP,
    fornecedor_id BIGINT,
    preco_custo DECIMAL(10,4),
    margem_lucro_realizada DECIMAL(5,2),
    garantia_meses INT DEFAULT 0,
    numero_serie VARCHAR(100),
    observacoes TEXT,
    aprovado_cliente BOOLEAN DEFAULT FALSE,
    data_aprovacao_cliente TIMESTAMP,
    devolvido BOOLEAN DEFAULT FALSE,
    quantidade_devolvida DECIMAL(10,2) DEFAULT 0,
    motivo_devolucao TEXT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_itens_os_produtos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_os_produtos_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE INDEX idx_itens_os_produtos_os ON itens_os_produtos(ordem_servico_id);
CREATE INDEX idx_itens_os_produtos_produto ON itens_os_produtos(produto_id);
CREATE INDEX idx_itens_os_produtos_lote ON itens_os_produtos(lote_numero);
CREATE INDEX idx_itens_os_produtos_fornecedor ON itens_os_produtos(fornecedor_id);

COMMENT ON TABLE itens_os_produtos IS 'Itens de produtos das ordens de serviço';
