-- V90: Create cargos table
-- Description: Job positions/roles table

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_cargos START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE cargos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_cargos'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    codigo_cbo VARCHAR(10),
    nivel_hierarquico INT DEFAULT 1,
    cargo_superior_id BIGINT,
    salario_base_minimo DECIMAL(10,2),
    salario_base_maximo DECIMAL(10,2),
    requisitos TEXT,
    responsabilidades TEXT,
    beneficios TEXT,
    tem_comissao BOOLEAN DEFAULT FALSE,
    percentual_comissao_padrao DECIMAL(5,2),
    meta_vendas_mensal DECIMAL(10,2),
    carga_horaria_semanal INT DEFAULT 44,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_cargos_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_cargos_superior FOREIGN KEY (cargo_superior_id) REFERENCES cargos(id),
    CONSTRAINT uk_cargos_nome UNIQUE (empresa_id, nome)
);

-- Create indexes
CREATE INDEX idx_cargos_empresa ON cargos(empresa_id);
CREATE INDEX idx_cargos_superior ON cargos(cargo_superior_id);
CREATE INDEX idx_cargos_ativo ON cargos(ativo);
CREATE INDEX idx_cargos_nivel ON cargos(nivel_hierarquico);

-- Comments
COMMENT ON TABLE cargos IS 'Cargos/funções da empresa';
COMMENT ON COLUMN cargos.codigo_cbo IS 'Código CBO (Classificação Brasileira de Ocupações)';
COMMENT ON COLUMN cargos.nivel_hierarquico IS 'Nível hierárquico do cargo (1=mais alto)';
COMMENT ON COLUMN cargos.tem_comissao IS 'Se o cargo possui comissionamento';
