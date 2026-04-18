-- V72: Create itens_os_servicos table
-- Description: Service items for service orders

CREATE SEQUENCE IF NOT EXISTS seq_itens_os_servicos START WITH 1 INCREMENT BY 1;

CREATE TABLE itens_os_servicos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_itens_os_servicos'),
    ordem_servico_id BIGINT NOT NULL,
    -- TODO: Uncomment when servicos table is created
    -- servico_id BIGINT,
    servico_id BIGINT,
    descricao TEXT,
    quantidade DECIMAL(8,2) DEFAULT 1,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    desconto_percentual DECIMAL(5,2) DEFAULT 0,
    desconto_valor DECIMAL(10,2) DEFAULT 0,
    valor_final DECIMAL(10,2) NOT NULL,
    mecanico_executor_id BIGINT,
    tempo_execucao_previsto INT,
    tempo_execucao_real INT,
    data_inicio_execucao TIMESTAMP,
    data_fim_execucao TIMESTAMP,
    status_execucao VARCHAR(20),
    garantia_dias INT DEFAULT 90,
    observacoes TEXT,
    aprovado_cliente BOOLEAN DEFAULT FALSE,
    data_aprovacao_cliente TIMESTAMP,
    comissao_percentual DECIMAL(5,2),
    comissao_valor DECIMAL(10,2),
    ordem_execucao INT DEFAULT 0,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_itens_os_servicos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    -- TODO: Uncomment when servicos table is created
    -- CONSTRAINT fk_itens_os_servicos_servico FOREIGN KEY (servico_id) REFERENCES servicos(id),
    CONSTRAINT chk_itens_os_servicos_status CHECK (status_execucao IN ('PENDENTE', 'EM_EXECUCAO', 'CONCLUIDO', 'CANCELADO'))
);

CREATE INDEX idx_itens_os_servicos_os ON itens_os_servicos(ordem_servico_id);
CREATE INDEX idx_itens_os_servicos_servico ON itens_os_servicos(servico_id);
CREATE INDEX idx_itens_os_servicos_mecanico ON itens_os_servicos(mecanico_executor_id);
CREATE INDEX idx_itens_os_servicos_status ON itens_os_servicos(status_execucao);

COMMENT ON TABLE itens_os_servicos IS 'Itens de serviços das ordens de serviço';
