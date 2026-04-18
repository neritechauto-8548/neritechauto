package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.SistemaVeiculo;
import com.neritech.saas.ordemservico.domain.enums.UrgenciaDiagnostico;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DiagnosticoResponse(
        Long id,
        Long ordemServicoId,
        SistemaVeiculo sistemaVeiculo,
        String componenteEspecifico,
        String problemaIdentificado,
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
        LocalDateTime dataDiagnostico,
        String fotosDiagnostico,
        String videosDiagnostico,
        Boolean aprovadoCliente,
        LocalDateTime dataAprovacaoCliente,
        String observacoes,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao) {
}
