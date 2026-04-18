-- V202: Remove tabelas das classes CategoriaServico, KitServico, EspecialidadeServico,
--        ServicoPreco e TempoServico que foram excluídas do backend.
--
-- Ordem respeita as foreign keys:
--   kits_servicos → categorias_servicos
--   especialidades_servicos, servicos_precos, tempos_servicos → servicos
--   servicos.categoria_id → categorias_servicos

-- 1. Tabelas filhas de servicos
DROP TABLE IF EXISTS kits_servicos CASCADE;
DROP TABLE IF EXISTS especialidades_servicos CASCADE;
DROP TABLE IF EXISTS servicos_precos CASCADE;
DROP TABLE IF EXISTS tempos_servicos CASCADE;

-- 2. Remove FK e coluna categoria_id de servicos (referenciava categorias_servicos)
ALTER TABLE servicos DROP COLUMN IF EXISTS categoria_id;

-- 3. Tabela pai (pode ser dropada agora que kits_servicos e servicos.categoria_id foram removidos)
DROP TABLE IF EXISTS categorias_servicos CASCADE;
