-- V56__create_localizacoes_estoque_table.sql
CREATE SEQUENCE localizacoes_estoque_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE localizacoes_estoque (
    id BIGINT PRIMARY KEY DEFAULT nextval('localizacoes_estoque_seq'),
    empresa_id BIGINT NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_localizacao VARCHAR(20) CHECK (tipo_localizacao IN ('PRATELEIRA', 'GAVETA', 'ARMARIO', 'AREA', 'DEPOSITO', 'VITRINE', 'OUTROS')),
    setor VARCHAR(50),
    corredor VARCHAR(10),
    prateleira VARCHAR(10),
    posicao VARCHAR(10),
    capacidade_maxima DECIMAL(10,2),
    temperatura_controlada BOOLEAN DEFAULT FALSE,
    temperatura_min DECIMAL(4,1),
    temperatura_max DECIMAL(4,1),
    umidade_controlada BOOLEAN DEFAULT FALSE,
    umidade_min DECIMAL(5,2),
    umidade_max DECIMAL(5,2),
    acesso_restrito BOOLEAN DEFAULT FALSE,
    usuarios_acesso JSONB,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT uk_localizacao_codigo UNIQUE (empresa_id, codigo),
    CONSTRAINT fk_localizacao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_localizacoes_empresa ON localizacoes_estoque(empresa_id);
CREATE INDEX idx_localizacoes_codigo ON localizacoes_estoque(codigo);
CREATE INDEX idx_localizacoes_ativo ON localizacoes_estoque(ativo);
