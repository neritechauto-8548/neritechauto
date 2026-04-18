-- V103: Create ferias_funcionarios table
-- Description: Employee vacation tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_ferias_funcionarios START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE ferias_funcionarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_ferias_funcionarios'),
    funcionario_id BIGINT NOT NULL,
    periodo_aquisitivo_inicio DATE NOT NULL,
    periodo_aquisitivo_fim DATE NOT NULL,
    dias_direito INT DEFAULT 30,
    dias_vendidos INT DEFAULT 0,
    dias_usufruir INT NOT NULL,
    data_inicio_ferias DATE NOT NULL,
    data_fim_ferias DATE NOT NULL,
    data_retorno DATE NOT NULL,
    abono_pecuniario BOOLEAN DEFAULT FALSE,
    valor_abono DECIMAL(8,2),
    adicional_um_terco DECIMAL(8,2),
    adiantamento_13_salario BOOLEAN DEFAULT FALSE,
    valor_adiantamento_13 DECIMAL(8,2),
    status VARCHAR(20),
    motivo_cancelamento TEXT,
    substituido_por BIGINT,
    observacoes TEXT,
    solicitado_funcionario BOOLEAN DEFAULT FALSE,
    data_solicitacao DATE,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_ferias_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_ferias_substituto FOREIGN KEY (substituido_por) REFERENCES funcionarios(id),
    CONSTRAINT chk_ferias_status CHECK (status IN ('PROGRAMADAS', 'EM_ANDAMENTO', 'FINALIZADAS', 'CANCELADAS'))
);

-- Create indexes
CREATE INDEX idx_ferias_funcionario ON ferias_funcionarios(funcionario_id);
CREATE INDEX idx_ferias_periodo_aquisitivo ON ferias_funcionarios(periodo_aquisitivo_inicio, periodo_aquisitivo_fim);
CREATE INDEX idx_ferias_periodo_gozo ON ferias_funcionarios(data_inicio_ferias, data_fim_ferias);
CREATE INDEX idx_ferias_status ON ferias_funcionarios(status);

-- Comments
COMMENT ON TABLE ferias_funcionarios IS 'Férias dos funcionários';
COMMENT ON COLUMN ferias_funcionarios.status IS 'Status: PROGRAMADAS, EM_ANDAMENTO, FINALIZADAS, CANCELADAS';
COMMENT ON COLUMN ferias_funcionarios.abono_pecuniario IS 'Venda de até 10 dias de férias';
COMMENT ON COLUMN ferias_funcionarios.adicional_um_terco IS 'Adicional de 1/3 constitucional';
