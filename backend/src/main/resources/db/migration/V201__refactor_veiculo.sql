-- Remover colunas obsoletas
ALTER TABLE veiculos DROP COLUMN valor_estimado;
ALTER TABLE veiculos DROP COLUMN data_valor_estimado;
ALTER TABLE veiculos DROP COLUMN foto_principal_url;

-- Adicionar nova coluna e restrição
ALTER TABLE veiculos ADD COLUMN combustivel_id BIGINT;
ALTER TABLE veiculos ADD CONSTRAINT fk_veiculo_combustivel FOREIGN KEY (combustivel_id) REFERENCES tipos_combustivel(id);
