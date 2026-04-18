-- Migração V124: Criar tabela de Garantias
-- Armazena as garantias emitidas para clientes

CREATE SEQUENCE seq_gar_garantias_id START WITH 1 INCREMENT BY 1;

CREATE TABLE gar_garantias (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_gar_garantias_id'),
    empresa_id BIGINT NOT NULL,
    numero_garantia VARCHAR(30) UNIQUE NOT NULL,
    tipo_garantia_id BIGINT NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    veiculo_id BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    dias_garantia INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    valor_original_servico DECIMAL(10,2) NOT NULL,
    valor_cobertura_garantia DECIMAL(10,2) NOT NULL,
    kilometragem_inicio INT,
    kilometragem_limite INT,
    condicoes_especiais TEXT,
    observacoes TEXT,
    certificado_url VARCHAR(500),
    qr_code_verificacao TEXT,
    transferida_para_cliente_id BIGINT,
    data_transferencia TIMESTAMP,
    motivo_transferencia TEXT,
    renovada_de_garantia_id BIGINT,
    data_renovacao TIMESTAMP,
    cancelada_por BIGINT,
    data_cancelamento TIMESTAMP,
    motivo_cancelamento TEXT,
    total_acionamentos INT DEFAULT 0,
    valor_total_acionamentos DECIMAL(10,2) DEFAULT 0,
    data_ultimo_acionamento TIMESTAMP,
    alerta_vencimento_enviado BOOLEAN DEFAULT FALSE,
    data_alerta_vencimento TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    emitida_por BIGINT,
    
    -- Constraints
    CONSTRAINT fk_garantias_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_garantias_tipo FOREIGN KEY (tipo_garantia_id) REFERENCES gar_tipos_garantia(id),
    CONSTRAINT fk_garantias_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id),
    CONSTRAINT fk_garantias_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_garantias_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id),
    CONSTRAINT fk_garantias_transferida_cliente FOREIGN KEY (transferida_para_cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_garantias_renovada FOREIGN KEY (renovada_de_garantia_id) REFERENCES gar_garantias(id),
    CONSTRAINT chk_status_garantia CHECK (status IN ('ATIVA', 'EXPIRADA', 'CANCELADA', 'SUSPENSA', 'UTILIZADA')),
    CONSTRAINT chk_data_fim CHECK (data_fim >= data_inicio),
    CONSTRAINT chk_dias_garantia CHECK (dias_garantia > 0),
    CONSTRAINT chk_valor_cobertura CHECK (valor_cobertura_garantia <= valor_original_servico)
);

-- Índices para otimização de consultas
CREATE INDEX idx_garantias_empresa ON gar_garantias(empresa_id);
CREATE INDEX idx_garantias_numero ON gar_garantias(numero_garantia);
CREATE INDEX idx_garantias_tipo ON gar_garantias(tipo_garantia_id);
CREATE INDEX idx_garantias_ordem_servico ON gar_garantias(ordem_servico_id);
CREATE INDEX idx_garantias_cliente ON gar_garantias(cliente_id);
CREATE INDEX idx_garantias_veiculo ON gar_garantias(veiculo_id);
CREATE INDEX idx_garantias_status ON gar_garantias(status);
CREATE INDEX idx_garantias_data_fim ON gar_garantias(data_fim);
CREATE INDEX idx_garantias_ativas ON gar_garantias(empresa_id, status) WHERE status = 'ATIVA';

-- Comentários
COMMENT ON TABLE gar_garantias IS 'Garantias emitidas para clientes';
COMMENT ON COLUMN gar_garantias.numero_garantia IS 'Número único da garantia';
COMMENT ON COLUMN gar_garantias.status IS 'Status: ATIVA, EXPIRADA, CANCELADA, SUSPENSA, UTILIZADA';
COMMENT ON COLUMN gar_garantias.qr_code_verificacao IS 'QR Code para verificação da garantia';
