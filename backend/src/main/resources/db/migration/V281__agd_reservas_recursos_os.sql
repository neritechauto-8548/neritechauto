-- Reserva normalizada de recursos da Agenda para OS, preservando agendamentos legados.
CREATE TABLE agd_reservas_recursos_os (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    recurso_id BIGINT NOT NULL REFERENCES agd_recursos_agenda(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    chave UUID NOT NULL,
    inicio TIMESTAMP NOT NULL,
    fim TIMESTAMP NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('RESERVADO','OCUPADO','LIBERADO','CANCELADO')),
    versao INTEGER NOT NULL DEFAULT 0,
    criado_por BIGINT NOT NULL,
    atualizado_por BIGINT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (fim > inicio),
    UNIQUE (empresa_id, chave)
);
CREATE INDEX idx_agd_reservas_recurso_periodo
    ON agd_reservas_recursos_os (empresa_id, recurso_id, inicio, fim)
    WHERE estado IN ('RESERVADO','OCUPADO');
CREATE INDEX idx_agd_reservas_os
    ON agd_reservas_recursos_os (empresa_id, ordem_servico_id, estado);
