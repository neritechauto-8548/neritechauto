package com.neritech.saas.agendamento.domain.enums;


/**
 * Enum para status na lista de espera
 */
public enum StatusListaEspera {
    AGUARDANDO, // Aguardando vaga
    NOTIFICADO, // Cliente notificado sobre disponibilidade
    AGENDADO, // Agendamento realizado
    CANCELADO, // Cancelado pelo cliente
    EXPIRADO // Prazo expirado
}
