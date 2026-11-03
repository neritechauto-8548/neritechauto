-- Guarda segredos cifrados junto ao registro que já é referenciado por configuracoes_nfe.
-- Colunas legadas permanecem por compatibilidade; o fluxo novo não grava conteúdo nelas.
ALTER TABLE certificados_digitais
    ADD COLUMN IF NOT EXISTS arquivo_cifrado BYTEA,
    ADD COLUMN IF NOT EXISTS senha_cifrada BYTEA,
    ADD COLUMN IF NOT EXISTS email_contato VARCHAR(254);

CREATE INDEX IF NOT EXISTS idx_certificados_digitais_empresa_validade
    ON certificados_digitais(empresa_id, data_vencimento);
