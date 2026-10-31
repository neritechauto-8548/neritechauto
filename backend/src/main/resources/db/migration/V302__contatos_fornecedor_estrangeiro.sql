-- A coluna Estrangeiro consta da tabela de contatos exibida na referência.
ALTER TABLE contatos_fornecedor ADD COLUMN IF NOT EXISTS estrangeiro BOOLEAN NOT NULL DEFAULT FALSE;
