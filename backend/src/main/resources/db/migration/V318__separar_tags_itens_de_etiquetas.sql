-- Tags de itens pertencem às configurações da empresa e não compartilham
-- registros com as etiquetas gerais do menu Configurações.
CREATE TABLE tags_itens_ordem_servico_empresa (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    descricao VARCHAR(100) NOT NULL,
    cor VARCHAR(9) NOT NULL,
    criado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tags_itens_os_empresa
    ON tags_itens_ordem_servico_empresa (empresa_id);

CREATE UNIQUE INDEX ux_tags_itens_os_empresa_descricao
    ON tags_itens_ordem_servico_empresa (empresa_id, lower(descricao));
