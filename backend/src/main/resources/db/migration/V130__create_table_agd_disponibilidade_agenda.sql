-- Migração V130: Tabela de Disponibilidade de Agenda
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Controle de disponibilidade de funcionários na agenda

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_disponibilidade_agenda_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_disponibilidade_agenda (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_disponibilidade_agenda_id'),
    empresa_id BIGINT NOT NULL,
    funcionario_id BIGINT NOT NULL,
    data_disponibilidade DATE NOT NULL,
    dia_semana INT NOT NULL CHECK (dia_semana >= 0 AND dia_semana <= 6),
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    intervalo_almoco_inicio TIME,
    intervalo_almoco_fim TIME,
    disponivel BOOLEAN DEFAULT TRUE,
    capacidade_atendimentos INT DEFAULT 1,
    especialidades_disponiveis TEXT,
    tipos_agendamento_aceitos TEXT,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT
);

-- Criar índices
CREATE INDEX idx_agd_disponibilidade_empresa ON agd_disponibilidade_agenda(empresa_id);
CREATE INDEX idx_agd_disponibilidade_funcionario ON agd_disponibilidade_agenda(funcionario_id);
CREATE INDEX idx_agd_disponibilidade_data ON agd_disponibilidade_agenda(data_disponibilidade);
CREATE INDEX idx_agd_disponibilidade_dia_semana ON agd_disponibilidade_agenda(dia_semana);

-- Comentários
COMMENT ON TABLE agd_disponibilidade_agenda IS 'Disponibilidade de funcionários para agendamentos';
COMMENT ON COLUMN agd_disponibilidade_agenda.dia_semana IS '0=Domingo, 1=Segunda, ..., 6=Sábado';
COMMENT ON COLUMN agd_disponibilidade_agenda.especialidades_disponiveis IS 'JSON com especialidades disponíveis';
COMMENT ON COLUMN agd_disponibilidade_agenda.tipos_agendamento_aceitos IS 'JSON com tipos de agendamento aceitos';
