CREATE TABLE configuracoes_smtp_usuario (
    usuario_id BIGINT PRIMARY KEY REFERENCES usuarios(id) ON DELETE CASCADE,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    email VARCHAR(254) NOT NULL,
    senha_cifrada BYTEA NOT NULL,
    servidor_smtp VARCHAR(255) NOT NULL,
    porta_smtp INTEGER NOT NULL CHECK (porta_smtp BETWEEN 1 AND 65535),
    usar_ssl BOOLEAN NOT NULL DEFAULT FALSE,
    usar_tls BOOLEAN NOT NULL DEFAULT FALSE,
    assinatura TEXT NOT NULL DEFAULT '',
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_smtp_usuario_ssl_tls CHECK (NOT (usar_ssl AND usar_tls))
);
CREATE INDEX idx_configuracoes_smtp_usuario_empresa ON configuracoes_smtp_usuario(empresa_id, usuario_id);
