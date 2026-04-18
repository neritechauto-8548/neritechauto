package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.StatusCertificacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CertificacaoResponse(
        Long id,
        Long funcionarioId,
        String nomeCertificacao,
        String entidadeCertificadora,
        String categoria,
        String numeroCertificado,
        LocalDate dataEmissao,
        LocalDate dataValidade,
        Boolean temValidade,
        Integer cargaHoraria,
        BigDecimal notaObtida,
        BigDecimal notaMinimaAprovacao,
        BigDecimal custoCertificacao,
        Boolean pagoPelaEmpresa,
        String arquivoCertificadoUrl,
        StatusCertificacao status,
        Boolean reconhecidaEmpresa,
        BigDecimal adicionalSalarial,
        String observacoes,
        Long cadastradoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
