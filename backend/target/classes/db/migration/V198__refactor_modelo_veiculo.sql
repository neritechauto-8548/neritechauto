-- Flyway Migration V198__refactor_modelo_veiculo.sql
-- Adiciona logoUrl, converte HATCH para HATCHBACK e remove colunas antigas

-- Atualizar categoria legada para garantir compatibilidade com o novo Enum
UPDATE modelos_veiculos SET categoria = 'HATCHBACK' WHERE categoria = 'HATCH';

-- Adicionar nova coluna logo_url
ALTER TABLE modelos_veiculos ADD COLUMN logo_url VARCHAR(500);

-- Remover colunas obsoletas
ALTER TABLE modelos_veiculos
DROP COLUMN IF EXISTS segmento,
DROP COLUMN IF EXISTS numero_portas,
DROP COLUMN IF EXISTS numero_lugares,
DROP COLUMN IF EXISTS tipo_tracao;
