-- V102: Create faltas_atrasos table
-- Description: Employee absences and delays tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_faltas_atrasos START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE faltas_atrasos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_faltas_atrasos'),
    funcionario_id BIGINT NOT NULL,
    data_ocorrencia DATE NOT NULL,
    tipo_ocorrencia VARCHAR(20),
    horario_previsto TIME,
    horario_real TIME,
    minutos_diferenca INT,
    justificado BOOLEAN DEFAULT FALSE,
    tipo_justificativa VARCHAR(30),
    descricao_justificativa TEXT,
    documento_comprobatorio_url VARCHAR(500),
    aprovada_justificativa BOOLEAN DEFAULT FALSE,
    data_aprovacao_justificativa TIMESTAMP,
    aprovada_por BIGINT,
    desconto_folha DECIMAL(8,2) DEFAULT 0,
    gera_advertencia BOOLEAN DEFAULT FALSE,
    observacoes TEXT,
    registrado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_faltas_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_faltas_tipo_ocorrencia CHECK (tipo_ocorrencia IN ('FALTA', 'ATRASO', 'SAIDA_ANTECIPADA', 'FALTA_PARCIAL')),
    CONSTRAINT chk_faltas_tipo_justificativa CHECK (tipo_justificativa IN ('ATESTADO_MEDICO', 'LICENCA', 'FALTA_TRANSPORTE', 'PROBLEMA_FAMILIAR', 'OUTROS'))
);

-- Create indexes
CREATE INDEX idx_faltas_funcionario ON faltas_atrasos(funcionario_id);
CREATE INDEX idx_faltas_data ON faltas_atrasos(data_ocorrencia);
CREATE INDEX idx_faltas_tipo ON faltas_atrasos(tipo_ocorrencia);
CREATE INDEX idx_faltas_justificado ON faltas_atrasos(justificado);

-- Comments
COMMENT ON TABLE faltas_atrasos IS 'Registro de faltas e atrasos dos funcionários';
COMMENT ON COLUMN faltas_atrasos.tipo_ocorrencia IS 'Tipo: FALTA, ATRASO, SAIDA_ANTECIPADA, FALTA_PARCIAL';
COMMENT ON COLUMN faltas_atrasos.tipo_justificativa IS 'Tipo: ATESTADO_MEDICO, LICENCA, FALTA_TRANSPORTE, PROBLEMA_FAMILIAR, OUTROS';
