ALTER TABLE configuracoes_smtp_empresa
    DROP CONSTRAINT IF EXISTS ck_configuracoes_smtp_empresa_ssl_tls;

ALTER TABLE configuracoes_smtp_usuario
    DROP CONSTRAINT IF EXISTS ck_smtp_usuario_ssl_tls;
