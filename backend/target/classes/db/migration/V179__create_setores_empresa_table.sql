CREATE TABLE setores_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_setor_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_setores_empresa_empresa ON setores_empresa(empresa_id);
