CREATE TABLE categorias_servicos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    categoria_pai_id BIGINT,
    codigo VARCHAR(20) UNIQUE,
    cor_identificacao VARCHAR(7),
    icone VARCHAR(50),
    ordem_exibicao INT DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_categoria_servico_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_categoria_servico_pai FOREIGN KEY (categoria_pai_id) REFERENCES categorias_servicos(id)
);
