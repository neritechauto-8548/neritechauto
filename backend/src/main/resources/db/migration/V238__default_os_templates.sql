-- Migração V238: Inserção de Templates Padrão de Notificação de OS
-- Autor: Sistema NeriTech
-- Descrição: Templates base para automação de notificações de status da Ordem de Serviço

INSERT INTO com_templates_comunicacao (empresa_id, nome, tipo_template, categoria, assunto, conteudo, variaveis_disponiveis, padrao_categoria)
VALUES 
(1, 'OS Abertura - WhatsApp', 'WHATSAPP', 'STATUS_OS', 'Abertura de OS', 'Olá {{CLIENTE}}, sua Ordem de Serviço {{OS_NUMERO}} foi aberta com sucesso. Estamos cuidando do seu veículo!', '{{CLIENTE}},{{OS_NUMERO}}', TRUE),

(1, 'Orçamento Pronto - WhatsApp', 'WHATSAPP', 'STATUS_OS', 'Orçamento Disponível', 'Olá {{CLIENTE}}, o orçamento para a sua OS {{OS_NUMERO}} já está disponível para aprovação. Valor total: R$ {{VALOR_TOTAL}}.', '{{CLIENTE}},{{OS_NUMERO}},{{VALOR_TOTAL}}', TRUE),

(1, 'Veículo Pronto - WhatsApp', 'WHATSAPP', 'STATUS_OS', 'Veículo Pronto', 'Olá {{CLIENTE}}, seu veículo já está pronto para retirada! A Ordem de Serviço {{OS_NUMERO}} foi finalizada. Valor total: R$ {{VALOR_TOTAL}}.', '{{CLIENTE}},{{OS_NUMERO}},{{VALOR_TOTAL}}', TRUE);
