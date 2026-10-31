-- ADM-004: catálogo e entregas de equipamentos de proteção individual.
-- Sem exclusão em cascata: histórico de segurança do trabalho deve ser preservado.
CREATE TABLE rh_epis (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    nome VARCHAR(160) NOT NULL,
    numero_ca VARCHAR(32),
    categoria VARCHAR(90),
    fabricante VARCHAR(120),
    tamanho VARCHAR(40),
    especificacoes TEXT,
    descricao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    versao INT NOT NULL DEFAULT 0,
    CONSTRAINT ck_rh_epis_nome CHECK (length(trim(nome)) >= 2)
);
CREATE UNIQUE INDEX uk_rh_epis_empresa_nome_ca_ativo
 ON rh_epis (empresa_id, lower(nome), coalesce(lower(numero_ca), ''))
 WHERE ativo = true;
CREATE INDEX idx_rh_epis_empresa_ativo ON rh_epis (empresa_id, ativo);

CREATE TABLE rh_epi_entregas (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    funcionario_id BIGINT NOT NULL REFERENCES funcionarios(id),
    epi_id BIGINT NOT NULL REFERENCES rh_epis(id),
    data_entrega DATE NOT NULL,
    data_validade DATE,
    observacao TEXT,
    registrado_por BIGINT,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_rh_epi_entrega_validade CHECK (data_validade IS NULL OR data_validade >= data_entrega)
);
CREATE INDEX idx_rh_epi_entregas_func ON rh_epi_entregas (empresa_id, funcionario_id, data_entrega DESC);
CREATE INDEX idx_rh_epi_entregas_epi ON rh_epi_entregas (empresa_id, epi_id);
