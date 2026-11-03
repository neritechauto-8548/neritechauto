ALTER TABLE configuracoes_complementares_empresa
    DROP CONSTRAINT ck_configuracao_complementar_secao,
    ADD CONSTRAINT ck_configuracao_complementar_secao CHECK (secao IN (
        'ATENDIMENTO', 'COMERCIAL', 'ESTOQUE_FINANCEIRO', 'INTEGRACOES',
        'GESTAO_PATIO', 'FERIADOS_LOCAIS', 'PRODUTO_COMPOSICAO',
        'PRODUTO_CUSTO_LUCRO', 'COMPRAS', 'AGENDAMENTO', 'FINANCEIRO', 'TERMOS',
        'FISCAL_GERAL', 'FISCAL_NFE', 'FISCAL_NFCE', 'FISCAL_NFSE',
        'FISCAL_FCP', 'FISCAL_SINTEGRA', 'FISCAL_SPED'
    ));

ALTER TABLE configuracoes_complementares_empresa
    ADD COLUMN senha_nfse_cifrada BYTEA,
    ADD COLUMN csc_nfce_cifrada BYTEA,
    ADD COLUMN token_nfce_cifrado BYTEA;
