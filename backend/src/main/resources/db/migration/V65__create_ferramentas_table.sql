-- Migration: Create ferramentas table
-- Description: Tabela para gerenciamento de ferramentas

CREATE SEQUENCE seq_ferramentas START WITH 1 INCREMENT BY 1;

CREATE TABLE ferramentas (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_ferramentas'),
    empresa_id BIGINT NOT NULL,
    codigo_ferramenta VARCHAR(20) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    tipo_ferramenta VARCHAR(20) NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    data_aquisicao DATE,
    valor_aquisicao DECIMAL(10,2),
    fornecedor_id BIGINT,
    vida_util_anos INT,
    valor_depreciacao_anual DECIMAL(8,2),
    valor_atual DECIMAL(10,2),
    localizacao_atual VARCHAR(255),
    responsavel_atual_id BIGINT,
    status VARCHAR(20) NOT NULL,
    condicao VARCHAR(20),
    requer_calibracao BOOLEAN DEFAULT FALSE,
    frequencia_calibracao_meses INT,
    data_ultima_calibracao DATE,
    data_proxima_calibracao DATE,
    certificado_calibracao VARCHAR(500),
    requer_manutencao_preventiva BOOLEAN DEFAULT TRUE,
    frequencia_manutencao_horas INT,
    horas_uso_total INT DEFAULT 0,
    horas_ultima_manutencao INT DEFAULT 0,
    proxima_manutencao_horas INT,
    manual_operacao_url VARCHAR(500),
    especificacoes_tecnicas JSONB,
    acessorios_inclusos TEXT,
    observacoes TEXT,
    foto_ferramenta_url VARCHAR(500),
    codigo_barras VARCHAR(50),
    rfid_tag VARCHAR(50),
    seguro_ativo BOOLEAN DEFAULT FALSE,
    numero_apolice_seguro VARCHAR(50),
    data_vencimento_seguro DATE,
    valor_segurado DECIMAL(10,2),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_ferramentas_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_ferramentas_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT uk_ferramentas_codigo UNIQUE (empresa_id, codigo_ferramenta)
);

CREATE INDEX idx_ferramentas_empresa ON ferramentas(empresa_id);
CREATE INDEX idx_ferramentas_status ON ferramentas(status);
CREATE INDEX idx_ferramentas_tipo ON ferramentas(tipo_ferramenta);
CREATE INDEX idx_ferramentas_responsavel ON ferramentas(responsavel_atual_id);
CREATE INDEX idx_ferramentas_calibracao ON ferramentas(data_proxima_calibracao) WHERE requer_calibracao = TRUE;

COMMENT ON TABLE ferramentas IS 'Tabela para gerenciamento de ferramentas';
COMMENT ON COLUMN ferramentas.tipo_ferramenta IS 'MANUAL, ELETRICA, PNEUMATICA, HIDRAULICA, MEDICAO, OUTROS';
COMMENT ON COLUMN ferramentas.status IS 'DISPONIVEL, EM_USO, MANUTENCAO, QUEBRADA, EMPRESTADA, DESCARTADA';
COMMENT ON COLUMN ferramentas.condicao IS 'NOVA, OTIMA, BOA, REGULAR, RUIM, INUTILIZAVEL';
