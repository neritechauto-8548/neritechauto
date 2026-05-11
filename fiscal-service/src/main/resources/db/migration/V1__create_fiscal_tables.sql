CREATE TABLE IF NOT EXISTS fis_notas_fiscais (
    id                  BIGSERIAL PRIMARY KEY,
    empresa_id          BIGINT NOT NULL,
    numero              INTEGER NOT NULL,
    serie               VARCHAR(3) NOT NULL DEFAULT '1',
    chave_acesso        VARCHAR(44),
    modelo              VARCHAR(2) NOT NULL DEFAULT '55',       -- 55=NF-e, 65=NFC-e
    natureza_operacao   VARCHAR(60) NOT NULL,
    tipo_operacao       VARCHAR(1) NOT NULL DEFAULT '1',        -- 0=Entrada, 1=Saída
    finalidade          VARCHAR(1) NOT NULL DEFAULT '1',        -- 1=Normal
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, AUTORIZADA, CANCELADA, REJEITADA, DENEGADA
    protocolo_autorizacao VARCHAR(60),
    data_autorizacao    TIMESTAMP,
    xml_enviado         TEXT,
    xml_retorno         TEXT,
    xml_proc_nfe        TEXT,
    motivo_rejeicao     TEXT,
    -- Destinatário
    dest_nome           VARCHAR(60) NOT NULL,
    dest_cpf_cnpj       VARCHAR(14) NOT NULL,
    dest_ie             VARCHAR(14),
    dest_email          VARCHAR(60),
    dest_logradouro     VARCHAR(60),
    dest_numero         VARCHAR(60),
    dest_bairro         VARCHAR(60),
    dest_municipio      VARCHAR(60),
    dest_uf             VARCHAR(2),
    dest_cep            VARCHAR(8),
    dest_fone           VARCHAR(14),
    -- Totais
    val_produtos        NUMERIC(15,2) NOT NULL DEFAULT 0,
    val_frete           NUMERIC(15,2) DEFAULT 0,
    val_seguro          NUMERIC(15,2) DEFAULT 0,
    val_desconto        NUMERIC(15,2) DEFAULT 0,
    val_outras          NUMERIC(15,2) DEFAULT 0,
    val_ipi             NUMERIC(15,2) DEFAULT 0,
    val_icms            NUMERIC(15,2) DEFAULT 0,
    val_pis             NUMERIC(15,2) DEFAULT 0,
    val_cofins          NUMERIC(15,2) DEFAULT 0,
    val_total_nota      NUMERIC(15,2) NOT NULL DEFAULT 0,
    -- Auditoria
    criado_em           TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em       TIMESTAMP NOT NULL DEFAULT NOW(),
    criado_por          VARCHAR(100),
    versao              BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS fis_itens_nota (
    id                  BIGSERIAL PRIMARY KEY,
    nota_id             BIGINT NOT NULL REFERENCES fis_notas_fiscais(id) ON DELETE CASCADE,
    numero_item         INTEGER NOT NULL,
    codigo_produto      VARCHAR(60) NOT NULL,
    descricao           VARCHAR(120) NOT NULL,
    ncm                 VARCHAR(8) NOT NULL,
    cfop                VARCHAR(4) NOT NULL,
    unidade             VARCHAR(6) NOT NULL DEFAULT 'UN',
    quantidade          NUMERIC(15,4) NOT NULL,
    valor_unitario      NUMERIC(15,4) NOT NULL,
    valor_total         NUMERIC(15,2) NOT NULL,
    valor_desconto      NUMERIC(15,2) DEFAULT 0,
    -- ICMS
    cst_icms            VARCHAR(3),
    aliq_icms           NUMERIC(5,2) DEFAULT 0,
    val_icms            NUMERIC(15,2) DEFAULT 0,
    base_calculo_icms   NUMERIC(15,2) DEFAULT 0,
    -- PIS
    cst_pis             VARCHAR(2) DEFAULT '07',
    aliq_pis            NUMERIC(5,2) DEFAULT 0,
    val_pis             NUMERIC(15,2) DEFAULT 0,
    -- COFINS
    cst_cofins          VARCHAR(2) DEFAULT '07',
    aliq_cofins         NUMERIC(5,2) DEFAULT 0,
    val_cofins          NUMERIC(15,2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS fis_eventos_nfe (
    id                  BIGSERIAL PRIMARY KEY,
    nota_id             BIGINT NOT NULL REFERENCES fis_notas_fiscais(id),
    tipo_evento         VARCHAR(30) NOT NULL,  -- CANCELAMENTO, CCE, etc.
    n_seq_evento        INTEGER NOT NULL DEFAULT 1,
    protocolo           VARCHAR(60),
    justificativa       TEXT,
    xml_evento          TEXT,
    xml_retorno         TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    criado_em           TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_notas_empresa   ON fis_notas_fiscais(empresa_id);
CREATE INDEX IF NOT EXISTS idx_notas_chave     ON fis_notas_fiscais(chave_acesso);
CREATE INDEX IF NOT EXISTS idx_notas_status    ON fis_notas_fiscais(status);
CREATE INDEX IF NOT EXISTS idx_itens_nota_id   ON fis_itens_nota(nota_id);
