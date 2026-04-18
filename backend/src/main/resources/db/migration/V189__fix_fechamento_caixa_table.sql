DROP TABLE IF EXISTS fin_fechamento_caixa CASCADE;

CREATE TABLE fin_fechamento_caixa (
    id BIGSERIAL PRIMARY KEY,
    data_cadastro TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    data_atualizacao TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0 NOT NULL,
    empresa_id BIGINT NOT NULL,
    situacao VARCHAR(20) NOT NULL,
    data_abertura TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_fechamento TIMESTAMP WITHOUT TIME ZONE,
    saldo_inicial NUMERIC(19, 2) NOT NULL,
    saldo_final NUMERIC(19, 2),
    total_entradas NUMERIC(19, 2),
    total_saidas NUMERIC(19, 2),
    observacoes TEXT,
    usuario_responsavel BIGINT NOT NULL,
    CONSTRAINT fk_fechamento_caixa_empresa FOREIGN KEY (empresa_id) REFERENCES empresa (id)
);

CREATE INDEX idx_fin_fechamento_caixa_empresa ON fin_fechamento_caixa(empresa_id);
CREATE INDEX idx_fin_fechamento_caixa_data ON fin_fechamento_caixa(data_abertura);
