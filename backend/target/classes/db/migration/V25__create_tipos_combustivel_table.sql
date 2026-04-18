CREATE TABLE tipos_combustivel (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    sigla VARCHAR(10) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    versao INT DEFAULT 0
);
