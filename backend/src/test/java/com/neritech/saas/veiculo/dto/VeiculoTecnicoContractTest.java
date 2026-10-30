package com.neritech.saas.veiculo.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class VeiculoTecnicoContractTest {

    @Test
    void dadosTecnicosSaoPreservadosNoRecord() {
        var request = new VeiculoRequest(
            10L, 1L, 2L, 3L, 4L, "ABC1D23", null, null, null, "Prata",
            null, null, null, null, 14000, 10000,
            null, null, null, null, "observacao",
            "G5", "Frota 1", "Hatch", "Ar-condicionado",
            new BigDecimal("1.60"), 8, 4, "EA111", "CCRA",
            "101,104", "Hatch", "Natural", "ABS", "Dianteira",
            "Multiponto", "Inline 4", "Disco ventilado", "Tambor");
        assertEquals("G5", request.geracao());
        assertEquals(new BigDecimal("1.60"), request.cilindrada());
        assertEquals(8, request.quantidadeValvulas());
        assertEquals("Dianteira", request.sistemaTracao());
    }

    @Test
    void construtorAntigoNaoFabricaDadosTecnicos() {
        var request = new VeiculoRequest(
            10L, null, null, null, null, "ABC1D23", null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null);
        assertNull(request.cilindrada());
        assertNull(request.sistemaFreios());
    }
}
