CREATE TABLE fin_contas_receber (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    numero_titulo VARCHAR(30) NOT NULL,
    cliente_id BIGINT NOT NULL,
    fatura_id BIGINT,
    tipo_titulo VARCHAR(30),
    data_emissao DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    valor_nominal DECIMAL(10,2) NOT NULL,
    valor_juros DECIMAL(8,2) DEFAULT 0,
    valor_multa DECIMAL(8,2) DEFAULT 0,
    valor_desconto DECIMAL(8,2) DEFAULT 0,
    valor_pago DECIMAL(10,2) DEFAULT 0,
    valor_pendente DECIMAL(10,2),
    percentual_juros_dia DECIMAL(8,6) DEFAULT 0,
    percentual_multa DECIMAL(5,2) DEFAULT 0,
    dias_carencia_juros INT DEFAULT 0,
    status VARCHAR(30),
    historico_negociacao JSONB,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT uk_fin_contas_receber_numero UNIQUE (empresa_id, numero_titulo),
    CONSTRAINT fk_fin_contas_receber_fatura FOREIGN KEY (fatura_id) REFERENCES fin_faturas(id)
);

CREATE INDEX idx_fin_contas_receber_empresa ON fin_contas_receber(empresa_id);
CREATE INDEX idx_fin_contas_receber_cliente ON fin_contas_receber(cliente_id);
