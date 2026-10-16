ALTER TABLE cargos ADD COLUMN IF NOT EXISTS area_id BIGINT;

ALTER TABLE cargos
  ADD CONSTRAINT fk_cargos_area FOREIGN KEY (area_id) REFERENCES departamentos_contabio(id);

CREATE UNIQUE INDEX IF NOT EXISTS uk_cargos_empresa_nome_ci
  ON cargos (empresa_id, lower(nome));

CREATE UNIQUE INDEX IF NOT EXISTS uk_cargos_empresa_codigo_ci
  ON cargos (empresa_id, lower(codigo_cbo)) WHERE codigo_cbo IS NOT NULL AND trim(codigo_cbo) <> '';
