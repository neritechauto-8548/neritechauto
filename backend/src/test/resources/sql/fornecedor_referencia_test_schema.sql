-- Schema complementar dos testes com Flyway desativado (Hibernate create-drop).
CREATE TABLE IF NOT EXISTS contatos_fornecedor(
 id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL,
 fornecedor_id BIGINT NOT NULL, nome VARCHAR(255) NOT NULL,
 sexo VARCHAR(20), data_nascimento DATE, contato_principal BOOLEAN NOT NULL DEFAULT FALSE,
 ativo BOOLEAN NOT NULL DEFAULT TRUE,versao INTEGER NOT NULL DEFAULT 0,
 data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,data_atualizacao TIMESTAMP
);
CREATE TABLE IF NOT EXISTS meios_contato_fornecedor(
 id BIGSERIAL PRIMARY KEY,empresa_id BIGINT NOT NULL,
 contato_id BIGINT NOT NULL,tipo VARCHAR(20) NOT NULL,
 valor VARCHAR(255) NOT NULL,whatsapp BOOLEAN NOT NULL DEFAULT FALSE,versao INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS creditos_fornecedor(
 id BIGSERIAL PRIMARY KEY,empresa_id BIGINT NOT NULL,fornecedor_id BIGINT NOT NULL,
 pedido_id BIGINT,numero_documento VARCHAR(100),lancamento_referencia VARCHAR(100),
 conta_id BIGINT,valor NUMERIC(12,2) NOT NULL,data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS itens_devolvidos_fornecedor(
 id BIGSERIAL PRIMARY KEY,empresa_id BIGINT NOT NULL,fornecedor_id BIGINT NOT NULL,
 pedido_id BIGINT,produto_id BIGINT,chave_nfe VARCHAR(44),
 quantidade NUMERIC(12,2) NOT NULL,valor NUMERIC(12,2) NOT NULL,
 data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
