-- ==========================================
-- V171__insert_base_dados_exemplo.sql
-- Base exemplo multi-tenant para SaaS NeriTech
-- Inclui módulos: Empresa, Clientes, Contatos, Veículos, Marcas/Modelos/Anos,
-- Produtos, Serviços, Fornecedores, Tabelas de Preços, Estoque, OS e Itens,
-- Financeiro (Contas, Faturas, Receber/Pagar, Pagamentos, Fluxo), Agenda
-- (Tipos, Agendamentos, Recursos, Disponibilidade), Comunicação (Listas,
-- Configurações Email, Comunicações Enviadas), Equipamentos e Manutenções.
-- Idempotente onde possível.

-- ==========================
-- EMPRESAS
-- ==========================
INSERT INTO empresa (id, nome_fantasia, razao_social, cnpj, ativo, data_cadastro)
VALUES
    (1, 'Oficina Alpha', 'Oficina Alpha LTDA', '10000000000001', TRUE, '2024-01-10 10:00:00'),
    (2, 'Oficina Beta', 'Oficina Beta LTDA', '10000000000002', TRUE, '2024-02-12 09:30:00'),
    (3, 'Oficina Gama', 'Oficina Gama LTDA', '10000000000003', TRUE, '2024-03-15 14:20:00')
ON CONFLICT (id) DO NOTHING;

-- ==========================
-- CONFIGURAÇÕES DA EMPRESA
-- ==========================
INSERT INTO configuracoes_empresa (empresa_id, regime_tributario, codigo_cnae_principal, capital_social, porte_empresa, tipo_estabelecimento, situacao_cadastral)
SELECT 1, 'SIMPLES', '4520001', 150000, 'ME', 'MATRIZ', 'ATIVA'
WHERE NOT EXISTS (SELECT 1 FROM configuracoes_empresa WHERE empresa_id=1);
INSERT INTO configuracoes_empresa (empresa_id, regime_tributario, codigo_cnae_principal, capital_social, porte_empresa, tipo_estabelecimento, situacao_cadastral)
SELECT 2, 'SIMPLES', '4520001', 200000, 'ME', 'MATRIZ', 'ATIVA'
WHERE NOT EXISTS (SELECT 1 FROM configuracoes_empresa WHERE empresa_id=2);
INSERT INTO configuracoes_empresa (empresa_id, regime_tributario, codigo_cnae_principal, capital_social, porte_empresa, tipo_estabelecimento, situacao_cadastral)
SELECT 3, 'SIMPLES', '4520001', 300000, 'ME', 'MATRIZ', 'ATIVA'
WHERE NOT EXISTS (SELECT 1 FROM configuracoes_empresa WHERE empresa_id=3);

-- ==========================
-- ENDEREÇOS DA EMPRESA
-- ==========================
INSERT INTO enderecos_empresa (empresa_id, tipo_endereco, cep, logradouro, numero, complemento, bairro, cidade, estado, pais, principal, ativo)
SELECT 1, 'COMERCIAL', '01001-000', 'Rua das Flores', '100', NULL, 'Centro', 'São Paulo', 'SP', 'Brasil', TRUE, TRUE
WHERE NOT EXISTS (
  SELECT 1 FROM enderecos_empresa 
  WHERE empresa_id=1 AND tipo_endereco='COMERCIAL' AND cep='01001-000' AND logradouro='Rua das Flores' AND numero='100'
);
INSERT INTO enderecos_empresa (empresa_id, tipo_endereco, cep, logradouro, numero, complemento, bairro, cidade, estado, pais, principal, ativo)
SELECT 2, 'COMERCIAL', '20010-000', 'Av. Atlântica', '200', 'Sala 12', 'Copacabana', 'Rio de Janeiro', 'RJ', 'Brasil', TRUE, TRUE
WHERE NOT EXISTS (
  SELECT 1 FROM enderecos_empresa 
  WHERE empresa_id=2 AND tipo_endereco='COMERCIAL' AND cep='20010-000' AND logradouro='Av. Atlântica' AND numero='200'
);
INSERT INTO enderecos_empresa (empresa_id, tipo_endereco, cep, logradouro, numero, complemento, bairro, cidade, estado, pais, principal, ativo)
SELECT 3, 'COMERCIAL', '30140-110', 'Rua da Bahia', '300', NULL, 'Funcionários', 'Belo Horizonte', 'MG', 'Brasil', TRUE, TRUE
WHERE NOT EXISTS (
  SELECT 1 FROM enderecos_empresa 
  WHERE empresa_id=3 AND tipo_endereco='COMERCIAL' AND cep='30140-110' AND logradouro='Rua da Bahia' AND numero='300'
);

-- ==========================
-- UNIDADES DE MEDIDA
-- ==========================
INSERT INTO unidades_medida (nome, sigla, descricao, tipo, unidade_base)
SELECT 'Unidade', 'UN', 'Unidade', 'QUANTIDADE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM unidades_medida WHERE sigla='UN');
INSERT INTO unidades_medida (nome, sigla, descricao, tipo, unidade_base)
SELECT 'Quilograma', 'KG', 'Quilograma', 'PESO', FALSE
WHERE NOT EXISTS (SELECT 1 FROM unidades_medida WHERE sigla='KG');
INSERT INTO unidades_medida (nome, sigla, descricao, tipo, unidade_base)
SELECT 'Litro', 'L', 'Litro', 'VOLUME', FALSE
WHERE NOT EXISTS (SELECT 1 FROM unidades_medida WHERE sigla='L');
INSERT INTO unidades_medida (nome, sigla, descricao, tipo, unidade_base)
SELECT 'Metro', 'M', 'Metro', 'COMPRIMENTO', FALSE
WHERE NOT EXISTS (SELECT 1 FROM unidades_medida WHERE sigla='M');

