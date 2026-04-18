CREATE TABLE especialidades_servicos (
    id BIGSERIAL PRIMARY KEY,
    servico_id BIGINT NOT NULL,
    especialidade_nome VARCHAR(100) NOT NULL,
    nivel_requerido VARCHAR(20),
    obrigatoria BOOLEAN DEFAULT TRUE,
    certificacao_necessaria VARCHAR(255),
    experiencia_minima_anos INT DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_especialidade_servico FOREIGN KEY (servico_id) REFERENCES servicos(id)
);
