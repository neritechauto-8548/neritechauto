-- V88: Create additional indexes for performance
-- Description: Performance optimization indexes for Service Orders module

-- Composite indexes for common queries
CREATE INDEX idx_ordens_servico_empresa_status ON ordens_servico(empresa_id, status_id);
CREATE INDEX idx_ordens_servico_empresa_tipo ON ordens_servico(empresa_id, tipo_os);
CREATE INDEX idx_ordens_servico_empresa_data ON ordens_servico(empresa_id, data_abertura DESC);
CREATE INDEX idx_ordens_servico_cliente_data ON ordens_servico(cliente_id, data_abertura DESC);
CREATE INDEX idx_ordens_servico_veiculo_data ON ordens_servico(veiculo_id, data_abertura DESC);

-- Indexes for items
CREATE INDEX idx_itens_os_servicos_os_status ON itens_os_servicos(ordem_servico_id, status_execucao);
CREATE INDEX idx_itens_os_produtos_os_produto ON itens_os_produtos(ordem_servico_id, produto_id);

-- Indexes for workflow
CREATE INDEX idx_execucoes_workflow_os_status ON execucoes_workflow(ordem_servico_id, status);
CREATE INDEX idx_execucoes_workflow_etapa_status ON execucoes_workflow(etapa_workflow_id, status);

-- Indexes for diagnostics
CREATE INDEX idx_diagnosticos_os_sistema ON diagnosticos(ordem_servico_id, sistema_veiculo);
CREATE INDEX idx_diagnosticos_aprovado ON diagnosticos(aprovado_cliente, data_diagnostico);

-- Indexes for quotes
CREATE INDEX idx_orcamentos_os_status ON orcamentos(ordem_servico_id, status);
CREATE INDEX idx_orcamentos_data_vencimento ON orcamentos(data_vencimento) WHERE status IN ('ENVIADO', 'RASCUNHO');

-- Indexes for approvals
CREATE INDEX idx_aprovacoes_cliente_os_status ON aprovacoes_cliente(ordem_servico_id, status);
CREATE INDEX idx_aprovacoes_cliente_data_expiracao ON aprovacoes_cliente(data_expiracao) WHERE status = 'PENDENTE';

-- Indexes for evaluations
CREATE INDEX idx_avaliacoes_servico_os_nota ON avaliacoes_servico(ordem_servico_id, nota_geral);
CREATE INDEX idx_avaliacoes_servico_data ON avaliacoes_servico(data_avaliacao DESC);
CREATE INDEX idx_avaliacoes_servico_publica_aprovada ON avaliacoes_servico(publica, aprovada_publicacao) WHERE publica = TRUE;

-- Full-text search indexes (PostgreSQL specific)
CREATE INDEX idx_ordens_servico_problema_fts ON ordens_servico USING gin(to_tsvector('portuguese', COALESCE(problema_relatado, '')));
CREATE INDEX idx_diagnosticos_problema_fts ON diagnosticos USING gin(to_tsvector('portuguese', COALESCE(problema_identificado, '')));

COMMENT ON INDEX idx_ordens_servico_empresa_status IS 'Composite index for filtering OS by company and status';
COMMENT ON INDEX idx_ordens_servico_problema_fts IS 'Full-text search index for problem descriptions';
