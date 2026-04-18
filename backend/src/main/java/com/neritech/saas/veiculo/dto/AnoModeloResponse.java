package com.neritech.saas.veiculo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnoModeloResponse(
                Long id,
                Long modeloId,
                String modeloNome,
                Integer anoFabricacao,
                Integer anoModelo,
                String codigoFipe,
                BigDecimal valorFipe,
                LocalDate dataValorFipe) {
}
