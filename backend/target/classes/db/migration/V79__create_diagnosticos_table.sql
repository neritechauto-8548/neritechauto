-- V79: Create diagnosticos table
CREATE SEQUENCE IF NOT EXISTS seq_diagnosticos START WITH 1 INCREMENT BY 1;

CREATE TABLE diagnosticos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_diagnosticos'),
    ordem_servico_id BIGINT NOT NULL,
    sistema_veiculo VARCHAR(30),
    componente_especifico VARCHAR(255),
    problema_identificado TEXT NOT NULL,
    causa_provavel TEXT,
    solucao_recomendada TEXT,
    urgencia VARCHAR(20),
    impacto_seguranca BOOLEAN DEFAULT FALSE,
    impacto_dirigibilidade BOOLEAN DEFAULT FALSE,
    custo_estimado DECIMAL(10,2),
    tempo_estimado_reparo INT,
    ferramentas_necessarias TEXT,
    pecas_necessarias JSONB,
    evidencias_encontradas TEXT,
    testes_realizados TEXT,
    codigo_erro VARCHAR(50),
    mecanico_diagnostico_id BIGINT,
    data_diagnostico TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fotos_diagnostico JSONB,
    videos_diagnostico JSONB,
    aprovado_cliente BOOLEAN DEFAULT FALSE,
    data_aprovacao_cliente TIMESTAMP,
    observacoes TEXT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_diagnosticos_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT chk_diagnosticos_sistema CHECK (sistema_veiculo IN ('MOTOR', 'TRANSMISSAO', 'FREIOS', 'SUSPENSAO', 'DIRECAO', 'ELETRICO', 'CLIMATIZACAO', 'COMBUSTIVEL', 'ESCAPE', 'OUTROS')),
    CONSTRAINT chk_diagnosticos_urgencia CHECK (urgencia IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA'))
);

CREATE INDEX idx_diagnosticos_os ON diagnosticos(ordem_servico_id);
CREATE INDEX idx_diagnosticos_sistema ON diagnosticos(sistema_veiculo);
CREATE INDEX idx_diagnosticos_urgencia ON diagnosticos(urgencia);
CREATE INDEX idx_diagnosticos_mecanico ON diagnosticos(mecanico_diagnostico_id);
COMMENT ON TABLE diagnosticos IS 'Diagnósticos técnicos das ordens de serviço';
