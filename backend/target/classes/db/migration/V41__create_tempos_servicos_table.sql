CREATE TABLE tempos_servicos (
    id BIGSERIAL PRIMARY KEY,
    servico_id BIGINT NOT NULL,
    tipo_veiculo_id BIGINT,
    mecanico_especialidade VARCHAR(100),
    tempo_minimo_minutos INT NOT NULL,
    tempo_maximo_minutos INT NOT NULL,
    tempo_medio_minutos INT NOT NULL,
    complexidade_fator DECIMAL(3,2) DEFAULT 1.00,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_tempo_servico_servico FOREIGN KEY (servico_id) REFERENCES servicos(id)
);
