CREATE TABLE credenciais_fiscais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id),
    senha_prefeitura_nfse_cifrada BYTEA,
    codigo_seguranca_nfce_cifrado BYTEA,
    token_nfce_cifrado BYTEA,
    atualizado_por BIGINT NOT NULL REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_credenciais_fiscais_empresa_conteudo CHECK (
        senha_prefeitura_nfse_cifrada IS NOT NULL
        OR codigo_seguranca_nfce_cifrado IS NOT NULL
        OR token_nfce_cifrado IS NOT NULL
    )
);

INSERT INTO credenciais_fiscais_empresa (
    empresa_id, senha_prefeitura_nfse_cifrada, codigo_seguranca_nfce_cifrado,
    token_nfce_cifrado, atualizado_por
)
SELECT empresa_id,
       (SELECT c.senha_nfse_cifrada FROM configuracoes_complementares_empresa c WHERE c.empresa_id = empresas.empresa_id AND c.senha_nfse_cifrada IS NOT NULL ORDER BY c.atualizado_em DESC LIMIT 1),
       (SELECT c.csc_nfce_cifrada FROM configuracoes_complementares_empresa c WHERE c.empresa_id = empresas.empresa_id AND c.csc_nfce_cifrada IS NOT NULL ORDER BY c.atualizado_em DESC LIMIT 1),
       (SELECT c.token_nfce_cifrado FROM configuracoes_complementares_empresa c WHERE c.empresa_id = empresas.empresa_id AND c.token_nfce_cifrado IS NOT NULL ORDER BY c.atualizado_em DESC LIMIT 1),
       (SELECT c.atualizado_por FROM configuracoes_complementares_empresa c WHERE c.empresa_id = empresas.empresa_id AND (c.senha_nfse_cifrada IS NOT NULL OR c.csc_nfce_cifrada IS NOT NULL OR c.token_nfce_cifrado IS NOT NULL) ORDER BY c.atualizado_em DESC LIMIT 1)
FROM (SELECT DISTINCT empresa_id FROM configuracoes_complementares_empresa WHERE senha_nfse_cifrada IS NOT NULL OR csc_nfce_cifrada IS NOT NULL OR token_nfce_cifrado IS NOT NULL) empresas
ON CONFLICT (empresa_id) DO UPDATE SET
    senha_prefeitura_nfse_cifrada = COALESCE(EXCLUDED.senha_prefeitura_nfse_cifrada, credenciais_fiscais_empresa.senha_prefeitura_nfse_cifrada),
    codigo_seguranca_nfce_cifrado = COALESCE(EXCLUDED.codigo_seguranca_nfce_cifrado, credenciais_fiscais_empresa.codigo_seguranca_nfce_cifrado),
    token_nfce_cifrado = COALESCE(EXCLUDED.token_nfce_cifrado, credenciais_fiscais_empresa.token_nfce_cifrado),
    atualizado_por = EXCLUDED.atualizado_por,
    atualizado_em = CURRENT_TIMESTAMP;

ALTER TABLE configuracoes_complementares_empresa
    DROP COLUMN senha_nfse_cifrada,
    DROP COLUMN csc_nfce_cifrada,
    DROP COLUMN token_nfce_cifrado;
