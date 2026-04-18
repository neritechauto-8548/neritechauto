-- Migração V141: Tabela de Configurações de SMS
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Configurações de integração com provedores de SMS

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_configuracoes_sms_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_configuracoes_sms (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_configuracoes_sms_id'),
    empresa_id BIGINT NOT NULL,
    provedor_servico VARCHAR(30) NOT NULL CHECK (provedor_servico IN ('ZENVIA', 'TOTALVOICE', 'TWILIO', 'MOVILE', 'OUTROS')),
    api_endpoint VARCHAR(500),
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    usuario_api VARCHAR(100),
    senha_api VARCHAR(255),
    remetente_padrao VARCHAR(20),
    limite_caracteres INT DEFAULT 160,
    custo_por_sms DECIMAL(6,4),
    creditos_disponiveis INT DEFAULT 0,
    webhook_entrega VARCHAR(500),
    webhook_resposta VARCHAR(500),
    ativo BOOLEAN DEFAULT TRUE,
    ambiente VARCHAR(30) CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO')),
    observacoes TEXT,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    configurado_por BIGINT,
    data_ultima_sincronizacao TIMESTAMP
);

-- Criar índices
CREATE INDEX idx_com_config_sms_empresa ON com_configuracoes_sms(empresa_id);
CREATE INDEX idx_com_config_sms_ativo ON com_configuracoes_sms(ativo);

-- Comentários
COMMENT ON TABLE com_configuracoes_sms IS 'Configurações de integração com provedores de SMS';
COMMENT ON COLUMN com_configuracoes_sms.api_key IS 'Chave de API (deve ser criptografada)';
COMMENT ON COLUMN com_configuracoes_sms.api_secret IS 'Secret da API (deve ser criptografado)';
