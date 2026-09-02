-- TELA-AUTO-OS-006 — Execução e Apontamentos
-- Sessões autoritativas de trabalho por técnico/serviço.

CREATE TABLE os_work_sessions (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresas(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    item_os_servico_id BIGINT NOT NULL REFERENCES itens_os_servicos(id) ON DELETE CASCADE,
    technician_user_id BIGINT NOT NULL REFERENCES usuarios(id),
    source VARCHAR(20) NOT NULL DEFAULT 'WEB',
    status VARCHAR(20) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    active_segment_started_at TIMESTAMP,
    paused_at TIMESTAMP,
    ended_at TIMESTAMP,
    pause_reason VARCHAR(40),
    pause_note VARCHAR(500),
    elapsed_seconds BIGINT NOT NULL DEFAULT 0,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_os_work_sessions_status
        CHECK (status IN ('EM_EXECUCAO', 'PAUSADA', 'FINALIZADA')),
    CONSTRAINT ck_os_work_sessions_elapsed
        CHECK (elapsed_seconds >= 0)
);

CREATE INDEX ix_os_work_sessions_order
    ON os_work_sessions (empresa_id, ordem_servico_id, started_at DESC);

CREATE INDEX ix_os_work_sessions_service
    ON os_work_sessions (empresa_id, item_os_servico_id, started_at DESC);

-- Uma sessão pausada continua sendo a sessão operacional aberta do técnico.
CREATE UNIQUE INDEX ux_os_work_sessions_active_technician
    ON os_work_sessions (empresa_id, technician_user_id)
    WHERE status IN ('EM_EXECUCAO', 'PAUSADA');

CREATE TABLE os_work_session_commands (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresas(id),
    actor_user_id BIGINT NOT NULL REFERENCES usuarios(id),
    command VARCHAR(24) NOT NULL,
    idempotency_key VARCHAR(128) NOT NULL,
    request_hash VARCHAR(64) NOT NULL,
    work_session_id BIGINT REFERENCES os_work_sessions(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT ux_os_work_session_commands
        UNIQUE (empresa_id, actor_user_id, command, idempotency_key)
);

CREATE INDEX ix_os_work_session_commands_session
    ON os_work_session_commands (work_session_id);
