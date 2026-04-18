CREATE TABLE documentos_veiculos (
    id BIGSERIAL PRIMARY KEY,
    veiculo_id BIGINT,
    tipo_documento VARCHAR(20),
    numero_documento VARCHAR(100),
    orgao_emissor VARCHAR(100),
    data_emissao DATE,
    data_vencimento DATE,
    status VARCHAR(20),
    arquivo_url VARCHAR(500),
    arquivo_nome VARCHAR(255),
    arquivo_tamanho BIGINT,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    CONSTRAINT fk_documento_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id)
);
