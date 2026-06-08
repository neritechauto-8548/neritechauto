-- Tabela para armazenar as regras de comissao de cada funcionario
CREATE SEQUENCE IF NOT EXISTS seq_rh_regras_comissao START WITH 1 INCREMENT BY 1;

CREATE TABLE rh_regras_comissao (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_rh_regras_comissao'),
    empresa_id BIGINT NOT NULL,
    funcionario_id BIGINT NOT NULL,
    setor_id BIGINT,
    percentual DECIMAL(5,2) NOT NULL,
    sobre_servico VARCHAR(20) DEFAULT 'NAO',
    sobre_produtos VARCHAR(20) DEFAULT 'NAO',
    faturamento_geral VARCHAR(50) DEFAULT 'NAO',
    ativo BOOLEAN DEFAULT TRUE,
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_final TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_regra_com_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_regra_com_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_regra_com_setor FOREIGN KEY (setor_id) REFERENCES setores_empresa(id) ON DELETE SET NULL
);

CREATE INDEX idx_rh_regras_com_empresa ON rh_regras_comissao(empresa_id);
CREATE INDEX idx_rh_regras_com_funcionario ON rh_regras_comissao(funcionario_id);
