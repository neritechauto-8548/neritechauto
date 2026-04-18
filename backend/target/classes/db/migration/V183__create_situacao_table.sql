CREATE TABLE situacao (
    id BIGSERIAL PRIMARY KEY,
    nm_situacao VARCHAR(255) NOT NULL,
    ds_situacao VARCHAR(1000),
    cor_situacao VARCHAR(7),
    empresa_id BIGINT NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_situacao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_situacao_empresa ON situacao(empresa_id);
