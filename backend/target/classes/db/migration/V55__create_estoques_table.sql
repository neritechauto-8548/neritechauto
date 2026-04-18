-- V55__create_estoques_table.sql
CREATE SEQUENCE estoques_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE estoques (
    id BIGINT PRIMARY KEY DEFAULT nextval('estoques_seq'),
    empresa_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade_atual DECIMAL(10,2),
    fornecedor_id BIGINT,
    preco_custo_lote DECIMAL(10,4),
    nota_fiscal_numero VARCHAR(50),
    certificado_qualidade_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'BLOQUEADO', 'VENCIDO', 'RECALL')),
    motivo_bloqueio TEXT,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_estoque_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_estoque_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_estoque_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE INDEX idx_estoques_empresa ON estoques(empresa_id);
CREATE INDEX idx_estoques_produto ON estoques(produto_id);
CREATE INDEX idx_estoques_fornecedor ON estoques(fornecedor_id);
CREATE INDEX idx_estoques_status ON estoques(status);
