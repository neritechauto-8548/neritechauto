-- Add descricao column to fin_contas_receber
ALTER TABLE fin_contas_receber ADD COLUMN IF NOT EXISTS descricao TEXT;

-- Copy existing descriptions from numero_titulo
UPDATE fin_contas_receber SET descricao = numero_titulo WHERE descricao IS NULL;

-- Make it NOT NULL
ALTER TABLE fin_contas_receber ALTER COLUMN descricao SET NOT NULL;
