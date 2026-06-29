ALTER TABLE fin_comissoes_funcionarios ADD COLUMN item_fatura_id BIGINT;
ALTER TABLE fin_comissoes_funcionarios ADD CONSTRAINT fk_fin_comissoes_item_fatura FOREIGN KEY (item_fatura_id) REFERENCES fin_itens_fatura(id);
CREATE INDEX idx_fin_comissoes_item_fatura ON fin_comissoes_funcionarios(item_fatura_id);
