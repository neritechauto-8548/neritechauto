-- V93: Create especialidades table
-- Description: Technical specialties/skills catalog

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_especialidades START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE especialidades (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_especialidades'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(50),
    nivel_complexidade VARCHAR(20),
    certificacao_obrigatoria BOOLEAN DEFAULT FALSE,
    nome_certificacao VARCHAR(255),
    entidade_certificadora VARCHAR(255),
    validade_certificacao_meses INT,
    experiencia_minima_anos INT DEFAULT 0,
    adicional_salarial_percentual DECIMAL(5,2) DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_especialidades_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_especialidades_nome UNIQUE (empresa_id, nome),
    CONSTRAINT chk_especialidades_nivel CHECK (nivel_complexidade IN ('BASICO', 'INTERMEDIARIO', 'AVANCADO', 'ESPECIALISTA'))
);

-- Create indexes
CREATE INDEX idx_especialidades_empresa ON especialidades(empresa_id);
CREATE INDEX idx_especialidades_categoria ON especialidades(categoria);
CREATE INDEX idx_especialidades_nivel ON especialidades(nivel_complexidade);
CREATE INDEX idx_especialidades_ativo ON especialidades(ativo);

-- Comments
COMMENT ON TABLE especialidades IS 'Especialidades técnicas e habilidades';
COMMENT ON COLUMN especialidades.nivel_complexidade IS 'Nível: BASICO, INTERMEDIARIO, AVANCADO, ESPECIALISTA';
COMMENT ON COLUMN especialidades.certificacao_obrigatoria IS 'Se requer certificação obrigatória';
