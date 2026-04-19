ALTER TABLE cliente DROP CONSTRAINT IF EXISTS cliente_cpf_key;
ALTER TABLE cliente DROP CONSTRAINT IF EXISTS cliente_cnpj_key;

-- We don't know the exact name Hibernate generated if it's not the default, but usually it's table_column_key or uk_...
-- We will try to drop common names just in case if the above fails, but Flyway will fail if constraint doesn't exist?
-- No, IF EXISTS handles that safely.

-- Postgres allows creating unique constraints with a specific name
ALTER TABLE cliente ADD CONSTRAINT uc_cliente_cpf_empresa UNIQUE (cpf, empresa_id);
ALTER TABLE cliente ADD CONSTRAINT uc_cliente_cnpj_empresa UNIQUE (cnpj, empresa_id);
