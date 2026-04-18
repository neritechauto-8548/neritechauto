package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.StatusCertificacao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CertificacaoRequest(
        @NotNull Long funcionarioId,
        @NotBlank @Size(max = 255) String nomeCertificacao,
        @NotBlank @Size(max = 255) String entidadeCertificadora,
        @Size(max = 100) String categoria,
        @Size(max = 100) String numeroCertificado,
        @NotNull LocalDate dataEmissao,
        LocalDate dataValidade,
        Boolean temValidade,
        Integer cargaHoraria,
        BigDecimal notaObtida,
        BigDecimal notaMinimaAprovacao,
        BigDecimal custoCertificacao,
        Boolean pagoPelaEmpresa,
        @Size(max = 500) String arquivoCertificadoUrl,
        StatusCertificacao status,
        Boolean reconhecidaEmpresa,
        BigDecimal adicionalSalarial,
        String observacoes) {
}
