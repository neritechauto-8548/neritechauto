-- Adiciona nova coluna de email
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS email VARCHAR(100);

-- Remove colunas que não são mais exigidas
ALTER TABLE cliente DROP COLUMN IF EXISTS rg;
ALTER TABLE cliente DROP COLUMN IF EXISTS estado_civil;
ALTER TABLE cliente DROP COLUMN IF EXISTS profissao;
ALTER TABLE cliente DROP COLUMN IF EXISTS detalhes_origem;
ALTER TABLE cliente DROP COLUMN IF EXISTS motivo_bloqueio;
ALTER TABLE cliente DROP COLUMN IF EXISTS data_bloqueio;
