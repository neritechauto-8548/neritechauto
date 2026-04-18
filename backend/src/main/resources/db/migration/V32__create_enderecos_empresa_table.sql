CREATE TABLE enderecos_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    tipo_endereco VARCHAR(20),
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    pais VARCHAR(50) DEFAULT 'Brasil',
    principal BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_endereco_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);
