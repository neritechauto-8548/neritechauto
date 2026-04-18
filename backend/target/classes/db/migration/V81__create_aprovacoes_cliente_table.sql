-- V81: Create aprovacoes_cliente table
CREATE SEQUENCE IF NOT EXISTS seq_aprovacoes_cliente START WITH 1 INCREMENT BY 1;

CREATE TABLE aprovacoes_cliente (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_aprovacoes_cliente'),
    ordem_servico_id BIGINT NOT NULL,
    orcamento_id BIGINT,
    tipo_aprovacao VARCHAR(30),
    descricao TEXT NOT NULL,
    valor_aprovado DECIMAL(10,2),
    prazo_adicional_horas INT,
    metodo_aprovacao VARCHAR(20),
    aprovado_por VARCHAR(255) NOT NULL,
    documento_aprovador VARCHAR(20),
    telefone_aprovador VARCHAR(20),
    email_aprovador VARCHAR(255),
    data_solicitacao TIMESTAMP NOT NULL,
    data_aprovacao TIMESTAMP,
    data_expiracao TIMESTAMP,
    status VARCHAR(20),
    token_aprovacao VARCHAR(100),
    ip_aprovacao VARCHAR(45),
    assinatura_digital TEXT,
    motivo_rejeicao TEXT,
    observacoes TEXT,
    anexos_comprobatorios JSONB,
    usuario_solicitante_id BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_aprovacoes_cliente_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT fk_aprovacoes_cliente_orcamento FOREIGN KEY (orcamento_id) REFERENCES orcamentos(id),
    CONSTRAINT chk_aprovacoes_cliente_tipo CHECK (tipo_aprovacao IN ('ORCAMENTO', 'SERVICO_ADICIONAL', 'ALTERACAO_VALOR', 'ALTERACAO_PRAZO')),
    CONSTRAINT chk_aprovacoes_cliente_metodo CHECK (metodo_aprovacao IN ('PRESENCIAL', 'TELEFONE', 'WHATSAPP', 'EMAIL', 'APP', 'SITE')),
    CONSTRAINT chk_aprovacoes_cliente_status CHECK (status IN ('PENDENTE', 'APROVADO', 'REJEITADO', 'EXPIRADO'))
);

CREATE INDEX idx_aprovacoes_cliente_os ON aprovacoes_cliente(ordem_servico_id);
CREATE INDEX idx_aprovacoes_cliente_orcamento ON aprovacoes_cliente(orcamento_id);
CREATE INDEX idx_aprovacoes_cliente_status ON aprovacoes_cliente(status);
CREATE INDEX idx_aprovacoes_cliente_token ON aprovacoes_cliente(token_aprovacao);
COMMENT ON TABLE aprovacoes_cliente IS 'Aprovações de clientes para ordens de serviço';
