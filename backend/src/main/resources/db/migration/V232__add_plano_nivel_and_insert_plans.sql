-- Adiciona a coluna nivel na tabela de planos
ALTER TABLE planos_assinatura ADD COLUMN IF NOT EXISTS nivel INTEGER DEFAULT 1;

-- Limpa planos antigos para evitar duplicidade (opcional, dependendo do estado do banco)
-- DELETE FROM planos_assinatura WHERE nome IN ('Pro', 'Elite');

-- Insere o Plano Pro (Nível 1)
INSERT INTO planos_assinatura (nome, descricao, preco_mensal, preco_anual, max_usuarios, nivel, ativo)
VALUES ('Pro', 'Plano ideal para oficinas em crescimento. Inclui operacional completo.', 99.90, 999.00, 5, 1, true);

-- Insere o Plano Elite (Nível 2)
INSERT INTO planos_assinatura (nome, descricao, preco_mensal, preco_anual, max_usuarios, nivel, ativo)
VALUES ('Elite', 'Plano completo com Financeiro, Fiscal e Relatórios Avançados.', 199.90, 1999.00, 20, 2, true);
