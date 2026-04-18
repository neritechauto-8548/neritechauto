-- Migração V136: Tabela de Lista de Espera
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Lista de espera para agendamentos

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_lista_espera_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_lista_espera (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_lista_espera_id'),
    empresa_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    veiculo_id BIGINT NOT NULL,
    tipo_agendamento_id BIGINT,
    data_preferida DATE,
    periodo_preferido VARCHAR(30) CHECK (periodo_preferido IN ('MANHA', 'TARDE', 'NOITE', 'QUALQUER')),
    servicos_desejados TEXT,
    mecanico_preferido_id BIGINT,
    urgencia VARCHAR(30) NOT NULL CHECK (urgencia IN ('BAIXA', 'MEDIA', 'ALTA', 'URGENTE')),
    observacoes TEXT,
    notificar_disponibilidade BOOLEAN DEFAULT TRUE,
    telefone_contato VARCHAR(20) NOT NULL,
    email_contato VARCHAR(255),
    whatsapp_contato VARCHAR(20),
    raio_disponibilidade_km INT DEFAULT 0,
    flexibilidade_data_dias INT DEFAULT 7,
    status VARCHAR(30) NOT NULL CHECK (status IN ('AGUARDANDO', 'NOTIFICADO', 'AGENDADO', 'CANCELADO', 'EXPIRADO')),
    data_notificacao TIMESTAMP,
    data_expiracao TIMESTAMP,
    agendamento_gerado_id BIGINT,
    posicao_lista INT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,
    
    CONSTRAINT fk_agd_lista_espera_tipo FOREIGN KEY (tipo_agendamento_id) REFERENCES agd_tipos_agendamento(id),
    CONSTRAINT fk_agd_lista_espera_agendamento FOREIGN KEY (agendamento_gerado_id) REFERENCES agd_agendamentos(id)
);

-- Criar índices
CREATE INDEX idx_agd_lista_espera_empresa ON agd_lista_espera(empresa_id);
CREATE INDEX idx_agd_lista_espera_cliente ON agd_lista_espera(cliente_id);
CREATE INDEX idx_agd_lista_espera_status ON agd_lista_espera(status);
CREATE INDEX idx_agd_lista_espera_urgencia ON agd_lista_espera(urgencia);
CREATE INDEX idx_agd_lista_espera_posicao ON agd_lista_espera(posicao_lista);

-- Comentários
COMMENT ON TABLE agd_lista_espera IS 'Lista de espera para agendamentos';
COMMENT ON COLUMN agd_lista_espera.servicos_desejados IS 'JSON com serviços desejados';
COMMENT ON COLUMN agd_lista_espera.raio_disponibilidade_km IS 'Raio de disponibilidade para atendimento';
COMMENT ON COLUMN agd_lista_espera.flexibilidade_data_dias IS 'Flexibilidade de dias para agendamento';
