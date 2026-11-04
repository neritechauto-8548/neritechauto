-- Tabelas específicas substituem o armazenamento genérico por seção.

DROP TABLE IF EXISTS configuracoes_complementares_empresa;

DROP TABLE IF EXISTS itens_configuracao_empresa;

CREATE TABLE configuracoes_atendimento_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    campo_km_obrigatorio BOOLEAN,
    campo_prisma_obrigatorio BOOLEAN,
    campo_nivel_tanque_obrigatorio BOOLEAN,
    exibir_campos_indicacao_corretor BOOLEAN,
    vincular_observacoes_ordens_atividades BOOLEAN,
    preencher_folha_mecanico_automaticamente BOOLEAN
);

CREATE TABLE configuracoes_comerciais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    margem_referencia NUMERIC(15,4),
    desconto_maximo_peca NUMERIC(15,4),
    desconto_maximo_servico NUMERIC(15,4),
    aplicar_acrescimo_os BOOLEAN
);

CREATE TABLE configuracoes_estoque_financeiro_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    baixa_automatica_estoque BOOLEAN,
    notificar_estoque_minimo BOOLEAN,
    modo_condicao_pagamento VARCHAR(500)
);

CREATE TABLE configuracoes_integracoes_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_hora_pintura NUMERIC(15,4),
    area_pintura VARCHAR(500),
    valor_hora_troca NUMERIC(15,4),
    area_troca VARCHAR(500),
    valor_hora_reparacao NUMERIC(15,4),
    area_reparacao VARCHAR(500),
    utilizar_checklist_bosch BOOLEAN,
    enviar_os_my_bcs_automaticamente BOOLEAN,
    receber_eventos_my_bcs BOOLEAN
);

CREATE TABLE configuracoes_gestao_patio_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criar_atividade_ao_gerar_os BOOLEAN,
    mover_cartao_ao_fechar_os BOOLEAN
);

CREATE TABLE configuracoes_feriados_locais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE configuracoes_composicao_produto_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    percentual_ipi BOOLEAN,
    frete BOOLEAN,
    custo_fixo BOOLEAN,
    despesas_operacionais BOOLEAN,
    substituicao_tributaria BOOLEAN,
    outras_despesas BOOLEAN,
    icms_desoneracao BOOLEAN,
    calcular_margem_lucro BOOLEAN
);

CREATE TABLE configuracoes_custo_lucro_produto_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    percentual_custo_fixo NUMERIC(15,4),
    percentual_lucro_fixo NUMERIC(15,4)
);

CREATE TABLE configuracoes_compras_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bloquear_cfop_saida BOOLEAN,
    permitir_excluir_itens_importados BOOLEAN,
    permitir_preco_venda_na_compra BOOLEAN,
    exigir_motivo_cancelamento BOOLEAN
);

CREATE TABLE configuracoes_agendamento_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE configuracoes_financeiras_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    parcelas_livres_venda_direta BOOLEAN,
    data_venda_referencia_vencimento BOOLEAN,
    permitir_liquido_negativo_pagar BOOLEAN
);

CREATE TABLE termos_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    termo_lgpd TEXT,
    termo_garantia TEXT,
    termo_quitacao TEXT,
    termo_pecas_fornecidas TEXT,
    termo_responsabilidade TEXT,
    declaracao_permanencia TEXT,
    termo_corte_selo_placa TEXT,
    declaracao_assinatura_os TEXT
);

CREATE TABLE configuracoes_fiscais_gerais_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ambiente_emissao VARCHAR(500),
    regime_tributario_especial VARCHAR(500),
    optante_simples_nacional BOOLEAN,
    habilitar_nfe BOOLEAN,
    habilitar_nfce BOOLEAN,
    habilitar_nfse BOOLEAN,
    emite_proprias_notas BOOLEAN,
    mostrar_valor_total_notas BOOLEAN,
    recuperar_peso_peca BOOLEAN,
    incentivo_cultural BOOLEAN,
    incentivo_fiscal BOOLEAN,
    deduzir_icms_pis_cofins BOOLEAN,
    codigo_peca_danfe VARCHAR(500),
    inscricao_estadual VARCHAR(500),
    inscricao_municipal VARCHAR(500),
    regime_tributario VARCHAR(500),
    cnae VARCHAR(500),
    cep VARCHAR(500),
    estado VARCHAR(500),
    cidade VARCHAR(500),
    bairro VARCHAR(500),
    logradouro VARCHAR(500),
    numero VARCHAR(500),
    complemento VARCHAR(500)
);

