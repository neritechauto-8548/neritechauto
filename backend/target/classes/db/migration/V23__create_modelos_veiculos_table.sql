CREATE TABLE modelos_veiculos (
    id BIGSERIAL PRIMARY KEY,
    marca_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    segmento VARCHAR(20) NOT NULL,
    numero_portas INT,
    numero_lugares INT,
    tipo_tracao VARCHAR(20) NOT NULL,
    id_empresa BIGINT NOT NULL,
    data_cadastro TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_modelo_marca FOREIGN KEY (marca_id) REFERENCES marcas_veiculos(id),
    CONSTRAINT uk_modelo_marca_nome UNIQUE (marca_id, nome)
);