-- Migração V133: Tabela de Lembretes de Agendamento
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Lembretes enviados aos clientes sobre agendamentos

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_lembretes_agendamento_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_lembretes_agendamento (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_lembretes_agendamento_id'),
    agendamento_id BIGINT NOT NULL,
    tipo_lembrete VARCHAR(30) NOT NULL CHECK (tipo_lembrete IN ('SMS', 'EMAIL', 'WHATSAPP', 'LIGACAO', 'PUSH_NOTIFICATION')),
    destinatario VARCHAR(255) NOT NULL,
    assunto VARCHAR(255),
    mensagem TEXT NOT NULL,
    data_agendamento_envio TIMESTAMP NOT NULL,
    data_envio TIMESTAMP,
    status_envio VARCHAR(30) NOT NULL CHECK (status_envio IN ('AGENDADO', 'ENVIADO', 'ENTREGUE', 'FALHOU', 'CANCELADO')),
    tentativas_envio INT DEFAULT 0,
    erro_envio TEXT,
    resposta_cliente TEXT,
    data_resposta TIMESTAMP,
    custo_envio DECIMAL(6,4),
    template_usado VARCHAR(100),
    variaveis_template TEXT,
    automatico BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_agd_lembretes_agendamento FOREIGN KEY (agendamento_id) REFERENCES agd_agendamentos(id)
);

-- Criar índices
CREATE INDEX idx_agd_lembretes_agendamento ON agd_lembretes_agendamento(agendamento_id);
CREATE INDEX idx_agd_lembretes_status ON agd_lembretes_agendamento(status_envio);
CREATE INDEX idx_agd_lembretes_data_envio ON agd_lembretes_agendamento(data_agendamento_envio);

-- Comentários
COMMENT ON TABLE agd_lembretes_agendamento IS 'Lembretes enviados aos clientes';
COMMENT ON COLUMN agd_lembretes_agendamento.variaveis_template IS 'JSON com variáveis do template';
COMMENT ON COLUMN agd_lembretes_agendamento.automatico IS 'Se foi enviado automaticamente ou manualmente';
