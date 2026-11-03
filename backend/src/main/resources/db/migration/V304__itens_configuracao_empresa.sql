CREATE TABLE itens_configuracao_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    grupo VARCHAR(40) NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    cor VARCHAR(9) NOT NULL,
    criado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_itens_configuracao_grupo CHECK (grupo IN ('CLIENTE_CATEGORIA', 'FINANCEIRO_ETIQUETA', 'VENDA_DIRETA_ETIQUETA'))
);
CREATE UNIQUE INDEX ux_itens_configuracao_empresa_grupo_descricao
    ON itens_configuracao_empresa (empresa_id, grupo, lower(descricao));
CREATE INDEX idx_itens_configuracao_empresa_grupo
    ON itens_configuracao_empresa (empresa_id, grupo);
