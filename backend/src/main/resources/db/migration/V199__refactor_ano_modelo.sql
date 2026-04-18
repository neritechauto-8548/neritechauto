-- Flyway Migration V199__refactor_ano_modelo.sql
-- Remove coluna desnecessária da tabela anos_modelo

ALTER TABLE anos_modelo
DROP COLUMN IF EXISTS ativo;
