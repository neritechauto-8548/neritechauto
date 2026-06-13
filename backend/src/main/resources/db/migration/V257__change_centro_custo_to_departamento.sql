-- 1. Clear old cost center associations to prevent FK violations on migration
UPDATE fin_contas_receber SET centro_custo_id = NULL;
UPDATE fin_contas_pagar SET centro_custo_id = NULL;
UPDATE fin_fluxo_caixa SET centro_custo_id = NULL;
UPDATE fin_lancamentos_contabeis SET centro_custo_id = NULL;

-- 2. Drop old foreign keys referencing fin_centros_custo
ALTER TABLE fin_contas_receber DROP CONSTRAINT IF EXISTS fk_fin_contas_receber_centro;
ALTER TABLE fin_contas_pagar DROP CONSTRAINT IF EXISTS fk_fin_contas_pagar_centro;
ALTER TABLE fin_fluxo_caixa DROP CONSTRAINT IF EXISTS fk_fin_fluxo_caixa_centro;
ALTER TABLE fin_lancamentos_contabeis DROP CONSTRAINT IF EXISTS fk_fin_lancamentos_centro;

-- 3. Add new foreign keys referencing departamentos_contabio
ALTER TABLE fin_contas_receber ADD CONSTRAINT fk_fin_contas_receber_departamento FOREIGN KEY (centro_custo_id) REFERENCES departamentos_contabio(id);
ALTER TABLE fin_contas_pagar ADD CONSTRAINT fk_fin_contas_pagar_departamento FOREIGN KEY (centro_custo_id) REFERENCES departamentos_contabio(id);
ALTER TABLE fin_fluxo_caixa ADD CONSTRAINT fk_fin_fluxo_caixa_departamento FOREIGN KEY (centro_custo_id) REFERENCES departamentos_contabio(id);
ALTER TABLE fin_lancamentos_contabeis ADD CONSTRAINT fk_fin_lancamentos_departamento FOREIGN KEY (centro_custo_id) REFERENCES departamentos_contabio(id);

-- 4. Seed default departments if the table is empty for company 1
INSERT INTO departamentos_contabio (empresa_id, descricao, criado_por)
SELECT 1, 'Oficina', 1 WHERE NOT EXISTS (SELECT 1 FROM departamentos_contabio WHERE empresa_id = 1 AND descricao = 'Oficina');
INSERT INTO departamentos_contabio (empresa_id, descricao, criado_por)
SELECT 1, 'Administrativo', 1 WHERE NOT EXISTS (SELECT 1 FROM departamentos_contabio WHERE empresa_id = 1 AND descricao = 'Administrativo');
INSERT INTO departamentos_contabio (empresa_id, descricao, criado_por)
SELECT 1, 'Financeiro', 1 WHERE NOT EXISTS (SELECT 1 FROM departamentos_contabio WHERE empresa_id = 1 AND descricao = 'Financeiro');
INSERT INTO departamentos_contabio (empresa_id, descricao, criado_por)
SELECT 1, 'Vendas', 1 WHERE NOT EXISTS (SELECT 1 FROM departamentos_contabio WHERE empresa_id = 1 AND descricao = 'Vendas');