CREATE TABLE configuracoes_nfe_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cfop_padrao VARCHAR(500),
    natureza_operacao VARCHAR(500),
    unidade VARCHAR(500),
    codigo_beneficio_fiscal VARCHAR(500),
    calcular_ibpt_automaticamente BOOLEAN,
    cst_icms VARCHAR(500),
    aliquota_icms NUMERIC(15,4),
    cst_ipi VARCHAR(500),
    aliquota_ipi NUMERIC(15,4),
    codigo_enquadramento_ipi VARCHAR(500),
    cst_pis VARCHAR(500),
    aliquota_pis NUMERIC(15,4),
    cst_cofins VARCHAR(500),
    aliquota_cofins NUMERIC(15,4),
    aliquota_csll NUMERIC(15,4),
    aliquota_irrf NUMERIC(15,4),
    cst_ibs_cbs VARCHAR(500),
    classificacao_tributaria VARCHAR(500),
    aliquota_ibs_uf NUMERIC(15,4),
    aliquota_ibs_municipal NUMERIC(15,4),
    aliquota_cbs NUMERIC(15,4),
    diferimento_ibs_uf NUMERIC(15,4),
    diferimento_ibs_municipal NUMERIC(15,4),
    diferimento_cbs NUMERIC(15,4),
    devolvido_ibs_uf NUMERIC(15,4),
    devolvido_ibs_municipal NUMERIC(15,4),
    devolvido_cbs NUMERIC(15,4),
    tipo_emissao VARCHAR(500),
    csrt_estado VARCHAR(500),
    impressao_fcp BOOLEAN,
    partilha_impressao BOOLEAN,
    consultar_dfe BOOLEAN,
    enviar_email BOOLEAN,
    utilizar_qr_code_versao3 BOOLEAN,
    enviar_email_entrega BOOLEAN,
    informacao_interesse_fisco TEXT,
    informacao_interesse_contribuinte TEXT,
    informacao_devolucao TEXT
);

CREATE TABLE configuracoes_nfce_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cfop_padrao VARCHAR(500),
    natureza_operacao VARCHAR(500),
    unidade VARCHAR(500),
    codigo_beneficio_fiscal VARCHAR(500),
    calcular_ibpt_automaticamente BOOLEAN,
    cst_icms VARCHAR(500),
    aliquota_icms NUMERIC(15,4),
    cst_ipi VARCHAR(500),
    aliquota_ipi NUMERIC(15,4),
    codigo_enquadramento_ipi VARCHAR(500),
    cst_pis VARCHAR(500),
    aliquota_pis NUMERIC(15,4),
    cst_cofins VARCHAR(500),
    aliquota_cofins NUMERIC(15,4),
    aliquota_csll NUMERIC(15,4),
    aliquota_irrf NUMERIC(15,4),
    cst_ibs_cbs VARCHAR(500),
    classificacao_tributaria VARCHAR(500),
    aliquota_ibs_uf NUMERIC(15,4),
    aliquota_ibs_municipal NUMERIC(15,4),
    aliquota_cbs NUMERIC(15,4),
    diferimento_ibs_uf NUMERIC(15,4),
    diferimento_ibs_municipal NUMERIC(15,4),
    diferimento_cbs NUMERIC(15,4),
    devolvido_ibs_uf NUMERIC(15,4),
    devolvido_ibs_municipal NUMERIC(15,4),
    devolvido_cbs NUMERIC(15,4),
    tipo_emissao VARCHAR(500),
    csrt_estado VARCHAR(500),
    impressao_fcp BOOLEAN,
    partilha_impressao BOOLEAN,
    consultar_dfe BOOLEAN,
    enviar_email BOOLEAN,
    utilizar_qr_code_versao3 BOOLEAN,
    enviar_email_entrega BOOLEAN,
    informacao_interesse_fisco TEXT,
    informacao_interesse_contribuinte TEXT,
    informacao_devolucao TEXT
);

