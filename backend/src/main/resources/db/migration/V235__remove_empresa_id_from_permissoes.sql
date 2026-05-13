-- Removendo a coluna empresa_id da tabela permissoes
-- Primeiro removemos a constraint que depende dela
ALTER TABLE permissoes DROP CONSTRAINT IF EXISTS uk_permissoes_valor_empresa;

-- Removemos as duplicatas se houver (mantendo apenas uma de cada valor)
DELETE FROM permissoes a USING permissoes b 
WHERE a.id > b.id AND a.valor = b.valor;

-- Agora removemos a coluna
ALTER TABLE permissoes DROP COLUMN IF EXISTS empresa_id;

-- Criamos uma nova constraint unique apenas para o valor
ALTER TABLE permissoes ADD CONSTRAINT uk_permissoes_valor UNIQUE (valor);
