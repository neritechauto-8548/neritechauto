package com.neritech.saas.veiculo.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AnoModeloRequest(
                @NotNull(message = "O modelo Ã© obrigatÃ³rio") Long modeloId,

                @NotNull(message = "O ano de fabricaÃ§Ã£o Ã© obrigatÃ³rio") Integer anoFabricacao,

                @NotNull(message = "O ano do modelo Ã© obrigatÃ³rio") Integer anoModelo,

                String codigoFipe,
                BigDecimal valorFipe,
                LocalDate dataValorFipe) {
}
