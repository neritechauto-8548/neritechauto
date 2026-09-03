-- TELA-AUTO-OS-010/011 — Diário operacional da Ordem de Serviço.
-- Comentários humanos são imutáveis após o registro e separados da trilha formal de auditoria.

CREATE TABLE comentarios_ordem_servico (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    usuario_autor_id BIGINT NOT NULL REFERENCES usuarios(id),
    nome_autor_registrado VARCHAR(180) NOT NULL,
    conteudo VARCHAR(2000) NOT NULL,
    visibilidade VARCHAR(24) NOT NULL DEFAULT 'INTERNO',
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_comentarios_os_conteudo CHECK (char_length(trim(conteudo)) > 0),
    CONSTRAINT ck_comentarios_os_visibilidade CHECK (visibilidade IN ('INTERNO'))
);

CREATE INDEX ix_comentarios_os_ordem
    ON comentarios_ordem_servico (empresa_id, ordem_servico_id, data_cadastro DESC, id DESC);

CREATE INDEX ix_comentarios_os_autor
    ON comentarios_ordem_servico (empresa_id, usuario_autor_id, data_cadastro DESC);
