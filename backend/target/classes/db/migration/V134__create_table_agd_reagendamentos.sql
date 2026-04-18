-- Migração V134: Tabela de Reagendamentos
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Histórico de reagendamentos

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_reagendamentos_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_reagendamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_reagendamentos_id'),
    agendamento_original_id BIGINT NOT NULL,
    agendamento_novo_id BIGINT,
    data_original DATE NOT NULL,
    hora_original TIME NOT NULL,
    data_novo DATE NOT NULL,
    hora_novo TIME NOT NULL,
    motivo_reagendamento VARCHAR(30) NOT NULL CHECK (motivo_reagendamento IN ('SOLICITACAO_CLIENTE', 'INDISPONIBILIDADE_MECANICO', 'FALTA_RECURSO', 'EMERGENCIA', 'OUTROS')),
    descricao_motivo TEXT,
    solicitado_por VARCHAR(30) NOT NULL CHECK (solicitado_por IN ('CLIENTE', 'OFICINA')),
    taxa_reagendamento DECIMAL(6,2) DEFAULT 0,
    aprovado_cliente BOOLEAN DEFAULT FALSE,
    data_aprovacao TIMESTAMP,
    observacoes TEXT,
    usuario_responsavel BIGINT,
    data_reagendamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_agd_reagendamentos_original FOREIGN KEY (agendamento_original_id) REFERENCES agd_agendamentos(id),
    CONSTRAINT fk_agd_reagendamentos_novo FOREIGN KEY (agendamento_novo_id) REFERENCES agd_agendamentos(id)
);

-- Criar índices
CREATE INDEX idx_agd_reagendamentos_original ON agd_reagendamentos(agendamento_original_id);
CREATE INDEX idx_agd_reagendamentos_novo ON agd_reagendamentos(agendamento_novo_id);
CREATE INDEX idx_agd_reagendamentos_data ON agd_reagendamentos(data_reagendamento);

-- Comentários
COMMENT ON TABLE agd_reagendamentos IS 'Histórico de reagendamentos';
COMMENT ON COLUMN agd_reagendamentos.taxa_reagendamento IS 'Taxa cobrada pelo reagendamento, se aplicável';
