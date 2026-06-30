-- Atualiza os planos existentes no banco
UPDATE planos_assinatura SET nome = 'NeriTech Pro', preco_mensal = 79.90, preco_anual = 799.00 WHERE nivel = 1;
UPDATE planos_assinatura SET nome = 'NeriTech Ultra', preco_mensal = 230.00, preco_anual = 2300.00 WHERE nivel = 2;

-- Insere o Plano NeriTech Admin (Nível 3) se não existir
INSERT INTO planos_assinatura (nome, descricao, preco_mensal, preco_anual, max_usuarios, nivel, ativo)
SELECT 'NeriTech Admin', 'Plano administrativo com acesso total às ferramentas e relatórios.', 0.00, 0.00, 999, 3, true
WHERE NOT EXISTS (SELECT 1 FROM planos_assinatura WHERE nivel = 3);
