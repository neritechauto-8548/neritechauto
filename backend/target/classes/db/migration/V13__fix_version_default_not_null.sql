-- Corrige coluna de versão otimista nas tabelas empresa e cliente
-- Define default 0, torna NOT NULL e normaliza registros legados com versao nula

-- Empresa
ALTER TABLE IF EXISTS empresa ALTER COLUMN versao SET DEFAULT 0;
UPDATE empresa SET versao = 0 WHERE versao IS NULL;
ALTER TABLE IF EXISTS empresa ALTER COLUMN versao SET NOT NULL;

-- Cliente
ALTER TABLE IF EXISTS cliente ALTER COLUMN versao SET DEFAULT 0;
UPDATE cliente SET versao = 0 WHERE versao IS NULL;
ALTER TABLE IF EXISTS cliente ALTER COLUMN versao SET NOT NULL;

-- Observação: caso existam outras tabelas versionadas, aplicar mesma estratégia nelas.