-- ==========================
-- MARCAS DE VEÍCULOS (globais)
-- ==========================
INSERT INTO marcas_veiculos (nome, logo_url, website, ativo, created_date, last_modified_date, created_by, last_modified_by, version)
SELECT 'Chevrolet', NULL, 'https://www.chevrolet.com.br', TRUE, '2024-01-01 08:00:00', '2024-01-01 08:00:00', 'seed', 'seed', 1
WHERE NOT EXISTS (SELECT 1 FROM marcas_veiculos WHERE nome='Chevrolet');
INSERT INTO marcas_veiculos (nome, logo_url, website, ativo, created_date, last_modified_date, created_by, last_modified_by, version)
SELECT 'Volkswagen', NULL, 'https://www.vw.com.br', TRUE, '2024-01-01 08:00:00', '2024-01-01 08:00:00', 'seed', 'seed', 1
WHERE NOT EXISTS (SELECT 1 FROM marcas_veiculos WHERE nome='Volkswagen');
INSERT INTO marcas_veiculos (nome, logo_url, website, ativo, created_date, last_modified_date, created_by, last_modified_by, version)
SELECT 'Fiat', NULL, 'https://www.fiat.com.br', TRUE, '2024-01-01 08:00:00', '2024-01-01 08:00:00', 'seed', 'seed', 1
WHERE NOT EXISTS (SELECT 1 FROM marcas_veiculos WHERE nome='Fiat');

-- ==========================
-- CATEGORIAS DE PRODUTOS
-- ==========================
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 1, 'Peças', 'Peças automotivas', 'E1-PECAS', TRUE, TRUE, 20.00, 3.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E1-PECAS');
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 1, 'Óleos e Lubrificantes', 'Óleos e fluidos', 'E1-OLEOS', TRUE, TRUE, 18.00, 2.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E1-OLEOS');
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 2, 'Peças', 'Peças automotivas', 'E2-PECAS', TRUE, TRUE, 22.00, 3.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E2-PECAS');
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 2, 'Óleos e Lubrificantes', 'Óleos e fluidos', 'E2-OLEOS', TRUE, TRUE, 18.00, 2.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E2-OLEOS');
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 3, 'Peças', 'Peças automotivas', 'E3-PECAS', TRUE, TRUE, 20.00, 3.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E3-PECAS');
INSERT INTO categorias_produtos (empresa_id, nome, descricao, codigo, controla_estoque, ativo, margem_lucro_padrao, comissao_venda_padrao)
SELECT 3, 'Óleos e Lubrificantes', 'Óleos e fluidos', 'E3-OLEOS', TRUE, TRUE, 18.00, 2.50
WHERE NOT EXISTS (SELECT 1 FROM categorias_produtos WHERE codigo='E3-OLEOS');

-- ==========================
-- CATEGORIAS DE SERVIÇOS
-- ==========================
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 1, 'Mecânica Geral', 'Serviços mecânicos', 'E1-SERV-MEC', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E1-SERV-MEC');
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 1, 'Elétrica', 'Serviços elétricos', 'E1-SERV-ELE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E1-SERV-ELE');
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 2, 'Mecânica Geral', 'Serviços mecânicos', 'E2-SERV-MEC', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E2-SERV-MEC');
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 2, 'Elétrica', 'Serviços elétricos', 'E2-SERV-ELE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E2-SERV-ELE');
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 3, 'Mecânica Geral', 'Serviços mecânicos', 'E3-SERV-MEC', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E3-SERV-MEC');
INSERT INTO categorias_servicos (empresa_id, nome, descricao, codigo, ativo)
SELECT 3, 'Elétrica', 'Serviços elétricos', 'E3-SERV-ELE', TRUE
WHERE NOT EXISTS (SELECT 1 FROM categorias_servicos WHERE codigo='E3-SERV-ELE');

-- ==========================
-- TIPOS DE AGENDAMENTO
-- ==========================
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 1, 'Troca de Óleo', 'Agendamento rápido', '#3B82F6', 'oil', 60, TRUE, FALSE, '"[\"Troca de Óleo\"]"', 'Confirmar tipo de óleo', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=1 AND nome='Troca de Óleo');
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 1, 'Revisão', 'Revisão completa', '#10B981', 'wrench', 120, FALSE, TRUE, '"[\"Revisão Completa\"]"', 'Verificar histórico', TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=1 AND nome='Revisão');
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 2, 'Troca de Pastilha', 'Freios', '#F59E0B', 'brake', 90, TRUE, FALSE, '"[\"Troca de Pastilha\"]"', 'Testes de frenagem', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=2 AND nome='Troca de Pastilha');
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 2, 'Diagnóstico Elétrico', 'Sistema elétrico', '#8B5CF6', 'bolt', 120, FALSE, TRUE, '"[\"Diagnóstico Elétrico\"]"', 'Levar scanner', TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=2 AND nome='Diagnóstico Elétrico');
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 3, 'Revisão', 'Revisão completa', '#10B981', 'wrench', 120, FALSE, TRUE, '"[\"Revisão Completa\"]"', 'Checklist completo', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=3 AND nome='Revisão');
INSERT INTO agd_tipos_agendamento (empresa_id, nome, descricao, cor_identificacao, icone, duracao_padrao_minutos, permite_encaixe, requer_orcamento, servicos_inclusos, observacoes_padrao, ativo, ordem_exibicao)
SELECT 3, 'Correia Dentada', 'Serviço complexo', '#EF4444', 'cog', 180, FALSE, TRUE, '"[\"Troca de Correia Dentada\"]"', 'Confirmar peças', TRUE, 2
WHERE NOT EXISTS (SELECT 1 FROM agd_tipos_agendamento WHERE empresa_id=3 AND nome='Correia Dentada');

-- ==========================
-- FORNECEDORES
-- ==========================
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM fornecedores WHERE cnpj = '11111111000100') THEN
        INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, razao_social, cnpj, email_principal, telefone_principal, ativo, prazo_pagamento_dias)
        VALUES (1, 'PJ', 'Auto Peças Alpha', 'Auto Peças Alpha LTDA', '11111111000100', 'contato@alpha.com', '(11)3000-1000', TRUE, 30);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM fornecedores WHERE cnpj = '22222222000100') THEN
        INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, razao_social, cnpj, email_principal, telefone_principal, ativo, prazo_pagamento_dias)
        VALUES (2, 'PJ', 'Auto Peças Beta', 'Auto Peças Beta LTDA', '22222222000100', 'contato@beta.com', '(21)4000-1000', TRUE, 30);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM fornecedores WHERE cnpj = '33333333000100') THEN
        INSERT INTO fornecedores (empresa_id, tipo_pessoa, nome_fantasia, razao_social, cnpj, email_principal, telefone_principal, ativo, prazo_pagamento_dias)
        VALUES (3, 'PJ', 'Auto Peças Gama', 'Auto Peças Gama LTDA', '33333333000100', 'contato@gama.com', '(31)5000-1000', TRUE, 30);
    END IF;
