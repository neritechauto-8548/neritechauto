-- V155: Create aliquotas_impostos table
-- Description: Tax rates definitions

CREATE SEQUENCE IF NOT EXISTS seq_aliquotas_impostos START WITH 1 INCREMENT BY 1;

CREATE TABLE aliquotas_impostos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_aliquotas_impostos'),
    regime_tributario_id BIGINT,
    tipo_imposto VARCHAR(20) NOT NULL,
    nome_imposto VARCHAR(100) NOT NULL,
    aliquota_percentual DECIMAL(8,4) NOT NULL,
    base_calculo VARCHAR(50),
    faixa_inicial DECIMAL(15,2) DEFAULT 0,
    faixa_final DECIMAL(15,2),
    anexo_simples VARCHAR(5),
    atividade_cnae VARCHAR(10),
    data_vigencia_inicio DATE NOT NULL,
    data_vigencia_fim DATE,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_aliquotas_impostos_regime FOREIGN KEY (regime_tributario_id) REFERENCES regimes_tributarios(id)
);

CREATE INDEX idx_aliquotas_impostos_regime ON aliquotas_impostos(regime_tributario_id);
CREATE INDEX idx_aliquotas_impostos_tipo ON aliquotas_impostos(tipo_imposto);

COMMENT ON TABLE aliquotas_impostos IS 'Alíquotas de impostos por regime e faixa';
