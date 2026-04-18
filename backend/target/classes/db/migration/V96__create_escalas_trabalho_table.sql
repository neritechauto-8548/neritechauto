-- V96: Create escalas_trabalho table
-- Description: Work shift schedules

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_escalas_trabalho START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE escalas_trabalho (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_escalas_trabalho'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_escala VARCHAR(20),
    mes_referencia INT NOT NULL,
    ano_referencia INT NOT NULL,
    funcionarios_incluidos JSONB,
    turnos_configurados JSONB,
    observacoes TEXT,
    aprovada BOOLEAN DEFAULT FALSE,
    data_aprovacao TIMESTAMP,
    aprovada_por BIGINT,
    data_publicacao TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_escalas_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_escalas_periodo UNIQUE (empresa_id, nome, mes_referencia, ano_referencia),
    CONSTRAINT chk_escalas_tipo CHECK (tipo_escala IN ('SEMANAL', 'QUINZENAL', 'MENSAL', 'PERSONALIZADA')),
    CONSTRAINT chk_escalas_mes CHECK (mes_referencia >= 1 AND mes_referencia <= 12)
);

-- Create indexes
CREATE INDEX idx_escalas_empresa ON escalas_trabalho(empresa_id);
CREATE INDEX idx_escalas_tipo ON escalas_trabalho(tipo_escala);
CREATE INDEX idx_escalas_periodo ON escalas_trabalho(ano_referencia, mes_referencia);
CREATE INDEX idx_escalas_aprovada ON escalas_trabalho(aprovada);
CREATE INDEX idx_escalas_ativo ON escalas_trabalho(ativo);

-- Comments
COMMENT ON TABLE escalas_trabalho IS 'Escalas de trabalho e turnos';
COMMENT ON COLUMN escalas_trabalho.tipo_escala IS 'Tipo: SEMANAL, QUINZENAL, MENSAL, PERSONALIZADA';
COMMENT ON COLUMN escalas_trabalho.funcionarios_incluidos IS 'JSON com IDs dos funcionários na escala';
COMMENT ON COLUMN escalas_trabalho.turnos_configurados IS 'JSON com configuração dos turnos';
