-- ADM-011: somente políticas gerais ainda não cobertas por Empresa/Oficina/Fiscal.
-- Valores permitidos são definidos em uma lista fechada no serviço.
CREATE TABLE politicas_gerais_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    chave VARCHAR(80) NOT NULL,
    valor VARCHAR(160) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    versao INTEGER NOT NULL DEFAULT 0,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT ux_politicas_gerais_empresa_chave UNIQUE (empresa_id, chave)
);
CREATE INDEX idx_politicas_gerais_empresa ON politicas_gerais_empresa(empresa_id, ativo);
-- Histórico prospectivo reutiliza logs_alteracoes existente (V149).
