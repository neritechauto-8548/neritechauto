-- Migração V129: Tabela de Tipos de Agendamento
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Configuração de tipos de agendamento

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_agd_tipos_agendamento_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE agd_tipos_agendamento (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_agd_tipos_agendamento_id'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    cor_identificacao VARCHAR(7),
    icone VARCHAR(50),
    duracao_padrao_minutos INT NOT NULL,
    permite_encaixe BOOLEAN DEFAULT FALSE,
    requer_orcamento BOOLEAN DEFAULT FALSE,
    servicos_inclusos TEXT,
    observacoes_padrao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    ordem_exibicao INT DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT
);

-- Criar índices
CREATE INDEX idx_agd_tipos_agendamento_empresa ON agd_tipos_agendamento(empresa_id);
CREATE INDEX idx_agd_tipos_agendamento_ativo ON agd_tipos_agendamento(ativo);

-- Comentários
COMMENT ON TABLE agd_tipos_agendamento IS 'Tipos de agendamento configuráveis';
COMMENT ON COLUMN agd_tipos_agendamento.cor_identificacao IS 'Cor em hexadecimal para identificação visual';
COMMENT ON COLUMN agd_tipos_agendamento.servicos_inclusos IS 'JSON com serviços inclusos neste tipo';
COMMENT ON COLUMN agd_tipos_agendamento.permite_encaixe IS 'Permite agendamento de encaixe';
