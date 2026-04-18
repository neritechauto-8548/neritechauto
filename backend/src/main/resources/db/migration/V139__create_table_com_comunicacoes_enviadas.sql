-- Migração V138: Tabela de Comunicações Enviadas
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Registro de todas as comunicações enviadas

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_comunicacoes_enviadas_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_comunicacoes_enviadas (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_comunicacoes_enviadas_id'),
    empresa_id BIGINT NOT NULL,
    template_id BIGINT,
    campanha_id BIGINT,
    tipo_comunicacao VARCHAR(30) NOT NULL CHECK (tipo_comunicacao IN ('EMAIL', 'SMS', 'WHATSAPP', 'PUSH_NOTIFICATION', 'LIGACAO')),
    destinatario_tipo VARCHAR(30) NOT NULL CHECK (destinatario_tipo IN ('CLIENTE', 'FUNCIONARIO', 'FORNECEDOR', 'OUTROS')),
    destinatario_id BIGINT NOT NULL,
    destinatario_nome VARCHAR(255) NOT NULL,
    destinatario_contato VARCHAR(255) NOT NULL,
    assunto VARCHAR(255),
    conteudo TEXT NOT NULL,
    anexos TEXT,
    agendada_para TIMESTAMP,
    data_envio TIMESTAMP,
    data_entrega TIMESTAMP,
    data_leitura TIMESTAMP,
    data_clique TIMESTAMP,
    status VARCHAR(30) NOT NULL CHECK (status IN ('AGENDADA', 'ENVIADA', 'ENTREGUE', 'LIDA', 'CLICADA', 'FALHOU', 'CANCELADA')),
    tentativas_envio INT DEFAULT 0,
    erro_envio TEXT,
    custo_envio DECIMAL(8,4),
    resposta_destinatario TEXT,
    data_resposta TIMESTAMP,
    avaliacao_conteudo INT CHECK (avaliacao_conteudo >= 1 AND avaliacao_conteudo <= 5),
    motivo_avaliacao TEXT,
    automatica BOOLEAN DEFAULT FALSE,
    ordem_servico_id BIGINT,
    agendamento_id BIGINT,
    fatura_id BIGINT,
    usuario_envio BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_com_comunicacoes_template FOREIGN KEY (template_id) REFERENCES com_templates_comunicacao(id),
    CONSTRAINT fk_com_comunicacoes_campanha FOREIGN KEY (campanha_id) REFERENCES com_campanhas_marketing(id)
);

-- Criar índices
CREATE INDEX idx_com_comunicacoes_empresa ON com_comunicacoes_enviadas(empresa_id);
CREATE INDEX idx_com_comunicacoes_template ON com_comunicacoes_enviadas(template_id);
CREATE INDEX idx_com_comunicacoes_campanha ON com_comunicacoes_enviadas(campanha_id);
CREATE INDEX idx_com_comunicacoes_tipo ON com_comunicacoes_enviadas(tipo_comunicacao);
CREATE INDEX idx_com_comunicacoes_status ON com_comunicacoes_enviadas(status);
CREATE INDEX idx_com_comunicacoes_destinatario ON com_comunicacoes_enviadas(destinatario_id, destinatario_tipo);
CREATE INDEX idx_com_comunicacoes_data_envio ON com_comunicacoes_enviadas(data_envio);

-- Comentários
COMMENT ON TABLE com_comunicacoes_enviadas IS 'Registro de todas as comunicações enviadas';
COMMENT ON COLUMN com_comunicacoes_enviadas.anexos IS 'JSON com lista de anexos';
