-- CRM · respostas internas aos questionários existentes.
-- Retenção e distribuição externa dependem de política de produto; não expor este endpoint publicamente.
CREATE TABLE pesquisa_respostas (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    questionario_id BIGINT NOT NULL REFERENCES questionario(id),
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    chave_idempotencia VARCHAR(36) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT uq_pesquisa_respostas_tenant_chave UNIQUE (empresa_id, chave_idempotencia)
);
CREATE INDEX idx_pesquisa_respostas_empresa_questionario
    ON pesquisa_respostas (empresa_id, questionario_id, data_cadastro DESC);
CREATE INDEX idx_pesquisa_respostas_empresa_cliente
    ON pesquisa_respostas (empresa_id, cliente_id);

CREATE TABLE pesquisa_resposta_itens (
    id BIGSERIAL PRIMARY KEY,
    resposta_id BIGINT NOT NULL REFERENCES pesquisa_respostas(id) ON DELETE RESTRICT,
    pergunta_id BIGINT NOT NULL REFERENCES it_questionario(id),
    pergunta_texto VARCHAR(255) NOT NULL,
    pergunta_tipo VARCHAR(2) NOT NULL,
    resposta_texto VARCHAR(1000) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT uq_pesquisa_resposta_item UNIQUE (resposta_id, pergunta_id),
    CONSTRAINT ck_pesquisa_resposta_tipo CHECK (pergunta_tipo IN ('SN','AV','AB'))
);
CREATE INDEX idx_pesquisa_resposta_itens_resposta
    ON pesquisa_resposta_itens (resposta_id);
