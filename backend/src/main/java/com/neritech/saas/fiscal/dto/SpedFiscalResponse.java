package com.neritech.saas.fiscal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SpedFiscalResponse(
        Long id,
        LocalDate periodoInicio,
        LocalDate periodoFim,
        String tipoArquivo,
        String status,
        String logProcessamento,
        LocalDateTime dataGeracao,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
