-- V54__create_comparativos_cotacao_table.sql
CREATE SEQUENCE comparativos_cotacao_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE comparativos_cotacao (
    id BIGINT PRIMARY KEY DEFAULT nextval('comparativos_cotacao_seq'),
    cotacao_id BIGINT NOT NULL,
    item_cotacao_id BIGINT NOT NULL,
    resposta_cotacao_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    produto_oferecido VARCHAR(255),
    marca_oferecida VARCHAR(100),
    modelo_oferecido VARCHAR(100),
    especificacoes_oferecidas TEXT,
    quantidade_oferecida DECIMAL(10,2),
    preco_unitario DECIMAL(10,4) NOT NULL,
    preco_total_item DECIMAL(10,2) NOT NULL,
    prazo_entrega_item INTEGER,
    garantia_item TEXT,
    observacoes_item_fornecedor TEXT,
    atende_especificacao BOOLEAN DEFAULT TRUE,
    produto_alternativo BOOLEAN DEFAULT FALSE,
    justificativa_alternativo TEXT,
    vantagens_oferecidas TEXT,
    pontuacao_item DECIMAL(6,2),
    classificacao_item INTEGER,
    recomendado BOOLEAN DEFAULT FALSE,
    motivo_recomendacao TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_comparativo_cotacao FOREIGN KEY (cotacao_id) REFERENCES cotacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_comparativo_item FOREIGN KEY (item_cotacao_id) REFERENCES itens_cotacao(id) ON DELETE CASCADE,
    CONSTRAINT fk_comparativo_resposta FOREIGN KEY (resposta_cotacao_id) REFERENCES respostas_cotacao(id) ON DELETE CASCADE,
    CONSTRAINT fk_comparativo_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE INDEX idx_comparativos_cotacao ON comparativos_cotacao(cotacao_id);
CREATE INDEX idx_comparativos_item ON comparativos_cotacao(item_cotacao_id);
CREATE INDEX idx_comparativos_resposta ON comparativos_cotacao(resposta_cotacao_id);
CREATE INDEX idx_comparativos_fornecedor ON comparativos_cotacao(fornecedor_id);
CREATE INDEX idx_comparativos_recomendado ON comparativos_cotacao(item_cotacao_id, recomendado);
