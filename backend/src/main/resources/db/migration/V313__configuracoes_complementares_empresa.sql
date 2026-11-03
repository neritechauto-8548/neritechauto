CREATE TABLE configuracoes_complementares_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    secao VARCHAR(40) NOT NULL,
    dados JSONB NOT NULL DEFAULT '{}'::jsonb,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT NOT NULL REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_configuracao_complementar_empresa UNIQUE (empresa_id, secao),
    CONSTRAINT ck_configuracao_complementar_secao CHECK (secao IN (
        'ATENDIMENTO', 'COMERCIAL', 'ESTOQUE_FINANCEIRO', 'INTEGRACOES',
        'GESTAO_PATIO', 'FERIADOS_LOCAIS', 'PRODUTO_COMPOSICAO',
        'PRODUTO_CUSTO_LUCRO', 'COMPRAS', 'AGENDAMENTO', 'FINANCEIRO', 'TERMOS'
    ))
);

CREATE INDEX idx_configuracoes_complementares_empresa
    ON configuracoes_complementares_empresa (empresa_id, secao);
