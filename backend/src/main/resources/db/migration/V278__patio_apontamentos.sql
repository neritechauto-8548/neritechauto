-- PATIO-001: evolui os tempos da OS, preservando apontamentos anteriores.
ALTER TABLE tempos_execucao ADD COLUMN empresa_id BIGINT;
ALTER TABLE tempos_execucao ADD COLUMN item_os_servico_id BIGINT;
ALTER TABLE tempos_execucao ADD COLUMN estado VARCHAR(20);
ALTER TABLE tempos_execucao ADD COLUMN segundos_ativos BIGINT NOT NULL DEFAULT 0;
ALTER TABLE tempos_execucao ADD COLUMN trecho_inicio TIMESTAMP;
ALTER TABLE tempos_execucao ADD CONSTRAINT fk_patio_tempo_item
    FOREIGN KEY (item_os_servico_id) REFERENCES itens_os_servicos(id);
ALTER TABLE tempos_execucao ADD CONSTRAINT chk_patio_tempo_estado
    CHECK (estado IS NULL OR estado IN ('EM_EXECUCAO','PAUSADO','FINALIZADO'));
ALTER TABLE tempos_execucao ADD CONSTRAINT chk_patio_tempo_segundos
    CHECK (segundos_ativos >= 0);
CREATE UNIQUE INDEX uq_patio_tempo_ativo
    ON tempos_execucao (empresa_id, item_os_servico_id, funcionario_id)
    WHERE estado IN ('EM_EXECUCAO','PAUSADO');
CREATE INDEX idx_patio_tempo_item ON tempos_execucao (empresa_id, item_os_servico_id);

-- O comando e a resposta ficam na mesma transação que o apontamento.
CREATE TABLE patio_timer_comandos (
    empresa_id BIGINT NOT NULL,
    chave UUID NOT NULL,
    acao VARCHAR(12) NOT NULL,
    ator_id BIGINT NOT NULL,
    resultado JSONB,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (empresa_id, chave),
    CONSTRAINT fk_patio_timer_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);
