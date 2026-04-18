-- V86: Create observacoes_os table
CREATE SEQUENCE IF NOT EXISTS seq_observacoes_os START WITH 1 INCREMENT BY 1;

CREATE TABLE observacoes_os (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_observacoes_os'),
    ordem_servico_id BIGINT NOT NULL,
    tipo_observacao VARCHAR(20),
    titulo VARCHAR(255),
    conteudo TEXT NOT NULL,
    prioridade VARCHAR(20),
    visibilidade VARCHAR(20),
    usuarios_acesso JSONB,
    anexos JSONB,
    data_validade DATE,
    ativo BOOLEAN DEFAULT TRUE,
    fixar_topo BOOLEAN DEFAULT FALSE,
    notificar_equipe BOOLEAN DEFAULT FALSE,
    usuario_criacao_id BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_observacoes_os_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    CONSTRAINT chk_observacoes_os_tipo CHECK (tipo_observacao IN ('TECNICA', 'COMERCIAL', 'ATENDIMENTO', 'GERAL', 'INTERNA', 'CLIENTE')),
    CONSTRAINT chk_observacoes_os_prioridade CHECK (prioridade IN ('BAIXA', 'MEDIA', 'ALTA', 'URGENTE')),
    CONSTRAINT chk_observacoes_os_visibilidade CHECK (visibilidade IN ('PUBLICA', 'INTERNA', 'RESTRITA'))
);

CREATE INDEX idx_observacoes_os_os ON observacoes_os(ordem_servico_id);
CREATE INDEX idx_observacoes_os_tipo ON observacoes_os(tipo_observacao);
CREATE INDEX idx_observacoes_os_usuario ON observacoes_os(usuario_criacao_id);
COMMENT ON TABLE observacoes_os IS 'Observações das ordens de serviço';
