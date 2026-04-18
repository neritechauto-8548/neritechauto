-- V99: Create treinamentos table
-- Description: Training programs and courses

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_treinamentos START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE treinamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_treinamentos'),
    empresa_id BIGINT NOT NULL,
    nome_treinamento VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    tipo_treinamento VARCHAR(20),
    instrutor_interno_id BIGINT,
    instrutor_externo VARCHAR(255),
    empresa_treinamento VARCHAR(255),
    carga_horaria INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    local_realizacao VARCHAR(255),
    capacidade_maxima INT,
    custo_total DECIMAL(10,2),
    custo_por_participante DECIMAL(8,2),
    objetivo_treinamento TEXT,
    conteudo_programatico TEXT,
    material_necessario TEXT,
    certificacao_emitida BOOLEAN DEFAULT FALSE,
    nome_certificacao VARCHAR(255),
    validade_certificacao_meses INT,
    obrigatorio BOOLEAN DEFAULT FALSE,
    cargos_obrigatorios JSONB,
    departamentos_obrigatorios JSONB,
    status VARCHAR(20),
    avaliacao_media DECIMAL(3,2),
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_treinamentos_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_treinamentos_instrutor FOREIGN KEY (instrutor_interno_id) REFERENCES funcionarios(id),
    CONSTRAINT chk_treinamentos_tipo CHECK (tipo_treinamento IN ('INTERNO', 'EXTERNO', 'ONLINE', 'PRESENCIAL', 'EAD')),
    CONSTRAINT chk_treinamentos_status CHECK (status IN ('PLANEJADO', 'EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO', 'ADIADO'))
);

-- Create indexes
CREATE INDEX idx_treinamentos_empresa ON treinamentos(empresa_id);
CREATE INDEX idx_treinamentos_tipo ON treinamentos(tipo_treinamento);
CREATE INDEX idx_treinamentos_status ON treinamentos(status);
CREATE INDEX idx_treinamentos_periodo ON treinamentos(data_inicio, data_fim);
CREATE INDEX idx_treinamentos_instrutor ON treinamentos(instrutor_interno_id);

-- Comments
COMMENT ON TABLE treinamentos IS 'Programas de treinamento e capacitação';
COMMENT ON COLUMN treinamentos.tipo_treinamento IS 'Tipo: INTERNO, EXTERNO, ONLINE, PRESENCIAL, EAD';
COMMENT ON COLUMN treinamentos.status IS 'Status: PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO, ADIADO';
