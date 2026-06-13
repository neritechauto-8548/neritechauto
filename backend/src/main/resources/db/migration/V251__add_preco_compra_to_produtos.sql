-- V251__add_preco_compra_to_produtos.sql
ALTER TABLE produtos ADD COLUMN preco_compra DECIMAL(10,4);
