-- Migração V143: Tabela de Configurações de WhatsApp
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Configurações de integração com WhatsApp Business API

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_configuracoes_whatsapp_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_configuracoes_whatsapp (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_configuracoes_whatsapp_id'),
    empresa_id BIGINT NOT NULL,
    tipo_integracao VARCHAR(30) NOT NULL CHECK (tipo_integracao IN ('WHATSAPP_BUSINESS_API', 'WEBHOOK', 'CHATBOT', 'TERCEIROS')),
    provedor_servico VARCHAR(100),
    numero_whatsapp VARCHAR(20) NOT NULL,
    token_acesso VARCHAR(500),
    webhook_url VARCHAR(500),
    webhook_token VARCHAR(255),
    phone_number_id VARCHAR(50),
    business_account_id VARCHAR(50),
    app_id VARCHAR(50),
    app_secret VARCHAR(255),
    mensagens_template_aprovadas TEXT,
    horario_atendimento_inicio TIME DEFAULT '08:00:00',
    horario_atendimento_fim TIME DEFAULT '18:00:00',
    mensagem_fora_horario TEXT,
    mensagem_boas_vindas TEXT,
    mensagem_menu_principal TEXT,
    chatbot_ativo BOOLEAN DEFAULT FALSE,
    integracao_ativa BOOLEAN DEFAULT TRUE,
    ambiente VARCHAR(30) CHECK (ambiente IN ('PRODUCAO', 'DESENVOLVIMENTO')),
    observacoes TEXT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    configurado_por BIGINT,
    data_ultima_verificacao TIMESTAMP,
    status_verificacao VARCHAR(50)
);

-- Criar índices
CREATE INDEX idx_com_config_whatsapp_empresa ON com_configuracoes_whatsapp(empresa_id);
CREATE INDEX idx_com_config_whatsapp_ativo ON com_configuracoes_whatsapp(integracao_ativa);

-- Comentários
COMMENT ON TABLE com_configuracoes_whatsapp IS 'Configurações de integração com WhatsApp Business API';
COMMENT ON COLUMN com_configuracoes_whatsapp.token_acesso IS 'Token de acesso (deve ser criptografado)';
COMMENT ON COLUMN com_configuracoes_whatsapp.mensagens_template_aprovadas IS 'JSON com templates aprovados';
