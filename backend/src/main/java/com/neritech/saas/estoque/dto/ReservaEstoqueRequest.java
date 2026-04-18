package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.domain.enums.TipoReserva;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaEstoqueRequest(
        @NotNull Long produtoId,
        @NotNull BigDecimal quantidadeReservada,
        TipoReserva tipoReserva,
        @NotNull Long documentoId,
        @NotNull String documentoTipo,
        LocalDateTime dataValidadeReserva,
        StatusReserva status,
        Long usuarioResponsavel,
        String observacoes,
        String motivoCancelamento) {
}
