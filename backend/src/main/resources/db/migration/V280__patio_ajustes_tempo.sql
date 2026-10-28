-- Ajustes são lançamentos imutáveis; o apontamento original permanece intacto.
CREATE TABLE patio_ajustes_tempo (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    item_os_servico_id BIGINT NOT NULL REFERENCES itens_os_servicos(id),
    tempo_execucao_id BIGINT NOT NULL REFERENCES tempos_execucao(id),
    chave UUID NOT NULL,
    delta_minutos INTEGER NOT NULL CHECK (delta_minutos <> 0 AND delta_minutos BETWEEN -1440 AND 1440),
    motivo VARCHAR(500) NOT NULL CHECK (length(trim(motivo)) >= 10),
    usuario_responsavel BIGINT NOT NULL,
    versao_item_antes INTEGER NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (empresa_id, chave)
);
CREATE INDEX idx_patio_ajustes_tempo_item ON patio_ajustes_tempo (empresa_id, item_os_servico_id, criado_em DESC);
