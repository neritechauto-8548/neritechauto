-- Migração para adicionar o campo segmento na tabela empresa
ALTER TABLE empresa ADD COLUMN segmento VARCHAR(50);