CREATE TABLE configuracoes_nfse_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    codigo_servico_issqn VARCHAR(500),
    codigo_nbs_padrao VARCHAR(500),
    tipo_operacao VARCHAR(500),
    natureza_tributacao VARCHAR(500),
    codigo_indicador_operacao VARCHAR(500),
    unidade VARCHAR(500),
    agrupar_servicos_por_area BOOLEAN,
    regime_apuracao VARCHAR(500),
    lei_anexo_tabela VARCHAR(500),
    data_inicio_atividade VARCHAR(500),
    receita_bruta NUMERIC(15,4),
    aliquota_ibpt_simples NUMERIC(15,4),
    aliquota_ibpt_federal NUMERIC(15,4),
    aliquota_ibpt_estadual NUMERIC(15,4),
    aliquota_ibpt_municipal NUMERIC(15,4),
    aliquota_aproximada_tributos NUMERIC(15,4),
    ocultar_ibpt_descricao BOOLEAN,
    calcular_ibpt_automaticamente BOOLEAN,
    emitir_nfs_nacional BOOLEAN,
    login_prefeitura VARCHAR(500),
    numeracao_automatica_rps BOOLEAN,
    lote_rps NUMERIC(15,4),
    quebrar_linha_padrao BOOLEAN,
    enviar_email BOOLEAN,
    cst_ibs_cbs VARCHAR(500),
    classificacao_tributaria VARCHAR(500),
    aliquota_ibs_uf NUMERIC(15,4),
    aliquota_ibs_municipal NUMERIC(15,4),
    aliquota_cbs NUMERIC(15,4),
    diferimento_ibs_uf NUMERIC(15,4),
    diferimento_ibs_municipal NUMERIC(15,4),
    diferimento_cbs NUMERIC(15,4),
    devolvido_ibs_uf NUMERIC(15,4),
    devolvido_ibs_municipal NUMERIC(15,4),
    devolvido_cbs NUMERIC(15,4),
    informacao_interesse_fisco TEXT
);

CREATE TABLE aliquotas_fcp_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fcp_a_c NUMERIC(15,4),
    fcp_a_l NUMERIC(15,4),
    fcp_a_m NUMERIC(15,4),
    fcp_a_p NUMERIC(15,4),
    fcp_b_a NUMERIC(15,4),
    fcp_c_e NUMERIC(15,4),
    fcp_d_f NUMERIC(15,4),
    fcp_e_s NUMERIC(15,4),
    fcp_g_o NUMERIC(15,4),
    fcp_m_a NUMERIC(15,4),
    fcp_m_g NUMERIC(15,4),
    fcp_m_s NUMERIC(15,4),
    fcp_m_t NUMERIC(15,4),
    fcp_p_a NUMERIC(15,4),
    fcp_p_b NUMERIC(15,4),
    fcp_p_e NUMERIC(15,4),
    fcp_p_i NUMERIC(15,4),
    fcp_p_r NUMERIC(15,4),
    fcp_r_j NUMERIC(15,4),
    fcp_r_n NUMERIC(15,4),
    fcp_r_o NUMERIC(15,4),
    fcp_r_r NUMERIC(15,4),
    fcp_r_s NUMERIC(15,4),
    fcp_s_c NUMERIC(15,4),
    fcp_s_e NUMERIC(15,4),
    fcp_s_p NUMERIC(15,4),
    fcp_t_o NUMERIC(15,4)
);

CREATE TABLE configuracoes_sintegra_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    codigo_estrutura VARCHAR(500),
    codigo_natureza_operacoes VARCHAR(500),
    nome_responsavel VARCHAR(500),
    telefone_responsavel VARCHAR(500),
    gerar_registro51 BOOLEAN,
    gerar_registro53 BOOLEAN
);

