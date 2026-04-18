-- V175: Add missing audit and tenancy columns to anos_modelo
-- Ensures compatibility with BaseEntity fields used by AnoModelo

-- Add columns if not exist
ALTER TABLE anos_modelo 
    ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP,
    ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP,
    ADD COLUMN IF NOT EXISTS criado_por BIGINT,
    ADD COLUMN IF NOT EXISTS atualizado_por BIGINT,
    ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

-- Backfill data_cadastro for existing rows
UPDATE anos_modelo 
SET data_cadastro = CURRENT_TIMESTAMP 
WHERE data_cadastro IS NULL;

-- Enforce NOT NULL on data_cadastro
ALTER TABLE anos_modelo 
    ALTER COLUMN data_cadastro SET NOT NULL;

-- Index for tenancy
CREATE INDEX IF NOT EXISTS idx_anos_modelo_empresa ON anos_modelo(empresa_id);

