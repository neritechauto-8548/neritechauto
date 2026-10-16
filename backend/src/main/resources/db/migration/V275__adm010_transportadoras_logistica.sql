-- ADM-010: transportadora é um papel de Fornecedor (mesmo cadastro fiscal e documental).
-- Nunca duplicar pessoa/empresa. Dados de rastreio pertencem ao pedido, não ao cadastro.
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS transportadora BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS modalidade_transporte VARCHAR(20);
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS transportadora_ativa BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE fornecedores ADD CONSTRAINT ck_fornecedores_modalidade_transporte
    CHECK (modalidade_transporte IS NULL OR modalidade_transporte IN ('RODOVIARIO','OUTROS'));

ALTER TABLE pedidos_fornecedor ADD COLUMN IF NOT EXISTS transportadora_id BIGINT;
ALTER TABLE pedidos_fornecedor ADD COLUMN IF NOT EXISTS documento_rastreio VARCHAR(120);
ALTER TABLE pedidos_fornecedor ADD CONSTRAINT fk_pedidos_transportadora
    FOREIGN KEY (transportadora_id) REFERENCES fornecedores(id);
CREATE INDEX IF NOT EXISTS idx_pedidos_transportadora_empresa
    ON pedidos_fornecedor(empresa_id, transportadora_id);

-- As FKs cruzadas de tenant devem ser garantidas também no banco, inclusive para
-- inserções externas à API. Índices reutilizáveis para chaves compostas.
CREATE UNIQUE INDEX IF NOT EXISTS ux_fornecedores_id_empresa ON fornecedores(id, empresa_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_pedidos_id_empresa ON pedidos_fornecedor(id, empresa_id);
ALTER TABLE pedidos_fornecedor ADD CONSTRAINT fk_pedidos_transportadora_tenant
    FOREIGN KEY (transportadora_id, empresa_id) REFERENCES fornecedores(id, empresa_id);
