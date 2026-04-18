-- Migração V140: Tabela de Listas de Contatos
-- Autor: Sistema NeriTech
-- Data: 2025-11-21
-- Descrição: Listas de contatos para campanhas

-- Criar sequence para o ID
CREATE SEQUENCE IF NOT EXISTS seq_com_listas_contatos_id START WITH 1 INCREMENT BY 1;

-- Criar tabela
CREATE TABLE com_listas_contatos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_com_listas_contatos_id'),
    empresa_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    tipo_lista VARCHAR(30) NOT NULL CHECK (tipo_lista IN ('ESTATICA', 'DINAMICA', 'IMPORTADA')),
    criterios_segmentacao TEXT,
    total_contatos INT DEFAULT 0,
    contatos_ativos INT DEFAULT 0,
    data_ultima_atualizacao TIMESTAMP,
    frequencia_atualizacao VARCHAR(30) CHECK (frequencia_atualizacao IN ('MANUAL', 'DIARIA', 'SEMANAL', 'MENSAL')),
    proxima_atualizacao TIMESTAMP,
    tags VARCHAR(255),
    privada BOOLEAN DEFAULT FALSE,
    compartilhada_com TEXT,
    origem_lista VARCHAR(100),
    ativa BOOLEAN DEFAULT TRUE,
    criada_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índices
CREATE INDEX idx_com_listas_empresa ON com_listas_contatos(empresa_id);
CREATE INDEX idx_com_listas_tipo ON com_listas_contatos(tipo_lista);
CREATE INDEX idx_com_listas_ativa ON com_listas_contatos(ativa);

-- Comentários
COMMENT ON TABLE com_listas_contatos IS 'Listas de contatos para campanhas de marketing';
COMMENT ON COLUMN com_listas_contatos.criterios_segmentacao IS 'JSON com critérios de segmentação';
COMMENT ON COLUMN com_listas_contatos.compartilhada_com IS 'JSON com usuários que têm acesso';
