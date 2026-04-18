CREATE TABLE fin_condicoes_pagamento (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(30),
    numero_parcelas INT DEFAULT 1,
    intervalo_dias INT DEFAULT 30,
    valor_entrada_percentual DECIMAL(5,2) DEFAULT 0,
    desconto_a_vista_percentual DECIMAL(5,2) DEFAULT 0,
    juros_parcelamento_percentual DECIMAL(5,2) DEFAULT 0,
    vencimento_primeira_parcela_dias INT DEFAULT 30,
    formas_pagamento_aceitas JSONB,
    padrao BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT
);

CREATE INDEX idx_fin_condicoes_pagamento_empresa ON fin_condicoes_pagamento(empresa_id);
