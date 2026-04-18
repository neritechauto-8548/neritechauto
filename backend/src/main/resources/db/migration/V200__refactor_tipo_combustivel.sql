-- Flyway Migration V200__refactor_tipo_combustivel.sql
-- Remove colunas desnecessárias da tabela tipos_combustivel

ALTER TABLE tipos_combustivel
DROP COLUMN IF EXISTS sigla,
DROP COLUMN IF EXISTS descricao,
DROP COLUMN IF EXISTS ativo;
