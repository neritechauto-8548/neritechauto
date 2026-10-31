-- Complementos de Gestão de Recursos - registros sempre limitados por empresa.
-- As tabelas anteriores de áreas/serviços permanecem para preservar vínculos com OS, RH e estoque.
CREATE TABLE IF NOT EXISTS subareas_oficina (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    area_id BIGINT NOT NULL REFERENCES departamentos_contabio(id),
    descricao VARCHAR(200) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_subarea_nome CHECK (length(trim(descricao)) > 0)
);
CREATE INDEX IF NOT EXISTS idx_subareas_empresa_area ON subareas_oficina(empresa_id, area_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_subarea_nome_ativa ON subareas_oficina(empresa_id, area_id, lower(trim(descricao))) WHERE ativo;

CREATE TABLE IF NOT EXISTS acessorios_veiculo (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    descricao VARCHAR(200) NOT NULL,
    checklist BOOLEAN NOT NULL DEFAULT FALSE,
    padrao BOOLEAN NOT NULL DEFAULT FALSE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_acessorio_nome CHECK (length(trim(descricao)) > 0)
);
CREATE INDEX IF NOT EXISTS idx_acessorios_empresa_ativo ON acessorios_veiculo(empresa_id, ativo);
CREATE UNIQUE INDEX IF NOT EXISTS uq_acessorio_nome_ativo ON acessorios_veiculo(empresa_id, lower(trim(descricao))) WHERE ativo;

ALTER TABLE servicos ADD COLUMN IF NOT EXISTS vida_util_dias INTEGER;
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS km_manutencao INTEGER;
ALTER TABLE servicos ADD COLUMN IF NOT EXISTS nbs VARCHAR(30);
ALTER TABLE servicos ADD CONSTRAINT chk_servicos_vida_util_nao_negativa CHECK (vida_util_dias IS NULL OR vida_util_dias >= 0);
ALTER TABLE servicos ADD CONSTRAINT chk_servicos_km_nao_negativo CHECK (km_manutencao IS NULL OR km_manutencao >= 0);

CREATE TABLE IF NOT EXISTS precos_servico_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    servico_id BIGINT NOT NULL REFERENCES servicos(id),
    preco NUMERIC(12,2) NOT NULL DEFAULT 0,
    custo_fixo NUMERIC(12,2) NOT NULL DEFAULT 0,
    comissao_percentual NUMERIC(5,2) NOT NULL DEFAULT 0,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    versao INTEGER NOT NULL DEFAULT 0,
    UNIQUE (empresa_id, servico_id),
    CHECK (preco >= 0 AND custo_fixo >= 0 AND comissao_percentual BETWEEN 0 AND 100)
);
CREATE INDEX IF NOT EXISTS idx_precos_servico_empresa ON precos_servico_empresa(empresa_id, servico_id);

CREATE TABLE IF NOT EXISTS aplicacoes_servico_modelo (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    servico_id BIGINT NOT NULL REFERENCES servicos(id),
    marca_id BIGINT NOT NULL REFERENCES marcas_veiculos(id),
    modelo_id BIGINT NOT NULL REFERENCES modelos_veiculos(id),
    tempo NUMERIC(8,2) NOT NULL CHECK (tempo > 0),
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (empresa_id, servico_id, modelo_id)
);
CREATE INDEX IF NOT EXISTS idx_aplicacoes_empresa_servico ON aplicacoes_servico_modelo(empresa_id, servico_id);