END $$;

-- ==========================
-- CLIENTES (amostra)
-- ==========================
INSERT INTO cliente (empresa_id, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
SELECT 1, 'PESSOA_FISICA', 'João da Silva', '11122233301', '1234561', '1985-05-10', 'M', 'CASADO', 'INDICACAO', 'ATIVO', '2024-04-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE cpf='11122233301');
INSERT INTO cliente (empresa_id, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
SELECT 1, 'PESSOA_FISICA', 'Maria Souza', '11122233302', '1234562', '1990-07-12', 'F', 'SOLTEIRO', 'SITE', 'ATIVO', '2024-04-02 11:00:00'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE cpf='11122233302');
INSERT INTO cliente (empresa_id, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
SELECT 2, 'PESSOA_FISICA', 'Fernanda Costa', '22233344401', '2234561', '1987-06-15', 'F', 'CASADO', 'SITE', 'ATIVO', '2024-05-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE cpf='22233344401');
INSERT INTO cliente (empresa_id, tipo_cliente, nome_completo, cpf, rg, data_nascimento, sexo, estado_civil, origem_cliente, status, data_cadastro)
SELECT 3, 'PESSOA_FISICA', 'Gustavo Teixeira', '33344455501', '3234561', '1987-06-15', 'M', 'CASADO', 'SITE', 'ATIVO', '2024-06-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE cpf='33344455501');

-- ==========================
-- CONTATOS DE CLIENTES
-- ==========================
INSERT INTO contatos_clientes (empresa_id, cliente_id, tipo_contato, contato, principal, ativo)
SELECT 1, id, 'WHATSAPP', '+55 (11) 90000-0001', TRUE, TRUE FROM cliente WHERE empresa_id = 1 LIMIT 1;
INSERT INTO contatos_clientes (empresa_id, cliente_id, tipo_contato, contato, principal, ativo)
SELECT 2, id, 'WHATSAPP', '+55 (21) 90000-0002', TRUE, TRUE FROM cliente WHERE empresa_id = 2 LIMIT 1;
INSERT INTO contatos_clientes (empresa_id, cliente_id, tipo_contato, contato, principal, ativo)
SELECT 3, id, 'WHATSAPP', '+55 (31) 90000-0003', TRUE, TRUE FROM cliente WHERE empresa_id = 3 LIMIT 1;

-- ==========================
-- MODELOS/ANOS DE VEÍCULO POR EMPRESA
-- ==========================
INSERT INTO modelos_veiculos (marca_id, nome, categoria, segmento, numero_portas, numero_lugares, tipo_tracao, empresa_id, data_cadastro)
SELECT (SELECT id FROM marcas_veiculos WHERE nome = 'Chevrolet'), 'Onix', 'HATCH', 'COMPACTO', 4, 5, 'DIANTEIRA', 1, '2024-04-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM modelos_veiculos WHERE nome = 'Onix' AND empresa_id = 1);
INSERT INTO anos_modelo (modelo_id, ano_fabricacao, ano_modelo, codigo_fipe, valor_fipe, data_valor_fipe, ativo)
SELECT id, 2022, 2023, '001234-0', 65000.00, '2024-01-01', TRUE FROM modelos_veiculos WHERE nome = 'Onix' AND empresa_id = 1;

INSERT INTO modelos_veiculos (marca_id, nome, categoria, segmento, numero_portas, numero_lugares, tipo_tracao, empresa_id, data_cadastro)
SELECT (SELECT id FROM marcas_veiculos WHERE nome = 'Volkswagen'), 'Gol', 'HATCH', 'COMPACTO', 4, 5, 'DIANTEIRA', 2, '2024-05-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM modelos_veiculos WHERE nome = 'Gol' AND empresa_id = 2);
INSERT INTO anos_modelo (modelo_id, ano_fabricacao, ano_modelo, codigo_fipe, valor_fipe, data_valor_fipe, ativo)
SELECT id, 2016, 2017, '002345-1', 35000.00, '2024-01-01', TRUE FROM modelos_veiculos WHERE nome = 'Gol' AND empresa_id = 2;

INSERT INTO modelos_veiculos (marca_id, nome, categoria, segmento, numero_portas, numero_lugares, tipo_tracao, empresa_id, data_cadastro)
SELECT (SELECT id FROM marcas_veiculos WHERE nome = 'Fiat'), 'Argo', 'HATCH', 'COMPACTO', 4, 5, 'DIANTEIRA', 3, '2024-06-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM modelos_veiculos WHERE nome = 'Argo' AND empresa_id = 3);
INSERT INTO anos_modelo (modelo_id, ano_fabricacao, ano_modelo, codigo_fipe, valor_fipe, data_valor_fipe, ativo)
SELECT id, 2020, 2021, '003456-2', 55000.00, '2024-01-01', TRUE FROM modelos_veiculos WHERE nome = 'Argo' AND empresa_id = 3;

-- ==========================
-- VEÍCULOS (amostra)
-- ==========================
INSERT INTO veiculos (empresa_id, cliente_id, marca_id, modelo_id, ano_modelo_id, placa, renavam, chassi, cor_externa, quilometragem_atual, status, data_cadastro)
SELECT 1, c.id, (SELECT id FROM marcas_veiculos WHERE nome='Chevrolet'),
       (SELECT id FROM modelos_veiculos WHERE nome='Onix' AND empresa_id=1),
       (SELECT id FROM anos_modelo WHERE modelo_id=(SELECT id FROM modelos_veiculos WHERE nome='Onix' AND empresa_id=1) LIMIT 1),
       'BRA1A23', '12345678901', '9BWZZZ377VT004251', 'Prata', 32000, 'ATIVO', '2024-04-02 10:00:00'
FROM cliente c WHERE c.empresa_id = 1 LIMIT 1;

INSERT INTO veiculos (empresa_id, cliente_id, marca_id, modelo_id, ano_modelo_id, placa, renavam, chassi, cor_externa, quilometragem_atual, status, data_cadastro)
SELECT 2, c.id, (SELECT id FROM marcas_veiculos WHERE nome='Volkswagen'),
       (SELECT id FROM modelos_veiculos WHERE nome='Gol' AND empresa_id=2),
       (SELECT id FROM anos_modelo WHERE modelo_id=(SELECT id FROM modelos_veiculos WHERE nome='Gol' AND empresa_id=2) LIMIT 1),
       'BRA3C45', '22345678901', '9BWZZZ377VT004253', 'Branco', 16000, 'ATIVO', '2024-05-02 10:00:00'
FROM cliente c WHERE c.empresa_id = 2 LIMIT 1;

INSERT INTO veiculos (empresa_id, cliente_id, marca_id, modelo_id, ano_modelo_id, placa, renavam, chassi, cor_externa, quilometragem_atual, status, data_cadastro)
SELECT 3, c.id, (SELECT id FROM marcas_veiculos WHERE nome='Fiat'),
       (SELECT id FROM modelos_veiculos WHERE nome='Argo' AND empresa_id=3),
       (SELECT id FROM anos_modelo WHERE modelo_id=(SELECT id FROM modelos_veiculos WHERE nome='Argo' AND empresa_id=3) LIMIT 1),
       'BRA4D56', '32345678901', '9BWZZZ377VT004254', 'Vermelho', 24000, 'ATIVO', '2024-06-02 10:00:00'
FROM cliente c WHERE c.empresa_id = 3 LIMIT 1;

-- ==========================
-- PRODUTOS (amostra)
-- ==========================
INSERT INTO produtos (empresa_id, categoria_id, unidade_medida_id, codigo_interno, nome, descricao, preco_custo, preco_venda, ativo)
SELECT 1, (SELECT id FROM categorias_produtos WHERE codigo='E1-PECAS'),
       (SELECT id FROM unidades_medida WHERE sigla='UN'),
       'E1-P001', 'Filtro de Óleo', 'Filtro padrão', 20.00, 39.90, TRUE
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE codigo_interno='E1-P001');

INSERT INTO produtos (empresa_id, categoria_id, unidade_medida_id, codigo_interno, nome, descricao, preco_custo, preco_venda, ativo)
SELECT 1, (SELECT id FROM categorias_produtos WHERE codigo='E1-OLEOS'),
       (SELECT id FROM unidades_medida WHERE sigla='L'),
       'E1-P002', 'Óleo Motor 5W30', 'Sintético 5W30', 25.00, 59.90, TRUE
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE codigo_interno='E1-P002');

