CREATE TABLE anos_modelo (
    id BIGSERIAL PRIMARY KEY,
    modelo_id BIGINT NOT NULL,
    ano_fabricacao INT NOT NULL,
    ano_modelo INT NOT NULL,
    codigo_fipe VARCHAR(20),
    valor_fipe DECIMAL(12,2),
    data_valor_fipe DATE,
    ativo BOOLEAN DEFAULT TRUE,
    versao INT DEFAULT 0,
    CONSTRAINT fk_anos_modelo_modelo FOREIGN KEY (modelo_id) REFERENCES modelos_veiculos(id)
);
