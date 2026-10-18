ALTER TABLE planos_assinatura
    ALTER COLUMN periodo_teste_dias SET DEFAULT 14;

UPDATE planos_assinatura
SET periodo_teste_dias = 14
WHERE periodo_teste_dias IS DISTINCT FROM 14;
