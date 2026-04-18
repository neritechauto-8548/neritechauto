-- V186: Create os_fotos table for Ordem de Serviço images
CREATE TABLE IF NOT EXISTS os_fotos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    arquivo_url VARCHAR(1000),
    arquivo_path VARCHAR(1000),
    content_type VARCHAR(200),
    tamanho BIGINT,
    descricao VARCHAR(500),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    CONSTRAINT fk_os_fotos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_os_fotos_os ON os_fotos(ordem_servico_id);
CREATE INDEX IF NOT EXISTS idx_os_fotos_empresa ON os_fotos(empresa_id);
COMMENT ON TABLE os_fotos IS 'Fotos vinculadas às Ordens de Serviço';
