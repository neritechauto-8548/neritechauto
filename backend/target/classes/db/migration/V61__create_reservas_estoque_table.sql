-- V61__create_reservas_estoque_table.sql
CREATE SEQUENCE reservas_estoque_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE reservas_estoque (
    id BIGINT PRIMARY KEY DEFAULT nextval('reservas_estoque_seq'),
    produto_id BIGINT NOT NULL,
    quantidade_reservada DECIMAL(10,2) NOT NULL,
    tipo_reserva VARCHAR(30) CHECK (tipo_reserva IN ('ORDEM_SERVICO', 'ORCAMENTO', 'PEDIDO_COMPRA', 'TRANSFERENCIA', 'OUTROS')),
    documento_id BIGINT NOT NULL,
    documento_tipo VARCHAR(50) NOT NULL,
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_validade_reserva TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ATIVA' CHECK (status IN ('ATIVA', 'UTILIZADA', 'CANCELADA', 'EXPIRADA')),
    usuario_responsavel BIGINT,
    observacoes TEXT,
    data_utilizacao TIMESTAMP,
    data_cancelamento TIMESTAMP,
    motivo_cancelamento TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_reserva_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_reservas_produto ON reservas_estoque(produto_id);
CREATE INDEX idx_reservas_status ON reservas_estoque(status);
CREATE INDEX idx_reservas_tipo ON reservas_estoque(tipo_reserva);
CREATE INDEX idx_reservas_documento ON reservas_estoque(documento_tipo, documento_id);
