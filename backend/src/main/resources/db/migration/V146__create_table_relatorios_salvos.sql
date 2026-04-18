-- V146: Create relatorios_salvos table
-- Description: Stores saved report configurations and definitions

CREATE SEQUENCE IF NOT EXISTS seq_relatorios_salvos START WITH 1 INCREMENT BY 1;

CREATE TABLE relatorios_salvos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_relatorios_salvos'),
    empresa_id BIGINT NOT NULL,
    nome_relatorio VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo_relatorio VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    consulta_sql TEXT, -- Changed to TEXT as LONGTEXT is not standard PostgreSQL (TEXT is sufficient)
    parametros_relatorio TEXT, -- JSON
    campos_exibidos TEXT, -- JSON
    ordenacao_padrao VARCHAR(255),
    filtros_padrao TEXT, -- JSON
    formato_saida VARCHAR(20) CHECK (formato_saida IN ('PDF', 'EXCEL', 'CSV', 'HTML', 'JSON')),
    orientacao_pagina VARCHAR(20) CHECK (orientacao_pagina IN ('RETRATO', 'PAISAGEM')),
    template_layout TEXT,
    agrupamentos TEXT, -- JSON
    totalizadores TEXT, -- JSON
    graficos_inclusos TEXT, -- JSON
    publico BOOLEAN DEFAULT FALSE,
    compartilhado_com TEXT, -- JSON
    agendamento_ativo BOOLEAN DEFAULT FALSE,
    frequencia_agendamento VARCHAR(20) CHECK (frequencia_agendamento IN ('DIARIO', 'SEMANAL', 'MENSAL', 'TRIMESTRAL')),
    proximo_envio TIMESTAMP,
    emails_envio TEXT, -- JSON
    pasta_destino VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    total_execucoes INT DEFAULT 0,
    data_ultima_execucao TIMESTAMP,
    tempo_medio_execucao INT,
    tamanho_medio_arquivo BIGINT,
    criado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_relatorios_salvos_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_relatorios_salvos_empresa ON relatorios_salvos(empresa_id);
CREATE INDEX idx_relatorios_salvos_categoria ON relatorios_salvos(categoria);
CREATE INDEX idx_relatorios_salvos_tipo ON relatorios_salvos(tipo_relatorio);

COMMENT ON TABLE relatorios_salvos IS 'Configurações de relatórios salvos e personalizados';
COMMENT ON COLUMN relatorios_salvos.parametros_relatorio IS 'JSON com definição dos parâmetros';
COMMENT ON COLUMN relatorios_salvos.campos_exibidos IS 'JSON com lista de campos a exibir';
