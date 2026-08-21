package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.enums.StatusCliente;
import com.neritech.saas.cliente.domain.enums.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClienteListResponse", description = "Resumo de cliente seguro para listagens")
public record ClienteListResponse(
        @Schema(description = "Identificador interno do cliente") Long id,
        @Schema(description = "Nome de exibição") String displayName,
        @Schema(description = "Tipo do cliente") TipoCliente type,
        @Schema(description = "CPF/CNPJ mascarado") String maskedTaxId,
        @Schema(description = "Contato operacional resumido e mascarado") String primaryContactSummary,
        @Schema(description = "Status cadastral") StatusCliente status) {
}
