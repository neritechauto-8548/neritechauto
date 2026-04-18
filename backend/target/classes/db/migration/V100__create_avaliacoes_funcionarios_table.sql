-- V100: Create avaliacoes_funcionarios table
-- Description: Employee performance evaluations

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_avaliacoes_funcionarios START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE avaliacoes_funcionarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_avaliacoes_funcionarios'),
    funcionario_id BIGINT NOT NULL,
    periodo_avaliacao VARCHAR(20) NOT NULL,
    tipo_avaliacao VARCHAR(20),
    avaliador_id BIGINT NOT NULL,
    data_avaliacao DATE NOT NULL,
    nota_pontualidade INT,
    nota_assiduidade INT,
    nota_qualidade_trabalho INT,
    nota_produtividade INT,
    nota_iniciativa INT,
    nota_trabalho_equipe INT,
    nota_lideranca INT,
    nota_comunicacao INT,
    nota_organizacao INT,
    nota_relacionamento_cliente INT,
    nota_conhecimento_tecnico INT,
    nota_aprendizado INT,
    nota_geral DECIMAL(4,2) NOT NULL,
    pontos_fortes TEXT,
    pontos_melhoria TEXT,
    objetivos_periodo TEXT,
    metas_alcancadas TEXT,
    plano_desenvolvimento TEXT,
    feedback_funcionario TEXT,
    recomendacao VARCHAR(20),
    aprovada_rh BOOLEAN DEFAULT FALSE,
    data_aprovacao_rh TIMESTAMP,
    aprovada_por_rh BIGINT,
    observacoes_rh TEXT,
    proxima_avaliacao DATE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_avaliacoes_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_avaliacoes_avaliador FOREIGN KEY (avaliador_id) REFERENCES funcionarios(id),
    CONSTRAINT chk_avaliacoes_tipo CHECK (tipo_avaliacao IN ('PERIODICA', 'PROMOCAO', 'EXPERIENCIA', 'DESLIGAMENTO', 'ESPECIAL')),
    CONSTRAINT chk_avaliacoes_recomendacao CHECK (recomendacao IN ('PROMOCAO', 'MANUTENCAO', 'TREINAMENTO', 'ATENCAO', 'DESLIGAMENTO'))
);

-- Create indexes
CREATE INDEX idx_avaliacoes_funcionario ON avaliacoes_funcionarios(funcionario_id);
CREATE INDEX idx_avaliacoes_avaliador ON avaliacoes_funcionarios(avaliador_id);
CREATE INDEX idx_avaliacoes_tipo ON avaliacoes_funcionarios(tipo_avaliacao);
CREATE INDEX idx_avaliacoes_data ON avaliacoes_funcionarios(data_avaliacao);
CREATE INDEX idx_avaliacoes_periodo ON avaliacoes_funcionarios(periodo_avaliacao);

-- Comments
COMMENT ON TABLE avaliacoes_funcionarios IS 'Avaliações de desempenho dos funcionários';
COMMENT ON COLUMN avaliacoes_funcionarios.tipo_avaliacao IS 'Tipo: PERIODICA, PROMOCAO, EXPERIENCIA, DESLIGAMENTO, ESPECIAL';
COMMENT ON COLUMN avaliacoes_funcionarios.recomendacao IS 'Recomendação: PROMOCAO, MANUTENCAO, TREINAMENTO, ATENCAO, DESLIGAMENTO';
