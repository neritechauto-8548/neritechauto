-- Mantém o período comercial do trial alinhado ao trial configurado na Stripe.
-- Níveis 1 (Pro) e 2 (Ultra) podem ser oferecidos em avaliação; Admin não recebe trial.
UPDATE planos_assinatura
SET periodo_teste_dias = 180
WHERE nivel IN (1, 2)
  AND ativo = true;
