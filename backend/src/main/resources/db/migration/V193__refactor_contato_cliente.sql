-- Remove colunas obsoletas da tabela contatos_clientes
ALTER TABLE contatos_clientes DROP COLUMN IF EXISTS ativo;
ALTER TABLE contatos_clientes DROP COLUMN IF EXISTS observacoes;

-- A coluna tipo_contato é um VARCHAR(20), não um tipo ENUM nativo do PostgreSQL.
-- As linhas que tinham 'EMAIL' ou 'SKYPE' serão transferidas para 'OUTROS'
-- para preservar a integridade com o novo Enum Java.
UPDATE contatos_clientes SET tipo_contato = 'OUTROS' WHERE tipo_contato IN ('EMAIL', 'SKYPE');
