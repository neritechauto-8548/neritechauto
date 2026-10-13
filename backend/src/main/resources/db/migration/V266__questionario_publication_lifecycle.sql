-- CRM / pesquisas: estado explícito e publicação manual.
-- Registros existentes com respostas preservam capacidade de consulta e estado operacional;
-- questionários sem respostas são rascunhos e não são publicados implicitamente.
ALTER TABLE questionario ADD COLUMN status_pesquisa VARCHAR(20) NOT NULL DEFAULT 'DRAFT';
UPDATE questionario q
SET status_pesquisa = CASE WHEN COALESCE(q.sn_ativo, false) THEN 'PUBLISHED' ELSE 'PAUSED' END
WHERE EXISTS (SELECT 1 FROM pesquisa_respostas r WHERE r.questionario_id = q.id);
ALTER TABLE questionario ADD CONSTRAINT ck_questionario_status_pesquisa
    CHECK (status_pesquisa IN ('DRAFT','PUBLISHED','PAUSED','CLOSED'));
CREATE INDEX idx_questionario_empresa_status ON questionario(empresa_id, status_pesquisa);
