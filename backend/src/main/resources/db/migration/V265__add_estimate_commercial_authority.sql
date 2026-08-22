-- ORC-004: autoridade comercial, preco fechado e desconto rastreavel.
-- Valores comerciais continuam canônicos no backend. A UI envia apenas a
-- intenção; distribuição, alçada, impacto e bloqueios são persistidos aqui.

ALTER TABLE estimate_service_groups
    ADD COLUMN IF NOT EXISTS package_price NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS package_distribution_method VARCHAR(24),
    ADD COLUMN IF NOT EXISTS package_original_subtotal NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS package_adjustment_amount NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS package_price_source_type VARCHAR(40),
    ADD COLUMN IF NOT EXISTS package_price_source_id BIGINT,
    ADD COLUMN IF NOT EXISTS package_price_source_version INTEGER,
    ADD COLUMN IF NOT EXISTS package_applied_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS package_override_reason VARCHAR(500),
    ADD COLUMN IF NOT EXISTS package_authority_status VARCHAR(24);

ALTER TABLE estimate_line_items
    ADD COLUMN IF NOT EXISTS gross_amount NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS allocated_package_amount NUMERIC(14,2),
    ADD COLUMN IF NOT EXISTS package_adjustment_amount NUMERIC(14,2) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS price_source_type VARCHAR(40),
    ADD COLUMN IF NOT EXISTS price_source_id BIGINT,
    ADD COLUMN IF NOT EXISTS price_source_version INTEGER,
    ADD COLUMN IF NOT EXISTS price_applied_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS price_overridden BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS price_override_reason VARCHAR(500),
    ADD COLUMN IF NOT EXISTS discount_type VARCHAR(20) NOT NULL DEFAULT 'NONE',
    ADD COLUMN IF NOT EXISTS discount_value NUMERIC(14,4) NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS discount_reason VARCHAR(500),
    ADD COLUMN IF NOT EXISTS discount_authority_status VARCHAR(24) NOT NULL DEFAULT 'NONE',
    ADD COLUMN IF NOT EXISTS discount_authority_limit_percent NUMERIC(7,4),
    ADD COLUMN IF NOT EXISTS discount_requested_by BIGINT;

UPDATE estimate_line_items
SET gross_amount = ROUND(quantity * unit_price, 2),
    price_source_type = CASE source
        WHEN 'PRODUCT_CATALOG' THEN 'PRODUCT_CATALOG'
        WHEN 'SERVICE_CATALOG' THEN 'SERVICE_CATALOG'
        WHEN 'KIT' THEN 'KIT_VERSION'
        ELSE 'MANUAL'
    END,
    price_source_id = catalog_item_id,
    price_source_version = catalog_version,
    price_applied_at = COALESCE(data_cadastro, CURRENT_TIMESTAMP)
WHERE gross_amount IS NULL
   OR price_source_type IS NULL
   OR price_applied_at IS NULL;

ALTER TABLE estimate_line_items
    ALTER COLUMN gross_amount SET NOT NULL,
    ALTER COLUMN price_source_type SET NOT NULL,
    ALTER COLUMN price_applied_at SET NOT NULL;

ALTER TABLE estimate_service_groups
    ADD CONSTRAINT chk_estimate_group_package_price CHECK (package_price IS NULL OR package_price >= 0),
    ADD CONSTRAINT chk_estimate_group_package_method CHECK (
        package_distribution_method IS NULL OR package_distribution_method IN ('WEIGHTED', 'LABOR_FIRST', 'POLICY')),
    ADD CONSTRAINT chk_estimate_group_package_authority CHECK (
        package_authority_status IS NULL OR package_authority_status IN ('APPROVED', 'PENDING_APPROVAL')),
    ADD CONSTRAINT chk_estimate_group_package_complete CHECK (
        (package_price IS NULL AND package_distribution_method IS NULL)
        OR (package_price IS NOT NULL AND package_distribution_method IS NOT NULL));

ALTER TABLE estimate_line_items
    ADD CONSTRAINT chk_estimate_line_gross CHECK (gross_amount >= 0),
    ADD CONSTRAINT chk_estimate_line_discount_type CHECK (discount_type IN ('NONE', 'FIXED', 'PERCENT')),
    ADD CONSTRAINT chk_estimate_line_discount_value CHECK (discount_value >= 0),
    ADD CONSTRAINT chk_estimate_line_discount_authority CHECK (
        discount_authority_status IN ('NONE', 'APPROVED', 'PENDING_APPROVAL', 'REJECTED'));

CREATE TABLE estimate_discount_authority_limits (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    funcao_id BIGINT NOT NULL,
    max_percentage NUMERIC(7,4) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_discount_limit_function FOREIGN KEY (funcao_id) REFERENCES funcoes(id),
    CONSTRAINT chk_estimate_discount_limit_percentage CHECK (max_percentage BETWEEN 0 AND 100),
    CONSTRAINT uk_estimate_discount_limit_function UNIQUE (empresa_id, funcao_id)
);

