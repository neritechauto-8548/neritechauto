package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DiagnosticoRequest(
        @NotNull Long ordemServicoId,
        SistemaVeiculo sistemaVeiculo,
        String componenteEspecifico,
        @NotBlank String problemaIdentificado,
        String causaProvavel,
        String solucaoRecomendada,
        UrgenciaDiagnostico urgencia,
        Boolean impactoSeguranca,
        Boolean impactoDirigibilidade,
        BigDecimal custoEstimado,
        Integer tempoEstimadoReparo,
        String ferramentasNecessarias,
        String pecasNecessarias,
        String evidenciasEncontradas,
        String testesRealizados,
        String codigoErro,
        Long mecanicoDiagnosticoId,
        String fotosDiagnostico,
        String videosDiagnostico,
        Boolean aprovadoCliente,
        String observacoes) {
}
