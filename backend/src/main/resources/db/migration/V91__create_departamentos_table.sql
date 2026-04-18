-- V91: Create departamentos table
-- Description: Company departments/divisions table

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_departamentos START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE departamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_departamentos'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    codigo VARCHAR(20),
    departamento_pai_id BIGINT,
    -- TODO: Uncomment when funcionarios table is created
    -- gerente_id BIGINT,
    gerente_id BIGINT,
    centro_custo VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_departamentos_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_departamentos_pai FOREIGN KEY (departamento_pai_id) REFERENCES departamentos(id),
    -- TODO: Uncomment when funcionarios table is created
    -- CONSTRAINT fk_departamentos_gerente FOREIGN KEY (gerente_id) REFERENCES funcionarios(id),
    CONSTRAINT uk_departamentos_codigo UNIQUE (empresa_id, codigo),
    CONSTRAINT uk_departamentos_nome UNIQUE (empresa_id, nome)
);

-- Create indexes
CREATE INDEX idx_departamentos_empresa ON departamentos(empresa_id);
CREATE INDEX idx_departamentos_pai ON departamentos(departamento_pai_id);
CREATE INDEX idx_departamentos_gerente ON departamentos(gerente_id);
CREATE INDEX idx_departamentos_ativo ON departamentos(ativo);
CREATE INDEX idx_departamentos_codigo ON departamentos(codigo);

-- Comments
COMMENT ON TABLE departamentos IS 'Departamentos/setores da empresa';
COMMENT ON COLUMN departamentos.departamento_pai_id IS 'Departamento pai para hierarquia';
COMMENT ON COLUMN departamentos.centro_custo IS 'Centro de custo para contabilidade';
