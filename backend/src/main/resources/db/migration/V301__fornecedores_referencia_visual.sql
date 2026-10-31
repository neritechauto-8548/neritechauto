-- Fornecedores: complemento visual sem perda das dependências de compras e transportadoras.
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS logradouro VARCHAR(255);
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS numero VARCHAR(30);
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS bairro VARCHAR(120);
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS complemento VARCHAR(255);
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS plano_contas_id BIGINT;
ALTER TABLE fornecedores ADD COLUMN IF NOT EXISTS linha_pecas VARCHAR(255);
ALTER TABLE fornecedores ALTER COLUMN classificacao TYPE VARCHAR(20);
ALTER TABLE fornecedores ADD CONSTRAINT fk_fornecedores_plano_contas FOREIGN KEY (plano_contas_id) REFERENCES fin_plano_contas(id);
-- Preserve 'endereco_completo': consumidores fiscais antigos ainda dependem dele.
CREATE INDEX IF NOT EXISTS idx_fornecedores_empresa_pessoa ON fornecedores(empresa_id,tipo_pessoa,ativo);

CREATE TABLE IF NOT EXISTS contatos_fornecedor (
 id BIGSERIAL PRIMARY KEY,
 empresa_id BIGINT NOT NULL REFERENCES empresa(id),
 fornecedor_id BIGINT NOT NULL REFERENCES fornecedores(id),
 nome VARCHAR(255) NOT NULL,
 sexo VARCHAR(20),
 data_nascimento DATE,
 contato_principal BOOLEAN NOT NULL DEFAULT FALSE,
 ativo BOOLEAN NOT NULL DEFAULT TRUE,
 data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 data_atualizacao TIMESTAMP,
 versao INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT ck_nome_contato_fornecedor CHECK (length(btrim(nome)) > 0),
 CONSTRAINT ck_sexo_contato_fornecedor CHECK (sexo IS NULL OR sexo IN ('MASCULINO','FEMININO','NAO_INFORMADO'))
);
CREATE INDEX IF NOT EXISTS idx_contatos_fornecedor_tenant ON contatos_fornecedor(empresa_id, fornecedor_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_contato_principal_fornecedor ON contatos_fornecedor(empresa_id, fornecedor_id) WHERE contato_principal AND ativo;

CREATE TABLE IF NOT EXISTS meios_contato_fornecedor (
 id BIGSERIAL PRIMARY KEY,
 empresa_id BIGINT NOT NULL REFERENCES empresa(id),
 contato_id BIGINT NOT NULL REFERENCES contatos_fornecedor(id) ON DELETE CASCADE,
 tipo VARCHAR(20) NOT NULL,
 valor VARCHAR(255) NOT NULL,
 whatsapp BOOLEAN NOT NULL DEFAULT FALSE,
 versao INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT ck_meio_contato_tipo CHECK (tipo IN ('EMAIL','CELULAR','TELEFONE')),
 CONSTRAINT ck_meio_contato_valor CHECK (length(btrim(valor)) > 0)
);
CREATE INDEX IF NOT EXISTS idx_meios_contato_fornecedor ON meios_contato_fornecedor(empresa_id,contato_id);

-- Atribuição financeira deve ser explícita: NÃO deduzir crédito de uma conta a pagar.
CREATE TABLE IF NOT EXISTS creditos_fornecedor (
 id BIGSERIAL PRIMARY KEY,
 empresa_id BIGINT NOT NULL REFERENCES empresa(id),
 fornecedor_id BIGINT NOT NULL REFERENCES fornecedores(id),
 pedido_id BIGINT REFERENCES pedidos_fornecedor(id),
 numero_documento VARCHAR(100),
 lancamento_referencia VARCHAR(100),
 conta_id BIGINT REFERENCES fin_plano_contas(id),
 valor NUMERIC(12,2) NOT NULL,
 data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT ck_credito_fornecedor_valor CHECK (valor >= 0)
);
CREATE INDEX IF NOT EXISTS idx_creditos_fornecedor_empresa ON creditos_fornecedor(empresa_id,fornecedor_id);

CREATE TABLE IF NOT EXISTS itens_devolvidos_fornecedor (
 id BIGSERIAL PRIMARY KEY,
 empresa_id BIGINT NOT NULL REFERENCES empresa(id),
 fornecedor_id BIGINT NOT NULL REFERENCES fornecedores(id),
 pedido_id BIGINT NOT NULL REFERENCES pedidos_fornecedor(id),
 produto_id BIGINT NOT NULL REFERENCES produtos(id),
 chave_nfe VARCHAR(44),
 quantidade NUMERIC(12,2) NOT NULL,
 valor NUMERIC(12,2) NOT NULL,
 data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT ck_devolucao_fornecedor_qtd CHECK (quantidade > 0),
 CONSTRAINT ck_devolucao_fornecedor_valor CHECK (valor >= 0)
);
CREATE INDEX IF NOT EXISTS idx_itens_devolvidos_fornecedor ON itens_devolvidos_fornecedor(empresa_id,fornecedor_id,pedido_id);
