-- V101: Create comissoes table
-- Description: Employee commissions tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_comissoes START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE comissoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_comissoes'),
    empresa_id BIGINT NOT NULL,
    funcionario_id BIGINT NOT NULL,
    tipo_comissao VARCHAR(20),
    periodo_referencia VARCHAR(7) NOT NULL,
    base_calculo VARCHAR(20),
    valor_base DECIMAL(12,2) NOT NULL,
    percentual_comissao DECIMAL(5,2) NOT NULL,
    valor_comissao DECIMAL(10,2) NOT NULL,
    meta_estabelecida DECIMAL(12,2),
    meta_atingida DECIMAL(12,2),
    percentual_meta_atingido DECIMAL(5,2),
    bonus_meta DECIMAL(8,2) DEFAULT 0,
    desconto_retrabalho DECIMAL(8,2) DEFAULT 0,
    valor_liquido DECIMAL(10,2),
    data_competencia DATE NOT NULL,
    data_pagamento DATE,
    status_pagamento VARCHAR(20),
    observacoes TEXT,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    paga_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_comissoes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_comissoes_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_comissoes_tipo CHECK (tipo_comissao IN ('VENDAS', 'SERVICOS', 'METAS', 'PRODUTIVIDADE', 'QUALIDADE')),
    CONSTRAINT chk_comissoes_base CHECK (base_calculo IN ('VALOR_VENDIDO', 'MARGEM_LUCRO', 'QUANTIDADE', 'METAS', 'FIXO')),
    CONSTRAINT chk_comissoes_status CHECK (status_pagamento IN ('CALCULADA', 'APROVADA', 'PAGA', 'CANCELADA'))
);

-- Create indexes
CREATE INDEX idx_comissoes_empresa ON comissoes(empresa_id);
CREATE INDEX idx_comissoes_funcionario ON comissoes(funcionario_id);
CREATE INDEX idx_comissoes_tipo ON comissoes(tipo_comissao);
CREATE INDEX idx_comissoes_periodo ON comissoes(periodo_referencia);
CREATE INDEX idx_comissoes_status ON comissoes(status_pagamento);
CREATE INDEX idx_comissoes_competencia ON comissoes(data_competencia);

-- Comments
COMMENT ON TABLE comissoes IS 'Comissões dos funcionários';
COMMENT ON COLUMN comissoes.tipo_comissao IS 'Tipo: VENDAS, SERVICOS, METAS, PRODUTIVIDADE, QUALIDADE';
COMMENT ON COLUMN comissoes.base_calculo IS 'Base: VALOR_VENDIDO, MARGEM_LUCRO, QUANTIDADE, METAS, FIXO';
COMMENT ON COLUMN comissoes.status_pagamento IS 'Status: CALCULADA, APROVADA, PAGA, CANCELADA';
COMMENT ON COLUMN comissoes.periodo_referencia IS 'Formato: YYYY-MM';
