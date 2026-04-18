-- V158: Create cst_produtos table
-- Description: Tax Situation Codes for products

CREATE SEQUENCE IF NOT EXISTS seq_cst_produtos START WITH 1 INCREMENT BY 1;

CREATE TABLE cst_produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_cst_produtos'),
    produto_id BIGINT,
    cst_icms VARCHAR(3) NOT NULL,
    cst_pis VARCHAR(2) NOT NULL,
    cst_cofins VARCHAR(2) NOT NULL,
    cst_ipi VARCHAR(2),
    origem_produto VARCHAR(1) CHECK (origem_produto IN ('0', '1', '2', '3', '4', '5', '6', '7', '8')),
    modalidade_bc_icms VARCHAR(1) CHECK (modalidade_bc_icms IN ('0', '1', '2', '3')),
    reducao_bc_icms DECIMAL(5,2) DEFAULT 0,
    aliquota_icms DECIMAL(5,2),
    modalidade_bc_st VARCHAR(1) CHECK (modalidade_bc_st IN ('0', '1', '2', '3', '4', '5')),
    margem_st DECIMAL(5,2),
    reducao_bc_st DECIMAL(5,2),
    aliquota_st DECIMAL(5,2),
    aliquota_pis DECIMAL(8,4),
    aliquota_cofins DECIMAL(8,4),
    codigo_anp VARCHAR(9),
    descricao_anp VARCHAR(95),
    percentual_glp DECIMAL(5,2),
    percentual_gnn DECIMAL(5,2),
    percentual_gni DECIMAL(5,2),
    valor_partida DECIMAL(15,2),
    observacoes TEXT,
    data_vigencia_inicio DATE NOT NULL,
    data_vigencia_fim DATE,
    ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_cst_produtos_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

CREATE INDEX idx_cst_produtos_produto ON cst_produtos(produto_id);

COMMENT ON TABLE cst_produtos IS 'Código de Situação Tributária (CST) por produto';
