-- Migration: Create equipamentos table
-- Description: Tabela para gerenciamento de equipamentos

CREATE SEQUENCE seq_equipamentos START WITH 1 INCREMENT BY 1;

CREATE TABLE equipamentos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_equipamentos'),
    empresa_id BIGINT NOT NULL,
    codigo_equipamento VARCHAR(20) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    tipo_equipamento VARCHAR(20) NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100),
    ano_fabricacao INT,
    potencia VARCHAR(50),
    voltagem VARCHAR(20),
    consumo_energia_kw DECIMAL(8,2),
    peso_kg DECIMAL(8,2),
    dimensoes VARCHAR(100),
    capacidade_maxima VARCHAR(100),
    data_aquisicao DATE,
    valor_aquisicao DECIMAL(15,2),
    fornecedor_id BIGINT,
    vida_util_anos INT DEFAULT 10,
    valor_depreciacao_anual DECIMAL(10,2),
    valor_residual DECIMAL(10,2),
    localizacao_instalacao VARCHAR(255),
    area_ocupada_m2 DECIMAL(6,2),
    responsavel_operacao_id BIGINT,
    status_operacional VARCHAR(20) NOT NULL,
    condicao_geral VARCHAR(20),
    horas_funcionamento_total INT DEFAULT 0,
    horas_funcionamento_mes INT DEFAULT 0,
    data_ultima_utilizacao TIMESTAMP,
    requer_operador_habilitado BOOLEAN DEFAULT FALSE,
    operadores_habilitados JSONB,
    manual_operacao_url VARCHAR(500),
    manual_manutencao_url VARCHAR(500),
    especificacoes_tecnicas JSONB,
    acessorios_opcionais TEXT,
    consumiveis_necessarios JSONB,
    frequencia_lubrificacao_horas INT,
    tipo_lubrificante VARCHAR(100),
    quantidade_lubrificante VARCHAR(50),
    nivel_ruido_db DECIMAL(5,2),
    certificacoes_seguranca JSONB,
    epi_obrigatorio TEXT,
    instrucoes_seguranca TEXT,
    historico_problemas TEXT,
    melhorias_sugeridas TEXT,
    observacoes_operacao TEXT,
    foto_equipamento_url VARCHAR(500),
    diagrama_eletrico_url VARCHAR(500),
    codigo_patrimonio VARCHAR(30),
    numero_nota_fiscal VARCHAR(50),
    garantia_fabricante_meses INT,
    data_fim_garantia DATE,
    contrato_manutencao_ativo BOOLEAN DEFAULT FALSE,
    empresa_manutencao VARCHAR(255),
    valor_contrato_manutencao DECIMAL(8,2),
    data_inicio_contrato DATE,
    data_fim_contrato DATE,
    seguro_ativo BOOLEAN DEFAULT FALSE,
    numero_apolice_equipamento VARCHAR(50),
    valor_segurado_equipamento DECIMAL(15,2),
    data_vencimento_seguro_equip DATE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cadastrado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INT DEFAULT 0,
    
    CONSTRAINT fk_equipamentos_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_equipamentos_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id),
    CONSTRAINT uk_equipamentos_codigo UNIQUE (empresa_id, codigo_equipamento)
);

CREATE INDEX idx_equipamentos_empresa ON equipamentos(empresa_id);
CREATE INDEX idx_equipamentos_status ON equipamentos(status_operacional);
CREATE INDEX idx_equipamentos_tipo ON equipamentos(tipo_equipamento);
CREATE INDEX idx_equipamentos_responsavel ON equipamentos(responsavel_operacao_id);
CREATE INDEX idx_equipamentos_garantia ON equipamentos(data_fim_garantia) WHERE data_fim_garantia IS NOT NULL;

COMMENT ON TABLE equipamentos IS 'Tabela para gerenciamento de equipamentos';
COMMENT ON COLUMN equipamentos.tipo_equipamento IS 'ELEVADOR, COMPRESSOR, BALANCEADORA, ALINHADOR, SCANNER, SOLDADOR, TORNO, PRENSA, OUTROS';
COMMENT ON COLUMN equipamentos.status_operacional IS 'OPERANTE, PARADO_MANUTENCAO, QUEBRADO, APOSENTADO, EM_INSTALACAO';
COMMENT ON COLUMN equipamentos.condicao_geral IS 'EXCELENTE, BOA, REGULAR, RUIM, PESSIMA';
