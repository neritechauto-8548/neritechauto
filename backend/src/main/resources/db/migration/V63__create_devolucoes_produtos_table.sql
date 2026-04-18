-- V63__create_devolucoes_produtos_table.sql
CREATE SEQUENCE devolucoes_produtos_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE devolucoes_produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('devolucoes_produtos_seq'),
    produto_id BIGINT NOT NULL,
    ordem_servico_id BIGINT,
    cliente_id BIGINT,
    quantidade_devolvida DECIMAL(10,2) NOT NULL,
    motivo_devolucao VARCHAR(30) CHECK (motivo_devolucao IN ('DEFEITO', 'PRODUTO_ERRADO', 'NAO_APROVADO', 'GARANTIA', 'OUTROS')),
    descricao_motivo TEXT,
    condicao_produto VARCHAR(20) CHECK (condicao_produto IN ('NOVO', 'USADO_BOM', 'USADO_RUIM', 'DEFEITUOSO', 'DANIFICADO')),
    acao_tomada VARCHAR(30) CHECK (acao_tomada IN ('REESTOQUE', 'DESCARTE', 'DEVOLUCAO_FORNECEDOR', 'REPARO', 'ANALISE')),
    valor_devolvido DECIMAL(10,2),
    aprovado_por BIGINT,
    data_aprovacao TIMESTAMP,
    observacoes TEXT,
    fotos_produto_url JSONB,
    data_devolucao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_responsavel BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_devolucao_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_devolucoes_produto ON devolucoes_produtos(produto_id);
CREATE INDEX idx_devolucoes_data ON devolucoes_produtos(data_devolucao);
CREATE INDEX idx_devolucoes_motivo ON devolucoes_produtos(motivo_devolucao);
