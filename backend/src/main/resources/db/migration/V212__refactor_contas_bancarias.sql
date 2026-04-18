-- Refactor Contas Bancárias table
-- Rename table from fin_contas_bancarias to contas_bancarias
ALTER TABLE fin_contas_bancarias RENAME TO contas_bancarias;

-- Adjust columns to match requested schema
ALTER TABLE contas_bancarias DROP COLUMN gerente_nome;
ALTER TABLE contas_bancarias DROP COLUMN gerente_telefone;
ALTER TABLE contas_bancarias DROP COLUMN gerente_email;
ALTER TABLE contas_bancarias DROP COLUMN principal;
ALTER TABLE contas_bancarias DROP COLUMN observacoes;

-- Adjust types/constraints
ALTER TABLE contas_bancarias ALTER COLUMN saldo_atual TYPE DECIMAL(15,2);
ALTER TABLE contas_bancarias ALTER COLUMN limite_cheque_especial TYPE DECIMAL(12,2);

-- Ensure defaults and nullable constraints (mostly already there but confirming)
ALTER TABLE contas_bancarias ALTER COLUMN banco_codigo SET NOT NULL;
ALTER TABLE contas_bancarias ALTER COLUMN banco_nome SET NOT NULL;
ALTER TABLE contas_bancarias ALTER COLUMN agencia SET NOT NULL;
ALTER TABLE contas_bancarias ALTER COLUMN conta SET NOT NULL;
ALTER TABLE contas_bancarias ALTER COLUMN titular_conta SET NOT NULL;
ALTER TABLE contas_bancarias ALTER COLUMN cpf_cnpj_titular SET NOT NULL;
