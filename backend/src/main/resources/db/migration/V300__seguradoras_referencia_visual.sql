-- Seguradoras: campos funcionais estritamente presentes nas capturas da referência.
-- Não altera as tabelas seguradoras_os e logs_alteracoes (necessárias às OS e auditoria).
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS inscricao_estadual VARCHAR(50);
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS inscricao_municipal VARCHAR(50);
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS margem_refatoramento NUMERIC(5,2);
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS site VARCHAR(500);
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS retencao_iss BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE seguradoras ADD COLUMN IF NOT EXISTS observacoes TEXT;

-- Preserve os registros anteriores ao migrar o conteúdo do campo de notas.
UPDATE seguradoras SET observacoes = notas
WHERE observacoes IS NULL AND notas IS NOT NULL;
UPDATE seguradoras SET razao_social = COALESCE(NULLIF(btrim(razao_social), ''), NULLIF(btrim(nome_fantasia), ''))
WHERE razao_social IS NULL OR btrim(razao_social) = '';
ALTER TABLE seguradoras ALTER COLUMN razao_social SET NOT NULL;
ALTER TABLE seguradoras ALTER COLUMN nome_fantasia DROP NOT NULL;

-- Eliminação de campos excluídos da especificação, após migração do conteúdo válido.
ALTER TABLE seguradoras DROP COLUMN IF EXISTS registro_susep;
ALTER TABLE seguradoras DROP COLUMN IF EXISTS nome_contato;
ALTER TABLE seguradoras DROP COLUMN IF EXISTS telefone;
ALTER TABLE seguradoras DROP COLUMN IF EXISTS endereco;
ALTER TABLE seguradoras DROP COLUMN IF EXISTS tipo_convenio;
ALTER TABLE seguradoras DROP COLUMN IF EXISTS notas;

ALTER TABLE seguradoras ADD CONSTRAINT ck_seguradoras_margem_refatoramento
  CHECK (margem_refatoramento IS NULL OR margem_refatoramento BETWEEN 0 AND 100);
ALTER TABLE seguradoras ADD CONSTRAINT ck_seguradoras_razao_social
  CHECK (length(btrim(razao_social)) > 0);
CREATE INDEX IF NOT EXISTS idx_seguradoras_empresa_razao_social
  ON seguradoras (empresa_id, lower(razao_social));
