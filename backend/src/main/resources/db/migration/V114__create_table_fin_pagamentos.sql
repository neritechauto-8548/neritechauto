CREATE TABLE fin_pagamentos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    numero_pagamento VARCHAR(30) NOT NULL,
    fatura_id BIGINT,
    cliente_id BIGINT NOT NULL,
    forma_pagamento_id BIGINT NOT NULL,
    valor_pago DECIMAL(10,2) NOT NULL,
    valor_troco DECIMAL(8,2) DEFAULT 0,
    data_pagamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_compensacao DATE,
    status VARCHAR(30),
    numero_autorizacao VARCHAR(100),
    numero_transacao VARCHAR(100),
    bandeira_cartao VARCHAR(50),
    ultimos_digitos_cartao VARCHAR(4),
    taxa_administracao DECIMAL(5,4),
    valor_liquido DECIMAL(10,2),
    conta_bancaria_id BIGINT,
    comprovante_url VARCHAR(500),
    observacoes TEXT,
    processado_por BIGINT NOT NULL,
    data_processamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT uk_fin_pagamentos_numero UNIQUE (empresa_id, numero_pagamento),
    CONSTRAINT fk_fin_pagamentos_fatura FOREIGN KEY (fatura_id) REFERENCES fin_faturas(id),
    CONSTRAINT fk_fin_pagamentos_forma FOREIGN KEY (forma_pagamento_id) REFERENCES fin_formas_pagamento(id),
    CONSTRAINT fk_fin_pagamentos_conta FOREIGN KEY (conta_bancaria_id) REFERENCES fin_contas_bancarias(id)
);

CREATE INDEX idx_fin_pagamentos_empresa ON fin_pagamentos(empresa_id);
CREATE INDEX idx_fin_pagamentos_fatura ON fin_pagamentos(fatura_id);
