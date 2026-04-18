-- V89: Create views for reporting
-- Description: Materialized and regular views for Service Orders reporting

-- View: Summary of service orders by status
CREATE OR REPLACE VIEW vw_ordens_servico_por_status AS
SELECT 
    os.empresa_id,
    s.nome AS status_nome,
    s.codigo AS status_codigo,
    COUNT(*) AS total_ordens,
    SUM(os.valor_total) AS valor_total,
    AVG(os.valor_total) AS valor_medio,
    SUM(CASE WHEN os.aprovado_cliente THEN 1 ELSE 0 END) AS total_aprovadas,
    AVG(os.tempo_total_execucao_minutos) AS tempo_medio_execucao
FROM ordens_servico os
INNER JOIN status_os s ON os.status_id = s.id
GROUP BY os.empresa_id, s.id, s.nome, s.codigo;

COMMENT ON VIEW vw_ordens_servico_por_status IS 'Resumo de ordens de serviço agrupadas por status';

-- View: Service orders with customer evaluation
CREATE OR REPLACE VIEW vw_ordens_servico_com_avaliacao AS
SELECT 
    os.id,
    os.numero_os,
    os.empresa_id,
    os.cliente_id,
    os.veiculo_id,
    os.data_abertura,
    os.data_entrega,
    os.valor_total,
    av.nota_geral,
    av.nota_atendimento,
    av.nota_qualidade_servico,
    av.nota_prazo_entrega,
    av.comentario_positivo,
    av.comentario_negativo,
    av.recomendaria_oficina,
    av.data_avaliacao
FROM ordens_servico os
LEFT JOIN avaliacoes_servico av ON os.id = av.ordem_servico_id;

COMMENT ON VIEW vw_ordens_servico_com_avaliacao IS 'Ordens de serviço com suas avaliações de clientes';

-- View: Pending approvals summary
CREATE OR REPLACE VIEW vw_aprovacoes_pendentes AS
SELECT 
    ac.id,
    ac.ordem_servico_id,
    os.numero_os,
    os.empresa_id,
    ac.tipo_aprovacao,
    ac.descricao,
    ac.valor_aprovado,
    ac.data_solicitacao,
    ac.data_expiracao,
    ac.metodo_aprovacao,
    EXTRACT(EPOCH FROM (ac.data_expiracao - CURRENT_TIMESTAMP))/3600 AS horas_ate_expiracao
FROM aprovacoes_cliente ac
INNER JOIN ordens_servico os ON ac.ordem_servico_id = os.id
WHERE ac.status = 'PENDENTE'
  AND ac.data_expiracao > CURRENT_TIMESTAMP
ORDER BY ac.data_expiracao ASC;

COMMENT ON VIEW vw_aprovacoes_pendentes IS 'Aprovações pendentes com tempo restante até expiração';

-- View: Diagnostics summary
CREATE OR REPLACE VIEW vw_diagnosticos_resumo AS
SELECT 
    d.ordem_servico_id,
    os.numero_os,
    os.empresa_id,
    COUNT(*) AS total_diagnosticos,
    SUM(CASE WHEN d.urgencia = 'CRITICA' THEN 1 ELSE 0 END) AS total_criticos,
    SUM(CASE WHEN d.impacto_seguranca THEN 1 ELSE 0 END) AS total_impacto_seguranca,
    SUM(CASE WHEN d.aprovado_cliente THEN 1 ELSE 0 END) AS total_aprovados,
    SUM(d.custo_estimado) AS custo_total_estimado
FROM diagnosticos d
INNER JOIN ordens_servico os ON d.ordem_servico_id = os.id
GROUP BY d.ordem_servico_id, os.numero_os, os.empresa_id;

COMMENT ON VIEW vw_diagnosticos_resumo IS 'Resumo de diagnósticos por ordem de serviço';

-- View: Workflow execution status
CREATE OR REPLACE VIEW vw_workflow_execucao_status AS
SELECT 
    ew.ordem_servico_id,
    os.numero_os,
    os.empresa_id,
    w.nome AS workflow_nome,
    et.nome AS etapa_nome,
    et.ordem_execucao,
    ew.status,
    ew.data_inicio,
    ew.data_fim,
    ew.tempo_execucao_real_minutos,
    et.tempo_estimado_minutos,
    CASE 
        WHEN ew.tempo_execucao_real_minutos IS NOT NULL AND et.tempo_estimado_minutos IS NOT NULL
        THEN ROUND((ew.tempo_execucao_real_minutos::DECIMAL / et.tempo_estimado_minutos) * 100, 2)
        ELSE NULL
    END AS percentual_tempo_estimado
FROM execucoes_workflow ew
INNER JOIN ordens_servico os ON ew.ordem_servico_id = os.id
INNER JOIN etapas_workflow et ON ew.etapa_workflow_id = et.id
INNER JOIN workflow_os w ON et.workflow_id = w.id;

COMMENT ON VIEW vw_workflow_execucao_status IS 'Status de execução de workflows com comparação de tempo estimado vs real';
