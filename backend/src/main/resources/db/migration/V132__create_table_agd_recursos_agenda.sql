-- Migração V132: Tabela de Recursos de Agenda
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Recursos disponíveis para agendamento (elevadores, equipamentos, etc)

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_recursos_agenda_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_recursos_agenda (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_recursos_agenda_id'),
    empresa_id BIGINT NOT NULL,
    nome_recurso VARCHAR(100) NOT NULL,
    tipo_recurso VARCHAR(30) NOT NULL CHECK (tipo_recurso IN ('ELEVADOR', 'EQUIPAMENTO', 'FERRAMENTA', 'BAIA', 'SALA', 'VEICULO_CORTESIA')),
    descricao TEXT,
    capacidade_simultanea INT DEFAULT 1,
    localizacao VARCHAR(255),
    disponivel BOOLEAN DEFAULT TRUE,
    requer_agendamento BOOLEAN DEFAULT TRUE,
    tempo_setup_minutos INT DEFAULT 0,
    tempo_cleanup_minutos INT DEFAULT 0,
    custo_hora DECIMAL(8,2) DEFAULT 0,
    observacoes TEXT,
    responsavel_id BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT
);

-- Criar índices
CREATE INDEX idx_agd_recursos_empresa ON agd_recursos_agenda(empresa_id);
CREATE INDEX idx_agd_recursos_tipo ON agd_recursos_agenda(tipo_recurso);
CREATE INDEX idx_agd_recursos_disponivel ON agd_recursos_agenda(disponivel);

-- Comentários
COMMENT ON TABLE agd_recursos_agenda IS 'Recursos disponíveis para agendamento';
COMMENT ON COLUMN agd_recursos_agenda.capacidade_simultanea IS 'Quantos agendamentos simultâneos o recurso suporta';
COMMENT ON COLUMN agd_recursos_agenda.tempo_setup_minutos IS 'Tempo de preparação antes do uso';
COMMENT ON COLUMN agd_recursos_agenda.tempo_cleanup_minutos IS 'Tempo de limpeza após o uso';
