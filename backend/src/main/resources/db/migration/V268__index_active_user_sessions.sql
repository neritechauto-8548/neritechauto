-- Session lookup is part of every authenticated request after D5 revocation hardening.
-- Partial indexes keep revoked/history rows out of the hot path without deleting audit data.

CREATE INDEX IF NOT EXISTS idx_sessoes_usuario_access_ativo
    ON sessoes_usuario (token_sessao)
    WHERE ativo = true;

CREATE INDEX IF NOT EXISTS idx_sessoes_usuario_refresh_ativo
    ON sessoes_usuario (refresh_token)
    WHERE ativo = true AND refresh_token IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_sessoes_usuario_usuario_ativo
    ON sessoes_usuario (usuario_id)
    WHERE ativo = true;
