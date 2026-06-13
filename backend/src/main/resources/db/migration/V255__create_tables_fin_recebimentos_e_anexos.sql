-- V255: Tabelas para Recebimentos Parciais, Anexos Locais e Renegociações de Contas a Receber

-- Tabela de Recebimentos (Permite parciais, 1 forma de pagamento por registro)
CREATE TABLE fin_recebimentos_titulos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    conta_receber_id BIGINT NOT NULL,
    data_recebimento DATE NOT NULL,
    valor_recebido DECIMAL(10,2) NOT NULL,
    valor_juros DECIMAL(8,2) DEFAULT 0,
    valor_multa DECIMAL(8,2) DEFAULT 0,
    valor_desconto DECIMAL(8,2) DEFAULT 0,
    forma_pagamento_id BIGINT,
    conta_bancaria_id BIGINT,
    observacoes TEXT,
    
    -- Colunas de Auditoria Padronizadas
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,

    CONSTRAINT fk_fin_recebimentos_conta FOREIGN KEY (conta_receber_id) REFERENCES fin_contas_receber(id)
);

CREATE INDEX idx_fin_recebimentos_titulos_empresa ON fin_recebimentos_titulos(empresa_id);
CREATE INDEX idx_fin_recebimentos_titulos_conta ON fin_recebimentos_titulos(conta_receber_id);

-- Tabela de Anexos (Armazenamento Local)
CREATE TABLE fin_anexos_titulos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    conta_receber_id BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    tipo_arquivo VARCHAR(50) NOT NULL,
    tamanho_bytes BIGINT,
    caminho_local VARCHAR(500) NOT NULL,
    observacoes TEXT,
    
    -- Colunas de Auditoria Padronizadas
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,

    CONSTRAINT fk_fin_anexos_titulos_conta FOREIGN KEY (conta_receber_id) REFERENCES fin_contas_receber(id)
);

CREATE INDEX idx_fin_anexos_titulos_empresa ON fin_anexos_titulos(empresa_id);
CREATE INDEX idx_fin_anexos_titulos_conta ON fin_anexos_titulos(conta_receber_id);

-- Tabela de Renegociações (Para rastrear divisões e unificações)
CREATE TABLE fin_renegociacoes (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    titulo_original_id BIGINT NOT NULL,
    novo_titulo_id BIGINT NOT NULL,
    data_renegociacao DATE NOT NULL,
    motivo TEXT,
    
    -- Colunas de Auditoria Padronizadas
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,

    CONSTRAINT fk_fin_renegociacoes_original FOREIGN KEY (titulo_original_id) REFERENCES fin_contas_receber(id),
    CONSTRAINT fk_fin_renegociacoes_novo FOREIGN KEY (novo_titulo_id) REFERENCES fin_contas_receber(id)
);

CREATE INDEX idx_fin_renegociacoes_empresa ON fin_renegociacoes(empresa_id);
CREATE INDEX idx_fin_renegociacoes_original ON fin_renegociacoes(titulo_original_id);
