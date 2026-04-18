CREATE TABLE checklist (
    id BIGSERIAL PRIMARY KEY,
    id_empresa BIGINT NOT NULL,
    ds_checklist VARCHAR(255) NOT NULL,
    url_imagem VARCHAR(500),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_checklist_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id)
);

CREATE INDEX idx_checklist_empresa ON checklist(id_empresa);

CREATE TABLE it_checklist (
    id BIGSERIAL PRIMARY KEY,
    id_check_list BIGINT NOT NULL,
    ds_itchecklist VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    CONSTRAINT fk_it_checklist_checklist FOREIGN KEY (id_check_list) REFERENCES checklist(id)
);

CREATE INDEX idx_it_checklist_checklist ON it_checklist(id_check_list);
