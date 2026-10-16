-- ADM-001: preferência visual validada por tenant. Não altera documentos fiscais.
ALTER TABLE configuracoes_oficina
    ADD COLUMN IF NOT EXISTS cor_tema VARCHAR(16) NOT NULL DEFAULT 'light';

ALTER TABLE configuracoes_oficina
    ADD CONSTRAINT ck_configuracoes_oficina_cor_tema
    CHECK (cor_tema IN ('light', 'dark', 'auto'));
