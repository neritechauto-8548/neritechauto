-- Migração V125: Criar tabela de Itens de Garantia
-- Armazena os itens cobertos por cada garantia

CREATE SEQUENCE seq_gar_itens_garantia_id START WITH 1 INCREMENT BY 1;

CREATE TABLE gar_itens_garantia (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_gar_itens_garantia_id'),
    garantia_id BIGINT NOT NULL,
    tipo_item VARCHAR(30) NOT NULL,
    servico_id BIGINT,
    produto_id BIGINT,
    descricao_item TEXT NOT NULL,
    quantidade_original DECIMAL(10,2) NOT NULL,
    valor_unitario_original DECIMAL(10,4) NOT NULL,
    valor_total_original DECIMAL(10,2) NOT NULL,
    percentual_cobertura DECIMAL(5,2) DEFAULT 100.00,
    valor_cobertura DECIMAL(10,2) NOT NULL,
    condicoes_especificas TEXT,
    defeito_coberto TEXT,
    defeito_nao_coberto TEXT,
    prazo_acionamento_dias INT,
    quantidade_utilizada DECIMAL(10,2) DEFAULT 0,
    valor_utilizado DECIMAL(10,2) DEFAULT 0,
    saldo_disponivel DECIMAL(10,2),
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    
    -- Constraints
    CONSTRAINT fk_itens_garantia_garantia FOREIGN KEY (garantia_id) REFERENCES gar_garantias(id) ON DELETE CASCADE,
    CONSTRAINT fk_itens_garantia_servico FOREIGN KEY (servico_id) REFERENCES servicos(id),
    CONSTRAINT fk_itens_garantia_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT chk_tipo_item CHECK (tipo_item IN ('SERVICO', 'PRODUTO', 'MAO_OBRA')),
    CONSTRAINT chk_percentual_cobertura_item CHECK (percentual_cobertura >= 0 AND percentual_cobertura <= 100),
    CONSTRAINT chk_quantidade_original CHECK (quantidade_original > 0),
    CONSTRAINT chk_valor_unitario CHECK (valor_unitario_original >= 0),
    CONSTRAINT chk_quantidade_utilizada CHECK (quantidade_utilizada >= 0 AND quantidade_utilizada <= quantidade_original)
);

-- Índices para otimização de consultas
CREATE INDEX idx_itens_garantia_garantia ON gar_itens_garantia(garantia_id);
CREATE INDEX idx_itens_garantia_tipo ON gar_itens_garantia(tipo_item);
CREATE INDEX idx_itens_garantia_servico ON gar_itens_garantia(servico_id);
CREATE INDEX idx_itens_garantia_produto ON gar_itens_garantia(produto_id);
CREATE INDEX idx_itens_garantia_ativo ON gar_itens_garantia(ativo);

-- Comentários
COMMENT ON TABLE gar_itens_garantia IS 'Itens cobertos por cada garantia';
COMMENT ON COLUMN gar_itens_garantia.tipo_item IS 'Tipo: SERVICO, PRODUTO, MAO_OBRA';
COMMENT ON COLUMN gar_itens_garantia.saldo_disponivel IS 'Saldo disponível para utilização';
