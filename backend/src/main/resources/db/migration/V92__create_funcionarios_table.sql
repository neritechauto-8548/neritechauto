-- V92: Create funcionarios table
-- Description: Employees/staff table with comprehensive HR data

-- Create sequence
CREATE SEQUENCE IF NOT EXISTS seq_funcionarios START WITH 1 INCREMENT BY 1;

-- Create table
CREATE TABLE funcionarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_funcionarios'),
    empresa_id BIGINT NOT NULL,
    -- TODO: Uncomment when usuarios table is created
    -- usuario_id BIGINT,
    usuario_id BIGINT,
    matricula VARCHAR(20) NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    rg VARCHAR(20),
    data_nascimento DATE,
    sexo VARCHAR(1),
    estado_civil VARCHAR(20),
    nacionalidade VARCHAR(50) DEFAULT 'Brasileira',
    naturalidade VARCHAR(100),
    nome_mae VARCHAR(255),
    nome_pai VARCHAR(255),
    escolaridade VARCHAR(30),
    profissao VARCHAR(100),
    cargo_id BIGINT,
    departamento_id BIGINT,
    data_admissao DATE NOT NULL,
    data_demissao DATE,
    tipo_contrato VARCHAR(20),
    salario_base DECIMAL(10,2),
    comissao_percentual DECIMAL(5,2) DEFAULT 0,
    vale_transporte DECIMAL(8,2) DEFAULT 0,
    vale_alimentacao DECIMAL(8,2) DEFAULT 0,
    plano_saude BOOLEAN DEFAULT FALSE,
    plano_odontologico BOOLEAN DEFAULT FALSE,
    status VARCHAR(20),
    motivo_inativacao TEXT,
    endereco_completo TEXT,
    telefone_principal VARCHAR(20),
    telefone_emergencia VARCHAR(20),
    contato_emergencia_nome VARCHAR(255),
    contato_emergencia_parentesco VARCHAR(50),
    email_pessoal VARCHAR(255),
    banco_codigo VARCHAR(5),
    banco_agencia VARCHAR(10),
    banco_conta VARCHAR(20),
    banco_tipo_conta VARCHAR(10),
    pis_pasep VARCHAR(15),
    titulo_eleitor VARCHAR(15),
    carteira_trabalho VARCHAR(20),
    certificado_reservista VARCHAR(20),
    cnh_numero VARCHAR(15),
    cnh_categoria VARCHAR(5),
    cnh_validade DATE,
    foto_funcionario_url VARCHAR(500),
    observacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    -- Constraints
    CONSTRAINT fk_funcionarios_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    -- TODO: Uncomment when usuarios table is created
    -- CONSTRAINT fk_funcionarios_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_funcionarios_cargo FOREIGN KEY (cargo_id) REFERENCES cargos(id),
    CONSTRAINT fk_funcionarios_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos(id),
    CONSTRAINT uk_funcionarios_matricula UNIQUE (empresa_id, matricula),
    CONSTRAINT uk_funcionarios_cpf UNIQUE (cpf),
    CONSTRAINT chk_funcionarios_sexo CHECK (sexo IN ('M', 'F', 'O')),
    CONSTRAINT chk_funcionarios_estado_civil CHECK (estado_civil IN ('SOLTEIRO', 'CASADO', 'DIVORCIADO', 'VIUVO', 'UNIAO_ESTAVEL')),
    CONSTRAINT chk_funcionarios_escolaridade CHECK (escolaridade IN ('FUNDAMENTAL_INCOMPLETO', 'FUNDAMENTAL_COMPLETO', 'MEDIO_INCOMPLETO', 'MEDIO_COMPLETO', 'SUPERIOR_INCOMPLETO', 'SUPERIOR_COMPLETO', 'POS_GRADUACAO', 'MESTRADO', 'DOUTORADO')),
    CONSTRAINT chk_funcionarios_tipo_contrato CHECK (tipo_contrato IN ('CLT', 'ESTAGIARIO', 'TERCEIRIZADO', 'PJ', 'TEMPORARIO', 'FREELANCER')),
    CONSTRAINT chk_funcionarios_status CHECK (status IN ('ATIVO', 'INATIVO', 'AFASTADO', 'DEMITIDO', 'APOSENTADO')),
    CONSTRAINT chk_funcionarios_tipo_conta CHECK (banco_tipo_conta IN ('CORRENTE', 'POUPANCA'))
);

-- Create indexes
CREATE INDEX idx_funcionarios_empresa ON funcionarios(empresa_id);
CREATE INDEX idx_funcionarios_usuario ON funcionarios(usuario_id);
CREATE INDEX idx_funcionarios_cargo ON funcionarios(cargo_id);
CREATE INDEX idx_funcionarios_departamento ON funcionarios(departamento_id);
CREATE INDEX idx_funcionarios_status ON funcionarios(status);
CREATE INDEX idx_funcionarios_cpf ON funcionarios(cpf);
CREATE INDEX idx_funcionarios_matricula ON funcionarios(matricula);
CREATE INDEX idx_funcionarios_nome ON funcionarios(nome_completo);
CREATE INDEX idx_funcionarios_data_admissao ON funcionarios(data_admissao);

-- Comments
COMMENT ON TABLE funcionarios IS 'Funcionários da empresa com dados completos de RH';
COMMENT ON COLUMN funcionarios.matricula IS 'Matrícula única do funcionário na empresa';
COMMENT ON COLUMN funcionarios.tipo_contrato IS 'Tipo de contrato: CLT, ESTAGIARIO, TERCEIRIZADO, PJ, TEMPORARIO, FREELANCER';
COMMENT ON COLUMN funcionarios.status IS 'Status: ATIVO, INATIVO, AFASTADO, DEMITIDO, APOSENTADO';
COMMENT ON COLUMN funcionarios.pis_pasep IS 'Número do PIS/PASEP';
