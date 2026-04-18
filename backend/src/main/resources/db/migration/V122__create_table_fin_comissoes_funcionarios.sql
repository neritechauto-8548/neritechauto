CREATE TABLE fin_comissoes_funcionarios (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id BIGINT,
    ordem_servico_id BIGINT,
    fatura_id BIGINT,
    tipo_comissao VARCHAR(30),
    base_calculo DECIMAL(10,2) NOT NULL,
    percentual_comissao DECIMAL(5,2) NOT NULL,
    valor_comissao DECIMAL(8,2) NOT NULL,
    data_competencia DATE NOT NULL,
    periodo_referencia VARCHAR(7),
    paga BOOLEAN DEFAULT FALSE,
    data_pagamento DATE,
    valor_pago DECIMAL(8,2),
    desconto_aplicado DECIMAL(8,2) DEFAULT 0,
    motivo_desconto TEXT,
    observacoes TEXT,
    calculada_por BIGINT,
    data_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT fk_fin_comissoes_fatura FOREIGN KEY (fatura_id) REFERENCES fin_faturas(id),
    CONSTRAINT fk_fin_comissoes_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);

CREATE INDEX idx_fin_comissoes_funcionario ON fin_comissoes_funcionarios(funcionario_id);