INSERT INTO produtos (empresa_id, categoria_id, unidade_medida_id, codigo_interno, nome, descricao, preco_custo, preco_venda, ativo)
SELECT 2, (SELECT id FROM categorias_produtos WHERE codigo='E2-PECAS'),
       (SELECT id FROM unidades_medida WHERE sigla='UN'),
       'E2-P001', 'Pastilha de Freio', 'Jogo dianteiro', 35.00, 79.90, TRUE
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE codigo_interno='E2-P001');

INSERT INTO produtos (empresa_id, categoria_id, unidade_medida_id, codigo_interno, nome, descricao, preco_custo, preco_venda, ativo)
SELECT 3, (SELECT id FROM categorias_produtos WHERE codigo='E3-OLEOS'),
       (SELECT id FROM unidades_medida WHERE sigla='L'),
       'E3-P002', 'Óleo Motor 10W40', 'Semissintético 10W40', 28.00, 64.90, TRUE
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE codigo_interno='E3-P002');

-- ==========================
-- SERVIÇOS (amostra)
-- ==========================
INSERT INTO servicos (empresa_id, categoria_id, codigo, nome, descricao, preco_base, tempo_execucao_minutos, ativo)
SELECT 1, (SELECT id FROM categorias_servicos WHERE codigo='E1-SERV-MEC'), 'E1-S001', 'Troca de Óleo', 'Troca de óleo padrão', 89.90, 60, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE codigo='E1-S001');

INSERT INTO servicos (empresa_id, categoria_id, codigo, nome, descricao, preco_base, tempo_execucao_minutos, ativo)
SELECT 2, (SELECT id FROM categorias_servicos WHERE codigo='E2-SERV-ELE'), 'E2-S001', 'Diagnóstico Elétrico', 'Diagnóstico completo', 149.90, 120, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE codigo='E2-S001');

INSERT INTO servicos (empresa_id, categoria_id, codigo, nome, descricao, preco_base, tempo_execucao_minutos, ativo)
SELECT 3, (SELECT id FROM categorias_servicos WHERE codigo='E3-SERV-MEC'), 'E3-S001', 'Revisão Completa', 'Revisão 40 itens', 399.90, 180, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos WHERE codigo='E3-S001');

-- ==========================
-- TABELAS DE PREÇOS
-- ==========================
INSERT INTO tabelas_precos (empresa_id, nome, descricao, tipo_tabela, margem_lucro_padrao, padrao, ativo)
SELECT 1, 'Tabela Padrão', 'Preços padrão da empresa', 'PADRAO', 20.00, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM tabelas_precos WHERE empresa_id=1 AND nome='Tabela Padrão');
INSERT INTO tabelas_precos (empresa_id, nome, descricao, tipo_tabela, margem_lucro_padrao, padrao, ativo)
SELECT 2, 'Tabela Padrão', 'Preços padrão da empresa', 'PADRAO', 20.00, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM tabelas_precos WHERE empresa_id=2 AND nome='Tabela Padrão');
INSERT INTO tabelas_precos (empresa_id, nome, descricao, tipo_tabela, margem_lucro_padrao, padrao, ativo)
SELECT 3, 'Tabela Padrão', 'Preços padrão da empresa', 'PADRAO', 20.00, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM tabelas_precos WHERE empresa_id=3 AND nome='Tabela Padrão');