CREATE TABLE estimate_discount_approval_requests (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    service_group_id BIGINT NOT NULL,
    line_item_id BIGINT NOT NULL,
    requested_revision BIGINT NOT NULL,
    discount_type VARCHAR(20) NOT NULL,
    discount_value NUMERIC(14,4) NOT NULL,
    calculated_amount NUMERIC(14,2) NOT NULL,
    equivalent_percentage NUMERIC(7,4) NOT NULL,
    authority_limit_percentage NUMERIC(7,4) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    status VARCHAR(24) NOT NULL,
    requested_by BIGINT NOT NULL,
    decided_by BIGINT,
    decision_reason VARCHAR(500),
    decided_at TIMESTAMP,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_discount_approval_budget
        FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_estimate_discount_approval_group
        FOREIGN KEY (service_group_id) REFERENCES estimate_service_groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_estimate_discount_approval_line
        FOREIGN KEY (line_item_id) REFERENCES estimate_line_items(id) ON DELETE CASCADE,
    CONSTRAINT chk_estimate_discount_approval_type CHECK (discount_type IN ('FIXED', 'PERCENT')),
    CONSTRAINT chk_estimate_discount_approval_status CHECK (
        status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    CONSTRAINT chk_estimate_discount_approval_values CHECK (
        discount_value > 0 AND calculated_amount > 0
        AND equivalent_percentage > 0 AND equivalent_percentage <= 100
        AND authority_limit_percentage >= 0 AND authority_limit_percentage <= 100)
);

CREATE UNIQUE INDEX uk_estimate_discount_pending_line
    ON estimate_discount_approval_requests (empresa_id, line_item_id)
    WHERE status = 'PENDING';

CREATE TABLE estimate_commercial_adjustments (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    service_group_id BIGINT,
    line_item_id BIGINT,
    estimate_revision BIGINT NOT NULL,
    adjustment_type VARCHAR(32) NOT NULL,
    previous_amount NUMERIC(14,4),
    new_amount NUMERIC(14,4),
    impact_amount NUMERIC(14,2) NOT NULL,
    distribution_method VARCHAR(24),
    price_source_type VARCHAR(40),
    price_source_id BIGINT,
    price_source_version INTEGER,
    reason VARCHAR(500),
    authority_status VARCHAR(24) NOT NULL,
    actor_id BIGINT NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_estimate_commercial_adjustment_budget
        FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_estimate_commercial_adjustment_group
        FOREIGN KEY (service_group_id) REFERENCES estimate_service_groups(id) ON DELETE SET NULL,
    CONSTRAINT fk_estimate_commercial_adjustment_line
        FOREIGN KEY (line_item_id) REFERENCES estimate_line_items(id) ON DELETE SET NULL,
    CONSTRAINT chk_estimate_commercial_adjustment_type CHECK (
        adjustment_type IN ('PACKAGE_PRICE', 'UNIT_PRICE_OVERRIDE', 'LINE_DISCOUNT', 'DISCOUNT_DECISION')),
    CONSTRAINT chk_estimate_commercial_authority_status CHECK (
        authority_status IN ('APPROVED', 'PENDING_APPROVAL', 'REJECTED', 'CANCELLED'))
);

CREATE INDEX idx_estimate_discount_approvals_budget
    ON estimate_discount_approval_requests (empresa_id, ordem_servico_id, status, data_cadastro);
CREATE INDEX idx_estimate_commercial_adjustments_budget
    ON estimate_commercial_adjustments (empresa_id, ordem_servico_id, estimate_revision, data_cadastro);

-- Permissões granulares são criadas por tenant e herdadas das permissões
-- legadas equivalentes. Limites percentuais não são inventados: sem uma linha
-- configurada em estimate_discount_authority_limits, o limite é 0% e qualquer
-- desconto positivo segue para aprovação gerencial.
INSERT INTO permissoes (
    empresa_id, chave, valor, descricao, versao, data_cadastro, data_atualizacao)
SELECT e.id, 'Permissões comerciais do orçamento', permission.valor, permission.descricao,
       0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM empresa e
CROSS JOIN (VALUES
    ('ORCAMENTO_PRECO_EDITAR', 'Editar preço unitário e preço fechado do orçamento'),
    ('ORCAMENTO_DESCONTO_APLICAR', 'Aplicar desconto conforme alçada configurada'),
    ('ORCAMENTO_DESCONTO_APROVAR', 'Decidir exceções de desconto do orçamento'),
    ('ORCAMENTO_CUSTO_VISUALIZAR', 'Visualizar custo e margem autorizados do orçamento')
) AS permission(valor, descricao)
ON CONFLICT (valor, empresa_id) DO NOTHING;

INSERT INTO funcoes_permissoes (funcao_id, permissao_id, empresa_id, created_at)
SELECT DISTINCT fp.funcao_id, target.id, fp.empresa_id, CURRENT_TIMESTAMP
FROM funcoes_permissoes fp
JOIN permissoes legacy ON legacy.id = fp.permissao_id AND legacy.empresa_id = fp.empresa_id
JOIN permissoes target ON target.empresa_id = fp.empresa_id
WHERE (target.valor = 'ORCAMENTO_PRECO_EDITAR' AND legacy.valor IN ('OS_EDITAR', 'OS_ALTERAR'))
   OR (target.valor = 'ORCAMENTO_DESCONTO_APLICAR' AND legacy.valor = 'OS_NEG_PAGAMENTO')
   OR (target.valor = 'ORCAMENTO_DESCONTO_APROVAR' AND legacy.valor IN ('GERAL_CONFIG_SISTEMA', 'FIN_FECHAMENTO'))
   OR (target.valor = 'ORCAMENTO_CUSTO_VISUALIZAR' AND legacy.valor = 'PS_VER_CUSTO')
ON CONFLICT (funcao_id, permissao_id) DO NOTHING;

COMMENT ON TABLE estimate_discount_authority_limits IS
    'Limite de desconto por função; ausência significa 0% e falha fechada para aprovação.';
COMMENT ON TABLE estimate_discount_approval_requests IS
    'Exceções de desconto preservadas até decisão gerencial; bloqueiam revisão enquanto PENDING.';
COMMENT ON TABLE estimate_commercial_adjustments IS
    'Trilha append-only de preço fechado, override, desconto e decisão comercial.';

