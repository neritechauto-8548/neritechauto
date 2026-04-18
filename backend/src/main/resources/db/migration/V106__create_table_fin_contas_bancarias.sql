CREATE TABLE fin_contas_bancarias (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    banco_codigo VARCHAR(5) NOT NULL,
    banco_nome VARCHAR(100) NOT NULL,
    agencia VARCHAR(10) NOT NULL,
    conta VARCHAR(20) NOT NULL,
    digito_conta VARCHAR(2),
    tipo_conta VARCHAR(20),
    titular_conta VARCHAR(255) NOT NULL,
    cpf_cnpj_titular VARCHAR(18) NOT NULL,
    saldo_atual DECIMAL(15,2) DEFAULT 0,
    limite_cheque_especial DECIMAL(12,2) DEFAULT 0,
    data_ultimo_saldo DATE,
    gerente_nome VARCHAR(255),
    gerente_telefone VARCHAR(20),
    gerente_email VARCHAR(255),
    principal BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT
);

CREATE INDEX idx_fin_contas_bancarias_empresa ON fin_contas_bancarias(empresa_id);
