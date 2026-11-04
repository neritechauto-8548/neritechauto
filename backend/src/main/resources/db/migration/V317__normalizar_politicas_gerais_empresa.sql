-- Preferências do menu Configurações, separadas das configurações operacionais
-- da empresa e das tags de itens da ordem de serviço. Os valores anteriores
-- são descartados conforme solicitado.
CREATE TABLE preferencias_operacionais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    locale VARCHAR(10), locale_ativo BOOLEAN,
    precisao_quantidades SMALLINT, precisao_quantidades_ativo BOOLEAN,
    validade_orcamento_dias SMALLINT, validade_orcamento_dias_ativo BOOLEAN,
    avisos_ativos BOOLEAN, avisos_ativos_ativo BOOLEAN,
    retencao_historico_dias SMALLINT, retencao_historico_dias_ativo BOOLEAN,
    codigo_padrao_pecas VARCHAR(20), codigo_padrao_pecas_ativo BOOLEAN,
    km_obrigatorio BOOLEAN, km_obrigatorio_ativo BOOLEAN,
    atualizar_km_automaticamente BOOLEAN, atualizar_km_automaticamente_ativo BOOLEAN,
    ordem_selecao_avaria VARCHAR(30), ordem_selecao_avaria_ativo BOOLEAN,
    periodo_expiracao_senhas VARCHAR(20), periodo_expiracao_senhas_ativo BOOLEAN
);

CREATE TABLE preferencias_clientes_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    categoria_em_contas_receber BOOLEAN, categoria_em_contas_receber_ativo BOOLEAN,
    categoria_em_venda_direta BOOLEAN, categoria_em_venda_direta_ativo BOOLEAN,
    categoria_em_nota_fiscal BOOLEAN, categoria_em_nota_fiscal_ativo BOOLEAN,
    categoria_em_ordem_servico BOOLEAN, categoria_em_ordem_servico_ativo BOOLEAN,
    categoria_em_listagem BOOLEAN, categoria_em_listagem_ativo BOOLEAN
);

CREATE TABLE preferencias_compras_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    alterar_preco_compra VARCHAR(20), alterar_preco_compra_ativo BOOLEAN,
    enviar_custo_compra_os BOOLEAN, enviar_custo_compra_os_ativo BOOLEAN,
    atualizar_fabricante_compra BOOLEAN, atualizar_fabricante_compra_ativo BOOLEAN,
    atualizar_origem_compra BOOLEAN, atualizar_origem_compra_ativo BOOLEAN,
    atualizar_ncm_importacao BOOLEAN, atualizar_ncm_importacao_ativo BOOLEAN
);

CREATE TABLE preferencias_financeiras_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    liquidar_boleto_no_vencimento BOOLEAN, liquidar_boleto_no_vencimento_ativo BOOLEAN,
    liquidacao_automatica_boleto BOOLEAN, liquidacao_automatica_boleto_ativo BOOLEAN,
    orientacao_impressao_boleto VARCHAR(12), orientacao_impressao_boleto_ativo BOOLEAN
);

CREATE TABLE preferencias_venda_direta_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estoque_todas_empresas_venda BOOLEAN, estoque_todas_empresas_venda_ativo BOOLEAN
);

CREATE TABLE preferencias_crm_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    calculo_revisao_crm VARCHAR(20), calculo_revisao_crm_ativo BOOLEAN,
    nome_remetente_crm VARCHAR(120), nome_remetente_crm_ativo BOOLEAN,
    email_resposta_crm VARCHAR(254), email_resposta_crm_ativo BOOLEAN,
    crm_aniversario BOOLEAN, crm_aniversario_ativo BOOLEAN,
    crm_servicos_futuros BOOLEAN, crm_servicos_futuros_ativo BOOLEAN,
    crm_proximas_revisoes BOOLEAN, crm_proximas_revisoes_ativo BOOLEAN,
    crm_clientes_sem_movimento BOOLEAN, crm_clientes_sem_movimento_ativo BOOLEAN,
    crm_pesquisa_satisfacao BOOLEAN, crm_pesquisa_satisfacao_ativo BOOLEAN,
    crm_cobranca_dividas BOOLEAN, crm_cobranca_dividas_ativo BOOLEAN,
    crm_via_smtp BOOLEAN, crm_via_smtp_ativo BOOLEAN
);

CREATE TABLE preferencias_relatorios_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    relatorio_quadro_anotacoes BOOLEAN, relatorio_quadro_anotacoes_ativo BOOLEAN,
    relatorio_separar_areas BOOLEAN, relatorio_separar_areas_ativo BOOLEAN,
    relatorio_dados_cliente BOOLEAN, relatorio_dados_cliente_ativo BOOLEAN,
    ordenacao_relatorio VARCHAR(20), ordenacao_relatorio_ativo BOOLEAN,
    base_calculo_comissao VARCHAR(12), base_calculo_comissao_ativo BOOLEAN
);

CREATE TABLE preferencias_fiscais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nota_fiscal_outra_empresa BOOLEAN, nota_fiscal_outra_empresa_ativo BOOLEAN
);

DROP TABLE politicas_gerais_empresa;
