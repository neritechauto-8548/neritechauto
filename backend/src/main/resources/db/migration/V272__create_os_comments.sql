-- TELA-AUTO-OS-010/011 — Diário operacional da Ordem de Serviço
-- Comentários humanos são append-only e separados da trilha formal de auditoria.

CREATE TABLE os_comments (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    author_user_id BIGINT NOT NULL REFERENCES usuarios(id),
    author_name_snapshot VARCHAR(180) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    visibility VARCHAR(24) NOT NULL DEFAULT 'INTERNAL',
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_os_comments_content CHECK (char_length(trim(content)) > 0),
    CONSTRAINT ck_os_comments_visibility CHECK (visibility IN ('INTERNAL'))
);

CREATE INDEX ix_os_comments_order
    ON os_comments (empresa_id, ordem_servico_id, data_cadastro DESC, id DESC);

CREATE INDEX ix_os_comments_author
    ON os_comments (empresa_id, author_user_id, data_cadastro DESC);
