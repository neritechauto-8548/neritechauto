-- V62__create_lotes_produtos_table.sql
CREATE SEQUENCE lotes_produtos_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE lotes_produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('lotes_produtos_seq'),
    produto_id BIGINT NOT NULL,
    numero_lote VARCHAR(50) NOT NULL,
    data_fabricacao DATE,
    data_validade DATE,
    quantidade_inicial DECIMAL(10,2) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_lote_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_lotes_produto ON lotes_produtos(produto_id);
CREATE INDEX idx_lotes_numero ON lotes_produtos(numero_lote);
CREATE INDEX idx_lotes_validade ON lotes_produtos(data_validade);
