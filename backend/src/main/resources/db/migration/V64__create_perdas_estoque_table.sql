-- V64__create_perdas_estoque_table.sql
CREATE SEQUENCE perdas_estoque_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE perdas_estoque (
    id BIGINT PRIMARY KEY DEFAULT nextval('perdas_estoque_seq'),
    produto_id BIGINT NOT NULL,
    lote_numero VARCHAR(50),
    quantidade_perdida DECIMAL(10,2) NOT NULL,
    tipo_perda VARCHAR(30) CHECK (tipo_perda IN ('VENCIMENTO', 'DETERIORACAO', 'FURTO', 'ROUBO', 'QUEBRA', 'ACIDENTE', 'ERRO_MANUSEIO', 'OUTROS')),
    descricao TEXT NOT NULL,
    valor_perda DECIMAL(10,2),
    responsavel_perda VARCHAR(255),
    localizacao_id BIGINT,
    foi_segurado BOOLEAN DEFAULT FALSE,
    numero_sinistro VARCHAR(50),
    valor_indenizado DECIMAL(10,2),
    data_ocorrencia DATE NOT NULL,
    data_descoberta DATE,
    boletim_ocorrencia VARCHAR(100),
    fotos_comprovantes_url JSONB,
    observacoes TEXT,
    aprovado_por BIGINT,
    data_aprovacao TIMESTAMP,
    usuario_cadastro BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_perda_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT fk_perda_localizacao FOREIGN KEY (localizacao_id) REFERENCES localizacoes_estoque(id)
);

CREATE INDEX idx_perdas_produto ON perdas_estoque(produto_id);
CREATE INDEX idx_perdas_tipo ON perdas_estoque(tipo_perda);
CREATE INDEX idx_perdas_data_ocorrencia ON perdas_estoque(data_ocorrencia);
