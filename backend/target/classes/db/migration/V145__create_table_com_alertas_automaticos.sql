-- Migração V145: Tabela de Alertas Automáticos
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Configuração de alertas automáticos do sistema

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_alertas_automaticos_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_alertas_automaticos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_alertas_automaticos_id'),
    empresa_id BIGINT NOT NULL,
    nome_alerta VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo_alerta VARCHAR(30) NOT NULL CHECK (tipo_alerta IN ('ESTOQUE_BAIXO', 'VENCIMENTO_DOCUMENTO', 'ATRASO_PAGAMENTO', 'META_VENDAS', 'ANIVERSARIO_CLIENTE', 'REVISAO_VEICULO', 'OUTROS')),
    condicoes_disparo TEXT,
    frequencia_verificacao VARCHAR(30) NOT NULL CHECK (frequencia_verificacao IN ('TEMPO_REAL', 'HORARIA', 'DIARIA', 'SEMANAL', 'MENSAL')),
    horario_verificacao TIME,
    dia_semana_verificacao INT CHECK (dia_semana_verificacao >= 0 AND dia_semana_verificacao <= 6),
    dia_mes_verificacao INT CHECK (dia_mes_verificacao >= 1 AND dia_mes_verificacao <= 31),
    usuarios_notificar TEXT,
    canais_notificacao TEXT,
    template_notificacao_id BIGINT,
    mensagem_personalizada TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    ultima_execucao TIMESTAMP,
    proxima_execucao TIMESTAMP,
    total_disparos INT DEFAULT 0,
    total_erros INT DEFAULT 0,
    log_execucoes TEXT,
    observacoes TEXT,
    criado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_com_alertas_template FOREIGN KEY (template_notificacao_id) REFERENCES com_templates_comunicacao(id)
);

-- Criar índices
CREATE INDEX idx_com_alertas_empresa ON com_alertas_automaticos(empresa_id);
CREATE INDEX idx_com_alertas_tipo ON com_alertas_automaticos(tipo_alerta);
CREATE INDEX idx_com_alertas_ativo ON com_alertas_automaticos(ativo);
CREATE INDEX idx_com_alertas_proxima_execucao ON com_alertas_automaticos(proxima_execucao);

-- Comentários
COMMENT ON TABLE com_alertas_automaticos IS 'Configuração de alertas automáticos do sistema';
COMMENT ON COLUMN com_alertas_automaticos.condicoes_disparo IS 'JSON com condições para disparo do alerta';
COMMENT ON COLUMN com_alertas_automaticos.usuarios_notificar IS 'JSON com IDs de usuários a notificar';
COMMENT ON COLUMN com_alertas_automaticos.canais_notificacao IS 'JSON com canais de notificação';
COMMENT ON COLUMN com_alertas_automaticos.log_execucoes IS 'JSON com histórico de execuções';
