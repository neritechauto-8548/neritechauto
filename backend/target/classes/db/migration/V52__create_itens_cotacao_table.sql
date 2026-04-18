-- V52__create_itens_cotacao_table.sql
CREATE SEQUENCE itens_cotacao_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_cotacao (
    id BIGINT PRIMARY KEY DEFAULT nextval('itens_cotacao_seq'),
    cotacao_id BIGINT NOT NULL,
    produto_id BIGINT,
    servico_id BIGINT,
    codigo_item VARCHAR(50),
    descricao_item TEXT NOT NULL,
    especificacoes_tecnicas TEXT,
    unidade_medida VARCHAR(20),
    quantidade_solicitada DECIMAL(10,2) NOT NULL,
    observacoes_item TEXT,
    alternativas_aceitas BOOLEAN DEFAULT FALSE,
    descricao_alternativas TEXT,
    prioridade_item VARCHAR(20) CHECK (prioridade_item IN ('ESSENCIAL', 'IMPORTANTE', 'DESEJAVEL')),
    categoria_item VARCHAR(100),
    marca_preferencial VARCHAR(100),
    modelo_preferencial VARCHAR(100),
    anexos_especificacoes JSONB,
    prazo_entrega_item_dias INTEGER,
    ordem_item INTEGER DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_item_cotacao FOREIGN KEY (cotacao_id) REFERENCES cotacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_item_servico FOREIGN KEY (servico_id) REFERENCES servicos(id)
);

CREATE INDEX idx_itens_cotacao_cotacao ON itens_cotacao(cotacao_id);
CREATE INDEX idx_itens_cotacao_produto ON itens_cotacao(produto_id);
CREATE INDEX idx_itens_cotacao_servico ON itens_cotacao(servico_id);
CREATE INDEX idx_itens_cotacao_ordem ON itens_cotacao(cotacao_id, ordem_item);
