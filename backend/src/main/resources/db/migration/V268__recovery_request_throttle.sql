-- AUTH-002: account-independent recovery abuse protection.
-- Store only SHA-256 identifiers; no raw e-mail/IP or recovery URL.
CREATE TABLE recuperacao_senha_tentativas (
    id BIGSERIAL PRIMARY KEY,
    email_hash VARCHAR(64) NOT NULL,
    ip_hash VARCHAR(64) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_recuperacao_email_periodo ON recuperacao_senha_tentativas(email_hash, criado_em);
CREATE INDEX idx_recuperacao_ip_periodo ON recuperacao_senha_tentativas(ip_hash, criado_em);
