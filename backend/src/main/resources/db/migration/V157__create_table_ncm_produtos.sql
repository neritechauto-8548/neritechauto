-- V157: Create ncm_produtos table
-- Description: Mercosul Common Nomenclature

CREATE SEQUENCE IF NOT EXISTS seq_ncm_produtos START WITH 1 INCREMENT BY 1;

CREATE TABLE ncm_produtos (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_ncm_produtos'),
    codigo_ncm VARCHAR(8) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    unidade_estatistica VARCHAR(10),
    aliquota_ipi_nacional DECIMAL(5,2),
    aliquota_ipi_importacao DECIMAL(5,2),
    ex_tipi VARCHAR(3),
    tabela_incidencia VARCHAR(2),
    inicio_vigencia DATE NOT NULL,
    fim_vigencia DATE,
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT uk_ncm_produtos_codigo UNIQUE (codigo_ncm)
);

CREATE INDEX idx_ncm_produtos_codigo ON ncm_produtos(codigo_ncm);

COMMENT ON TABLE ncm_produtos IS 'Nomenclatura Comum do Mercosul';
