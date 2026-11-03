ALTER TABLE empresa
    ADD COLUMN IF NOT EXISTS tipo_empresa VARCHAR(40) NOT NULL DEFAULT 'MECANICA',
    ADD COLUMN IF NOT EXISTS empresa_principal BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE configuracoes_oficina
    ADD COLUMN IF NOT EXISTS email_agendamento VARCHAR(120);

UPDATE configuracoes_oficina configuracao
SET email_agendamento = empresa.email
FROM empresa
WHERE configuracao.empresa_id = empresa.id
  AND configuracao.permite_agendamento_online = TRUE
  AND (configuracao.email_agendamento IS NULL OR BTRIM(configuracao.email_agendamento) = '')
  AND empresa.email IS NOT NULL
  AND BTRIM(empresa.email) <> '';

CREATE TABLE IF NOT EXISTS contatos_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    tipo_contato VARCHAR(20) NOT NULL,
    contato VARCHAR(160) NOT NULL,
    ordem INTEGER NOT NULL DEFAULT 0,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_contatos_empresa_tipo CHECK (tipo_contato IN ('EMAIL', 'TELEFONE'))
);

CREATE INDEX IF NOT EXISTS idx_contatos_empresa_empresa_ordem
    ON contatos_empresa (empresa_id, ordem, id);