-- ==========================
-- FINANCEIRO: CONTAS BANCÁRIAS / FORMAS / CONDIÇÕES
-- ==========================
INSERT INTO fin_contas_bancarias (empresa_id, banco_codigo, banco_nome, agencia, conta, titular_conta, cpf_cnpj_titular, saldo_atual, principal, ativo)
SELECT 1, '001', 'Banco do Brasil', '0001', '12345-6', 'Oficina Alpha LTDA', '10000000000001', 5000.00, TRUE, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM fin_contas_bancarias 
    WHERE empresa_id=1 AND banco_codigo='001' AND agencia='0001' AND conta='12345-6'
);
INSERT INTO fin_contas_bancarias (empresa_id, banco_codigo, banco_nome, agencia, conta, titular_conta, cpf_cnpj_titular, saldo_atual, principal, ativo)
SELECT 2, '237', 'Bradesco', '0002', '65432-1', 'Oficina Beta LTDA', '10000000000002', 7000.00, TRUE, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM fin_contas_bancarias 
    WHERE empresa_id=2 AND banco_codigo='237' AND agencia='0002' AND conta='65432-1'
);
INSERT INTO fin_contas_bancarias (empresa_id, banco_codigo, banco_nome, agencia, conta, titular_conta, cpf_cnpj_titular, saldo_atual, principal, ativo)
SELECT 3, '341', 'Itaú', '0003', '77777-7', 'Oficina Gama LTDA', '10000000000003', 9000.00, TRUE, TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM fin_contas_bancarias 
    WHERE empresa_id=3 AND banco_codigo='341' AND agencia='0003' AND conta='77777-7'
);

INSERT INTO fin_formas_pagamento (empresa_id, nome, tipo, aceita_parcelamento, parcelas_maximas, padrao, ativo)
SELECT 1, 'Dinheiro', 'DINHEIRO', FALSE, 1, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Dinheiro');
INSERT INTO fin_formas_pagamento (empresa_id, nome, tipo, aceita_parcelamento, parcelas_maximas, padrao, ativo)
SELECT 1, 'Pix', 'PIX', FALSE, 1, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Pix');
INSERT INTO fin_formas_pagamento (empresa_id, nome, tipo, aceita_parcelamento, parcelas_maximas, padrao, ativo)
SELECT 1, 'Cartão de Crédito', 'CARTAO_CREDITO', TRUE, 12, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Cartão de Crédito');
INSERT INTO fin_formas_pagamento (empresa_id, nome, tipo, aceita_parcelamento, parcelas_maximas, padrao, ativo)
SELECT 2, 'Dinheiro', 'DINHEIRO', FALSE, 1, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_formas_pagamento WHERE empresa_id=2 AND nome='Dinheiro');
INSERT INTO fin_formas_pagamento (empresa_id, nome, tipo, aceita_parcelamento, parcelas_maximas, padrao, ativo)
SELECT 3, 'Dinheiro', 'DINHEIRO', FALSE, 1, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_formas_pagamento WHERE empresa_id=3 AND nome='Dinheiro');

INSERT INTO fin_condicoes_pagamento (empresa_id, nome, tipo, numero_parcelas, intervalo_dias, valor_entrada_percentual, padrao, ativo)
SELECT 1, 'À Vista', 'AVISTA', 1, 0, 0, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_condicoes_pagamento WHERE empresa_id=1 AND nome='À Vista');
INSERT INTO fin_condicoes_pagamento (empresa_id, nome, tipo, numero_parcelas, intervalo_dias, valor_entrada_percentual, padrao, ativo)
SELECT 1, '3x Sem Juros', 'PARCELADO', 3, 30, 0, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_condicoes_pagamento WHERE empresa_id=1 AND nome='3x Sem Juros');
INSERT INTO fin_condicoes_pagamento (empresa_id, nome, tipo, numero_parcelas, intervalo_dias, valor_entrada_percentual, padrao, ativo)
SELECT 2, 'À Vista', 'AVISTA', 1, 0, 0, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_condicoes_pagamento WHERE empresa_id=2 AND nome='À Vista');
INSERT INTO fin_condicoes_pagamento (empresa_id, nome, tipo, numero_parcelas, intervalo_dias, valor_entrada_percentual, padrao, ativo)
SELECT 3, 'À Vista', 'AVISTA', 1, 0, 0, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM fin_condicoes_pagamento WHERE empresa_id=3 AND nome='À Vista');

-- ==========================
-- STATUS DE OS
-- ==========================
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 1, 'Aberta', 'ABERTA', '#3B82F6', 1, TRUE, FALSE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=1 AND codigo='ABERTA');
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 1, 'Concluída', 'CONCLUIDA', '#059669', 7, TRUE, TRUE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=1 AND codigo='CONCLUIDA');
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 2, 'Aberta', 'ABERTA', '#3B82F6', 1, TRUE, FALSE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=2 AND codigo='ABERTA');
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 2, 'Concluída', 'CONCLUIDA', '#059669', 7, TRUE, TRUE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=2 AND codigo='CONCLUIDA');
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 3, 'Aberta', 'ABERTA', '#3B82F6', 1, TRUE, FALSE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=3 AND codigo='ABERTA');
INSERT INTO status_os (empresa_id, nome, codigo, cor_identificacao, ordem_exibicao, sistema, finaliza_os, cancela_os, criado_por)
SELECT 3, 'Concluída', 'CONCLUIDA', '#059669', 7, TRUE, TRUE, FALSE, 1
WHERE NOT EXISTS (SELECT 1 FROM status_os WHERE empresa_id=3 AND codigo='CONCLUIDA');

