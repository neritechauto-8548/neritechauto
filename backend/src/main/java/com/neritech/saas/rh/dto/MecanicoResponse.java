package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.CapacidadeDiagnostico;
import com.neritech.saas.rh.domain.enums.NivelExperiencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MecanicoResponse(
        Long id,
        Long funcionarioId,
        String codigoMecanico,
        NivelExperiencia nivelExperiencia,
        Integer anosExperiencia,
        String especialidadesPrincipais,
        String certificacoesAtivas,
        BigDecimal produtividadeMedia,
        BigDecimal qualidadeMedia,
        Integer tempoMedioServico,
        Integer totalOsExecutadas,
        Integer totalRetrabalho,
        BigDecimal percentualRetrabalho,
        BigDecimal avaliacaoMediaCliente,
        CapacidadeDiagnostico capacidadeDiagnostico,
        Boolean podeLiderarEquipe,
        Boolean autorizadoTestDrive,
        Boolean autorizadoEquipamentosEspeciais,
        BigDecimal metaProdutividadeMensal,
        BigDecimal comissaoOsPercentual,
        BigDecimal bonusQualidadePercentual,
        String observacoesTecnicas,
        Boolean ativoExecucao,
        LocalDate dataUltimaAvaliacao,
        LocalDate dataProximaAvaliacao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
