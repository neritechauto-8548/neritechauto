package com.neritech.saas.fiscal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SpedFiscalRequest(
        @NotNull(message = "O perÃ­odo de inÃ­cio Ã© obrigatÃ³rio") LocalDate periodoInicio,

        @NotNull(message = "O perÃ­odo de fim Ã© obrigatÃ³rio") LocalDate periodoFim,

        @NotBlank(message = "O tipo de arquivo Ã© obrigatÃ³rio") String tipoArquivo,

        String status,
        byte[] arquivoGerado,
        String logProcessamento) {
}
