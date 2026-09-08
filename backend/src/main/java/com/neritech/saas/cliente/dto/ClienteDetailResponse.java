package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.OrigemCliente;
import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClienteDetailResponse", description = "Identidade minimizada para a visão 360° do cliente")
public record ClienteDetailResponse(
        Long id,
        String displayName,
        TipoCliente type,
        StatusCliente status,
        String maskedTaxId,
        String maskedEmail,
        OrigemCliente origin,
        boolean hasRelationshipNotes) {
}
