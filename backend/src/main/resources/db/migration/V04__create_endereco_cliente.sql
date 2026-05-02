
-- 3. Create enderecos_clientes
-- Based on EnderecoCliente.java entity mapping
CREATE TABLE IF NOT EXISTS enderecos_clientes (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    pais VARCHAR(50) DEFAULT 'Brasil',
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT fk_enderecos_clientes_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_enderecos_clientes_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);