-- V57__create_movimentacoes_estoque_table.sql
CREATE SEQUENCE movimentacoes_estoque_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE movimentacoes_estoque (
    id BIGINT PRIMARY KEY DEFAULT nextval('movimentacoes_estoque_seq'),
    empresa_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    tipo_movimentacao VARCHAR(20) NOT NULL CHECK (tipo_movimentacao IN ('ENTRADA', 'SAIDA', 'TRANSFERENCIA', 'AJUSTE', 'INVENTARIO', 'PERDA', 'DEVOLUCAO')),
    subtipo_movimentacao VARCHAR(50),
    quantidade DECIMAL(10,2) NOT NULL,
    quantidade_anterior DECIMAL(10,2),
    quantidade_atual DECIMAL(10,2),
    valor_unitario DECIMAL(10,4),
    valor_total DECIMAL(15,2),
    localizacao_origem_id BIGINT,
    localizacao_destino_id BIGINT,
    documento_tipo VARCHAR(50),
    documento_numero VARCHAR(50),
    documento_id BIGINT,
    fornecedor_id BIGINT,
    lote_numero VARCHAR(50),
    data_validade DATE,
    data_fabricacao DATE,
    motivo TEXT,
    observacoes TEXT,
    usuario_responsavel BIGINT NOT NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ordem_servico_id BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_movimentacao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_movimentacao_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_movimentacao_loc_origem FOREIGN KEY (localizacao_origem_id) REFERENCES localizacoes_estoque(id),
    CONSTRAINT fk_movimentacao_loc_destino FOREIGN KEY (localizacao_destino_id) REFERENCES localizacoes_estoque(id),
    CONSTRAINT fk_movimentacao_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE INDEX idx_movimentacoes_empresa ON movimentacoes_estoque(empresa_id);
CREATE INDEX idx_movimentacoes_produto ON movimentacoes_estoque(produto_id);
CREATE INDEX idx_movimentacoes_tipo ON movimentacoes_estoque(tipo_movimentacao);
CREATE INDEX idx_movimentacoes_data ON movimentacoes_estoque(data_movimentacao);
CREATE INDEX idx_movimentacoes_documento ON movimentacoes_estoque(documento_tipo, documento_numero);
