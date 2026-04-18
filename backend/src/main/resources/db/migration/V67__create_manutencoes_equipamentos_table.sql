-- Migration: Create manutencoes_equipamentos table
-- Description: Tabela para registro de manutenções de equipamentos

CREATE SEQUENCE seq_manutencoes_equipamentos START WITH 1 INCREMENT BY 1;

CREATE TABLE manutencoes_equipamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_manutencoes_equipamentos'),
    equipamento_id BIGINT NOT NULL,
    tipo_manutencao VARCHAR(20) NOT NULL,
    descricao_servico TEXT NOT NULL,
    data_agendamento DATE NOT NULL,
    data_inicio TIMESTAMP,
    data_fim TIMESTAMP,
    duracao_horas DECIMAL(6,2),
    responsavel_interno_id BIGINT,
    empresa_terceirizada VARCHAR(255),
    tecnico_responsavel VARCHAR(255),
    contato_tecnico VARCHAR(100),
    horas_equipamento_manutencao INT,
    status VARCHAR(20) NOT NULL,
    prioridade VARCHAR(20),
    problema_identificado TEXT,
    causa_raiz TEXT,
    solucao_aplicada TEXT,
    pecas_substituidas JSONB,
    custos_pecas DECIMAL(8,2),
    custos_mao_obra DECIMAL(8,2),
    custos_terceiros DECIMAL(8,2),
    custo_total DECIMAL(10,2),
    tempo_parada_horas DECIMAL(6,2),
    impacto_producao TEXT,
    melhorias_implementadas TEXT,
    proxima_manutencao_data DATE,
    proxima_manutencao_horas INT,
    observacoes_tecnicas TEXT,
    fotos_antes JSONB,
    fotos_durante JSONB,
    fotos_depois JSONB,
    documentos_anexos JSONB,
    garantia_servico_dias INT DEFAULT 90,
    avaliacao_servico INT,
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registrado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_manutencoes_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamentos(id),
    CONSTRAINT chk_avaliacao_servico CHECK (avaliacao_servico BETWEEN 1 AND 5)
);

CREATE INDEX idx_manutencoes_equipamento ON manutencoes_equipamentos(equipamento_id);
CREATE INDEX idx_manutencoes_status ON manutencoes_equipamentos(status);
CREATE INDEX idx_manutencoes_tipo ON manutencoes_equipamentos(tipo_manutencao);
CREATE INDEX idx_manutencoes_agendamento ON manutencoes_equipamentos(data_agendamento);
CREATE INDEX idx_manutencoes_prioridade ON manutencoes_equipamentos(prioridade);

COMMENT ON TABLE manutencoes_equipamentos IS 'Tabela para registro de manutenções de equipamentos';
COMMENT ON COLUMN manutencoes_equipamentos.tipo_manutencao IS 'PREVENTIVA, CORRETIVA, PREDITIVA, EMERGENCIAL';
COMMENT ON COLUMN manutencoes_equipamentos.status IS 'AGENDADA, EM_ANDAMENTO, CONCLUIDA, CANCELADA, ADIADA';
COMMENT ON COLUMN manutencoes_equipamentos.prioridade IS 'BAIXA, MEDIA, ALTA, URGENTE';
