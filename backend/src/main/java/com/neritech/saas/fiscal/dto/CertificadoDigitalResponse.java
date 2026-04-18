package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record CertificadoDigitalResponse(
        Long id,
        String nomeArquivo,
        LocalDateTime dataValidade,
        String emissor,
        String serialNumber,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
