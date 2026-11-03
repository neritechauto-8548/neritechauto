ALTER TABLE situacao ADD COLUMN tipo_status VARCHAR(20) NOT NULL DEFAULT 'ABERTURA';
ALTER TABLE situacao ADD COLUMN ordem_exibicao INTEGER NOT NULL DEFAULT 0;

WITH numeradas AS (
    SELECT id, ROW_NUMBER() OVER (PARTITION BY empresa_id ORDER BY data_cadastro, id) - 1 AS ordem
    FROM situacao
)
UPDATE situacao s SET ordem_exibicao = n.ordem FROM numeradas n WHERE s.id = n.id;

ALTER TABLE situacao ADD CONSTRAINT ck_situacao_tipo_status
    CHECK (tipo_status IN ('ABERTURA', 'PROGRESSO', 'FINALIZACAO', 'CONCLUIDO'));
