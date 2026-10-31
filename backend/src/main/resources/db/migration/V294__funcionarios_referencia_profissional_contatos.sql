-- Evolução aditiva: simplifica cadastro inicial sem inventar data de admissão.
CREATE SEQUENCE IF NOT EXISTS seq_funcionarios_registro_interno START 1;
ALTER TABLE funcionarios ALTER COLUMN data_admissao DROP NOT NULL;
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS registro_profissional VARCHAR(80);
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS tipo_profissional VARCHAR(80);
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS area_atuacao VARCHAR(100);
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS comissao_pecas NUMERIC(7,2);
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS comissao_servicos NUMERIC(7,2);
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS linhas_transporte VARCHAR(255);
ALTER TABLE funcionarios ADD CONSTRAINT chk_funcionario_comissoes_referencia
 CHECK ((comissao_pecas IS NULL OR comissao_pecas BETWEEN 0 AND 100) AND (comissao_servicos IS NULL OR comissao_servicos BETWEEN 0 AND 100));

CREATE TABLE rh_funcionario_contatos (
 id BIGSERIAL PRIMARY KEY,
 empresa_id BIGINT NOT NULL REFERENCES empresa(id),
 funcionario_id BIGINT NOT NULL REFERENCES funcionarios(id),
 nome VARCHAR(160) NOT NULL,
 sexo VARCHAR(1),
 data_nascimento DATE,
 principal BOOLEAN NOT NULL DEFAULT FALSE,
 data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 versao INTEGER NOT NULL DEFAULT 0,
 CONSTRAINT chk_contato_nome CHECK (length(btrim(nome)) >= 2)
);
CREATE UNIQUE INDEX uk_contato_principal_por_funcionario
 ON rh_funcionario_contatos (empresa_id,funcionario_id) WHERE principal;
CREATE INDEX idx_contatos_funcionario ON rh_funcionario_contatos(empresa_id,funcionario_id);
CREATE TABLE rh_funcionario_contato_meios (
 id BIGSERIAL PRIMARY KEY,
 contato_id BIGINT NOT NULL REFERENCES rh_funcionario_contatos(id) ON DELETE CASCADE,
 tipo VARCHAR(16) NOT NULL CHECK (tipo IN ('EMAIL','WHATSAPP','CELULAR','TELEFONE')),
 valor VARCHAR(255) NOT NULL,
 CONSTRAINT chk_contato_meio_valor CHECK (length(btrim(valor)) >= 3)
);
CREATE INDEX idx_contato_meios_contato ON rh_funcionario_contato_meios(contato_id);
