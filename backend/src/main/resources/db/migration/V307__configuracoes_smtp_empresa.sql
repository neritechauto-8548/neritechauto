CREATE TABLE configuracoes_smtp_empresa (
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    escopo VARCHAR(20) NOT NULL CHECK (escopo IN ('MATRIZ', 'CRM')),
    email VARCHAR(254) NOT NULL,
    senha_cifrada BYTEA NOT NULL,
    servidor_smtp VARCHAR(255) NOT NULL,
    porta_smtp INTEGER NOT NULL CHECK (porta_smtp BETWEEN 1 AND 65535),
    usar_ssl BOOLEAN NOT NULL DEFAULT FALSE,
    usar_tls BOOLEAN NOT NULL DEFAULT FALSE,
    assinatura TEXT NOT NULL DEFAULT '',
    criado_por BIGINT REFERENCES usuarios(id),
    atualizado_por BIGINT REFERENCES usuarios(id),
    versao INTEGER NOT NULL DEFAULT 0,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_configuracoes_smtp_empresa PRIMARY KEY (empresa_id, escopo),
    CONSTRAINT ck_configuracoes_smtp_empresa_ssl_tls CHECK (NOT (usar_ssl AND usar_tls))
);
