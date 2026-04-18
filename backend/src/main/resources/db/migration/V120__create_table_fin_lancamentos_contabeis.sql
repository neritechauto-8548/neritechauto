CREATE TABLE fin_lancamentos_contabeis (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    numero_lancamento VARCHAR(30) NOT NULL,
    data_lancamento DATE NOT NULL,
    tipo_lancamento VARCHAR(50) NOT NULL,
    documento_origem VARCHAR(100),
    historico TEXT NOT NULL,
    valor_total DECIMAL(12,2) NOT NULL,
    conta_debito_id BIGINT NOT NULL,
    conta_credito_id BIGINT NOT NULL,
    centro_custo_id BIGINT,
    usuario_lancamento BIGINT NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    auditoria_alteracao JSONB,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT uk_fin_lancamentos_numero UNIQUE (empresa_id, numero_lancamento),
    CONSTRAINT fk_fin_lancamentos_debito FOREIGN KEY (conta_debito_id) REFERENCES fin_plano_contas(id),
    CONSTRAINT fk_fin_lancamentos_credito FOREIGN KEY (conta_credito_id) REFERENCES fin_plano_contas(id),
    CONSTRAINT fk_fin_lancamentos_centro FOREIGN KEY (centro_custo_id) REFERENCES fin_centros_custo(id)
);

CREATE INDEX idx_fin_lancamentos_empresa ON fin_lancamentos_contabeis(empresa_id);
CREATE INDEX idx_fin_lancamentos_data ON fin_lancamentos_contabeis(data_lancamento);
