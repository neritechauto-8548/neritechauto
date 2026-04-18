-- Migração V128: Tabela de Agendamentos
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Tabela principal de agendamentos com todos os campos de controle

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_agendamentos_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_agendamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_agendamentos_id'),
    empresa_id BIGINT NOT NULL,
    numero_agendamento VARCHAR(20) UNIQUE NOT NULL,
    cliente_id BIGINT NOT NULL,
    veiculo_id BIGINT NOT NULL,
    tipo_agendamento_id BIGINT NOT NULL,
    data_agendamento DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    duracao_estimada_minutos INT NOT NULL,
    servicos_solicitados TEXT,
    problema_relatado TEXT,
    observacoes_cliente TEXT,
    observacoes_internas TEXT,
    mecanico_preferido_id BIGINT,
    mecanico_alocado_id BIGINT,
    recursos_necessarios TEXT,
    status VARCHAR(30) NOT NULL CHECK (status IN ('AGENDADO', 'CONFIRMADO', 'EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO', 'NAO_COMPARECEU', 'REAGENDADO')),
    confirmado_cliente BOOLEAN DEFAULT FALSE,
    data_confirmacao TIMESTAMP,
    metodo_confirmacao VARCHAR(30) CHECK (metodo_confirmacao IN ('TELEFONE', 'WHATSAPP', 'EMAIL', 'SMS', 'PRESENCIAL')),
    lembrete_enviado BOOLEAN DEFAULT FALSE,
    data_lembrete TIMESTAMP,
    chegada_cliente TIMESTAMP,
    inicio_atendimento TIMESTAMP,
    fim_atendimento TIMESTAMP,
    avaliacao_atendimento INT CHECK (avaliacao_atendimento >= 1 AND avaliacao_atendimento <= 5),
    comentario_avaliacao TEXT,
    ordem_servico_gerada_id BIGINT,
    valor_estimado DECIMAL(10,2),
    forma_pagamento_preferida_id BIGINT,
    canal_agendamento VARCHAR(30) NOT NULL CHECK (canal_agendamento IN ('TELEFONE', 'WHATSAPP', 'SITE', 'APP', 'PRESENCIAL', 'INDICACAO')),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    agendado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    
    CONSTRAINT fk_agd_agendamentos_tipo FOREIGN KEY (tipo_agendamento_id) REFERENCES agd_tipos_agendamento(id)
);

-- Criar índices para melhor performance
CREATE INDEX idx_agd_agendamentos_empresa ON agd_agendamentos(empresa_id);
CREATE INDEX idx_agd_agendamentos_cliente ON agd_agendamentos(cliente_id);
CREATE INDEX idx_agd_agendamentos_veiculo ON agd_agendamentos(veiculo_id);
CREATE INDEX idx_agd_agendamentos_data ON agd_agendamentos(data_agendamento);
CREATE INDEX idx_agd_agendamentos_status ON agd_agendamentos(status);
CREATE INDEX idx_agd_agendamentos_mecanico_alocado ON agd_agendamentos(mecanico_alocado_id);

-- Comentários
COMMENT ON TABLE agd_agendamentos IS 'Tabela principal de agendamentos de serviços';
COMMENT ON COLUMN agd_agendamentos.numero_agendamento IS 'Número único do agendamento';
COMMENT ON COLUMN agd_agendamentos.servicos_solicitados IS 'JSON com lista de serviços solicitados';
COMMENT ON COLUMN agd_agendamentos.recursos_necessarios IS 'JSON com recursos necessários para o atendimento';
