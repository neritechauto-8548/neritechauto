-- ORC-004: composicao tecnica e comercial do orcamento.
-- A composicao permanece separada dos itens executaveis da OS. Nenhuma linha
-- desta estrutura reserva, baixa ou movimenta estoque.

ALTER TABLE ordens_servico
    ADD COLUMN IF NOT EXISTS composition_revision BIGINT NOT NULL DEFAULT 0;

CREATE TABLE estimate_service_groups (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    customer_description TEXT,
    internal_note TEXT,
    recommended BOOLEAN NOT NULL DEFAULT FALSE,
    visibility VARCHAR(24) NOT NULL DEFAULT 'CUSTOMER_VISIBLE',
    position INTEGER NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_group_budget
        FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT chk_estimate_group_title CHECK (char_length(trim(title)) BETWEEN 3 AND 120),
    CONSTRAINT chk_estimate_group_visibility CHECK (visibility IN ('CUSTOMER_VISIBLE', 'INTERNAL_ONLY')),
    CONSTRAINT chk_estimate_group_position CHECK (position >= 0),
    CONSTRAINT uk_estimate_group_position UNIQUE (empresa_id, ordem_servico_id, position)
);

CREATE TABLE estimate_line_items (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    service_group_id BIGINT NOT NULL,
    line_type VARCHAR(24) NOT NULL,
    catalog_item_id BIGINT,
    catalog_version INTEGER,
    source VARCHAR(24) NOT NULL,
    description_snapshot VARCHAR(255) NOT NULL,
    reference_snapshot VARCHAR(100),
    quantity NUMERIC(12,3) NOT NULL,
    unit_price NUMERIC(14,4) NOT NULL,
    discount_amount NUMERIC(14,2) NOT NULL DEFAULT 0,
    total_amount NUMERIC(14,2) NOT NULL,
    availability_status VARCHAR(24) NOT NULL DEFAULT 'NOT_APPLICABLE',
    position INTEGER NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_line_group
        FOREIGN KEY (service_group_id) REFERENCES estimate_service_groups(id) ON DELETE CASCADE,
    CONSTRAINT chk_estimate_line_type CHECK (line_type IN ('PART', 'LABOR', 'FEE', 'SUBLET', 'DISCOUNT', 'NOTE')),
    CONSTRAINT chk_estimate_line_source CHECK (source IN ('PRODUCT_CATALOG', 'SERVICE_CATALOG', 'KIT', 'MANUAL')),
    CONSTRAINT chk_estimate_line_quantity CHECK (quantity > 0),
    CONSTRAINT chk_estimate_line_unit_price CHECK (unit_price >= 0),
    CONSTRAINT chk_estimate_line_discount CHECK (discount_amount >= 0),
    CONSTRAINT chk_estimate_line_total CHECK (total_amount >= 0),
    CONSTRAINT chk_estimate_line_availability CHECK (availability_status IN ('AVAILABLE', 'PARTIAL', 'NEEDED', 'NOT_APPLICABLE')),
    CONSTRAINT chk_estimate_line_position CHECK (position >= 0),
    CONSTRAINT uk_estimate_line_position UNIQUE (empresa_id, service_group_id, position)
);

CREATE INDEX idx_estimate_groups_budget
    ON estimate_service_groups (empresa_id, ordem_servico_id, position);
CREATE INDEX idx_estimate_lines_group
    ON estimate_line_items (empresa_id, service_group_id, position);
CREATE INDEX idx_estimate_lines_catalog
    ON estimate_line_items (empresa_id, line_type, catalog_item_id);

COMMENT ON TABLE estimate_service_groups IS
    'Grupos comerciais do draft de orcamento; nao representam execucao de OS.';
COMMENT ON TABLE estimate_line_items IS
    'Snapshots comerciais do draft; disponibilidade e informativa e nao reserva estoque.';
