-- Renomeando as colunas antigas (nome e modulo) para o novo padrão (chave e valor)
ALTER TABLE permissoes RENAME COLUMN nome TO chave;
ALTER TABLE permissoes RENAME COLUMN modulo TO valor;

-- Removendo constraint antiga de chave única no NOME (agora chave), 
-- pois a "chave" é o agrupamento e não o valor único. O valor único agora é o "valor" (ex: GERAL_USUARIO).
ALTER TABLE permissoes DROP CONSTRAINT IF EXISTS uk_permissoes_nome_empresa;
ALTER TABLE permissoes ADD CONSTRAINT uk_permissoes_valor_empresa UNIQUE (valor, empresa_id);

-- Limpando os dados antigos de permissões para popular as novas baseadas na imagem
DELETE FROM permissoes;

-- Inserindo as novas permissões (baseado nos módulos da UI solicitada)

-- Permissões gerais
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(1, 1, 'Permissões gerais', 'GERAL_USUARIO', 'Usuário', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(2, 1, 'Permissões gerais', 'GERAL_CALENDARIO', 'Calendário Geral', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(3, 1, 'Permissões gerais', 'GERAL_AGENDAMENTO_VISUALIZAR', 'Agendamento (Visualizar / Criar)', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(4, 1, 'Permissões gerais', 'GERAL_FATURAS', 'Faturas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(5, 1, 'Permissões gerais', 'GERAL_CONFIG_SISTEMA', 'Configuração geral do sistema', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(6, 1, 'Permissões gerais', 'GERAL_MEU_CALENDARIO', 'Ver meu Calendário', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(7, 1, 'Permissões gerais', 'GERAL_CONFIG_CHECKLIST', 'Configurar Checklist', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(8, 1, 'Permissões gerais', 'GERAL_ORCAMENTO', 'Orçamento', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(9, 1, 'Permissões gerais', 'GERAL_AGENDAMENTO_EDITAR', 'Agendamento (Editar / Excluir)', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(10, 1, 'Permissões gerais', 'GERAL_CONFIG_SITE', 'Configurar Site', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de Clientes
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(11, 1, 'Permissões de Clientes', 'CLIENTE_CRIAR', 'Cadastrar Clientes', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(12, 1, 'Permissões de Clientes', 'CLIENTE_EXPORTAR', 'Exportar CSV Clientes', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(13, 1, 'Permissões de Clientes', 'CLIENTE_EDITAR', 'Editar Clientes', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(14, 1, 'Permissões de Clientes', 'CLIENTE_EXCLUIR', 'Excluir Clientes', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões dos veículos
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(15, 1, 'Permissões dos veículos', 'VEICULO_CRIAR', 'Cadastrar Veículos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(16, 1, 'Permissões dos veículos', 'VEICULO_EXPORTAR', 'Exportar CSV Veículos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(17, 1, 'Permissões dos veículos', 'VEICULO_EDITAR', 'Editar Veículos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(18, 1, 'Permissões dos veículos', 'VEICULO_EXCLUIR', 'Excluir Veículos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de Alertas
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(19, 1, 'Permissões de Alertas', 'ALERTA_CRIAR_SMS', 'Criar Alerta SMS', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(20, 1, 'Permissões de Alertas', 'ALERTA_CRIAR_EMAIL', 'Criar Alerta Email', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(21, 1, 'Permissões de Alertas', 'ALERTA_ADC_CREDITO', 'Adicionar Créditos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(22, 1, 'Permissões de Alertas', 'ALERTA_EDITAR_SMS', 'Editar Alerta SMS', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(23, 1, 'Permissões de Alertas', 'ALERTA_EDITAR_EMAIL', 'Editar Alerta Email', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(24, 1, 'Permissões de Alertas', 'ALERTA_EXCLUIR_SMS', 'Excluir Alerta SMS', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(25, 1, 'Permissões de Alertas', 'ALERTA_EXCLUIR_EMAIL', 'Excluir Alerta Email', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões da ordem de serviço
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(26, 1, 'Permissões da ordem de serviço', 'OS_ALT_FUNCIONARIO', 'Alterar Funcionário', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(27, 1, 'Permissões da ordem de serviço', 'OS_ALT_STATUS', 'Alterar Status', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(28, 1, 'Permissões da ordem de serviço', 'OS_EXCLUIR', 'Excluir Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(29, 1, 'Permissões da ordem de serviço', 'OS_IMP_CLIENTE', 'Imprimir Ordem de serviço Cliente', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(30, 1, 'Permissões da ordem de serviço', 'OS_NEG_PAGAMENTO', 'Negociar Pagamento', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(31, 1, 'Permissões da ordem de serviço', 'OS_EXP_CSV', 'Exportar CSV de Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(32, 1, 'Permissões da ordem de serviço', 'OS_COMISSAO', 'Comissão Geral', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(33, 1, 'Permissões da ordem de serviço', 'OS_COMENTARIOS', 'Comentários', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(34, 1, 'Permissões da ordem de serviço', 'OS_STATUS_ENTREGUE', 'Status Entregue ser inalterável', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(35, 1, 'Permissões da ordem de serviço', 'OS_ALT_LOCALIZACAO', 'Alterar Localização', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(36, 1, 'Permissões da ordem de serviço', 'OS_EDITAR', 'Editar Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(37, 1, 'Permissões da ordem de serviço', 'OS_IMP_ACOMPANHAMENTO', 'Imprimir Acompanhamento', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(38, 1, 'Permissões da ordem de serviço', 'OS_VIS_SOLICITACOES', 'Visualizar Solicitações', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(39, 1, 'Permissões da ordem de serviço', 'OS_INC_ITENS', 'Incluir Itens em Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(40, 1, 'Permissões da ordem de serviço', 'OS_ENV_FOTOS', 'Enviar Fotos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(41, 1, 'Permissões da ordem de serviço', 'OS_VIS_CHECKLIST', 'Visualizar Checklist', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(42, 1, 'Permissões da ordem de serviço', 'OS_COMENTARIOS_OUTROS', 'Incluir comentários de outros usuários', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(43, 1, 'Permissões da ordem de serviço', 'OS_ALT_SITUACAO', 'Alterar Situação', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(44, 1, 'Permissões da ordem de serviço', 'OS_INCLUIR', 'Incluir Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(45, 1, 'Permissões da ordem de serviço', 'OS_IMP_INTERNO', 'Imprimir Ordem de serviço Controle interno', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(46, 1, 'Permissões da ordem de serviço', 'OS_EXC_SOLICITACOES', 'Excluir Solicitações', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(47, 1, 'Permissões da ordem de serviço', 'OS_VIS_ITENS_LIVRES', 'Ver itens livres em Ordem de serviço', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(48, 1, 'Permissões da ordem de serviço', 'OS_ADC_CHECKLIST', 'Adicionar e Excluir Checklist', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(49, 1, 'Permissões da ordem de serviço', 'OS_EDIT_CHECKLIST', 'Editar Status de itens em Checklist', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(50, 1, 'Permissões da ordem de serviço', 'OS_ENV_QUESTIONARIOS', 'Enviar questionários', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de produtos e serviços
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(51, 1, 'Permissões de produtos e serviços', 'PS_LISTAR_PROD', 'Listar Produtos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(52, 1, 'Permissões de produtos e serviços', 'PS_EXC_PROD', 'Excluir Produtos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(53, 1, 'Permissões de produtos e serviços', 'PS_LISTAR_SERV', 'Listar Serviços', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(54, 1, 'Permissões de produtos e serviços', 'PS_EXC_SERV', 'Excluir Serviços', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(55, 1, 'Permissões de produtos e serviços', 'PS_INC_PROD', 'Incluir Produtos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(56, 1, 'Permissões de produtos e serviços', 'PS_VER_CUSTO', 'Ver preço de custo', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(57, 1, 'Permissões de produtos e serviços', 'PS_INC_SERV', 'Incluir Serviços', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(58, 1, 'Permissões de produtos e serviços', 'PS_ALT_ESTOQUE', 'Alterar Estoque', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(59, 1, 'Permissões de produtos e serviços', 'PS_EDIT_PROD', 'Editar Produtos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(60, 1, 'Permissões de produtos e serviços', 'PS_CALC_CUSTO', 'Calcular preço de custo', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(61, 1, 'Permissões de produtos e serviços', 'PS_EDIT_SERV', 'Editar Serviços', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(62, 1, 'Permissões de produtos e serviços', 'PS_EXP_SERV', 'Exportar Serviços CSV', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de Financeiro
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(63, 1, 'Permissões de Financeiro', 'FIN_FECHAMENTO', 'Fazer fechamento de caixa', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(64, 1, 'Permissões de Financeiro', 'FIN_VIS_CAIXA', 'Visualizar caixa', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(65, 1, 'Permissões de Financeiro', 'FIN_INC_NFE_COMPRA', 'Incluir Nota fiscal de compra', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(66, 1, 'Permissões de Financeiro', 'FIN_EXC_COMISSOES', 'Excluir comissoes', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(67, 1, 'Permissões de Financeiro', 'FIN_EXP_VENDAS', 'Exportar Vendas CSV', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(68, 1, 'Permissões de Financeiro', 'FIN_EDIT_CONTA', 'Editar Conta', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(69, 1, 'Permissões de Financeiro', 'FIN_LISTAR_CONTAS', 'Listar contas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(70, 1, 'Permissões de Financeiro', 'FIN_VIS_REL_COMISSOES', 'Visualizar relatório de comissões', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(71, 1, 'Permissões de Financeiro', 'FIN_FAZER_TRANSF', 'Fazer Transferência', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(72, 1, 'Permissões de Financeiro', 'FIN_DESF_PAGAMENTO', 'Desfazer Pagamento', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(73, 1, 'Permissões de Financeiro', 'FIN_INC_CONTAS', 'Incluir contas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(74, 1, 'Permissões de Financeiro', 'FIN_INC_CONTAS_COMISSAO', 'Incluir Contas de comissões', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(75, 1, 'Permissões de Financeiro', 'FIN_EXC_CONTAS', 'Excluir Contas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de PDV balcão
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(76, 1, 'Permissões de PDV balcão', 'PDV_LISTAR_VENDAS', 'Listar Vendas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(77, 1, 'Permissões de PDV balcão', 'PDV_GERAR_NFCE', 'Gerar Nota Fiscal de Consumidor (NFC-e)', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(78, 1, 'Permissões de PDV balcão', 'PDV_REALIZAR_VENDAS', 'Realizar Vendas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(79, 1, 'Permissões de PDV balcão', 'PDV_GERAR_CUPOM', 'Gerar Cupom Não Fiscal', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(80, 1, 'Permissões de PDV balcão', 'PDV_ALT_VALOR', 'Alterar valor da Venda', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(81, 1, 'Permissões de PDV balcão', 'PDV_GERAR_NFE', 'Gerar Nota Fiscal de Venda (NF-e)', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(82, 1, 'Permissões de PDV balcão', 'PDV_EXC_VENDAS', 'Excluir Vendas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(83, 1, 'Permissões de PDV balcão', 'PDV_ALT_FUNC_RESP', 'Alterar Funcionário Responsável', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de fornecedores
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(84, 1, 'Permissões de fornecedores', 'FORN_LISTAR_PEDIDOS', 'Listar Pedidos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(85, 1, 'Permissões de fornecedores', 'FORN_EXC_PEDIDOS', 'Excluir Pedidos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(86, 1, 'Permissões de fornecedores', 'FORN_VER_ITENS', 'Ver Itens incluídos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(87, 1, 'Permissões de fornecedores', 'FORN_INC_PEDIDOS', 'Incluir Pedidos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(88, 1, 'Permissões de fornecedores', 'FORN_ALT_STATUS', 'Alterar Status', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(89, 1, 'Permissões de fornecedores', 'FORN_IMPRIMIR', 'Imprimir', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(90, 1, 'Permissões de fornecedores', 'FORN_EDIT_PEDIDOS', 'Editar Pedidos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(91, 1, 'Permissões de fornecedores', 'FORN_INC_ITENS', 'Incluir itens', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões de relatórios
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(92, 1, 'Permissões de relatórios', 'REL_VENDAS', 'Visualizar Relatórios de Vendas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(93, 1, 'Permissões de relatórios', 'REL_GRAFICOS', 'Visualizar Relatórios de Gráficos', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(94, 1, 'Permissões de relatórios', 'REL_ESTOQUE_SEM_VALOR', 'Visualizar Relatórios de Estoque sem valor de venda', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(95, 1, 'Permissões de relatórios', 'REL_CONTAS', 'Visualizar Relatórios de Contas', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(96, 1, 'Permissões de relatórios', 'REL_USO_SISTEMA', 'Visualizar Relatórios de Uso do Sistema', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(97, 1, 'Permissões de relatórios', 'REL_ESTOQUE', 'Visualizar Relatórios de Estoque', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(98, 1, 'Permissões de relatórios', 'REL_FLUXO_CAIXA', 'Visualizar Relatórios de Fluxo de Caixa', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(99, 1, 'Permissões de relatórios', 'REL_QUESTIONARIOS', 'Visualizar Relatórios de Questionários', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões do Módulo Consulta Veicular
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(100, 1, 'Permissões do Módulo Consulta Veicular', 'MOD_VEICULO_ATIVAR', 'Ativar módulo Consulta Veicular', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Permissões do Módulo Consulta CPF CNPJ
INSERT INTO permissoes (id, empresa_id, chave, valor, descricao, versao, data_cadastro, criado_por, data_atualizacao, atualizado_por) VALUES 
(101, 1, 'Permissões do Módulo Consulta CPF CNPJ', 'MOD_CPF_ATIVAR', 'Ativar módulo Pesquisa CPF/CNPJ', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL),
(102, 1, 'Permissões do Módulo Consulta CPF CNPJ', 'MOD_CPF_PESQUISAR', 'Permitir Realizar Pesquisa CPF/CNPJ', 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, NULL);

-- Atualiza a sequence do ID para os próximos inserts
SELECT setval('permissoes_id_seq', (SELECT MAX(id) FROM permissoes));
