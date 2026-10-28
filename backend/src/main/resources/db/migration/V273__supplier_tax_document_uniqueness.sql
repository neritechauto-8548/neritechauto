-- ADM-008: bloqueia documentos duplicados também em requisições concorrentes.
-- Mantém fornecedores inativos (e pedidos/notas históricas) sem exclusão ou alteração.
-- Se existirem duplicatas históricas, a migração falha de modo explícito para saneamento,
-- em vez de escolher arbitrariamente registros a apagar.
CREATE UNIQUE INDEX IF NOT EXISTS ux_fornecedores_empresa_cpf_normalizado
    ON fornecedores (empresa_id, regexp_replace(cpf, '[^0-9]', '', 'g'))
    WHERE cpf IS NOT NULL AND btrim(cpf) <> '';

CREATE UNIQUE INDEX IF NOT EXISTS ux_fornecedores_empresa_cnpj_normalizado
    ON fornecedores (empresa_id, upper(regexp_replace(cnpj, '[^a-zA-Z0-9]', '', 'g')))
    WHERE cnpj IS NOT NULL AND btrim(cnpj) <> '';
