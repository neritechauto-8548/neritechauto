-- Restaura colunas de auditoria para a tabela unidades_medida
-- Essas colunas são necessárias para entidades que estendem TenantEntity/BaseEntity

ALTER TABLE unidades_medida ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP;
ALTER TABLE unidades_medida ADD COLUMN IF NOT EXISTS criado_por BIGINT;
ALTER TABLE unidades_medida ADD COLUMN IF NOT EXISTS atualizado_por BIGINT;
ALTER TABLE unidades_medida ADD COLUMN IF NOT EXISTS versao INT DEFAULT 0;

-- Garante que versao não seja nula para registros existentes
UPDATE unidades_medida SET versao = 0 WHERE versao IS NULL;
ALTER TABLE unidades_medida ALTER COLUMN versao SET NOT NULL;
