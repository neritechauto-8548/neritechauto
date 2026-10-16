CREATE UNIQUE INDEX IF NOT EXISTS uk_funcoes_empresa_nome_ci
  ON funcoes (empresa_id, lower(nome));
