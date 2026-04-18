-- Recriar coluna id_empresa na tabela empresa, se não existir
ALTER TABLE empresa ADD COLUMN IF NOT EXISTS id_empresa BIGINT;
-- Opcional: criar índice para consultas por tenant
CREATE INDEX IF NOT EXISTS idx_empresa_id_empresa ON empresa(id_empresa);