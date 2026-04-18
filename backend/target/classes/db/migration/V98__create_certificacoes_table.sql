-- V98: Create certificacoes table
-- Description: Employee certifications tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_certificacoes START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE certificacoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_certificacoes'),
    funcionario_id BIGINT NOT NULL,
    nome_certificacao VARCHAR(255) NOT NULL,
    entidade_certificadora VARCHAR(255) NOT NULL,
    categoria VARCHAR(100),
    numero_certificado VARCHAR(100),
    data_emissao DATE NOT NULL,
    data_validade DATE,
    tem_validade BOOLEAN DEFAULT TRUE,
    carga_horaria INT,
    nota_obtida DECIMAL(4,2),
    nota_minima_aprovacao DECIMAL(4,2),
    custo_certificacao DECIMAL(8,2),
    pago_pela_empresa BOOLEAN DEFAULT FALSE,
    arquivo_certificado_url VARCHAR(500),
    status VARCHAR(20),
    reconhecida_empresa BOOLEAN DEFAULT TRUE,
    adicional_salarial DECIMAL(8,2) DEFAULT 0,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_certificacoes_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_certificacoes_status CHECK (status IN ('VALIDA', 'EXPIRADA', 'SUSPENSA', 'CANCELADA'))
);

-- Create indexes
CREATE INDEX idx_certificacoes_funcionario ON certificacoes(funcionario_id);
CREATE INDEX idx_certificacoes_status ON certificacoes(status);
CREATE INDEX idx_certificacoes_validade ON certificacoes(data_validade);
CREATE INDEX idx_certificacoes_categoria ON certificacoes(categoria);

-- Comments
COMMENT ON TABLE certificacoes IS 'Certificações dos funcionários';
COMMENT ON COLUMN certificacoes.status IS 'Status: VALIDA, EXPIRADA, SUSPENSA, CANCELADA';
