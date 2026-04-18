-- Flyway Migration V197__refactor_marca_veiculo.sql
-- Remove colunas desnecessárias da tabela marcas_veiculos
-- Exclusão: logo_url, website, ativo

ALTER TABLE marcas_veiculos
DROP COLUMN IF EXISTS logo_url,
DROP COLUMN IF EXISTS website,
DROP COLUMN IF EXISTS ativo;
