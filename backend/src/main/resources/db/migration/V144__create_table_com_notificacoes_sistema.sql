-- Migração V144: Tabela de Notificações do Sistema
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Notificações internas do sistema para usuários

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_notificacoes_sistema_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_notificacoes_sistema (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_notificacoes_sistema_id'),
    empresa_id BIGINT NOT NULL,
    usuario_destinatario_id BIGINT NOT NULL,
    tipo_notificacao VARCHAR(30) NOT NULL CHECK (tipo_notificacao IN ('INFO', 'ALERTA', 'ERRO', 'SUCESSO', 'LEMBRETE')),
    categoria VARCHAR(50),
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    dados_contexto TEXT,
    link_acao VARCHAR(500),
    texto_botao_acao VARCHAR(50),
    prioridade VARCHAR(30) NOT NULL CHECK (prioridade IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE')),
    exige_confirmacao BOOLEAN DEFAULT FALSE,
    lida BOOLEAN DEFAULT FALSE,
    data_leitura TIMESTAMP,
    confirmada BOOLEAN DEFAULT FALSE,
    data_confirmacao TIMESTAMP,
    data_expiracao TIMESTAMP,
    icone VARCHAR(50),
    cor VARCHAR(7),
    som_notificacao VARCHAR(100),
    enviar_email BOOLEAN DEFAULT FALSE,
    enviar_sms BOOLEAN DEFAULT FALSE,
    enviar_push BOOLEAN DEFAULT FALSE,
    origem_sistema VARCHAR(100),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índices
CREATE INDEX idx_com_notificacoes_empresa ON com_notificacoes_sistema(empresa_id);
CREATE INDEX idx_com_notificacoes_usuario ON com_notificacoes_sistema(usuario_destinatario_id);
CREATE INDEX idx_com_notificacoes_lida ON com_notificacoes_sistema(lida);
CREATE INDEX idx_com_notificacoes_tipo ON com_notificacoes_sistema(tipo_notificacao);
CREATE INDEX idx_com_notificacoes_prioridade ON com_notificacoes_sistema(prioridade);
CREATE INDEX idx_com_notificacoes_data ON com_notificacoes_sistema(data_cadastro);

-- Comentários
COMMENT ON TABLE com_notificacoes_sistema IS 'Notificações internas do sistema para usuários';
COMMENT ON COLUMN com_notificacoes_sistema.dados_contexto IS 'JSON com dados contextuais da notificação';
