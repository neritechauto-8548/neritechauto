-- Criação da tabela cliente com escopo de tenant (id_empresa)
CREATE TABLE IF NOT EXISTS cliente (
    id BIGSERIAL PRIMARY KEY,
    id_empresa BIGINT NOT NULL,
    tipo_cliente VARCHAR(20) NOT NULL,
    nome_completo VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    razao_social VARCHAR(255),
    cpf VARCHAR(14) UNIQUE,
    cnpj VARCHAR(18) UNIQUE,
    rg VARCHAR(20),
    inscricao_estadual VARCHAR(20),
    inscricao_municipal VARCHAR(20),
    data_nascimento DATE,
    sexo VARCHAR(1),
    estado_civil VARCHAR(20),
    profissao VARCHAR(100),
    origem_cliente VARCHAR(20),
    detalhes_origem VARCHAR(255),
    status VARCHAR(20),
    motivo_bloqueio TEXT,
    data_bloqueio TIMESTAMP,
    observacoes_gerais TEXT,

    -- Campos de auditoria herdados de BaseEntity
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    versao INTEGER
);

-- FK para empresa
DO $$
BEGIN
    -- Adiciona FK somente se a tabela empresa existir e a constraint ainda não existir
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'empresa'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.table_constraints tc
            WHERE tc.table_schema = 'public'
              AND tc.table_name = 'cliente'
              AND tc.constraint_name = 'fk_cliente_empresa'
        ) THEN
            ALTER TABLE cliente
            ADD CONSTRAINT fk_cliente_empresa
            FOREIGN KEY (id_empresa) REFERENCES empresa(id)
            ON UPDATE NO ACTION ON DELETE RESTRICT;
        END IF;
    END IF;
END $$;

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_cliente_id_empresa ON cliente(id_empresa);
CREATE INDEX IF NOT EXISTS idx_cliente_nome_completo ON cliente(nome_completo);
CREATE INDEX IF NOT EXISTS idx_cliente_razao_social ON cliente(razao_social);
CREATE INDEX IF NOT EXISTS idx_cliente_status ON cliente(status);