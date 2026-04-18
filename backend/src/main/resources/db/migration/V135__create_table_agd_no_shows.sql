-- Migração V135: Tabela de No-Shows
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Registro de não comparecimentos

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_no_shows_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_no_shows (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_no_shows_id'),
    agendamento_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    data_agendamento DATE NOT NULL,
    hora_agendamento TIME NOT NULL,
    tempo_tolerancia_minutos INT DEFAULT 15,
    tentativas_contato INT DEFAULT 0,
    meio_contato_tentado TEXT,
    reagendado BOOLEAN DEFAULT FALSE,
    novo_agendamento_id BIGINT,
    taxa_no_show DECIMAL(6,2) DEFAULT 0,
    taxa_aplicada BOOLEAN DEFAULT FALSE,
    motivo_declarado TEXT,
    justificativa_aceita BOOLEAN DEFAULT FALSE,
    observacoes TEXT,
    registrado_por BIGINT,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_agd_no_shows_agendamento FOREIGN KEY (agendamento_id) REFERENCES agd_agendamentos(id),
    CONSTRAINT fk_agd_no_shows_novo_agendamento FOREIGN KEY (novo_agendamento_id) REFERENCES agd_agendamentos(id)
);

-- Criar índices
CREATE INDEX idx_agd_no_shows_agendamento ON agd_no_shows(agendamento_id);
CREATE INDEX idx_agd_no_shows_cliente ON agd_no_shows(cliente_id);
CREATE INDEX idx_agd_no_shows_data ON agd_no_shows(data_agendamento);

-- Comentários
COMMENT ON TABLE agd_no_shows IS 'Registro de não comparecimentos';
COMMENT ON COLUMN agd_no_shows.meio_contato_tentado IS 'JSON com meios de contato tentados';
COMMENT ON COLUMN agd_no_shows.taxa_no_show IS 'Taxa cobrada por não comparecimento';
