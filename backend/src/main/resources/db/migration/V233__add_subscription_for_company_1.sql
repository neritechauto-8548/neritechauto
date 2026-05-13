-- Atribui o plano Elite (Nível 2) para a empresa 1 para garantir acesso total
INSERT INTO assinaturas_empresas (empresa_id, plano_id, data_inicio, data_fim, valor_mensal, status, forma_pagamento)
SELECT 
    1, 
    (SELECT id FROM planos_assinatura WHERE nome = 'Elite' LIMIT 1), 
    CURRENT_DATE, 
    CURRENT_DATE + INTERVAL '1 year', 
    199.90, 
    'ATIVO', 
    'CARTAO'
WHERE EXISTS (SELECT 1 FROM empresa WHERE id = 1)
  AND NOT EXISTS (SELECT 1 FROM assinaturas_empresas WHERE empresa_id = 1 AND status = 'ATIVA');
