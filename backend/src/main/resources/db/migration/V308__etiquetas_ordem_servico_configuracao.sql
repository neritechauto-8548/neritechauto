ALTER TABLE itens_configuracao_empresa
    DROP CONSTRAINT IF EXISTS ck_itens_configuracao_grupo;

ALTER TABLE itens_configuracao_empresa
    ADD CONSTRAINT ck_itens_configuracao_grupo
    CHECK (grupo IN (
        'CLIENTE_CATEGORIA',
        'FINANCEIRO_ETIQUETA',
        'VENDA_DIRETA_ETIQUETA',
        'ORDEM_SERVICO_ETIQUETA'
    ));
