CREATE TABLE fin_centros_custo (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    centro_custo_pai_id BIGINT,
    tipo VARCHAR(20),
    responsavel_id BIGINT,
    orcamento_mensal DECIMAL(12,2),
    orcamento_anual DECIMAL(12,2),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT uk_fin_centros_custo_codigo UNIQUE (empresa_id, codigo),
    CONSTRAINT fk_fin_centros_custo_pai FOREIGN KEY (centro_custo_pai_id) REFERENCES fin_centros_custo(id)
);

CREATE INDEX idx_fin_centros_custo_empresa ON fin_centros_custo(empresa_id);
