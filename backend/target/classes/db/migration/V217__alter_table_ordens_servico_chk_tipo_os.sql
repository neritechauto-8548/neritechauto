ALTER TABLE ordens_servico DROP CONSTRAINT IF EXISTS chk_ordens_servico_tipo;

ALTER TABLE ordens_servico ADD CONSTRAINT chk_ordens_servico_tipo
CHECK (tipo_os IN ('MANUTENCAO', 'REPARO', 'REVISAO', 'DIAGNOSTICO', 'ORCAMENTO', 'GARANTIA', 'RECALL', 'VENDA_PRODUTO'));
