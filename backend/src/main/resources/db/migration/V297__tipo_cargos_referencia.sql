-- Coluna presente na listagem de cargos das referências de 31/10/2026.
-- Opcional: o formulário de cadastro mostrado não define como preenchê-la.
ALTER TABLE cargos ADD COLUMN IF NOT EXISTS tipo VARCHAR(60);
COMMENT ON COLUMN cargos.tipo IS 'Classificação do cargo, quando definida por regra de negócio';
