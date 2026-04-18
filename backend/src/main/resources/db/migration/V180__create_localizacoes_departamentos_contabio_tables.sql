CREATE TABLE localizacoes_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_localizacao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_localizacoes_empresa_empresa ON localizacoes_empresa(empresa_id);

CREATE TABLE departamentos_contabio (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_departamento_contabio_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_departamentos_contabio_empresa ON departamentos_contabio(empresa_id);
