package com.neritech.saas.agendamento.domain.enums;


/**
 * Enum para status do agendamento
 */
public enum StatusAgendamento {
    AGENDADO, // Agendamento criado
    CONFIRMADO, // Confirmado pelo cliente
    EM_ANDAMENTO, // Atendimento em andamento
    CONCLUIDO, // Atendimento concluÃ­do
    CANCELADO, // Cancelado
    NAO_COMPARECEU, // Cliente nÃ£o compareceu
    REAGENDADO // Foi reagendado
}
