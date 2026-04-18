-- Migration: Create calibracoes table
-- Description: Tabela para registro de calibrações de equipamentos e ferramentas

CREATE SEQUENCE seq_calibracoes START WITH 1 INCREMENT BY 1;

CREATE TABLE calibracoes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_calibracoes'),
    equipamento_id BIGINT,
    ferramenta_id BIGINT,
    tipo_calibracao VARCHAR(20) NOT NULL,
    entidade_calibradora VARCHAR(255),
    numero_certificado VARCHAR(100),
    norma_aplicada VARCHAR(100),
    data_calibracao DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    resultado VARCHAR(30),
    tolerancia_especificada VARCHAR(100),
    tolerancia_encontrada VARCHAR(100),
    erro_maximo_encontrado VARCHAR(100),
    incerteza_medicao VARCHAR(100),
    pontos_calibrados INT,
    temperatura_ambiente DECIMAL(4,1),
    umidade_ambiente DECIMAL(5,2),
    responsavel_calibracao VARCHAR(255),
    observacoes_resultado TEXT,
    acoes_corretivas TEXT,
    restricoes_uso TEXT,
    custo_calibracao DECIMAL(8,2),
    arquivo_certificado_url VARCHAR(500),
    proxima_calibracao DATE,
    alerta_vencimento_dias INT DEFAULT 30,
    status_calibracao VARCHAR(20),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registrado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_calibracoes_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamentos(id),
    CONSTRAINT fk_calibracoes_ferramenta FOREIGN KEY (ferramenta_id) REFERENCES ferramentas(id),
    CONSTRAINT chk_calibracao_item CHECK (
        (equipamento_id IS NOT NULL AND ferramenta_id IS NULL) OR
        (equipamento_id IS NULL AND ferramenta_id IS NOT NULL)
    )
);

CREATE INDEX idx_calibracoes_equipamento ON calibracoes(equipamento_id);
CREATE INDEX idx_calibracoes_ferramenta ON calibracoes(ferramenta_id);
CREATE INDEX idx_calibracoes_vencimento ON calibracoes(data_vencimento);
CREATE INDEX idx_calibracoes_status ON calibracoes(status_calibracao);

COMMENT ON TABLE calibracoes IS 'Tabela para registro de calibrações de equipamentos e ferramentas';
COMMENT ON COLUMN calibracoes.tipo_calibracao IS 'INTERNA, EXTERNA, CERTIFICADA';
COMMENT ON COLUMN calibracoes.resultado IS 'APROVADO, APROVADO_COM_RESTRICOES, REPROVADO';
COMMENT ON COLUMN calibracoes.status_calibracao IS 'VALIDA, VENCIDA, SUSPENSA';
