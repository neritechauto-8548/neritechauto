-- V224: Fix funcionario unique constraints for multi-tenancy
-- Description: Remove global CPF unique constraint and add composite (empresa_id, cpf) constraint. Make CPF nullable.

ALTER TABLE funcionarios DROP CONSTRAINT IF EXISTS uk_funcionarios_cpf;

-- Add composite unique constraint only for non-null CPFs (some dialects handle this differently, 
-- but in Postgres multiple NULLs are allowed in a UNIQUE constraint, which is what we want).
ALTER TABLE funcionarios ADD CONSTRAINT uk_funcionarios_empresa_cpf UNIQUE (empresa_id, cpf);

-- Make CPF nullable
ALTER TABLE funcionarios ALTER COLUMN cpf DROP NOT NULL;
