-- Estado operacional dos alertas derivados de fatos canônicos da OS.
CREATE TABLE patio_alertas (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    tipo VARCHAR(40) NOT NULL,
    dedupe_key VARCHAR(100) NOT NULL,
    severidade VARCHAR(10) NOT NULL CHECK (severidade IN ('BAIXA','MEDIA','ALTA','CRITICA')),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('OPEN','ACKNOWLEDGED','SNOOZED','RESOLVED')),
    motivo VARCHAR(255) NOT NULL,
    detectado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    adiado_ate TIMESTAMP,
    resolvido_em TIMESTAMP,
    versao INTEGER NOT NULL DEFAULT 0,
    UNIQUE (empresa_id, dedupe_key)
);
CREATE INDEX idx_patio_alertas_estado ON patio_alertas (empresa_id, estado, severidade, detectado_em DESC);
CREATE TABLE patio_alerta_eventos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    alerta_id BIGINT NOT NULL REFERENCES patio_alertas(id),
    estado_antes VARCHAR(20),
    estado_depois VARCHAR(20) NOT NULL,
    usuario_responsavel BIGINT,
    motivo VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_patio_alerta_eventos_alerta ON patio_alerta_eventos (empresa_id, alerta_id, criado_em DESC);
