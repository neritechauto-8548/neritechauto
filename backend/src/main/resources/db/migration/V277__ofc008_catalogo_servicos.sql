ALTER TABLE servicos ADD COLUMN IF NOT EXISTS codigo VARCHAR(50);
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS area_id BIGINT;
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS unidade VARCHAR(30);
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS tempo_previsto_horas DECIMAL(8,2);
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS aliquota_iss DECIMAL(5,2);
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS codigo_servico_municipal VARCHAR(30);

CREATE UNIQUE INDEX IF NOT EXISTS uk_servicos_empresa_codigo_ci
    ON servicos (empresa_id, lower(codigo)) WHERE codigo IS NOT NULL AND trim(codigo) <> '';
CREATE INDEX IF NOT EXISTS idx_servicos_empresa_area_ativo ON servicos (empresa_id, area_id, ativo);

ALTER TABLE servicos ADD CONSTRAINT chk_servicos_tempo_positivo
    CHECK (tempo_previsto_horas IS NULL OR tempo_previsto_horas > 0);
ALTER TABLE servicos ADD CONSTRAINT chk_servicos_valores_nao_negativos
    CHECK (preco_base >= 0 AND custo >= 0 AND (aliquota_iss IS NULL OR aliquota_iss >= 0));
