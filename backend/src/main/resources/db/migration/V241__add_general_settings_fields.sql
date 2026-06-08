-- V241__add_general_settings_fields.sql

ALTER TABLE configuracoes_oficina
  ADD COLUMN mostrar_logo_cupom BOOLEAN DEFAULT FALSE,
  ADD COLUMN mostrar_assinatura_os VARCHAR(20) DEFAULT 'FUNCIONARIO',
  ADD COLUMN termo_garantia_padrao TEXT,
  ADD COLUMN mensagem_envio_os_padrao TEXT,
  ADD COLUMN mostrar_exclusivo_mensagem_os BOOLEAN DEFAULT FALSE,
  ADD COLUMN mostrar_relatorios VARCHAR(20) DEFAULT 'NOME_FANTASIA',
  ADD COLUMN receber_email_resposta_questionario BOOLEAN DEFAULT TRUE,
  ADD COLUMN desconto_incide_comissao BOOLEAN DEFAULT FALSE,
  ADD COLUMN atualizar_preco_custo_venda_automaticamente BOOLEAN DEFAULT FALSE;
