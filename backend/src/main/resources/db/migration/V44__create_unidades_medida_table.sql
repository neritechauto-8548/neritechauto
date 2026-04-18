CREATE TABLE unidades_medida (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(10) NOT NULL UNIQUE,
    descricao TEXT,
    tipo VARCHAR(20),
    fator_conversao_base DECIMAL(15,6) DEFAULT 1.000000,
    unidade_base BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0
);
