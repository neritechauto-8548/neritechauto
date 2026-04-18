-- Migração V127: Criar tabela de Resoluções de Garantia
-- Armazena as resoluções das reclamações de garantia

CREATE SEQUENCE seq_gar_resolucoes_garantia_id START WITH 1 INCREMENT BY 1;

CREATE TABLE gar_resolucoes_garantia (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_gar_resolucoes_garantia_id'),
    reclamacao_id BIGINT NOT NULL,
    tipo_resolucao VARCHAR(30) NOT NULL,
    descricao_resolucao TEXT NOT NULL,
    servicos_executados TEXT, -- JSON armazenado como TEXT
    produtos_fornecidos TEXT, -- JSON armazenado como TEXT
    valor_servicos DECIMAL(10,2) DEFAULT 0,
    valor_produtos DECIMAL(8,2) DEFAULT 0,
    valor_total_resolucao DECIMAL(10,2) DEFAULT 0,
    valor_cobrado_cliente DECIMAL(8,2) DEFAULT 0,
    desconto_concedido DECIMAL(8,2) DEFAULT 0,
    justificativa_cobranca TEXT,
    nova_garantia_gerada BOOLEAN DEFAULT FALSE,
    nova_garantia_id BIGINT,
    prazo_nova_garantia_dias INT,
    data_inicio_execucao TIMESTAMP,
    data_conclusao TIMESTAMP,
    tempo_resolucao_horas INT,
    funcionario_executor_id BIGINT,
    equipe_execucao TEXT, -- JSON armazenado como TEXT
    qualidade_resolucao VARCHAR(30),
    cliente_satisfeito BOOLEAN,
    observacoes_execucao TEXT,
    fotos_apos_resolucao TEXT, -- JSON armazenado como TEXT
    documentos_comprobatorios TEXT, -- JSON armazenado como TEXT
    aprovada_gerencia BOOLEAN DEFAULT FALSE,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_resolucoes_reclamacao FOREIGN KEY (reclamacao_id) REFERENCES gar_reclamacoes_garantia(id),
    CONSTRAINT fk_resolucoes_nova_garantia FOREIGN KEY (nova_garantia_id) REFERENCES gar_garantias(id),
    CONSTRAINT fk_resolucoes_funcionario FOREIGN KEY (funcionario_executor_id) REFERENCES funcionarios(id),
    CONSTRAINT chk_tipo_resolucao CHECK (tipo_resolucao IN ('REPARO', 'TROCA', 'REEMBOLSO', 'DESCONTO', 'RETRABALHO', 'SEM_ACAO')),
    CONSTRAINT chk_qualidade_resolucao CHECK (qualidade_resolucao IS NULL OR qualidade_resolucao IN ('EXCELENTE', 'BOA', 'REGULAR', 'RUIM')),
    CONSTRAINT chk_valor_total CHECK (valor_total_resolucao = valor_servicos + valor_produtos)
);

-- Índices para otimização de consultas
CREATE INDEX idx_resolucoes_reclamacao ON gar_resolucoes_garantia(reclamacao_id);
CREATE INDEX idx_resolucoes_tipo ON gar_resolucoes_garantia(tipo_resolucao);
CREATE INDEX idx_resolucoes_funcionario ON gar_resolucoes_garantia(funcionario_executor_id);
CREATE INDEX idx_resolucoes_nova_garantia ON gar_resolucoes_garantia(nova_garantia_id);
CREATE INDEX idx_resolucoes_qualidade ON gar_resolucoes_garantia(qualidade_resolucao);

-- Comentários
COMMENT ON TABLE gar_resolucoes_garantia IS 'Resoluções das reclamações de garantia';
COMMENT ON COLUMN gar_resolucoes_garantia.tipo_resolucao IS 'Tipo: REPARO, TROCA, REEMBOLSO, DESCONTO, RETRABALHO, SEM_ACAO';
COMMENT ON COLUMN gar_resolucoes_garantia.qualidade_resolucao IS 'Qualidade: EXCELENTE, BOA, REGULAR, RUIM';
