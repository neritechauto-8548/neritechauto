-- V94: Create funcionarios_especialidades table
-- Description: Employee specialties mapping with certification tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_funcionarios_especialidades START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE funcionarios_especialidades (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_funcionarios_especialidades'),
    funcionario_id BIGINT NOT NULL,
    especialidade_id BIGINT NOT NULL,
    nivel_dominio VARCHAR(20),
    data_certificacao DATE,
    numero_certificado VARCHAR(100),
    entidade_certificadora VARCHAR(255),
    data_validade_certificacao DATE,
    anexo_certificado_url VARCHAR(500),
    experiencia_anos INT DEFAULT 0,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_func_espec_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_func_espec_especialidade FOREIGN KEY (especialidade_id) REFERENCES especialidades(id),
    CONSTRAINT uk_func_espec UNIQUE (funcionario_id, especialidade_id),
    CONSTRAINT chk_func_espec_nivel CHECK (nivel_dominio IN ('BASICO', 'INTERMEDIARIO', 'AVANCADO', 'ESPECIALISTA'))
);

-- Create indexes
CREATE INDEX idx_func_espec_funcionario ON funcionarios_especialidades(funcionario_id);
CREATE INDEX idx_func_espec_especialidade ON funcionarios_especialidades(especialidade_id);
CREATE INDEX idx_func_espec_nivel ON funcionarios_especialidades(nivel_dominio);
CREATE INDEX idx_func_espec_validade ON funcionarios_especialidades(data_validade_certificacao);

-- Comments
COMMENT ON TABLE funcionarios_especialidades IS 'Especialidades dos funcionários com certificações';
COMMENT ON COLUMN funcionarios_especialidades.nivel_dominio IS 'Nível: BASICO, INTERMEDIARIO, AVANCADO, ESPECIALISTA';
