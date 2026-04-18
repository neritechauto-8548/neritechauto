ALTER TABLE setores_empresa RENAME COLUMN descricao TO nome;
ALTER TABLE setores_empresa ADD COLUMN ativo BOOLEAN DEFAULT TRUE;
