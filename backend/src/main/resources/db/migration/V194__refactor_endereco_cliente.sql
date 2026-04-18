-- Remove colunas que não são mais utilizadas na entidade EnderecoCliente
ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS tipo_endereco;
ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS descricao;
ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS principal;
ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS ativo;
ALTER TABLE enderecos_clientes DROP COLUMN IF EXISTS observacoes;
