-- Remove a restrição de unicidade global da sigla
ALTER TABLE unidades_medida DROP CONSTRAINT IF EXISTS unidades_medida_sigla_key;
-- Adiciona uma restrição de unicidade combinando empresa_id e sigla
ALTER TABLE unidades_medida ADD CONSTRAINT uk_unidade_medida_empresa_sigla UNIQUE (empresa_id, sigla);
