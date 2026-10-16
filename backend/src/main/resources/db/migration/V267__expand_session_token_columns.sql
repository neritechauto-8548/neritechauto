-- BASE-01 / P0: JWT access and refresh tokens may exceed varchar(255/500).
-- Preserve all existing sessions and constraints without rewriting their values.
ALTER TABLE sessoes_usuario
    ALTER COLUMN token_sessao TYPE TEXT,
    ALTER COLUMN refresh_token TYPE TEXT;

-- B-tree text indexes reject sufficiently large JWTs (index row > ~2700 bytes).
-- Session lookups are equality-only; hash indexes support arbitrary token sizes.
DROP INDEX IF EXISTS idx_sessoes_token;
DROP INDEX IF EXISTS idx_sessoes_refresh;
CREATE INDEX idx_sessoes_token ON sessoes_usuario USING HASH (token_sessao);
CREATE INDEX idx_sessoes_refresh ON sessoes_usuario USING HASH (refresh_token);
