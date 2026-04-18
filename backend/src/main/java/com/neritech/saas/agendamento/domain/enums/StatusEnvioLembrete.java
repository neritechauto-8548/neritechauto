package com.neritech.saas.agendamento.domain.enums;


/**
 * Enum para status de envio de lembrete
 */
public enum StatusEnvioLembrete {
    AGENDADO, // Lembrete agendado para envio
    ENVIADO, // Lembrete enviado
    ENTREGUE, // Lembrete entregue ao destinatÃ¡rio
    FALHOU, // Falha no envio
    CANCELADO // Envio cancelado
}
