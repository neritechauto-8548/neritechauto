-- V166: Create sincronizacoes table
-- Description: Data synchronization history

CREATE SEQUENCE IF NOT EXISTS seq_sincronizacoes START WITH 1 INCREMENT BY 1;

CREATE TABLE sincronizacoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_sincronizacoes'),
    integracao_id BIGINT,
    tipo_sincronizacao VARCHAR(20) CHECK (tipo_sincronizacao IN ('FULL', 'INCREMENTAL', 'DIFERENCIAL')),
    direcao VARCHAR(20) CHECK (direcao IN ('IMPORT', 'EXPORT', 'BIDIRECTIONAL')),
    entidade_origem VARCHAR(100),
    entidade_destino VARCHAR(100),
    filtros_aplicados TEXT, -- JSON
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('EM_ANDAMENTO', 'CONCLUIDA', 'FALHOU', 'CANCELADA', 'PARCIAL')),
    total_registros_origem INT,
    registros_processados INT,
    registros_sucesso INT,
    registros_erro INT,
    registros_ignorados INT,
    progresso_percentual INT DEFAULT 0,
    velocidade_registros_segundo DECIMAL(8,2),
    tempo_estimado_restante INT,
    erros_encontrados TEXT, -- JSON
    relatorio_execucao TEXT,
    checkpoint_dados TEXT, -- JSON
    pode_retomar BOOLEAN DEFAULT FALSE,
    automatica BOOLEAN DEFAULT FALSE,
    agendamento_origem VARCHAR(100),
    executada_por BIGINT,
    observacoes TEXT,

    CONSTRAINT fk_sincronizacoes_integracao FOREIGN KEY (integracao_id) REFERENCES integracoes_ativas(id)
);

CREATE INDEX idx_sincronizacoes_integracao ON sincronizacoes(integracao_id);
CREATE INDEX idx_sincronizacoes_status ON sincronizacoes(status);

COMMENT ON TABLE sincronizacoes IS 'Histórico de sincronizações de dados';
