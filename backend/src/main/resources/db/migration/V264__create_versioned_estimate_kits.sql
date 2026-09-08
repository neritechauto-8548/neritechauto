-- ORC-004: catalogo versionado de kits e instanciacao idempotente.
-- O kit mestre e imutavel para a instancia do orcamento: grupo e linhas
-- carregam snapshots e nunca escrevem de volta no catalogo.

CREATE TABLE catalog_kits (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    reference VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    current_version INTEGER NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_catalog_kit_name CHECK (char_length(trim(name)) BETWEEN 3 AND 120),
    CONSTRAINT chk_catalog_kit_version CHECK (current_version > 0),
    CONSTRAINT uk_catalog_kit_reference UNIQUE (empresa_id, reference)
);

CREATE TABLE catalog_kit_versions (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    kit_id BIGINT NOT NULL,
    version_number INTEGER NOT NULL,
    title_snapshot VARCHAR(120) NOT NULL,
    description_snapshot TEXT,
    recommended_default BOOLEAN NOT NULL DEFAULT FALSE,
    published BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_catalog_kit_version_kit FOREIGN KEY (kit_id) REFERENCES catalog_kits(id),
    CONSTRAINT chk_catalog_kit_version_number CHECK (version_number > 0),
    CONSTRAINT uk_catalog_kit_version UNIQUE (empresa_id, kit_id, version_number)
);

CREATE TABLE catalog_kit_version_items (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    kit_version_id BIGINT NOT NULL,
    line_type VARCHAR(24) NOT NULL,
    catalog_item_id BIGINT NOT NULL,
    catalog_version INTEGER,
    description_snapshot VARCHAR(255) NOT NULL,
    reference_snapshot VARCHAR(100),
    quantity NUMERIC(12,3) NOT NULL,
    unit_price_snapshot NUMERIC(14,4) NOT NULL,
    position INTEGER NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_catalog_kit_item_version FOREIGN KEY (kit_version_id) REFERENCES catalog_kit_versions(id),
    CONSTRAINT chk_catalog_kit_item_type CHECK (line_type IN ('PART', 'LABOR')),
    CONSTRAINT chk_catalog_kit_item_quantity CHECK (quantity > 0),
    CONSTRAINT chk_catalog_kit_item_price CHECK (unit_price_snapshot >= 0),
    CONSTRAINT chk_catalog_kit_item_position CHECK (position >= 0),
    CONSTRAINT uk_catalog_kit_item_position UNIQUE (empresa_id, kit_version_id, position)
);

ALTER TABLE estimate_service_groups
    ADD COLUMN IF NOT EXISTS kit_origin_id BIGINT,
    ADD COLUMN IF NOT EXISTS kit_origin_version INTEGER;

ALTER TABLE estimate_line_items
    ADD COLUMN IF NOT EXISTS kit_origin_id BIGINT,
    ADD COLUMN IF NOT EXISTS kit_origin_version INTEGER;

CREATE TABLE estimate_kit_instantiations (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    service_group_id BIGINT NOT NULL,
    kit_origin_id BIGINT NOT NULL,
    kit_origin_version INTEGER NOT NULL,
    idempotency_key VARCHAR(200) NOT NULL,
    request_fingerprint VARCHAR(64) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_kit_instantiation_budget
        FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_estimate_kit_instantiation_group
        FOREIGN KEY (service_group_id) REFERENCES estimate_service_groups(id) ON DELETE CASCADE,
    CONSTRAINT uk_estimate_kit_idempotency UNIQUE (empresa_id, ordem_servico_id, idempotency_key)
);

CREATE INDEX idx_catalog_kits_search ON catalog_kits (empresa_id, active, name);
CREATE INDEX idx_catalog_kit_versions_current
    ON catalog_kit_versions (empresa_id, kit_id, version_number, published);
CREATE INDEX idx_estimate_kit_origin
    ON estimate_kit_instantiations (empresa_id, ordem_servico_id, kit_origin_id, kit_origin_version);

COMMENT ON TABLE catalog_kit_versions IS
    'Versao publicada e imutavel do kit; itens guardam snapshots comerciais.';
COMMENT ON TABLE estimate_kit_instantiations IS
    'Registro de idempotencia por orcamento; retry nunca duplica grupo ou linhas.';
