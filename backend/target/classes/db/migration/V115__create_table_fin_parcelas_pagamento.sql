CREATE TABLE fin_parcelas_pagamento (
    id BIGSERIAL PRIMARY KEY,
    pagamento_id BIGINT NOT NULL,
    numero_parcela INT NOT NULL,
    valor_parcela DECIMAL(10,2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor_pago DECIMAL(10,2),
    juros_aplicados DECIMAL(8,2) DEFAULT 0,
    multa_aplicada DECIMAL(8,2) DEFAULT 0,
    desconto_aplicado DECIMAL(8,2) DEFAULT 0,
    status VARCHAR(30),
    forma_pagamento_id BIGINT,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT fk_fin_parcelas_pagamento FOREIGN KEY (pagamento_id) REFERENCES fin_pagamentos(id),
    CONSTRAINT fk_fin_parcelas_forma FOREIGN KEY (forma_pagamento_id) REFERENCES fin_formas_pagamento(id)
);

CREATE INDEX idx_fin_parcelas_pagamento ON fin_parcelas_pagamento(pagamento_id);
