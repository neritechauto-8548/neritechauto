-- Atualiza a descrição do Plano Pro para incluir relatórios
UPDATE planos_assinatura 
SET descricao = 'Plano ideal para oficinas em crescimento. Inclui operacional completo e Relatórios Gerenciais.'
WHERE nivel = 1 AND nome = 'Pro';

UPDATE planos_assinatura 
SET descricao = 'Plano completo com Financeiro, Fiscal, Auditoria e Relatórios Avançados.'
WHERE nivel = 2 AND nome = 'Elite';
