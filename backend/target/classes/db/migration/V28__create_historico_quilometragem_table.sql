CREATE TABLE historico_quilometragem (
    id BIGSERIAL PRIMARY KEY,
    veiculo_id BIGINT,
    quilometragem INT NOT NULL,
    data_medicao DATE NOT NULL,
    tipo_medicao VARCHAR(20),
    origem_medicao VARCHAR(20),
    observacoes TEXT,
    foto_hodometro_url VARCHAR(500),
    usuario_responsavel BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    CONSTRAINT fk_historico_km_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);
