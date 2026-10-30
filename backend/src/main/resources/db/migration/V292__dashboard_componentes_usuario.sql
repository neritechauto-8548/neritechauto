-- HOME-001: a personalizacao da Visao geral pertence ao usuario e a empresa autenticados.
-- Nunca compartilhe este arranjo por empresa; cada usuario decide sua propria grade.
CREATE TABLE IF NOT EXISTS dashboard_componentes_usuario (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    configuracao TEXT NOT NULL,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dashboard_componentes_usuario UNIQUE (empresa_id, usuario_id),
    CONSTRAINT ck_dashboard_componentes_tamanho CHECK (length(configuracao) <= 12000)
);
CREATE INDEX IF NOT EXISTS idx_dashboard_componentes_empresa_usuario
    ON dashboard_componentes_usuario(empresa_id, usuario_id);
