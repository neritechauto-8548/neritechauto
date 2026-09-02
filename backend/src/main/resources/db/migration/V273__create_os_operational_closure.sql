-- Fechamento operacional canônico da Ordem de Serviço.
-- Importante: esta estrutura não cria título financeiro, fatura ou documento fiscal.

CREATE TABLE IF NOT EXISTS os_operational_closures (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    aggregate_version INTEGER NOT NULL,
    snapshot_json JSONB NOT NULL,
    completed_by_user_id BIGINT NOT NULL,
    completed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_os_operational_closure_tenant_order UNIQUE (empresa_id, ordem_servico_id)
);

CREATE INDEX IF NOT EXISTS idx_os_operational_closure_order
    ON os_operational_closures (empresa_id, ordem_servico_id);

CREATE TABLE IF NOT EXISTS os_closure_commands (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    actor_user_id BIGINT NOT NULL,
    command VARCHAR(40) NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    idempotency_key VARCHAR(128) NOT NULL,
    request_hash VARCHAR(64) NOT NULL,
    result_snapshot_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT uq_os_closure_command_idempotency
        UNIQUE (empresa_id, actor_user_id, command, idempotency_key)
);

CREATE INDEX IF NOT EXISTS idx_os_closure_command_order
    ON os_closure_commands (empresa_id, ordem_servico_id, created_at DESC);
