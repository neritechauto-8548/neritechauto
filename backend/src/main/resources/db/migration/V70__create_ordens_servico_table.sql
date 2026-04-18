-- V70: Create ordens_servico table
-- Description: Main service orders table with comprehensive tracking

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_ordens_servico START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE ordens_servico (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_ordens_servico'),
    empresa_id BIGINT NOT NULL,
    numero_os VARCHAR(20) NOT NULL,
    -- TODO: Uncomment when clientes table is created
    -- cliente_id BIGINT NOT NULL,
    cliente_id BIGINT,
    -- TODO: Uncomment when veiculos table is created
    -- veiculo_id BIGINT NOT NULL,
    veiculo_id BIGINT,
    status_id BIGINT,
    tipo_os VARCHAR(20) NOT NULL,
    prioridade VARCHAR(20),
    data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_promessa TIMESTAMP,
    data_inicio_execucao TIMESTAMP,
    data_fim_execucao TIMESTAMP,
    data_entrega TIMESTAMP,
    quilometragem_entrada INT,
    quilometragem_saida INT,
    nivel_combustivel_entrada VARCHAR(10),
    nivel_combustivel_saida VARCHAR(10),
    consultor_responsavel_id BIGINT,
    mecanico_responsavel_id BIGINT,
    equipe_execucao JSONB,
    problema_relatado TEXT,
    solucao_aplicada TEXT,
    observacoes_internas TEXT,
    observacoes_cliente TEXT,
    valor_servicos DECIMAL(10,2) DEFAULT 0,
    valor_produtos DECIMAL(10,2) DEFAULT 0,
    valor_desconto DECIMAL(10,2) DEFAULT 0,
    valor_acrescimo DECIMAL(10,2) DEFAULT 0,
    valor_total DECIMAL(10,2) NOT NULL,
    -- TODO: Uncomment when formas_pagamento table is created
    -- forma_pagamento_id BIGINT,
    forma_pagamento_id BIGINT,
    -- TODO: Uncomment when condicoes_pagamento table is created
    -- condicao_pagamento_id BIGINT,
    condicao_pagamento_id BIGINT,
    valor_entrada DECIMAL(10,2) DEFAULT 0,
    valor_financiado DECIMAL(10,2) DEFAULT 0,
    aprovado_cliente BOOLEAN DEFAULT FALSE,
    data_aprovacao_cliente TIMESTAMP,
    metodo_aprovacao VARCHAR(20),
    assinatura_digital_cliente TEXT,
    garantia_dias INT DEFAULT 90,
    data_vencimento_garantia DATE,
    nfe_emitida BOOLEAN DEFAULT FALSE,
    numero_nfe VARCHAR(20),
    chave_nfe VARCHAR(44),
    url_danfe VARCHAR(500),
    nota_avaliacao_cliente INT,
    comentario_avaliacao_cliente TEXT,
    data_avaliacao_cliente TIMESTAMP,
    tempo_total_execucao_minutos INT,
    margem_lucro_realizada DECIMAL(5,2),
    custo_total_real DECIMAL(10,2),
    rentabilidade_os DECIMAL(10,2),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_ordens_servico_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    -- TODO: Uncomment when clientes table is created
    -- CONSTRAINT fk_ordens_servico_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    -- TODO: Uncomment when veiculos table is created
    -- CONSTRAINT fk_ordens_servico_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculos(id),
    CONSTRAINT fk_ordens_servico_status FOREIGN KEY (status_id) REFERENCES status_os(id),
    -- TODO: Uncomment when formas_pagamento table is created
    -- CONSTRAINT fk_ordens_servico_forma_pagamento FOREIGN KEY (forma_pagamento_id) REFERENCES formas_pagamento(id),
    -- TODO: Uncomment when condicoes_pagamento table is created
    -- CONSTRAINT fk_ordens_servico_condicao_pagamento FOREIGN KEY (condicao_pagamento_id) REFERENCES condicoes_pagamento(id),
    CONSTRAINT uk_ordens_servico_numero UNIQUE (empresa_id, numero_os),
    CONSTRAINT chk_ordens_servico_tipo CHECK (tipo_os IN ('MANUTENCAO', 'REPARO', 'REVISAO', 'DIAGNOSTICO', 'ORCAMENTO', 'GARANTIA', 'RECALL')),
    CONSTRAINT chk_ordens_servico_prioridade CHECK (prioridade IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE')),
    CONSTRAINT chk_ordens_servico_nivel_combustivel_entrada CHECK (nivel_combustivel_entrada IN ('VAZIO', 'RESERVA', '1/4', '1/2', '3/4', 'CHEIO')),
    CONSTRAINT chk_ordens_servico_nivel_combustivel_saida CHECK (nivel_combustivel_saida IN ('VAZIO', 'RESERVA', '1/4', '1/2', '3/4', 'CHEIO')),
    CONSTRAINT chk_ordens_servico_metodo_aprovacao CHECK (metodo_aprovacao IN ('PRESENCIAL', 'TELEFONE', 'WHATSAPP', 'EMAIL', 'APP')),
    CONSTRAINT chk_ordens_servico_nota_avaliacao CHECK (nota_avaliacao_cliente >= 1 AND nota_avaliacao_cliente <= 5)
);

-- Create indexes
CREATE INDEX idx_ordens_servico_empresa ON ordens_servico(empresa_id);
CREATE INDEX idx_ordens_servico_cliente ON ordens_servico(cliente_id);
CREATE INDEX idx_ordens_servico_veiculo ON ordens_servico(veiculo_id);
CREATE INDEX idx_ordens_servico_status ON ordens_servico(status_id);
CREATE INDEX idx_ordens_servico_tipo ON ordens_servico(tipo_os);
CREATE INDEX idx_ordens_servico_data_abertura ON ordens_servico(data_abertura);
CREATE INDEX idx_ordens_servico_data_promessa ON ordens_servico(data_promessa);
CREATE INDEX idx_ordens_servico_consultor ON ordens_servico(consultor_responsavel_id);
CREATE INDEX idx_ordens_servico_mecanico ON ordens_servico(mecanico_responsavel_id);

-- Comments
COMMENT ON TABLE ordens_servico IS 'Ordens de serviço principais do sistema';
COMMENT ON COLUMN ordens_servico.tipo_os IS 'Tipo da OS: MANUTENCAO, REPARO, REVISAO, DIAGNOSTICO, ORCAMENTO, GARANTIA, RECALL';
COMMENT ON COLUMN ordens_servico.prioridade IS 'Prioridade: BAIXA, NORMAL, ALTA, URGENTE';
COMMENT ON COLUMN ordens_servico.equipe_execucao IS 'JSON com IDs dos membros da equipe';
COMMENT ON COLUMN ordens_servico.metodo_aprovacao IS 'Método: PRESENCIAL, TELEFONE, WHATSAPP, EMAIL, APP';
