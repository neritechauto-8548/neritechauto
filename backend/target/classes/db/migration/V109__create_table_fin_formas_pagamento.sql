CREATE TABLE fin_formas_pagamento (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30),
    aceita_parcelamento BOOLEAN DEFAULT FALSE,
    parcelas_maximas INT DEFAULT 1,
    taxa_administracao DECIMAL(5,4) DEFAULT 0,
    prazo_recebimento_dias INT DEFAULT 0,
    conta_bancaria_id BIGINT,
    ativo BOOLEAN DEFAULT TRUE,
    padrao BOOLEAN DEFAULT FALSE,
    exige_autorizacao BOOLEAN DEFAULT FALSE,
    limite_diario DECIMAL(10,2),
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT fk_fin_formas_pagamento_conta FOREIGN KEY (conta_bancaria_id) REFERENCES fin_contas_bancarias(id)
);

CREATE INDEX idx_fin_formas_pagamento_empresa ON fin_formas_pagamento(empresa_id);
