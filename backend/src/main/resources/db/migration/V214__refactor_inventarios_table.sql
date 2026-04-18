-- V214__refactor_inventarios_table.sql
ALTER TABLE inventarios DROP COLUMN IF EXISTS localizacoes_incluidas;
ALTER TABLE inventarios DROP COLUMN IF EXISTS categorias_incluidas;
ALTER TABLE inventarios DROP COLUMN IF EXISTS produtos_incluidos;
ALTER TABLE inventarios DROP COLUMN IF EXISTS usuarios_responsaveis;
