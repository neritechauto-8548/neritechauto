package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.StatusReserva;
import com.neritech.saas.estoque.domain.enums.TipoReserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaEstoqueResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        BigDecimal quantidadeReservada,
        TipoReserva tipoReserva,
        Long documentoId,
        String documentoTipo,
        LocalDateTime dataReserva,
        LocalDateTime dataValidadeReserva,
        StatusReserva status,
        Long usuarioResponsavel,
        String observacoes,
        LocalDateTime dataUtilizacao,
        LocalDateTime dataCancelamento,
        String motivoCancelamento) {
}
