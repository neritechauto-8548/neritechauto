-- Um técnico não pode executar dois serviços simultaneamente, inclusive em abas/dispositivos distintos.
-- Apontamentos pausados continuam preservados e não impedem outra execução.
CREATE UNIQUE INDEX uq_patio_tecnico_em_execucao
    ON tempos_execucao (empresa_id, funcionario_id)
    WHERE estado = 'EM_EXECUCAO';
