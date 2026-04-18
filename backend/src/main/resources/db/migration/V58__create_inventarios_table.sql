-- V58__create_inventarios_table.sql
CREATE SEQUENCE inventarios_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE inventarios (
    id BIGINT PRIMARY KEY DEFAULT nextval('inventarios_seq'),
    empresa_id BIGINT NOT NULL,
    codigo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    tipo_inventario VARCHAR(20) CHECK (tipo_inventario IN ('GERAL', 'PARCIAL', 'CICLICO', 'EMERGENCIAL')),
    data_inicio DATE NOT NULL,
    data_fim DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANEJADO' CHECK (status IN ('PLANEJADO', 'EM_ANDAMENTO', 'FINALIZADO', 'CANCELADO')),
    localizacoes_incluidas JSONB,
    categorias_incluidas JSONB,
    produtos_incluidos JSONB,
    usuarios_responsaveis JSONB,
    total_itens_planejados INTEGER,
    total_itens_contados INTEGER,
    total_divergencias INTEGER,
    valor_total_sistema DECIMAL(15,2),
    valor_total_contado DECIMAL(15,2),
    diferenca_valor DECIMAL(15,2),
    observacoes TEXT,
    finalizado_por BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0,
    
    CONSTRAINT uk_inventario_codigo UNIQUE (empresa_id, codigo),
    CONSTRAINT fk_inventario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);

CREATE INDEX idx_inventarios_empresa ON inventarios(empresa_id);
CREATE INDEX idx_inventarios_codigo ON inventarios(codigo);
CREATE INDEX idx_inventarios_status ON inventarios(status);
CREATE INDEX idx_inventarios_data_inicio ON inventarios(data_inicio);
