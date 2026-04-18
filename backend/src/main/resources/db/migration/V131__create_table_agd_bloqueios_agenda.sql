-- Migração V131: Tabela de Bloqueios de Agenda
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Bloqueios e indisponibilidades na agenda

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_bloqueios_agenda_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_bloqueios_agenda (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_bloqueios_agenda_id'),
    empresa_id BIGINT NOT NULL,
    funcionario_id BIGINT,
    tipo_bloqueio VARCHAR(30) NOT NULL CHECK (tipo_bloqueio IN ('FERIAS', 'LICENCA', 'TREINAMENTO', 'REUNIAO', 'MANUTENCAO', 'OUTROS')),
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    hora_inicio TIME,
    hora_fim TIME,
    recorrente BOOLEAN DEFAULT FALSE,
    dias_semana_recorrencia VARCHAR(7),
    motivo TEXT NOT NULL,
    afeta_todos_funcionarios BOOLEAN DEFAULT FALSE,
    funcionarios_afetados TEXT,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    criado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índices
CREATE INDEX idx_agd_bloqueios_empresa ON agd_bloqueios_agenda(empresa_id);
CREATE INDEX idx_agd_bloqueios_funcionario ON agd_bloqueios_agenda(funcionario_id);
CREATE INDEX idx_agd_bloqueios_data_inicio ON agd_bloqueios_agenda(data_inicio);
CREATE INDEX idx_agd_bloqueios_data_fim ON agd_bloqueios_agenda(data_fim);
CREATE INDEX idx_agd_bloqueios_ativo ON agd_bloqueios_agenda(ativo);

-- Comentários
COMMENT ON TABLE agd_bloqueios_agenda IS 'Bloqueios e indisponibilidades na agenda';
COMMENT ON COLUMN agd_bloqueios_agenda.dias_semana_recorrencia IS 'String com dias da semana (ex: 0,1,5 para Dom,Seg,Sex)';
COMMENT ON COLUMN agd_bloqueios_agenda.funcionarios_afetados IS 'JSON com IDs de funcionários afetados';