CREATE TABLE configuracoes_sped_empresa (
    empresa_id BIGINT PRIMARY KEY REFERENCES empresa(id) ON DELETE CASCADE,
    versao INTEGER NOT NULL DEFAULT 0,
    atualizado_por BIGINT REFERENCES usuarios(id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nome_contador VARCHAR(500),
    cpf_contador VARCHAR(500),
    crc_contador VARCHAR(500),
    telefone_contador VARCHAR(500),
    email_contador VARCHAR(500),
    regime_pis_cofins VARCHAR(500)
);

CREATE TABLE configuracoes_feriados_locais_itens (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, data_referencia VARCHAR(10) NOT NULL, ordem INTEGER NOT NULL, CONSTRAINT uq_feriados_locais_empresa_data UNIQUE (empresa_id, data_referencia));

CREATE TABLE configuracoes_agendamento_indisponibilidades (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(120) NOT NULL, data_indisponibilidade DATE NOT NULL, horario_inicio TIME NOT NULL, horario_fim TIME NOT NULL, todos_recursos BOOLEAN NOT NULL DEFAULT FALSE, quantidade_recursos INTEGER NOT NULL, ordem INTEGER NOT NULL);

CREATE TABLE contatos_fiscais_empresa (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, tipo_contato VARCHAR(20) NOT NULL, valor VARCHAR(254) NOT NULL, ordem INTEGER NOT NULL, CONSTRAINT ck_contatos_fiscais_tipo CHECK (tipo_contato IN ('E-mail','Celular','Fax','Telefone','WhatsApp','Estrangeiro')));

CREATE TABLE autorizacoes_fiscais_empresa (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, documento VARCHAR(14) NOT NULL, email VARCHAR(254) NOT NULL, ordem INTEGER NOT NULL);

CREATE TABLE series_documentos_fiscais_empresa (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, tipo_documento VARCHAR(10) NOT NULL, serie VARCHAR(20) NOT NULL, numero_inicial BIGINT NOT NULL, principal BOOLEAN NOT NULL DEFAULT FALSE, ordem INTEGER NOT NULL, CONSTRAINT ck_series_documentos_tipo CHECK (tipo_documento IN ('NFE','NFCE','NFSE')));

CREATE TABLE categorias_cliente (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(100) NOT NULL, cor VARCHAR(9) NOT NULL, criado_por BIGINT REFERENCES usuarios(id), atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP); CREATE INDEX idx_categorias_cliente_empresa ON categorias_cliente(empresa_id);

CREATE TABLE recomendacoes_cliente (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(100) NOT NULL, cor VARCHAR(9) NOT NULL, criado_por BIGINT REFERENCES usuarios(id), atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP); CREATE INDEX idx_recomendacoes_cliente_empresa ON recomendacoes_cliente(empresa_id);

CREATE TABLE etiquetas_financeiras (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(100) NOT NULL, cor VARCHAR(9) NOT NULL, criado_por BIGINT REFERENCES usuarios(id), atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP); CREATE INDEX idx_etiquetas_financeiras_empresa ON etiquetas_financeiras(empresa_id);

CREATE TABLE etiquetas_venda_direta (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(100) NOT NULL, cor VARCHAR(9) NOT NULL, criado_por BIGINT REFERENCES usuarios(id), atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP); CREATE INDEX idx_etiquetas_venda_direta_empresa ON etiquetas_venda_direta(empresa_id);

CREATE TABLE etiquetas_ordem_servico (id BIGSERIAL PRIMARY KEY, empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE, descricao VARCHAR(100) NOT NULL, cor VARCHAR(9) NOT NULL, criado_por BIGINT REFERENCES usuarios(id), atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP); CREATE INDEX idx_etiquetas_ordem_servico_empresa ON etiquetas_ordem_servico(empresa_id);

CREATE UNIQUE INDEX ux_categorias_cliente_empresa_descricao ON categorias_cliente(empresa_id, lower(descricao));
CREATE UNIQUE INDEX ux_recomendacoes_cliente_empresa_descricao ON recomendacoes_cliente(empresa_id, lower(descricao));
CREATE UNIQUE INDEX ux_etiquetas_financeiras_empresa_descricao ON etiquetas_financeiras(empresa_id, lower(descricao));
CREATE UNIQUE INDEX ux_etiquetas_venda_direta_empresa_descricao ON etiquetas_venda_direta(empresa_id, lower(descricao));
CREATE UNIQUE INDEX ux_etiquetas_ordem_servico_empresa_descricao ON etiquetas_ordem_servico(empresa_id, lower(descricao));
