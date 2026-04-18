CREATE TABLE fin_impostos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    tipo_imposto VARCHAR(30),
    nome_imposto VARCHAR(100) NOT NULL,
    aliquota_percentual DECIMAL(8,4) NOT NULL,
    base_calculo VARCHAR(30),
    codigo_receita VARCHAR(10),
    aplicavel_regime JSONB,
    ativo BOOLEAN DEFAULT TRUE,
    data_inicio_vigencia DATE NOT NULL,
    data_fim_vigencia DATE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT
);

CREATE INDEX idx_fin_impostos_empresa ON fin_impostos(empresa_id);
