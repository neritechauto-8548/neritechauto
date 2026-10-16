-- ADM-009: cadastro de seguradoras e vínculo opcional, manual e interno com a OS.
-- Não há emissão de apólice, integração externa ou aprovação automática.
CREATE TABLE seguradoras (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    nome_fantasia VARCHAR(255) NOT NULL,
    razao_social VARCHAR(255),
    cnpj VARCHAR(18),
    registro_susep VARCHAR(60),
    nome_contato VARCHAR(255),
    telefone VARCHAR(20),
    email VARCHAR(255),
    endereco VARCHAR(500),
    tipo_convenio VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    notas TEXT,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT ck_seguradoras_tipo_convenio CHECK
        (tipo_convenio IS NULL OR tipo_convenio IN ('CONVENIO', 'REEMBOLSO', 'AMBOS'))
);
CREATE INDEX idx_seguradoras_empresa_ativo ON seguradoras(empresa_id, ativo);
CREATE UNIQUE INDEX ux_seguradoras_empresa_cnpj
    ON seguradoras (empresa_id, upper(regexp_replace(cnpj, '[^A-Za-z0-9]', '', 'g')))
    WHERE cnpj IS NOT NULL AND btrim(cnpj) <> '';

CREATE TABLE seguradoras_os (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id),
    seguradora_id BIGINT NOT NULL REFERENCES seguradoras(id),
    ordem_servico_id BIGINT NOT NULL REFERENCES ordens_servico(id),
    tipo_vinculo VARCHAR(15) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_seguradoras_os_tipo CHECK (tipo_vinculo IN ('CONVENIO', 'REEMBOLSO')),
    CONSTRAINT uq_seguradoras_os_empresa_ordem UNIQUE (empresa_id, ordem_servico_id)
);
CREATE INDEX idx_seguradoras_os_seguradora ON seguradoras_os(empresa_id, seguradora_id);
-- A aplicação verifica o tenant das duas referências antes de vincular.
