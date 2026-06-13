-- Drop NOT NULL constraint on cliente_id
ALTER TABLE fin_contas_receber ALTER COLUMN cliente_id DROP NOT NULL;

-- Seed default Plano de Contas
INSERT INTO fin_plano_contas (empresa_id, codigo, nome, nivel, tipo_conta, natureza_saldo, aceita_lancamento, ativo, criado_por) VALUES
(1, '1.01', 'Receitas de Serviços', 1, 'RECEITA', 'CREDORA', true, true, 1),
(1, '1.02', 'Receitas de Peças', 1, 'RECEITA', 'CREDORA', true, true, 1),
(1, '2.01', 'Despesas Operacionais', 1, 'DESPESA', 'DEVEDORA', true, true, 1)
ON CONFLICT (empresa_id, codigo) DO NOTHING;

-- Seed default Centros de Custo
INSERT INTO fin_centros_custo (empresa_id, codigo, nome, ativo, criado_por) VALUES
(1, 'GER', 'Geral', true, 1),
(1, 'OFI', 'Oficina', true, 1),
(1, 'ADM', 'Administrativo', true, 1)
ON CONFLICT (empresa_id, codigo) DO NOTHING;
