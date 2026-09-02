package com.neritech.saas.ordemservico.domain.enums;

/**
 * Estados operacionais documentados para serviços de uma Ordem de Serviço.
 * Transições relevantes devem ocorrer por comandos de domínio, nunca por edição livre de select.
 */
public enum StatusExecucao {
    NAO_INICIADO,
    PRONTO,
    PENDENTE,
    EM_EXECUCAO,
    PAUSADO,
    BLOQUEADO,
    CONCLUIDO,
    REPROVADO_QUALIDADE,
    REABERTO,
    CANCELADO
}
