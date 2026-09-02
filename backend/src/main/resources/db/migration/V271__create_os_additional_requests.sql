-- TELA-AUTO-OS-009 — Adicionais e Aprovação Complementar
-- Novo escopo descoberto durante a OS é proposto e decidido separadamente.

CREATE TABLE os_additional_requests (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id) ON DELETE CASCADE,
    base_os_version INTEGER,
    title VARCHAR(160) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'RASCUNHO',
    amount_delta NUMERIC(12,2) NOT NULL DEFAULT 0,
    time_delta_minutes INTEGER NOT NULL DEFAULT 0,
    recipient_name VARCHAR(160),
    recipient_channel VARCHAR(30),
    recipient_masked VARCHAR(180),
    token_hash VARCHAR(64),
    token_expires_at TIMESTAMP,
    submitted_at TIMESTAMP,
    viewed_at TIMESTAMP,
    decided_at TIMESTAMP,
    revoked_at TIMESTAMP,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_os_additional_requests_status CHECK (status IN (
        'RASCUNHO','PRONTA_PARA_ENVIO','PENDENTE','VISUALIZADA','APROVADA','PARCIAL',
        'RECUSADA','EXPIRADA','REVOGADA','SUBSTITUIDA','CANCELADA'
    )),
    CONSTRAINT ck_os_additional_requests_time CHECK (time_delta_minutes >= 0)
);

CREATE UNIQUE INDEX ux_os_additional_requests_token_hash
    ON os_additional_requests (token_hash)
    WHERE token_hash IS NOT NULL;

CREATE INDEX ix_os_additional_requests_order
    ON os_additional_requests (empresa_id, ordem_servico_id, data_cadastro DESC);

CREATE TABLE os_additional_request_items (
    id BIGSERIAL PRIMARY KEY,
    additional_request_id BIGINT NOT NULL REFERENCES os_additional_requests(id) ON DELETE CASCADE,
    operation VARCHAR(12) NOT NULL,
    item_type VARCHAR(16) NOT NULL,
    source_item_id BIGINT,
    catalog_item_id BIGINT,
    description VARCHAR(500) NOT NULL,
    quantity NUMERIC(12,3) NOT NULL DEFAULT 1,
    unit VARCHAR(20),
    amount_delta NUMERIC(12,2) NOT NULL DEFAULT 0,
    time_delta_minutes INTEGER NOT NULL DEFAULT 0,
    decision VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    decision_comment VARCHAR(500),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_os_additional_item_operation CHECK (operation IN ('ADD','UPDATE','REMOVE')),
    CONSTRAINT ck_os_additional_item_type CHECK (item_type IN ('SERVICE','PRODUCT','OTHER')),
    CONSTRAINT ck_os_additional_item_decision CHECK (decision IN ('PENDING','APPROVED','REJECTED')),
    CONSTRAINT ck_os_additional_item_quantity CHECK (quantity > 0),
    CONSTRAINT ck_os_additional_item_time CHECK (time_delta_minutes >= 0)
);

CREATE INDEX ix_os_additional_request_items_request
    ON os_additional_request_items (additional_request_id, id);
