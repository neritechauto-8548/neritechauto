CREATE TABLE IF NOT EXISTS trial_cadastro_tentativas (
    id BIGSERIAL PRIMARY KEY,
    ip_hash CHAR(64) NOT NULL,
    email_hash CHAR(64) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_trial_tentativas_ip_tempo ON trial_cadastro_tentativas (ip_hash, criado_em);
CREATE INDEX IF NOT EXISTS idx_trial_tentativas_email_tempo ON trial_cadastro_tentativas (email_hash, criado_em);
