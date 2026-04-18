-- V104: Create documentos_funcionarios table
-- Description: Employee documents management

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_documentos_funcionarios START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE documentos_funcionarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_documentos_funcionarios'),
    funcionario_id BIGINT NOT NULL,
    tipo_documento VARCHAR(30),
    nome_documento VARCHAR(255) NOT NULL,
    numero_documento VARCHAR(100),
    orgao_emissor VARCHAR(100),
    data_emissao DATE,
    data_validade DATE,
    arquivo_url VARCHAR(500),
    arquivo_nome VARCHAR(255),
    arquivo_tamanho BIGINT,
    verificado BOOLEAN DEFAULT FALSE,
    data_verificacao TIMESTAMP,
    verificado_por BIGINT,
    obrigatorio BOOLEAN DEFAULT FALSE,
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_documentos_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_documentos_tipo CHECK (tipo_documento IN ('CPF', 'RG', 'CNH', 'TITULO_ELEITOR', 'PIS_PASEP', 'CTPS', 'CERTIFICADO_RESERVISTA', 'CERTIDAO_NASCIMENTO', 'CERTIDAO_CASAMENTO', 'COMPROVANTE_RESIDENCIA', 'DIPLOMA', 'CERTIFICADO', 'ATESTADO_MEDICO', 'CONTRATO_TRABALHO', 'OUTROS'))
);

-- Create indexes
CREATE INDEX idx_documentos_funcionario ON documentos_funcionarios(funcionario_id);
CREATE INDEX idx_documentos_tipo ON documentos_funcionarios(tipo_documento);
CREATE INDEX idx_documentos_verificado ON documentos_funcionarios(verificado);
CREATE INDEX idx_documentos_validade ON documentos_funcionarios(data_validade);

-- Comments
COMMENT ON TABLE documentos_funcionarios IS 'Documentos dos funcionários';
COMMENT ON COLUMN documentos_funcionarios.tipo_documento IS 'Tipo: CPF, RG, CNH, TITULO_ELEITOR, PIS_PASEP, CTPS, etc.';
COMMENT ON COLUMN documentos_funcionarios.verificado IS 'Se o documento foi verificado pelo RH';
