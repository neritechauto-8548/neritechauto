-- Impede referências cruzadas entre empresas também no banco.
ALTER TABLE ordens_servico ADD CONSTRAINT uq_patio_os_empresa UNIQUE (id, empresa_id);
ALTER TABLE agd_recursos_agenda ADD CONSTRAINT uq_patio_recurso_empresa UNIQUE (id, empresa_id);
ALTER TABLE tempos_execucao ADD CONSTRAINT uq_patio_tempo_item_empresa
    UNIQUE (id, item_os_servico_id, empresa_id);
ALTER TABLE patio_alertas ADD CONSTRAINT uq_patio_alerta_empresa UNIQUE (id, empresa_id);

ALTER TABLE agd_reservas_recursos_os ADD CONSTRAINT fk_patio_reserva_os_empresa
    FOREIGN KEY (ordem_servico_id, empresa_id) REFERENCES ordens_servico (id, empresa_id);
ALTER TABLE agd_reservas_recursos_os ADD CONSTRAINT fk_patio_reserva_recurso_empresa
    FOREIGN KEY (recurso_id, empresa_id) REFERENCES agd_recursos_agenda (id, empresa_id);
ALTER TABLE patio_alertas ADD CONSTRAINT fk_patio_alerta_os_empresa
    FOREIGN KEY (ordem_servico_id, empresa_id) REFERENCES ordens_servico (id, empresa_id);
ALTER TABLE patio_alerta_eventos ADD CONSTRAINT fk_patio_alerta_evento_empresa
    FOREIGN KEY (alerta_id, empresa_id) REFERENCES patio_alertas (id, empresa_id);
ALTER TABLE patio_ajustes_tempo ADD CONSTRAINT fk_patio_ajuste_tempo_item_empresa
    FOREIGN KEY (tempo_execucao_id, item_os_servico_id, empresa_id)
    REFERENCES tempos_execucao (id, item_os_servico_id, empresa_id);
