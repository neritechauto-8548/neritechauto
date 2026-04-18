-- V147: Create parametros_relatorios table
-- Description: Parameters definition for reports

CREATE SEQUENCE IF NOT EXISTS seq_parametros_relatorios START WITH 1 INCREMENT BY 1;

CREATE TABLE parametros_relatorios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_parametros_relatorios'),
    relatorio_id BIGINT NOT NULL,
    nome_parametro VARCHAR(100) NOT NULL,
    tipo_parametro VARCHAR(20) CHECK (tipo_parametro IN ('STRING', 'INTEGER', 'DECIMAL', 'DATE', 'DATETIME', 'BOOLEAN', 'LIST', 'MULTISELECT')),
    obrigatorio BOOLEAN DEFAULT FALSE,
    valor_padrao TEXT,
    opcoes_lista TEXT, -- JSON
    validacao_regex VARCHAR(255),
    mensagem_erro VARCHAR(255),
    ordem_exibicao INT DEFAULT 0,
    grupo_parametro VARCHAR(50),
    dependente_de BIGINT,
    condicao_dependencia TEXT, -- JSON
    ajuda_usuario TEXT,
    ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_parametros_relatorios_relatorio FOREIGN KEY (relatorio_id) REFERENCES relatorios_salvos(id),
    CONSTRAINT fk_parametros_relatorios_dependencia FOREIGN KEY (dependente_de) REFERENCES parametros_relatorios(id)
);

CREATE INDEX idx_parametros_relatorios_relatorio ON parametros_relatorios(relatorio_id);

COMMENT ON TABLE parametros_relatorios IS 'Definição de parâmetros para execução dos relatórios';
COMMENT ON COLUMN parametros_relatorios.opcoes_lista IS 'JSON com opções para tipos LIST/MULTISELECT';
