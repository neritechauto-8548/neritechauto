-- Migração V137: Tabela de Templates de Comunicação
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Templates para comunicações (email, SMS, WhatsApp, push)

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_templates_comunicacao_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_templates_comunicacao (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_templates_comunicacao_id'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo_template VARCHAR(30) NOT NULL CHECK (tipo_template IN ('EMAIL', 'SMS', 'WHATSAPP', 'PUSH_NOTIFICATION')),
    categoria VARCHAR(30) NOT NULL CHECK (categoria IN ('AGENDAMENTO', 'LEMBRETE', 'STATUS_OS', 'PROMOCIONAL', 'COBRANCA', 'PESQUISA_SATISFACAO', 'BOAS_VINDAS', 'ANIVERSARIO', 'OUTROS')),
    assunto VARCHAR(255),
    conteudo TEXT NOT NULL,
    variaveis_disponiveis TEXT,
    anexos_padrao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    padrao_categoria BOOLEAN DEFAULT FALSE,
    idioma VARCHAR(5) DEFAULT 'pt-BR',
    personalizavel BOOLEAN DEFAULT TRUE,
    requer_aprovacao BOOLEAN DEFAULT FALSE,
    tags VARCHAR(255),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT
);

-- Criar índices
CREATE INDEX idx_com_templates_empresa ON com_templates_comunicacao(empresa_id);
CREATE INDEX idx_com_templates_tipo ON com_templates_comunicacao(tipo_template);
CREATE INDEX idx_com_templates_categoria ON com_templates_comunicacao(categoria);
CREATE INDEX idx_com_templates_ativo ON com_templates_comunicacao(ativo);

-- Comentários
COMMENT ON TABLE com_templates_comunicacao IS 'Templates de comunicação para diversos canais';
COMMENT ON COLUMN com_templates_comunicacao.variaveis_disponiveis IS 'JSON com variáveis disponíveis no template';
COMMENT ON COLUMN com_templates_comunicacao.anexos_padrao IS 'JSON com anexos padrão';
