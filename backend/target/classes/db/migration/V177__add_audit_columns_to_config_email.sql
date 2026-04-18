ALTER TABLE com_configuracoes_email 
ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE com_configuracoes_email
ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP;

ALTER TABLE com_configuracoes_email
ADD COLUMN IF NOT EXISTS criado_por BIGINT;

ALTER TABLE com_configuracoes_email
ADD COLUMN IF NOT EXISTS atualizado_por BIGINT;

ALTER TABLE com_configuracoes_email
ADD COLUMN IF NOT EXISTS versao INTEGER NOT NULL DEFAULT 0;
