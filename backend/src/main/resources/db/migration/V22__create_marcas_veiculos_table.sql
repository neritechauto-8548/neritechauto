CREATE TABLE marcas_veiculos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(500),
    website VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    last_modified_by VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL
);