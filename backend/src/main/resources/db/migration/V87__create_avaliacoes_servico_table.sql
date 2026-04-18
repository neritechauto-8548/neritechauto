-- V87: Create avaliacoes_servico table
CREATE SEQUENCE IF NOT EXISTS seq_avaliacoes_servico START WITH 1 INCREMENT BY 1;

CREATE TABLE avaliacoes_servico (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_avaliacoes_servico'),
    ordem_servico_id BIGINT NOT NULL,
    -- TODO: Uncomment when clientes table is created
    -- cliente_id BIGINT,
    cliente_id BIGINT,
    nota_geral INT NOT NULL,
    nota_atendimento INT,
    nota_qualidade_servico INT,
    nota_prazo_entrega INT,
    nota_preco_justo INT,
    nota_limpeza_organizacao INT,
    comentario_positivo TEXT,
    comentario_negativo TEXT,
    sugestoes_melhoria TEXT,
    recomendaria_oficina BOOLEAN,
    voltaria_oficina BOOLEAN,
    canal_avaliacao VARCHAR(20),
    ip_avaliacao VARCHAR(45),
    token_avaliacao VARCHAR(100),
    data_envio_solicitacao TIMESTAMP,
    data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    respondida_por VARCHAR(255),
    publica BOOLEAN DEFAULT FALSE,
    aprovada_publicacao BOOLEAN DEFAULT FALSE,
    resposta_empresa TEXT,
    data_resposta_empresa TIMESTAMP,
    usuario_resposta_id BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_avaliacoes_servico_os FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico(id) ON DELETE CASCADE,
    -- TODO: Uncomment when clientes table is created
    -- CONSTRAINT fk_avaliacoes_servico_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT chk_avaliacoes_servico_canal CHECK (canal_avaliacao IN ('EMAIL', 'SMS', 'WHATSAPP', 'TELEFONE', 'PRESENCIAL', 'SITE', 'APP')),
    CONSTRAINT chk_avaliacoes_servico_nota_geral CHECK (nota_geral >= 1 AND nota_geral <= 5),
    CONSTRAINT chk_avaliacoes_servico_nota_atendimento CHECK (nota_atendimento IS NULL OR (nota_atendimento >= 1 AND nota_atendimento <= 5)),
    CONSTRAINT chk_avaliacoes_servico_nota_qualidade CHECK (nota_qualidade_servico IS NULL OR (nota_qualidade_servico >= 1 AND nota_qualidade_servico <= 5)),
    CONSTRAINT chk_avaliacoes_servico_nota_prazo CHECK (nota_prazo_entrega IS NULL OR (nota_prazo_entrega >= 1 AND nota_prazo_entrega <= 5)),
    CONSTRAINT chk_avaliacoes_servico_nota_preco CHECK (nota_preco_justo IS NULL OR (nota_preco_justo >= 1 AND nota_preco_justo <= 5)),
    CONSTRAINT chk_avaliacoes_servico_nota_limpeza CHECK (nota_limpeza_organizacao IS NULL OR (nota_limpeza_organizacao >= 1 AND nota_limpeza_organizacao <= 5))
);

CREATE INDEX idx_avaliacoes_servico_os ON avaliacoes_servico(ordem_servico_id);
CREATE INDEX idx_avaliacoes_servico_cliente ON avaliacoes_servico(cliente_id);
CREATE INDEX idx_avaliacoes_servico_nota ON avaliacoes_servico(nota_geral);
CREATE INDEX idx_avaliacoes_servico_publica ON avaliacoes_servico(publica);
COMMENT ON TABLE avaliacoes_servico IS 'Avaliações de clientes sobre os serviços';
