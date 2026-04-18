-- V60__create_alertas_estoque_table.sql
CREATE SEQUENCE alertas_estoque_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE alertas_estoque (
    id BIGINT PRIMARY KEY DEFAULT nextval('alertas_estoque_seq'),
    empresa_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    tipo_alerta VARCHAR(30) NOT NULL CHECK (tipo_alerta IN ('ESTOQUE_BAIXO', 'ESTOQUE_ALTO', 'SEM_ESTOQUE', 'VALIDADE_PROXIMA', 'PRODUTO_PARADO', 'RUPTURA')),
    descricao TEXT NOT NULL,
    nivel_prioridade VARCHAR(20) CHECK (nivel_prioridade IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')),
    quantidade_atual DECIMAL(10,2),
    quantidade_referencia DECIMAL(10,2),
    data_vencimento DATE,
    dias_parado INTEGER,
    valor_envolvido DECIMAL(15,2),
    status VARCHAR(20) DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'NOTIFICADO', 'RESOLVIDO', 'IGNORADO')),
    usuario_responsavel BIGINT,
    data_resolucao TIMESTAMP,
    observacoes_resolucao TEXT,
    notificacao_enviada BOOLEAN DEFAULT FALSE,
    data_notificacao TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_alerta_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_alerta_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_alertas_empresa ON alertas_estoque(empresa_id);
CREATE INDEX idx_alertas_produto ON alertas_estoque(produto_id);
CREATE INDEX idx_alertas_tipo ON alertas_estoque(tipo_alerta);
CREATE INDEX idx_alertas_status ON alertas_estoque(status);
CREATE INDEX idx_alertas_prioridade ON alertas_estoque(nivel_prioridade);
