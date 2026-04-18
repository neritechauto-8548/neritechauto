-- V105: Create HR indexes and views
-- Description: Performance indexes and reporting views for HR module

-- ============================================
-- PERFORMANCE INDEXES
-- ============================================

-- Composite indexes for common queries
CREATE INDEX idx_funcionarios_empresa_status ON funcionarios(empresa_id, status);
CREATE INDEX idx_funcionarios_cargo_departamento ON funcionarios(cargo_id, departamento_id);
CREATE INDEX idx_funcionarios_data_admissao_status ON funcionarios(data_admissao, status);

CREATE INDEX idx_comissoes_func_periodo ON comissoes(funcionario_id, periodo_referencia);
CREATE INDEX idx_comissoes_empresa_periodo ON comissoes(empresa_id, periodo_referencia, status_pagamento);

CREATE INDEX idx_faltas_func_periodo ON faltas_atrasos(funcionario_id, data_ocorrencia);
CREATE INDEX idx_ferias_func_periodo ON ferias_funcionarios(funcionario_id, periodo_aquisitivo_inicio);

CREATE INDEX idx_avaliacoes_func_periodo ON avaliacoes_funcionarios(funcionario_id, periodo_avaliacao);
CREATE INDEX idx_certificacoes_func_validade ON certificacoes(funcionario_id, data_validade, status);

-- Full-text search indexes
CREATE INDEX idx_funcionarios_nome_gin ON funcionarios USING gin(to_tsvector('portuguese', nome_completo));
CREATE INDEX idx_cargos_nome_gin ON cargos USING gin(to_tsvector('portuguese', nome));
CREATE INDEX idx_departamentos_nome_gin ON departamentos USING gin(to_tsvector('portuguese', nome));

-- ============================================
-- REPORTING VIEWS
-- ============================================

-- View: Funcionários ativos com cargo e departamento
CREATE OR REPLACE VIEW vw_funcionarios_ativos AS
SELECT 
    f.id,
    f.empresa_id,
    f.matricula,
    f.nome_completo,
    f.cpf,
    f.data_admissao,
    f.status,
    c.nome AS cargo_nome,
    d.nome AS departamento_nome,
    f.salario_base,
    f.telefone_principal,
    f.email_pessoal
FROM funcionarios f
LEFT JOIN cargos c ON f.cargo_id = c.id
LEFT JOIN departamentos d ON f.departamento_id = d.id
WHERE f.status = 'ATIVO';

-- View: Comissões pendentes de pagamento
CREATE OR REPLACE VIEW vw_comissoes_pendentes AS
SELECT 
    c.id,
    c.empresa_id,
    f.nome_completo AS funcionario_nome,
    f.matricula,
    c.tipo_comissao,
    c.periodo_referencia,
    c.valor_comissao,
    c.valor_liquido,
    c.status_pagamento,
    c.data_competencia
FROM comissoes c
INNER JOIN funcionarios f ON c.funcionario_id = f.id
WHERE c.status_pagamento IN ('CALCULADA', 'APROVADA')
ORDER BY c.data_competencia DESC;

-- View: Certificações próximas do vencimento (30 dias)
CREATE OR REPLACE VIEW vw_certificacoes_vencendo AS
SELECT 
    c.id,
    f.nome_completo AS funcionario_nome,
    f.matricula,
    c.nome_certificacao,
    c.entidade_certificadora,
    c.data_validade,
    (c.data_validade - CURRENT_DATE) AS dias_para_vencer
FROM certificacoes c
INNER JOIN funcionarios f ON c.funcionario_id = f.id
WHERE c.status = 'VALIDA'
  AND c.tem_validade = TRUE
  AND c.data_validade BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days'
ORDER BY c.data_validade;

-- View: Férias programadas por período
CREATE OR REPLACE VIEW vw_ferias_programadas AS
SELECT 
    f.id,
    func.empresa_id,
    func.nome_completo AS funcionario_nome,
    func.matricula,
    d.nome AS departamento_nome,
    f.data_inicio_ferias,
    f.data_fim_ferias,
    f.dias_usufruir,
    f.status,
    sub.nome_completo AS substituto_nome
FROM ferias_funcionarios f
INNER JOIN funcionarios func ON f.funcionario_id = func.id
LEFT JOIN departamentos d ON func.departamento_id = d.id
LEFT JOIN funcionarios sub ON f.substituido_por = sub.id
WHERE f.status IN ('PROGRAMADAS', 'EM_ANDAMENTO')
ORDER BY f.data_inicio_ferias;

-- View: Avaliações de desempenho por período
CREATE OR REPLACE VIEW vw_avaliacoes_desempenho AS
SELECT 
    a.id,
    f.empresa_id,
    f.nome_completo AS funcionario_nome,
    f.matricula,
    c.nome AS cargo_nome,
    a.periodo_avaliacao,
    a.tipo_avaliacao,
    a.nota_geral,
    a.recomendacao,
    a.data_avaliacao,
    a.aprovada_rh,
    av.nome_completo AS avaliador_nome
FROM avaliacoes_funcionarios a
INNER JOIN funcionarios f ON a.funcionario_id = f.id
LEFT JOIN cargos c ON f.cargo_id = c.id
LEFT JOIN funcionarios av ON a.avaliador_id = av.id
ORDER BY a.data_avaliacao DESC;

-- View: Mecânicos disponíveis com especialidades
CREATE OR REPLACE VIEW vw_mecanicos_disponiveis AS
SELECT 
    m.id,
    f.empresa_id,
    f.nome_completo,
    m.codigo_mecanico,
    m.nivel_experiencia,
    m.anos_experiencia,
    m.produtividade_media,
    m.qualidade_media,
    m.avaliacao_media_cliente,
    m.total_os_executadas,
    m.percentual_retrabalho,
    m.ativo_execucao
FROM mecanicos m
INNER JOIN funcionarios f ON m.funcionario_id = f.id
WHERE m.ativo_execucao = TRUE
  AND f.status = 'ATIVO'
ORDER BY m.produtividade_media DESC;

-- Comments on views
COMMENT ON VIEW vw_funcionarios_ativos IS 'Funcionários ativos com informações de cargo e departamento';
COMMENT ON VIEW vw_comissoes_pendentes IS 'Comissões calculadas ou aprovadas aguardando pagamento';
COMMENT ON VIEW vw_certificacoes_vencendo IS 'Certificações que vencem nos próximos 30 dias';
COMMENT ON VIEW vw_ferias_programadas IS 'Férias programadas e em andamento';
COMMENT ON VIEW vw_avaliacoes_desempenho IS 'Histórico de avaliações de desempenho';
COMMENT ON VIEW vw_mecanicos_disponiveis IS 'Mecânicos ativos disponíveis para execução';
