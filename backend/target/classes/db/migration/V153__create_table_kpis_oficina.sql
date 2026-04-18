-- V153: Create kpis_oficina table
-- Description: Key Performance Indicators for the workshop

CREATE SEQUENCE IF NOT EXISTS seq_kpis_oficina START WITH 1 INCREMENT BY 1;

CREATE TABLE kpis_oficina (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_kpis_oficina'),
    empresa_id BIGINT,
    nome_kpi VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    descricao TEXT,
    formula_calculo TEXT,
    valor_atual DECIMAL(15,4),
    valor_meta DECIMAL(15,4),
    valor_periodo_anterior DECIMAL(15,4),
    unidade_medida VARCHAR(20),
    frequencia_calculo VARCHAR(20) CHECK (frequencia_calculo IN ('DIARIO', 'SEMANAL', 'MENSAL', 'TRIMESTRAL', 'ANUAL')),
    periodo_referencia VARCHAR(20) NOT NULL,
    data_calculo TIMESTAMP NOT NULL,
    percentual_meta_atingido DECIMAL(8,4),
    variacao_periodo_anterior DECIMAL(8,4),
    tendencia VARCHAR(20) CHECK (tendencia IN ('POSITIVA', 'NEGATIVA', 'ESTAVEL')),
    cor_indicador VARCHAR(7),
    origem_dados TEXT, -- JSON
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    ordem_exibicao INT DEFAULT 0,
    dashboard_publico BOOLEAN DEFAULT FALSE,
    calculado_automaticamente BOOLEAN DEFAULT TRUE,
    data_proxima_atualizacao TIMESTAMP,

    CONSTRAINT fk_kpis_oficina_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_kpis_oficina_empresa ON kpis_oficina(empresa_id);
CREATE INDEX idx_kpis_oficina_nome ON kpis_oficina(nome_kpi);
CREATE INDEX idx_kpis_oficina_data ON kpis_oficina(data_calculo);

COMMENT ON TABLE kpis_oficina IS 'Indicadores chave de performance (KPIs)';
