ALTER TABLE fin_comissoes_funcionarios ADD COLUMN IF NOT EXISTS empresa_id BIGINT;

UPDATE fin_comissoes_funcionarios SET empresa_id = 1 WHERE empresa_id IS NULL;

ALTER TABLE fin_comissoes_funcionarios ALTER COLUMN empresa_id SET NOT NULL;

ALTER TABLE fin_comissoes_funcionarios ADD CONSTRAINT fk_fin_comissoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id);

CREATE INDEX IF NOT EXISTS idx_fin_comissoes_empresa ON fin_comissoes_funcionarios(empresa_id);
