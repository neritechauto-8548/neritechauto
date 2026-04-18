-- V69_1: Create status_os table
-- Description: Service order status definitions (Renamed from V71 to fix dependency order)

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_status_os START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE status_os (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_status_os'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    codigo VARCHAR(20) NOT NULL,
    cor_identificacao VARCHAR(7),
    icone VARCHAR(50),
    ordem_exibicao INT,
    permite_edicao BOOLEAN DEFAULT TRUE,
    notifica_cliente BOOLEAN DEFAULT FALSE,
    template_notificacao_id BIGINT,
    exige_aprovacao BOOLEAN DEFAULT FALSE,
    finaliza_os BOOLEAN DEFAULT FALSE,
    cancela_os BOOLEAN DEFAULT FALSE,
    sistema BOOLEAN DEFAULT FALSE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_status_os_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT uk_status_os_codigo UNIQUE (empresa_id, codigo)
);

-- Create indexes
CREATE INDEX idx_status_os_empresa ON status_os(empresa_id);
CREATE INDEX idx_status_os_codigo ON status_os(codigo);
CREATE INDEX idx_status_os_ordem ON status_os(ordem_exibicao);
CREATE INDEX idx_status_os_ativo ON status_os(ativo);

-- Comments
COMMENT ON TABLE status_os IS 'Status configuráveis para ordens de serviço';
COMMENT ON COLUMN status_os.sistema IS 'Se TRUE, status é do sistema e não pode ser excluído';
COMMENT ON COLUMN status_os.finaliza_os IS 'Se TRUE, ao aplicar este status a OS é finalizada';
COMMENT ON COLUMN status_os.cancela_os IS 'Se TRUE, ao aplicar este status a OS é cancelada';

-- Insert default statuses
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por) VALUES
(1, 'Aberta', 'ABERTA', '#3B82F6', 1, TRUE, FALSE, FALSE, 1),
(1, 'Em Diagnóstico', 'DIAGNOSTICO', '#F59E0B', 2, TRUE, FALSE, FALSE, 1),
(1, 'Aguardando Aprovação', 'AGUARDANDO_APROVACAO', '#EF4444', 3, TRUE, FALSE, FALSE, 1),
(1, 'Aprovada', 'APROVADA', '#10B981', 4, TRUE, FALSE, FALSE, 1),
(1, 'Em Execução', 'EM_EXECUCAO', '#8B5CF6', 5, TRUE, FALSE, FALSE, 1),
(1, 'Aguardando Peças', 'AGUARDANDO_PECAS', '#F97316', 6, TRUE, FALSE, FALSE, 1),
(1, 'Concluída', 'CONCLUIDA', '#059669', 7, TRUE, TRUE, FALSE, 1),
(1, 'Entregue', 'ENTREGUE', '#047857', 8, TRUE, TRUE, FALSE, 1),
(1, 'Cancelada', 'CANCELADA', '#DC2626', 9, TRUE, FALSE, TRUE, 1);
