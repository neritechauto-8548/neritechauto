CREATE TABLE IF NOT EXISTS trial_aceites_legais (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    versao VARCHAR(80) NOT NULL,
    celular VARCHAR(20) NOT NULL,
    aceito_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_trial_aceite_usuario_versao UNIQUE (usuario_id, versao)
);
CREATE INDEX IF NOT EXISTS idx_trial_aceites_empresa ON trial_aceites_legais (empresa_id);
