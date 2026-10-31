-- ADM-010: os campos estruturados já foram adicionados à tabela compartilhada
-- fornecedores pela V301; não criar outra tabela de pessoas.
-- Preserve endereços legados, sem inferir número, bairro ou CEP.
UPDATE fornecedores
   SET logradouro = LEFT(endereco_completo,255)
 WHERE transportadora = TRUE
   AND (logradouro IS NULL OR btrim(logradouro) = '')
   AND endereco_completo IS NOT NULL AND btrim(endereco_completo) <> '';
CREATE INDEX IF NOT EXISTS idx_fornecedores_transportadora_regiao
    ON fornecedores (empresa_id, transportadora, transportadora_ativa, estado, cidade);
