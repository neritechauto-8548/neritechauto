CREATE TABLE fotos_veiculos (
    id BIGSERIAL PRIMARY KEY,
    veiculo_id BIGINT,
    tipo_foto VARCHAR(30),
    descricao VARCHAR(255),
    arquivo_url VARCHAR(500) NOT NULL,
    arquivo_nome VARCHAR(255),
    arquivo_tamanho BIGINT,
    largura INT,
    altura INT,
    principal BOOLEAN DEFAULT FALSE,
    ordem_exibicao INT DEFAULT 0,
    data_foto TIMESTAMP,
    usuario_upload BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    CONSTRAINT fk_foto_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);
