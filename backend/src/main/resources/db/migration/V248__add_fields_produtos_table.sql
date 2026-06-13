-- V248__add_fields_produtos_table.sql
ALTER TABLE produtos 
ADD COLUMN endereco_estoque VARCHAR(100),
ADD COLUMN setor VARCHAR(100),
ADD COLUMN data_vencimento DATE,
ADD COLUMN codigo_substituto_1 VARCHAR(50),
ADD COLUMN codigo_substituto_2 VARCHAR(50),
ADD COLUMN desconto_fornecedor_percentual DECIMAL(5,2);
