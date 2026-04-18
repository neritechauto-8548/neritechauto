-- Migração V139: Tabela de Campanhas de Marketing
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Campanhas de marketing e comunicação

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_campanhas_marketing_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_campanhas_marketing (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_campanhas_marketing_id'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo_campanha VARCHAR(30) NOT NULL CHECK (tipo_campanha IN ('PROMOCIONAL', 'INFORMATIVA', 'FIDELIZACAO', 'REATIVACAO', 'PESQUISA', 'SAZONAL')),
    objetivo TEXT,
    publico_alvo TEXT,
    canais_comunicacao TEXT,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    orcamento_total DECIMAL(10,2),
    custo_realizado DECIMAL(10,2) DEFAULT 0,
    meta_alcance INT,
    alcance_realizado INT DEFAULT 0,
    meta_conversao INT,
    conversao_realizada INT DEFAULT 0,
    template_email_id BIGINT,
    template_sms_id BIGINT,
    template_whatsapp_id BIGINT,
    promocao_associada_id BIGINT,
    status VARCHAR(30) NOT NULL CHECK (status IN ('RASCUNHO', 'AGENDADA', 'EM_ANDAMENTO', 'PAUSADA', 'CONCLUIDA', 'CANCELADA')),
    aprovada_por BIGINT,
    data_aprovacao TIMESTAMP,
    observacoes TEXT,
    resultados_detalhados TEXT,
    roi_calculado DECIMAL(8,2),
    criada_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_com_campanhas_template_email FOREIGN KEY (template_email_id) REFERENCES com_templates_comunicacao(id),
    CONSTRAINT fk_com_campanhas_template_sms FOREIGN KEY (template_sms_id) REFERENCES com_templates_comunicacao(id),
    CONSTRAINT fk_com_campanhas_template_whatsapp FOREIGN KEY (template_whatsapp_id) REFERENCES com_templates_comunicacao(id)
);

-- Criar índices
CREATE INDEX idx_com_campanhas_empresa ON com_campanhas_marketing(empresa_id);
CREATE INDEX idx_com_campanhas_tipo ON com_campanhas_marketing(tipo_campanha);
CREATE INDEX idx_com_campanhas_status ON com_campanhas_marketing(status);
CREATE INDEX idx_com_campanhas_data_inicio ON com_campanhas_marketing(data_inicio);
CREATE INDEX idx_com_campanhas_data_fim ON com_campanhas_marketing(data_fim);

-- Comentários
COMMENT ON TABLE com_campanhas_marketing IS 'Campanhas de marketing e comunicação';
COMMENT ON COLUMN com_campanhas_marketing.publico_alvo IS 'JSON com critérios do público-alvo';
COMMENT ON COLUMN com_campanhas_marketing.canais_comunicacao IS 'JSON com canais utilizados';
COMMENT ON COLUMN com_campanhas_marketing.resultados_detalhados IS 'JSON com resultados detalhados';
