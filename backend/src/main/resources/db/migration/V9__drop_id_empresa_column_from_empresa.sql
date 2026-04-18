-- Remover coluna id_empresa da tabela empresa e índice associado
ALTER TABLE empresa DROP COLUMN IF EXISTS id_empresa;

-- Remover índice se existir
DROP INDEX IF EXISTS idx_empresa_id_empresa;