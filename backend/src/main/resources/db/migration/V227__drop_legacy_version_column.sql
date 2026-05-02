-- V227: Drop legacy 'version' column specifically in vehicle tables
-- This resolves the null constraint violation during FIPE data load

ALTER TABLE marcas_veiculos DROP COLUMN IF EXISTS version;
ALTER TABLE modelos_veiculos DROP COLUMN IF EXISTS version;
ALTER TABLE anos_modelo DROP COLUMN IF EXISTS version;
ALTER TABLE veiculos DROP COLUMN IF EXISTS version;
ALTER TABLE tipos_combustivel DROP COLUMN IF EXISTS version;
