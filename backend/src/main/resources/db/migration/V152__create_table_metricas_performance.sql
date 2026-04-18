-- V152: Create metricas_performance table
-- Description: System performance metrics

CREATE SEQUENCE IF NOT EXISTS seq_metricas_performance START WITH 1 INCREMENT BY 1;

CREATE TABLE metricas_performance (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_metricas_performance'),
    empresa_id BIGINT,
    metrica VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    valor_numerico DECIMAL(15,4),
    valor_texto VARCHAR(255),
    unidade_medida VARCHAR(20),
    data_medicao TIMESTAMP NOT NULL,
    periodo_referencia VARCHAR(20),
    contexto_adicional TEXT, -- JSON
    servidor VARCHAR(100),
    versao_aplicacao VARCHAR(20),
    benchmark_anterior DECIMAL(15,4),
    variacao_percentual DECIMAL(8,4),
    tendencia VARCHAR(20) CHECK (tendencia IN ('SUBINDO', 'DESCENDO', 'ESTAVEL')),
    alerta_gerado BOOLEAN DEFAULT FALSE,
    limite_alerta_min DECIMAL(15,4),
    limite_alerta_max DECIMAL(15,4),
    observacoes TEXT,

    CONSTRAINT fk_metricas_performance_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_metricas_performance_empresa ON metricas_performance(empresa_id);
CREATE INDEX idx_metricas_performance_metrica ON metricas_performance(metrica);
CREATE INDEX idx_metricas_performance_data ON metricas_performance(data_medicao);

COMMENT ON TABLE metricas_performance IS 'Métricas de performance técnica e de negócio';
