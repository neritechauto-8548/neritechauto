-- Adiciona empresa_id a tabela unidades_medida
ALTER TABLE unidades_medida ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

-- Atualiza registros existentes para a empresa 1 (fallback) para evitar erros de NOT NULL
UPDATE unidades_medida SET empresa_id = 1 WHERE empresa_id IS NULL;

-- Define a coluna como NOT NULL
ALTER TABLE unidades_medida ALTER COLUMN empresa_id SET NOT NULL;

-- Adiciona a constraint de chave estrangeira
ALTER TABLE unidades_medida ADD CONSTRAINT fk_unidade_medida_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id);

-- Adiciona index para performance
CREATE INDEX IF NOT EXISTS idx_unidades_medida_empresa_id ON unidades_medida(empresa_id);
