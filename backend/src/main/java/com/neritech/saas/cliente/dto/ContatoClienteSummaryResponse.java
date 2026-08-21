package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.TipoContato;

public record ContatoClienteSummaryResponse(
        Long id,
        TipoContato tipoContato,
        String maskedValue,
        boolean principal) {
}
