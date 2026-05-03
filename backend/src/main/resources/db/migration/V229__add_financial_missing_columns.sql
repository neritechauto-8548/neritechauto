-- V229__add_financial_missing_columns.sql
-- Adiciona colunas faltantes nas tabelas de contas a pagar e receber para sincronizar com as entidades JPA

-- Tabela de Contas a Pagar
ALTER TABLE fin_contas_pagar ADD COLUMN IF NOT EXISTS plano_contas_id BIGINT;
ALTER TABLE fin_contas_pagar ADD CONSTRAINT fk_fin_contas_pagar_plano FOREIGN KEY (plano_contas_id) REFERENCES fin_plano_contas(id);

-- Tabela de Contas a Receber
ALTER TABLE fin_contas_receber ADD COLUMN IF NOT EXISTS forma_pagamento_id BIGINT;
ALTER TABLE fin_contas_receber ADD COLUMN IF NOT EXISTS conta_bancaria_id BIGINT;
ALTER TABLE fin_contas_receber ADD COLUMN IF NOT EXISTS centro_custo_id BIGINT;
ALTER TABLE fin_contas_receber ADD COLUMN IF NOT EXISTS plano_contas_id BIGINT;

ALTER TABLE fin_contas_receber ADD CONSTRAINT fk_fin_contas_receber_forma FOREIGN KEY (forma_pagamento_id) REFERENCES fin_formas_pagamento(id);
ALTER TABLE fin_contas_receber ADD CONSTRAINT fk_fin_contas_receber_conta FOREIGN KEY (conta_bancaria_id) REFERENCES contas_bancarias(id);
ALTER TABLE fin_contas_receber ADD CONSTRAINT fk_fin_contas_receber_centro FOREIGN KEY (centro_custo_id) REFERENCES fin_centros_custo(id);
ALTER TABLE fin_contas_receber ADD CONSTRAINT fk_fin_contas_receber_plano FOREIGN KEY (plano_contas_id) REFERENCES fin_plano_contas(id);
