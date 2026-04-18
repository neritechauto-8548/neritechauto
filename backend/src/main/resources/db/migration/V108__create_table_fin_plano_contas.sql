CREATE TABLE fin_plano_contas (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    conta_pai_id BIGINT,
    nivel INT NOT NULL,
    tipo_conta VARCHAR(30),
    natureza_saldo VARCHAR(20),
    aceita_lancamento BOOLEAN DEFAULT TRUE,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_por BIGINT,
    versao INTEGER DEFAULT 0,
    data_atualizacao TIMESTAMP,
    atualizado_por BIGINT,
    CONSTRAINT uk_fin_plano_contas_codigo UNIQUE (empresa_id, codigo),
    CONSTRAINT fk_fin_plano_contas_pai FOREIGN KEY (conta_pai_id) REFERENCES fin_plano_contas(id)
);

CREATE INDEX idx_fin_plano_contas_empresa ON fin_plano_contas(empresa_id);
