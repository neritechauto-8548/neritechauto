-- V250__add_audit_fields_produtos_fiscais.sql
ALTER TABLE produtos_fiscais
    ADD COLUMN data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN criado_por BIGINT,
    ADD COLUMN atualizado_por BIGINT,
    ADD COLUMN versao BIGINT DEFAULT 0;
