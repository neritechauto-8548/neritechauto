-- V53__create_respostas_cotacao_table.sql
CREATE SEQUENCE respostas_cotacao_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE respostas_cotacao (
    id BIGINT PRIMARY KEY DEFAULT nextval('respostas_cotacao_seq'),
    cotacao_id BIGINT NOT NULL,
    fornecedor_id BIGINT NOT NULL,
    data_resposta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_limite_validade DATE,
    condicoes_pagamento_oferecidas TEXT,
    prazo_entrega_oferecido DATE,
    observacoes_comerciais TEXT,
    valor_total_cotado DECIMAL(12,2) NOT NULL,
    valor_frete DECIMAL(8,2) DEFAULT 0,
    valor_impostos DECIMAL(8,2) DEFAULT 0,
    valor_desconto DECIMAL(8,2) DEFAULT 0,
    valor_final DECIMAL(12,2) NOT NULL,
    garantia_oferecida TEXT,
    certificacoes_apresentadas TEXT,
    tempo_entrega_dias INTEGER,
    forma_entrega VARCHAR(100),
    responsabilidade_frete VARCHAR(20) CHECK (responsabilidade_frete IN ('FORNECEDOR', 'COMPRADOR', 'COMPARTILHADO')),
    contato_comercial_nome VARCHAR(255),
    contato_comercial_telefone VARCHAR(20),
    contato_comercial_email VARCHAR(255),
    anexos_proposta JSONB,
    observacoes_tecnicas TEXT,
    alternativas_propostas TEXT,
    status_avaliacao VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status_avaliacao IN ('PENDENTE', 'EM_ANALISE', 'APROVADA', 'REJEITADA', 'CLASSIFICADA')),
    pontuacao_total DECIMAL(6,2),
    pontuacao_preco DECIMAL(6,2),
    pontuacao_prazo DECIMAL(6,2),
    pontuacao_qualidade DECIMAL(6,2),
    pontuacao_condicoes DECIMAL(6,2),
    classificacao_final INTEGER,
    observacoes_avaliacao TEXT,
    selecionada BOOLEAN DEFAULT FALSE,
    motivo_selecao TEXT,
    motivo_rejeicao TEXT,
    avaliada_por BIGINT,
    data_avaliacao TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT fk_resposta_cotacao FOREIGN KEY (cotacao_id) REFERENCES cotacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_resposta_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE INDEX idx_respostas_cotacao_cotacao ON respostas_cotacao(cotacao_id);
CREATE INDEX idx_respostas_cotacao_fornecedor ON respostas_cotacao(fornecedor_id);
CREATE INDEX idx_respostas_cotacao_status ON respostas_cotacao(status_avaliacao);
CREATE INDEX idx_respostas_cotacao_selecionada ON respostas_cotacao(selecionada);
CREATE INDEX idx_respostas_cotacao_classificacao ON respostas_cotacao(cotacao_id, classificacao_final);
