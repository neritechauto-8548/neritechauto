-- Migração V126: Criar tabela de Reclamações de Garantia
-- Armazena as reclamações/acionamentos de garantia

CREATE SEQUENCE seq_gar_reclamacoes_garantia_id START WITH 1 INCREMENT BY 1;

CREATE TABLE gar_reclamacoes_garantia (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_gar_reclamacoes_garantia_id'),
    garantia_id BIGINT NOT NULL,
    numero_reclamacao VARCHAR(30) UNIQUE NOT NULL,
    data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_reclamacao VARCHAR(30) NOT NULL,
    problema_relatado TEXT NOT NULL,
    sintomas_observados TEXT,
    condicoes_uso TEXT,
    kilometragem_atual INT,
    tempo_uso_desde_servico INT,
    evidencias_fornecidas TEXT, -- JSON armazenado como TEXT
    fotos_problema TEXT, -- JSON armazenado como TEXT
    videos_problema TEXT, -- JSON armazenado como TEXT
    documentos_anexos TEXT, -- JSON armazenado como TEXT
    prioridade VARCHAR(30) NOT NULL,
    canal_abertura VARCHAR(30) NOT NULL,
    cliente_solicitante VARCHAR(255),
    contato_solicitante VARCHAR(255),
    endereco_atendimento TEXT,
    data_agendamento_analise TIMESTAMP,
    tecnico_responsavel_id BIGINT,
    data_inicio_analise TIMESTAMP,
    data_fim_analise TIMESTAMP,
    tempo_analise_horas INT,
    diagnostico_tecnico TEXT,
    causa_identificada TEXT,
    procedimento_realizado TEXT,
    aprovada BOOLEAN,
    motivo_reprovacao TEXT,
    valor_aprovado DECIMAL(10,2),
    itens_substituidos TEXT, -- JSON armazenado como TEXT
    servicos_refeitos TEXT, -- JSON armazenado como TEXT
    custos_adicionais DECIMAL(8,2) DEFAULT 0,
    justificativa_custos TEXT,
    satisfacao_cliente INT,
    comentario_satisfacao TEXT,
    status VARCHAR(30) NOT NULL,
    data_resolucao TIMESTAMP,
    observacoes_internas TEXT,
    aberta_por BIGINT,
    
    -- Constraints
    CONSTRAINT fk_reclamacoes_garantia FOREIGN KEY (garantia_id) REFERENCES gar_garantias(id),
    CONSTRAINT fk_reclamacoes_tecnico FOREIGN KEY (tecnico_responsavel_id) REFERENCES funcionarios(id),
    CONSTRAINT chk_tipo_reclamacao CHECK (tipo_reclamacao IN ('DEFEITO', 'MAU_FUNCIONAMENTO', 'QUEBRA', 'DESGASTE_PREMATURO', 'OUTROS')),
    CONSTRAINT chk_prioridade CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA', 'URGENTE')),
    CONSTRAINT chk_canal_abertura CHECK (canal_abertura IN ('PRESENCIAL', 'TELEFONE', 'WHATSAPP', 'EMAIL', 'SITE', 'APP')),
    CONSTRAINT chk_status_reclamacao CHECK (status IN ('ABERTA', 'EM_ANALISE', 'APROVADA', 'REPROVADA', 'EM_EXECUCAO', 'CONCLUIDA', 'CANCELADA')),
    CONSTRAINT chk_satisfacao_cliente CHECK (satisfacao_cliente IS NULL OR (satisfacao_cliente >= 1 AND satisfacao_cliente <= 5))
);

-- Índices para otimização de consultas
CREATE INDEX idx_reclamacoes_garantia ON gar_reclamacoes_garantia(garantia_id);
CREATE INDEX idx_reclamacoes_numero ON gar_reclamacoes_garantia(numero_reclamacao);
CREATE INDEX idx_reclamacoes_status ON gar_reclamacoes_garantia(status);
CREATE INDEX idx_reclamacoes_prioridade ON gar_reclamacoes_garantia(prioridade);
CREATE INDEX idx_reclamacoes_tecnico ON gar_reclamacoes_garantia(tecnico_responsavel_id);
CREATE INDEX idx_reclamacoes_data_abertura ON gar_reclamacoes_garantia(data_abertura);
CREATE INDEX idx_reclamacoes_abertas ON gar_reclamacoes_garantia(status) WHERE status IN ('ABERTA', 'EM_ANALISE', 'EM_EXECUCAO');

-- Comentários
COMMENT ON TABLE gar_reclamacoes_garantia IS 'Reclamações e acionamentos de garantia';
COMMENT ON COLUMN gar_reclamacoes_garantia.tipo_reclamacao IS 'Tipo: DEFEITO, MAU_FUNCIONAMENTO, QUEBRA, DESGASTE_PREMATURO, OUTROS';
COMMENT ON COLUMN gar_reclamacoes_garantia.prioridade IS 'Prioridade: BAIXA, MEDIA, ALTA, URGENTE';
COMMENT ON COLUMN gar_reclamacoes_garantia.canal_abertura IS 'Canal: PRESENCIAL, TELEFONE, WHATSAPP, EMAIL, SITE, APP';
COMMENT ON COLUMN gar_reclamacoes_garantia.status IS 'Status: ABERTA, EM_ANALISE, APROVADA, REPROVADA, EM_EXECUCAO, CONCLUIDA, CANCELADA';