-- ==========================
-- ORDENS DE SERVIÇO
-- ==========================
INSERT INTO ordens_servico (empresa_id, numero_os, cliente_id, veiculo_id, status_id, tipo_os, prioridade, data_abertura, valor_servicos, valor_produtos, valor_total, forma_pagamento_id, condicao_pagamento_id, aprovado_cliente, metodo_aprovacao)
SELECT
    1,
    'E1-OS-0001',
    (SELECT id FROM cliente WHERE empresa_id=1 LIMIT 1),
    (SELECT id FROM veiculos WHERE empresa_id=1 LIMIT 1),
    (SELECT id FROM status_os WHERE empresa_id=1 AND codigo='ABERTA' LIMIT 1),
    'MANUTENCAO',
    'NORMAL',
    '2024-04-11 09:00:00',
    219.80,
    99.90,
    319.70,
    (SELECT id FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Dinheiro'),
    (SELECT id FROM fin_condicoes_pagamento WHERE empresa_id=1 AND nome='À Vista'),
    TRUE,
    'PRESENCIAL'
WHERE NOT EXISTS (SELECT 1 FROM ordens_servico WHERE empresa_id=1 AND numero_os='E1-OS-0001');

-- ==========================
-- ITENS DE OS – SERVIÇOS
-- ==========================
INSERT INTO itens_os_servicos (ordem_servico_id, servico_id, descricao, quantidade, valor_unitario, valor_total, valor_final, status_execucao)
SELECT
    (SELECT id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    (SELECT id FROM servicos WHERE codigo='E1-S001'),
    'Troca de Óleo 5W30', 1, 89.90, 89.90, 89.90, 'PENDENTE'
WHERE NOT EXISTS (SELECT 1 FROM itens_os_servicos WHERE ordem_servico_id=(SELECT id FROM ordens_servico WHERE numero_os='E1-OS-0001') AND servico_id=(SELECT id FROM servicos WHERE codigo='E1-S001'));

-- ==========================
-- ITENS DE OS – PRODUTOS
-- ==========================
INSERT INTO itens_os_produtos (ordem_servico_id, produto_id, descricao, quantidade, valor_unitario, valor_total, valor_final, lote_numero, fornecedor_id)
SELECT
    (SELECT id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    (SELECT id FROM produtos WHERE codigo_interno='E1-P002'),
    'Óleo Motor 5W30', 2, 59.90, 119.80, 119.80, 'L5W30-2404', (SELECT id FROM fornecedores WHERE empresa_id=1 LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM itens_os_produtos WHERE ordem_servico_id=(SELECT id FROM ordens_servico WHERE numero_os='E1-OS-0001') AND produto_id=(SELECT id FROM produtos WHERE codigo_interno='E1-P002'));

-- ==========================
-- ESTOQUE: LOCALIZAÇÕES
-- ==========================
INSERT INTO localizacoes_estoque (empresa_id, codigo, nome, descricao, tipo_localizacao, setor, corredor, prateleira, posicao, ativo)
SELECT 1, 'E1-PRAT-A1', 'Prateleira A1', 'Prateleira principal', 'PRATELEIRA', 'Estoque', 'A', '1', '1', TRUE
WHERE NOT EXISTS (SELECT 1 FROM localizacoes_estoque WHERE empresa_id=1 AND codigo='E1-PRAT-A1');
INSERT INTO localizacoes_estoque (empresa_id, codigo, nome, descricao, tipo_localizacao, setor, corredor, prateleira, posicao, ativo)
SELECT 2, 'E2-PRAT-B1', 'Prateleira B1', 'Prateleira secundária', 'PRATELEIRA', 'Estoque', 'B', '1', '1', TRUE
WHERE NOT EXISTS (SELECT 1 FROM localizacoes_estoque WHERE empresa_id=2 AND codigo='E2-PRAT-B1');
INSERT INTO localizacoes_estoque (empresa_id, codigo, nome, descricao, tipo_localizacao, setor, corredor, prateleira, posicao, ativo)
SELECT 3, 'E3-PRAT-C1', 'Prateleira C1', 'Prateleira principal', 'PRATELEIRA', 'Estoque', 'C', '1', '1', TRUE
WHERE NOT EXISTS (SELECT 1 FROM localizacoes_estoque WHERE empresa_id=3 AND codigo='E3-PRAT-C1');

-- ==========================
-- ESTOQUE: MOVIMENTAÇÕES
-- ==========================
INSERT INTO movimentacoes_estoque (empresa_id, produto_id, tipo_movimentacao, subtipo_movimentacao, quantidade, valor_unitario, valor_total, data_movimentacao, documento_tipo, documento_numero, fornecedor_id, localizacao_destino_id, observacoes, usuario_responsavel)
SELECT
    1,
    (SELECT id FROM produtos WHERE codigo_interno='E1-P002'),
    'ENTRADA', 'COMPRA', 20, 45.00, 900.00, '2024-04-10', 'NF', '000123', (SELECT id FROM fornecedores WHERE empresa_id=1 LIMIT 1),
    (SELECT id FROM localizacoes_estoque WHERE empresa_id=1 AND codigo='E1-PRAT-A1'),
    'Compra de óleo 5W30',
    1
WHERE NOT EXISTS (SELECT 1 FROM movimentacoes_estoque WHERE documento_tipo='NF' AND documento_numero='000123' AND produto_id=(SELECT id FROM produtos WHERE codigo_interno='E1-P002'));

-- ==========================
-- FINANCEIRO: FATURA, RECEBER, PAGAMENTO, FLUXO
-- ==========================
INSERT INTO fin_faturas (empresa_id, cliente_id, ordem_servico_id, numero_fatura, data_emissao, data_vencimento, valor_total, forma_pagamento_id, condicao_pagamento_id, status)
SELECT
    1,
    (SELECT cliente_id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    (SELECT id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    'E1-FAT-0001',
    '2024-04-11',
    '2024-04-11',
    319.70,
    (SELECT id FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Dinheiro'),
    (SELECT id FROM fin_condicoes_pagamento WHERE empresa_id=1 AND nome='À Vista'),
    'EMITIDA'
WHERE NOT EXISTS (SELECT 1 FROM fin_faturas WHERE numero_fatura='E1-FAT-0001');

INSERT INTO fin_contas_receber (empresa_id, numero_titulo, cliente_id, fatura_id, tipo_titulo, data_emissao, data_vencimento, valor_nominal, status)
SELECT
    1,
    'E1-CR-0001',
    (SELECT cliente_id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    (SELECT id FROM fin_faturas WHERE numero_fatura='E1-FAT-0001'),
    'OS',
    '2024-04-11',
    '2024-04-11',
    319.70,
    'ABERTO'
WHERE NOT EXISTS (SELECT 1 FROM fin_contas_receber WHERE numero_titulo='E1-CR-0001');

INSERT INTO fin_pagamentos (empresa_id, numero_pagamento, fatura_id, cliente_id, forma_pagamento_id, valor_pago, data_pagamento, status, processado_por)
SELECT
    1,
    'E1-PAG-0001',
    (SELECT id FROM fin_faturas WHERE numero_fatura='E1-FAT-0001'),
    (SELECT cliente_id FROM ordens_servico WHERE numero_os='E1-OS-0001'),
    (SELECT id FROM fin_formas_pagamento WHERE empresa_id=1 AND nome='Dinheiro'),
    319.70,
    '2024-04-11 11:30:00',
    'CONFIRMADO',
    1
WHERE NOT EXISTS (SELECT 1 FROM fin_pagamentos WHERE numero_pagamento='E1-PAG-0001');

INSERT INTO fin_parcelas_pagamento (pagamento_id, numero_parcela, valor_parcela, data_vencimento, data_pagamento, valor_pago, status)
SELECT
    (SELECT id FROM fin_pagamentos WHERE numero_pagamento='E1-PAG-0001'),
    1,
    319.70,
    '2024-04-11',
    '2024-04-11',
    319.70,
    'PAGA'
WHERE NOT EXISTS (SELECT 1 FROM fin_parcelas_pagamento WHERE pagamento_id=(SELECT id FROM fin_pagamentos WHERE numero_pagamento='E1-PAG-0001') AND numero_parcela=1);

INSERT INTO fin_fluxo_caixa (empresa_id, conta_bancaria_id, tipo_movimentacao, categoria, descricao, valor, data_movimentacao, documento_tipo, documento_numero, usuario_responsavel)
SELECT
    1,
    (SELECT id FROM fin_contas_bancarias WHERE empresa_id=1 AND principal=TRUE),
    'ENTRADA',
    'VENDAS',
    'Recebimento OS E1-OS-0001',
    319.70,
    '2024-04-11',
    'FATURA',
    'E1-FAT-0001',
    1
WHERE NOT EXISTS (SELECT 1 FROM fin_fluxo_caixa WHERE documento_tipo='FATURA' AND documento_numero='E1-FAT-0001');

-- ==========================
-- AGENDA: AGENDAMENTOS
-- ==========================
INSERT INTO agd_agendamentos (empresa_id, numero_agendamento, cliente_id, veiculo_id, tipo_agendamento_id, data_agendamento, hora_inicio, hora_fim, duracao_estimada_minutos, servicos_solicitados, problema_relatado, status, confirmado_cliente, canal_agendamento, agendado_por)
SELECT
    1,
    'E1-AGD-0001',
    (SELECT id FROM cliente WHERE empresa_id=1 LIMIT 1),
    (SELECT id FROM veiculos WHERE empresa_id=1 LIMIT 1),
    (SELECT id FROM agd_tipos_agendamento WHERE empresa_id=1 AND nome='Troca de Óleo'),
    '2024-04-15', '09:00', '10:00', 60,
    '"[\"Troca de Óleo\"]"',
    'Solicita troca de óleo',
    'AGENDADO',
    TRUE,
    'WHATSAPP',
    1
WHERE NOT EXISTS (SELECT 1 FROM agd_agendamentos WHERE numero_agendamento='E1-AGD-0001');

-- ==========================
-- AGENDA: RECURSOS
-- ==========================
INSERT INTO agd_recursos_agenda (empresa_id, nome_recurso, tipo_recurso, descricao, capacidade_simultanea, disponivel, requer_agendamento, custo_hora)
SELECT 1, 'Elevador 1', 'ELEVADOR', 'Elevador hidráulico', 1, TRUE, TRUE, 0
WHERE NOT EXISTS (SELECT 1 FROM agd_recursos_agenda WHERE empresa_id=1 AND nome_recurso='Elevador 1');
INSERT INTO agd_recursos_agenda (empresa_id, nome_recurso, tipo_recurso, descricao, capacidade_simultanea, disponivel, requer_agendamento, custo_hora)
SELECT 2, 'Elevador 2', 'ELEVADOR', 'Elevador hidráulico', 1, TRUE, TRUE, 0
WHERE NOT EXISTS (SELECT 1 FROM agd_recursos_agenda WHERE empresa_id=2 AND nome_recurso='Elevador 2');
INSERT INTO agd_recursos_agenda (empresa_id, nome_recurso, tipo_recurso, descricao, capacidade_simultanea, disponivel, requer_agendamento, custo_hora)
SELECT 3, 'Elevador 1', 'ELEVADOR', 'Elevador hidráulico', 1, TRUE, TRUE, 0
WHERE NOT EXISTS (SELECT 1 FROM agd_recursos_agenda WHERE empresa_id=3 AND nome_recurso='Elevador 1');

-- ==========================
-- AGENDA: FUNCIONÁRIOS (amostra)
-- ==========================
INSERT INTO funcionarios (empresa_id, usuario_id, matricula, nome_completo, cpf, data_admissao, status)
SELECT 1, NULL, 'E1-FUNC-001', 'Carlos Mecânico', '11122233344', '2024-01-10', 'ATIVO'
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf='11122233344');
INSERT INTO funcionarios (empresa_id, usuario_id, matricula, nome_completo, cpf, data_admissao, status)
SELECT 2, NULL, 'E2-FUNC-001', 'Paulo Eletricista', '22233344455', '2024-02-12', 'ATIVO'
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf='22233344455');
INSERT INTO funcionarios (empresa_id, usuario_id, matricula, nome_completo, cpf, data_admissao, status)
SELECT 3, NULL, 'E3-FUNC-001', 'Ana Revisora', '33344455566', '2024-03-15', 'ATIVO'
WHERE NOT EXISTS (SELECT 1 FROM funcionarios WHERE cpf='33344455566');

-- ==========================
-- AGENDA: DISPONIBILIDADE
-- ==========================
INSERT INTO agd_disponibilidade_agenda (empresa_id, funcionario_id, data_disponibilidade, dia_semana, hora_inicio, hora_fim, disponivel, capacidade_atendimentos)
SELECT 1, (SELECT id FROM funcionarios WHERE empresa_id=1 LIMIT 1), '2024-04-15', 1, '08:00', '18:00', TRUE, 4
WHERE NOT EXISTS (SELECT 1 FROM agd_disponibilidade_agenda WHERE empresa_id=1 AND data_disponibilidade='2024-04-15');

-- ==========================
-- COMUNICAÇÃO: CONFIGURAÇÕES DE EMAIL
-- ==========================
INSERT INTO com_configuracoes_email (empresa_id, provedor_servico, servidor_smtp, porta_smtp, usuario_smtp, senha_smtp, criptografia, remetente_nome, remetente_email, ativo, data_configuracao)
SELECT 1, 'SMTP_CUSTOMIZADO', 'smtp.alpha.com', 587, 'noreply@alpha.com', '***', 'STARTTLS', 'Oficina Alpha', 'noreply@alpha.com', TRUE, '2024-04-01 08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM com_configuracoes_email WHERE empresa_id=1);
INSERT INTO com_configuracoes_email (empresa_id, provedor_servico, servidor_smtp, porta_smtp, usuario_smtp, senha_smtp, criptografia, remetente_nome, remetente_email, ativo, data_configuracao)
SELECT 2, 'SMTP_CUSTOMIZADO', 'smtp.beta.com', 587, 'noreply@beta.com', '***', 'STARTTLS', 'Oficina Beta', 'noreply@beta.com', TRUE, '2024-05-01 08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM com_configuracoes_email WHERE empresa_id=2);
INSERT INTO com_configuracoes_email (empresa_id, provedor_servico, servidor_smtp, porta_smtp, usuario_smtp, senha_smtp, criptografia, remetente_nome, remetente_email, ativo, data_configuracao)
SELECT 3, 'SMTP_CUSTOMIZADO', 'smtp.gama.com', 587, 'noreply@gama.com', '***', 'STARTTLS', 'Oficina Gama', 'noreply@gama.com', TRUE, '2024-06-01 08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM com_configuracoes_email WHERE empresa_id=3);

-- ==========================
-- COMUNICAÇÃO: LISTAS DE CONTATOS
-- ==========================
INSERT INTO com_listas_contatos (empresa_id, nome, descricao, tipo_lista, ativa, criada_por)
SELECT 1, 'Clientes Ativos', 'Lista de clientes ativos', 'ESTATICA', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM com_listas_contatos WHERE empresa_id=1 AND nome='Clientes Ativos');
INSERT INTO com_listas_contatos (empresa_id, nome, descricao, tipo_lista, ativa, criada_por)
SELECT 2, 'Clientes Ativos', 'Lista de clientes ativos', 'ESTATICA', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM com_listas_contatos WHERE empresa_id=2 AND nome='Clientes Ativos');
INSERT INTO com_listas_contatos (empresa_id, nome, descricao, tipo_lista, ativa, criada_por)
SELECT 3, 'Clientes Ativos', 'Lista de clientes ativos', 'ESTATICA', TRUE, 1
WHERE NOT EXISTS (SELECT 1 FROM com_listas_contatos WHERE empresa_id=3 AND nome='Clientes Ativos');

-- ==========================
-- COMUNICAÇÃO: ENVIOS
-- ==========================
INSERT INTO com_comunicacoes_enviadas (empresa_id, tipo_comunicacao, destinatario_tipo, destinatario_id, destinatario_nome, destinatario_contato, assunto, conteudo, status, data_envio)
SELECT 1, 'EMAIL', 'CLIENTE', id, nome_completo, 'cliente.alpha@example.com', 'Bem-vindo', 'Seja bem-vindo à Oficina Alpha!', 'ENVIADA', '2024-04-12 09:00:00'
FROM cliente WHERE empresa_id=1 LIMIT 1;

-- ==========================
-- EQUIPAMENTOS
-- ==========================
INSERT INTO equipamentos (empresa_id, codigo_equipamento, nome, tipo_equipamento, data_aquisicao, valor_aquisicao, fornecedor_id, status_operacional)
SELECT 1, 'EQ-ALFA-01', 'Elevador Hidráulico', 'ELEVADOR', '2023-12-10', 15000.00, (SELECT id FROM fornecedores WHERE empresa_id=1 LIMIT 1), 'OPERANTE'
WHERE NOT EXISTS (SELECT 1 FROM equipamentos WHERE empresa_id=1 AND codigo_equipamento='EQ-ALFA-01');

-- ==========================
-- MANUTENÇÕES DE EQUIPAMENTOS
-- ==========================
INSERT INTO manutencoes_equipamentos (equipamento_id, tipo_manutencao, descricao_servico, data_agendamento, status, prioridade)
SELECT (SELECT id FROM equipamentos WHERE codigo_equipamento='EQ-ALFA-01'), 'PREVENTIVA', 'Revisão de segurança', '2024-04-20', 'AGENDADA', 'NORMAL'
WHERE NOT EXISTS (SELECT 1 FROM manutencoes_equipamentos WHERE equipamento_id=(SELECT id FROM equipamentos WHERE codigo_equipamento='EQ-ALFA-01') AND data_agendamento='2024-04-20');

-- ==========================
-- EXPORTAÇÕES (exemplo)
-- ==========================
INSERT INTO exportacoes (empresa_id, usuario_solicitante, tipo_exportacao, formato_arquivo, nome_arquivo, total_registros, data_inicio, status)
SELECT 1, 1, 'CLIENTES', 'CSV', 'clientes_alpha_20240412.csv', 10, '2024-04-12 08:00:00', 'CONCLUIDA'
WHERE NOT EXISTS (SELECT 1 FROM exportacoes WHERE empresa_id=1 AND nome_arquivo='clientes_alpha_20240412.csv');

