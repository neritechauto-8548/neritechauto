-- V51__create_cotacoes_table.sql
CREATE SEQUENCE cotacoes_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE cotacoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('cotacoes_seq'),
    empresa_id BIGINT NOT NULL,
    numero_cotacao VARCHAR(30) NOT NULL,
    tipo_cotacao VARCHAR(20) NOT NULL CHECK (tipo_cotacao IN ('PRODUTOS', 'SERVICOS', 'MISTA')),
    descricao_geral TEXT,
    data_solicitacao DATE NOT NULL,
    data_limite_resposta DATE NOT NULL,
    prazo_entrega_desejado DATE,
    local_entrega TEXT,
    condicoes_pagamento_desejadas TEXT,
    observacoes_gerais TEXT,
    criterios_avaliacao JSONB,
    peso_preco INTEGER DEFAULT 40,
    peso_prazo INTEGER DEFAULT 20,
    peso_qualidade INTEGER DEFAULT 20,
    peso_condicoes INTEGER DEFAULT 20,
    status VARCHAR(20) NOT NULL DEFAULT 'RASCUNHO' CHECK (status IN ('RASCUNHO', 'ENVIADA', 'EM_ANALISE', 'FINALIZADA', 'CANCELADA')),
    total_fornecedores_convidados INTEGER DEFAULT 0,
    total_respostas_recebidas INTEGER DEFAULT 0,
    melhor_cotacao_id BIGINT,
    cotacao_vencedora_id BIGINT,
    motivo_escolha TEXT,
    economia_obtida DECIMAL(10,2),
    percentual_economia DECIMAL(5,2),
    responsavel_cotacao_id BIGINT NOT NULL,
    aprovador_id BIGINT,
    data_aprovacao TIMESTAMP,
    observacoes_aprovacao TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT uk_cotacao_numero UNIQUE (empresa_id, numero_cotacao),
    CONSTRAINT fk_cotacao_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_cotacao_melhor FOREIGN KEY (melhor_cotacao_id) REFERENCES cotacoes(id),
    CONSTRAINT fk_cotacao_vencedora FOREIGN KEY (cotacao_vencedora_id) REFERENCES cotacoes(id)
);

CREATE INDEX idx_cotacoes_empresa ON cotacoes(empresa_id);
CREATE INDEX idx_cotacoes_numero ON cotacoes(numero_cotacao);
CREATE INDEX idx_cotacoes_status ON cotacoes(status);
CREATE INDEX idx_cotacoes_data_solicitacao ON cotacoes(data_solicitacao);
CREATE INDEX idx_cotacoes_responsavel ON cotacoes(responsavel_cotacao_id);
