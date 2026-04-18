-- Migração V123: Criar tabela de Tipos de Garantia
-- Armazena os tipos de garantia configuráveis da empresa

CREATE SEQUENCE seq_gar_tipos_garantia_id START WITH 1 INCREMENT BY 1;

CREATE TABLE gar_tipos_garantia (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_gar_tipos_garantia_id'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    prazo_dias INT NOT NULL,
    tipo_cobertura VARCHAR(30) NOT NULL,
    percentual_cobertura DECIMAL(5,2) DEFAULT 100.00,
    valor_maximo_cobertura DECIMAL(10,2),
    condicoes_aplicacao TEXT,
    restricoes TEXT,
    documentacao_necessaria TEXT,
    processo_acionamento TEXT,
    sla_atendimento_horas INT DEFAULT 24,
    transferivel BOOLEAN DEFAULT FALSE,
    renovavel BOOLEAN DEFAULT FALSE,
    custos_adicionais TEXT,
    exclusoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    padrao_servicos BOOLEAN DEFAULT FALSE,
    padrao_produtos BOOLEAN DEFAULT FALSE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    
    -- Constraints
    CONSTRAINT fk_tipos_garantia_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT chk_tipo_cobertura CHECK (tipo_cobertura IN ('TOTAL', 'PARCIAL', 'MAO_OBRA', 'PECAS', 'AMBOS')),
    CONSTRAINT chk_percentual_cobertura CHECK (percentual_cobertura >= 0 AND percentual_cobertura <= 100),
    CONSTRAINT chk_prazo_dias CHECK (prazo_dias > 0)
);

-- Índices para otimização de consultas
CREATE INDEX idx_tipos_garantia_empresa ON gar_tipos_garantia(empresa_id);
CREATE INDEX idx_tipos_garantia_ativo ON gar_tipos_garantia(ativo);
CREATE INDEX idx_tipos_garantia_padrao_servicos ON gar_tipos_garantia(padrao_servicos) WHERE padrao_servicos = TRUE;
CREATE INDEX idx_tipos_garantia_padrao_produtos ON gar_tipos_garantia(padrao_produtos) WHERE padrao_produtos = TRUE;

-- Comentários
COMMENT ON TABLE gar_tipos_garantia IS 'Tipos de garantia configuráveis por empresa';
COMMENT ON COLUMN gar_tipos_garantia.tipo_cobertura IS 'Tipo de cobertura: TOTAL, PARCIAL, MAO_OBRA, PECAS, AMBOS';
COMMENT ON COLUMN gar_tipos_garantia.percentual_cobertura IS 'Percentual de cobertura da garantia (0-100)';
COMMENT ON COLUMN gar_tipos_garantia.sla_atendimento_horas IS 'SLA de atendimento em horas';
