-- V80: Create orcamentos table
CREATE SEQUENCE IF NOT EXISTS seq_orcamentos START WITH 1 INCREMENT BY 1;

CREATE TABLE orcamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_orcamentos'),
    ordem_servico_id BIGINT NOT NULL,
    numero_orcamento VARCHAR(20),
    versao INT DEFAULT 1,
    tipo_orcamento VARCHAR(20),
    valor_servicos DECIMAL(10,2) DEFAULT 0,
    valor_produtos DECIMAL(10,2) DEFAULT 0,
    valor_mao_obra DECIMAL(10,2) DEFAULT 0,
    valor_desconto DECIMAL(10,2) DEFAULT 0,
    valor_acrescimo DECIMAL(10,2) DEFAULT 0,
    valor_total DECIMAL(10,2) NOT NULL,
    prazo_validade_dias INT DEFAULT 30,
    data_vencimento DATE,
    prazo_execucao_dias INT,
    data_prevista_conclusao DATE,
    condicoes_pagamento TEXT,
    observacoes_comerciais TEXT,
    termos_condicoes TEXT,
    status VARCHAR(20),
    data_envio_cliente TIMESTAMP,
    data_resposta_cliente TIMESTAMP,
    metodo_envio VARCHAR(20),
    aprovado_por_cliente VARCHAR(255),
    documento_cliente VARCHAR(20),
    assinatura_digital_cliente TEXT,
    ip_aprovacao VARCHAR(45),
    data_aprovacao TIMESTAMP,
    motivo_rejeicao TEXT,
    responsavel_orcamento_id BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao_registro INT DEFAULT 0,
    
    CONSTRAINT fk_orcamentos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT uk_orcamentos_numero UNIQUE (numero_orcamento),
    CONSTRAINT chk_orcamentos_tipo CHECK (tipo_orcamento IN ('SIMPLES', 'DETALHADO', 'COMPARATIVO')),
    CONSTRAINT chk_orcamentos_status CHECK (status IN ('RASCUNHO', 'ENVIADO', 'APROVADO', 'REJEITADO', 'EXPIRADO', 'CANCELADO')),
    CONSTRAINT chk_orcamentos_metodo_envio CHECK (metodo_envio IN ('EMAIL', 'WHATSAPP', 'PRESENCIAL', 'POSTAL'))
);

CREATE INDEX idx_orcamentos_os ON orcamentos(ordem_servico_id);
CREATE INDEX idx_orcamentos_numero ON orcamentos(numero_orcamento);
CREATE INDEX idx_orcamentos_status ON orcamentos(status);
CREATE INDEX idx_orcamentos_responsavel ON orcamentos(responsavel_orcamento_id);
COMMENT ON TABLE orcamentos IS 'Orçamentos das ordens de serviço';
