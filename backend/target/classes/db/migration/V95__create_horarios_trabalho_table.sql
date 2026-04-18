-- V95: Create horarios_trabalho table
-- Description: Employee work schedules

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_horarios_trabalho START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE horarios_trabalho (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_horarios_trabalho'),
    funcionario_id BIGINT NOT NULL,
    tipo_horario VARCHAR(20),
    segunda_entrada TIME,
    segunda_saida TIME,
    segunda_intervalo_inicio TIME,
    segunda_intervalo_fim TIME,
    terca_entrada TIME,
    terca_saida TIME,
    terca_intervalo_inicio TIME,
    terca_intervalo_fim TIME,
    quarta_entrada TIME,
    quarta_saida TIME,
    quarta_intervalo_inicio TIME,
    quarta_intervalo_fim TIME,
    quinta_entrada TIME,
    quinta_saida TIME,
    quinta_intervalo_inicio TIME,
    quinta_intervalo_fim TIME,
    sexta_entrada TIME,
    sexta_saida TIME,
    sexta_intervalo_inicio TIME,
    sexta_intervalo_fim TIME,
    sabado_entrada TIME,
    sabado_saida TIME,
    sabado_intervalo_inicio TIME,
    sabado_intervalo_fim TIME,
    domingo_entrada TIME,
    domingo_saida TIME,
    domingo_intervalo_inicio TIME,
    domingo_intervalo_fim TIME,
    carga_horaria_semanal INT DEFAULT 44,
    tolerancia_entrada_minutos INT DEFAULT 10,
    tolerancia_saida_minutos INT DEFAULT 10,
    data_inicio_vigencia DATE NOT NULL,
    data_fim_vigencia DATE,
    ativo BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_horarios_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_horarios_tipo CHECK (tipo_horario IN ('FIXO', 'FLEXIVEL', 'ESCALA', 'PLANTAO'))
);

-- Create indexes
CREATE INDEX idx_horarios_funcionario ON horarios_trabalho(funcionario_id);
CREATE INDEX idx_horarios_tipo ON horarios_trabalho(tipo_horario);
CREATE INDEX idx_horarios_vigencia ON horarios_trabalho(data_inicio_vigencia, data_fim_vigencia);
CREATE INDEX idx_horarios_ativo ON horarios_trabalho(ativo);

-- Comments
COMMENT ON TABLE horarios_trabalho IS 'Horários de trabalho dos funcionários';
COMMENT ON COLUMN horarios_trabalho.tipo_horario IS 'Tipo: FIXO, FLEXIVEL, ESCALA, PLANTAO';
COMMENT ON COLUMN horarios_trabalho.carga_horaria_semanal IS 'Carga horária semanal em horas';
