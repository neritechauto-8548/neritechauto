-- V185__create_os_checklist_items.sql
CREATE SEQUENCE os_checklist_items_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE os_checklist_items (
    id BIGINT PRIMARY KEY DEFAULT nextval('os_checklist_items_seq'),
    ordem_servico_id BIGINT NOT NULL,
    checklist_id BIGINT,
    it_checklist_id BIGINT,
    descricao VARCHAR(255) NOT NULL,
    feito BOOLEAN DEFAULT FALSE,
    ordem INTEGER,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,

    CONSTRAINT fk_oscheck_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id),
    CONSTRAINT fk_oscheck_checklist FOREIGN KEY (checklist_id) REFERENCES checklist(id),
    CONSTRAINT fk_oscheck_itcheck FOREIGN KEY (it_checklist_id) REFERENCES it_checklist(id)
);

CREATE INDEX idx_os_checklist_items_os ON os_checklist_items(ordem_servico_id);
CREATE INDEX idx_os_checklist_items_feito ON os_checklist_items(feito);
