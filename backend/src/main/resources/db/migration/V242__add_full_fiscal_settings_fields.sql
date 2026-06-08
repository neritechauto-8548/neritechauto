-- V242__add_full_fiscal_settings_fields.sql

ALTER TABLE configuracoes_fiscais
  -- NFe Padrões
  ADD COLUMN situacao_tributaria_icms_nfe VARCHAR(100),
  ADD COLUMN situacao_tributaria_pis_nfe VARCHAR(100),
  ADD COLUMN situacao_tributaria_cofins_nfe VARCHAR(100),
  ADD COLUMN mensagem_dados_adicionais_nfe TEXT,
  ADD COLUMN mostrar_cnpj_nfe BOOLEAN DEFAULT FALSE,
  ADD COLUMN utilizar_codigo_produto_original_nfe BOOLEAN DEFAULT FALSE,

  -- NFCe Padrões
  ADD COLUMN situacao_tributaria_icms_nfce VARCHAR(100),
  ADD COLUMN situacao_tributaria_pis_nfce VARCHAR(100),
  ADD COLUMN situacao_tributaria_cofins_nfce VARCHAR(100),
  ADD COLUMN mensagem_dados_adicionais_nfce TEXT,
  ADD COLUMN serie_nfce INT DEFAULT 1,
  ADD COLUMN cfop_padrao_nfce VARCHAR(4) DEFAULT '5102',

  -- NFSe Padrões Homologação
  ADD COLUMN sequencial_rpse_homologacao BIGINT,
  ADD COLUMN serie_rpse_homologacao VARCHAR(5),
  ADD COLUMN sequencial_lote_rpse_homologacao BIGINT,
  ADD COLUMN usuario_acesso_provedor_homologacao VARCHAR(100),
  ADD COLUMN token_acesso_provedor_homologacao VARCHAR(255),

  -- NFSe Padrões Produção
  ADD COLUMN sequencial_rpse_producao BIGINT,
  ADD COLUMN serie_rpse_producao VARCHAR(5),
  ADD COLUMN sequencial_lote_rpse_producao BIGINT,
  ADD COLUMN usuario_acesso_provedor_producao VARCHAR(100),
  ADD COLUMN token_acesso_provedor_producao VARCHAR(255),

  -- NFSe Padrões Gerais
  ADD COLUMN cnae_servico VARCHAR(20),
  ADD COLUMN codigo_servico_municipal VARCHAR(50),
  ADD COLUMN item_lista_servico VARCHAR(50),
  ADD COLUMN codigo_nbs VARCHAR(20),
  ADD COLUMN codigo_tributacao_municipio VARCHAR(50),
  ADD COLUMN unidade_servico VARCHAR(20),
  ADD COLUMN descricao_servico_municipal TEXT,
  ADD COLUMN natureza_operacao_nfse VARCHAR(50),
  ADD COLUMN regime_especial_tributacao_nfse VARCHAR(50),
  ADD COLUMN iss_retido_fonte BOOLEAN DEFAULT FALSE,
  ADD COLUMN outras_informacoes_nfse TEXT,
  ADD COLUMN logo_nfse_path VARCHAR(255),

  -- Reforma Tributária (IBS/CBS)
  ADD COLUMN aliquota_ibs DECIMAL(8,4),
  ADD COLUMN percentual_diferimento_ibs DECIMAL(8,4),
  ADD COLUMN percentual_reducao_ibs DECIMAL(8,4),
  ADD COLUMN aliquota_cbs DECIMAL(8,4),
  ADD COLUMN percentual_diferimento_cbs DECIMAL(8,4),
  ADD COLUMN percentual_reducao_cbs DECIMAL(8,4),
  ADD COLUMN situacao_tributaria_ibs_cbs VARCHAR(100),
  ADD COLUMN classificacao_tributaria_ibs_cbs VARCHAR(100);
