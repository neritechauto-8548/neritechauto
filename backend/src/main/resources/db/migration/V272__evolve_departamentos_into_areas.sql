ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS codigo VARCHAR(30);
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS detalhes TEXT;
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS tipo_operacao VARCHAR(50);
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS supervisor_id BIGINT;
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS ordem INTEGER NOT NULL DEFAULT 0;
ALTER TABLE departamentos_contabio ADD COLUMN IF NOT EXISTS tempo_padrao_minutos INTEGER;

UPDATE departamentos_contabio SET codigo = 'AREA-' || id WHERE codigo IS NULL OR trim(codigo) = '';
ALTER TABLE departamentos_contabio ALTER COLUMN codigo SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_departamentos_contabio_empresa_nome_ci
  ON departamentos_contabio (empresa_id, lower(descricao));
CREATE UNIQUE INDEX IF NOT EXISTS uk_departamentos_contabio_empresa_codigo_ci
  ON departamentos_contabio (empresa_id, lower(codigo));
CREATE INDEX IF NOT EXISTS idx_departamentos_contabio_ativo ON departamentos_contabio (empresa_id, ativo);
