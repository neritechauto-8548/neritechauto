-- Questionários
CREATE SEQUENCE IF NOT EXISTS seq_questionario_id START WITH 1 INCREMENT BY 1;

CREATE TABLE questionario (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_questionario_id'),
    empresa_id BIGINT NOT NULL,
    ds_questionario VARCHAR(255) NOT NULL,
    url_imagem VARCHAR(500),
    sn_ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_questionario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_questionario_empresa ON questionario(empresa_id);
CREATE INDEX idx_questionario_ativo ON questionario(sn_ativo);

-- Itens de Checklist
CREATE SEQUENCE IF NOT EXISTS seq_it_questionario_id START WITH 1 INCREMENT BY 1;

CREATE TABLE it_questionario (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_it_questionario_id'),
    id_questionario BIGINT NOT NULL,
    ds_itquestionario VARCHAR(255) NOT NULL,
    tp_itquestionario VARCHAR(2) NOT NULL CHECK (tp_itquestionario IN ('SN','AV')),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_it_questionario_questionario FOREIGN KEY (id_questionario) REFERENCES questionario(id)
);

CREATE INDEX idx_it_questionario_questionario ON it_questionario(id_questionario);
