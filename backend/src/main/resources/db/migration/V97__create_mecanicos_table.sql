-- V97: Create mecanicos table
-- Description: Mechanics specialized employee data

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_mecanicos START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE mecanicos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_mecanicos'),
    funcionario_id BIGINT NOT NULL UNIQUE,
    codigo_mecanico VARCHAR(20) NOT NULL,
    nivel_experiencia VARCHAR(20),
    anos_experiencia INT DEFAULT 0,
    especialidades_principais JSONB,
    certificacoes_ativas JSONB,
    produtividade_media DECIMAL(5,2),
    qualidade_media DECIMAL(5,2),
    tempo_medio_servico INT,
    total_os_executadas INT DEFAULT 0,
    total_retrabalho INT DEFAULT 0,
    percentual_retrabalho DECIMAL(5,2) DEFAULT 0,
    avaliacao_media_cliente DECIMAL(3,2),
    capacidade_diagnostico VARCHAR(20),
    pode_liderar_equipe BOOLEAN DEFAULT FALSE,
    autorizado_test_drive BOOLEAN DEFAULT FALSE,
    autorizado_equipamentos_especiais BOOLEAN DEFAULT TRUE,
    meta_produtividade_mensal DECIMAL(5,2),
    comissao_os_percentual DECIMAL(5,2) DEFAULT 0,
    bonus_qualidade_percentual DECIMAL(5,2) DEFAULT 0,
    observacoes_tecnicas TEXT,
    ativo_execucao BOOLEAN DEFAULT TRUE,
    data_ultima_avaliacao DATE,
    data_proxima_avaliacao DATE,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_mecanicos_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT uk_mecanicos_codigo UNIQUE (codigo_mecanico),
    CONSTRAINT chk_mecanicos_nivel CHECK (nivel_experiencia IN ('JUNIOR', 'PLENO', 'SENIOR', 'ESPECIALISTA')),
    CONSTRAINT chk_mecanicos_capacidade CHECK (capacidade_diagnostico IN ('BASICA', 'INTERMEDIARIA', 'AVANCADA', 'ESPECIALISTA'))
);

-- Create indexes
CREATE INDEX idx_mecanicos_funcionario ON mecanicos(funcionario_id);
CREATE INDEX idx_mecanicos_codigo ON mecanicos(codigo_mecanico);
CREATE INDEX idx_mecanicos_nivel ON mecanicos(nivel_experiencia);
CREATE INDEX idx_mecanicos_ativo ON mecanicos(ativo_execucao);

-- Comments
COMMENT ON TABLE mecanicos IS 'Dados específicos de mecânicos';
COMMENT ON COLUMN mecanicos.nivel_experiencia IS 'Nível: JUNIOR, PLENO, SENIOR, ESPECIALISTA';
COMMENT ON COLUMN mecanicos.capacidade_diagnostico IS 'Capacidade: BASICA, INTERMEDIARIA, AVANCADA, ESPECIALISTA';
COMMENT ON COLUMN mecanicos.especialidades_principais IS 'JSON com IDs das especialidades principais';
COMMENT ON COLUMN mecanicos.certificacoes_ativas IS 'JSON com certificações ativas';
