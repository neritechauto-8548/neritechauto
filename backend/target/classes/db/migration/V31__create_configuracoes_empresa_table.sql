CREATE TABLE configuracoes_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    regime_tributario VARCHAR(20),
    codigo_cnae_principal VARCHAR(10),
    codigo_cnae_secundario TEXT,
    capital_social DECIMAL(15,2),
    porte_empresa VARCHAR(20),
    tipo_estabelecimento VARCHAR(20),
    situacao_cadastral VARCHAR(20),
    data_situacao_cadastral DATE,
    motivo_situacao_cadastral VARCHAR(255),
    situacao_especial VARCHAR(255),
    data_situacao_especial DATE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_configuracao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_configuracao_empresa UNIQUE (empresa_id)
);
