-- Migration: Create reservas_equipamentos table
-- Description: Tabela para gerenciamento de reservas de equipamentos e ferramentas

CREATE SEQUENCE seq_reservas_equipamentos START WITH 1 INCREMENT BY 1;

CREATE TABLE reservas_equipamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_reservas_equipamentos'),
    equipamento_id BIGINT,
    ferramenta_id BIGINT,
    usuario_solicitante_id BIGINT NOT NULL,
    finalidade_uso TEXT NOT NULL,
    data_inicio_reserva TIMESTAMP NOT NULL,
    data_fim_reserva TIMESTAMP NOT NULL,
    tempo_estimado_horas DECIMAL(6,2),
    ordem_servico_id BIGINT,
    projeto_associado VARCHAR(255),
    prioridade_uso VARCHAR(20),
    aprovacao_necessaria BOOLEAN DEFAULT FALSE,
    aprovado_por BIGINT,
    data_aprovacao TIMESTAMP,
    motivo_reprovacao TEXT,
    status_reserva VARCHAR(20) NOT NULL,
    data_inicio_real TIMESTAMP,
    data_fim_real TIMESTAMP,
    tempo_uso_real_horas DECIMAL(6,2),
    observacoes_uso TEXT,
    problemas_identificados TEXT,
    condicao_devolucao VARCHAR(20),
    custos_adicionais DECIMAL(8,2),
    justificativa_custos TEXT,
    avaliacao_reserva INT,
    comentarios_avaliacao TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_reservas_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamentos(id),
    CONSTRAINT fk_reservas_ferramenta FOREIGN KEY (ferramenta_id) REFERENCES ferramentas(id),
    CONSTRAINT chk_reserva_item CHECK (
        (equipamento_id IS NOT NULL AND ferramenta_id IS NULL) OR
        (equipamento_id IS NULL AND ferramenta_id IS NOT NULL)
    ),
    CONSTRAINT chk_avaliacao_reserva CHECK (avaliacao_reserva BETWEEN 1 AND 5)
);

CREATE INDEX IF NOT EXISTS idx_reservas_equipamento ON reservas_equipamentos(equipamento_id);
CREATE INDEX IF NOT EXISTS idx_reservas_ferramenta ON reservas_equipamentos(ferramenta_id);
CREATE INDEX IF NOT EXISTS idx_reservas_usuario ON reservas_equipamentos(usuario_solicitante_id);
CREATE INDEX IF NOT EXISTS idx_reservas_status ON reservas_equipamentos(status_reserva);
CREATE INDEX IF NOT EXISTS idx_reservas_periodo ON reservas_equipamentos(data_inicio_reserva, data_fim_reserva);

COMMENT ON TABLE reservas_equipamentos IS 'Tabela para gerenciamento de reservas de equipamentos e ferramentas';
COMMENT ON COLUMN reservas_equipamentos.prioridade_uso IS 'BAIXA, NORMAL, ALTA, URGENTE';
COMMENT ON COLUMN reservas_equipamentos.status_reserva IS 'SOLICITADA, APROVADA, REPROVADA, EM_USO, CONCLUIDA, CANCELADA';
COMMENT ON COLUMN reservas_equipamentos.condicao_devolucao IS 'PERFEITA, BOA, COM_PROBLEMAS, DANIFICADA';
