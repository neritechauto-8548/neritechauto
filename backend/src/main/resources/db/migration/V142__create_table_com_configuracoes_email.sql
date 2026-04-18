-- Migração V142: Tabela de Configurações de Email
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Configurações de integração com provedores de email

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_configuracoes_email_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_configuracoes_email (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_configuracoes_email_id'),
    empresa_id BIGINT NOT NULL,
    provedor_servico VARCHAR(30) NOT NULL CHECK (provedor_servico IN ('SENDGRID', 'MAILGUN', 'AWS_SES', 'SMTP_CUSTOMIZADO')),
    servidor_smtp VARCHAR(255),
    porta_smtp INT DEFAULT 587,
    usuario_smtp VARCHAR(255),
    senha_smtp VARCHAR(255),
    criptografia VARCHAR(30) CHECK (criptografia IN ('NONE', 'SSL', 'TLS', 'STARTTLS')),
    remetente_nome VARCHAR(255),
    remetente_email VARCHAR(255),
    email_resposta VARCHAR(255),
    email_copia_oculta VARCHAR(255),
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    dominio_autorizado VARCHAR(255),
    limite_envios_dia INT,
    envios_realizados_hoje INT DEFAULT 0,
    webhook_entrega VARCHAR(500),
    webhook_abertura VARCHAR(500),
    webhook_clique VARCHAR(500),
    assinatura_padrao TEXT,
    template_cabecalho TEXT,
    template_rodape TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    testado BOOLEAN DEFAULT FALSE,
    data_ultimo_teste TIMESTAMP,
    observacoes TEXT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    configurado_por BIGINT
);

-- Criar índices
CREATE INDEX idx_com_config_email_empresa ON com_configuracoes_email(empresa_id);
CREATE INDEX idx_com_config_email_ativo ON com_configuracoes_email(ativo);

-- Comentários
COMMENT ON TABLE com_configuracoes_email IS 'Configurações de integração com provedores de email';
COMMENT ON COLUMN com_configuracoes_email.senha_smtp IS 'Senha SMTP (deve ser criptografada)';
COMMENT ON COLUMN com_configuracoes_email.api_key IS 'Chave de API (deve ser criptografada)';
