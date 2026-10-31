-- Integração transacional entre quitação administrativa de comissão e título financeiro.
CREATE TABLE IF NOT EXISTS fin_comissao_conta_pagar (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    comissao_id BIGINT NOT NULL REFERENCES fin_comissoes_funcionarios(id),
    conta_pagar_id BIGINT NOT NULL REFERENCES fin_contas_pagar(id),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (empresa_id, comissao_id),
    UNIQUE (empresa_id, conta_pagar_id)
);
CREATE INDEX IF NOT EXISTS idx_comissao_conta_pagar_empresa
    ON fin_comissao_conta_pagar (empresa_id, comissao_id);
