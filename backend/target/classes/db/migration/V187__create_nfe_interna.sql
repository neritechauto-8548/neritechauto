-- V187: Tabela para Nota Fiscal interna (não transmitida)
CREATE TABLE IF NOT EXISTS nfe_interna (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    numero BIGINT NOT NULL,
    serie VARCHAR(10) NOT NULL,
    data_emissao TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    pdf_path VARCHAR(1000),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_nfe_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_nfe_interna_numero_serie_empresa ON nfe_interna(empresa_id, numero, serie);
