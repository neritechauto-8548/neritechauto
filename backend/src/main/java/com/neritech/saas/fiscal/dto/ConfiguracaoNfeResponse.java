package com.neritech.saas.fiscal.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import java.time.LocalDateTime;

public record ConfiguracaoNfeResponse(
        Long id,
        Ambiente ambiente,
        Integer serie,
        Long proximoNumero,
        CertificadoDigitalResponse certificadoDigital,
        String versaoLayout,
        boolean enviarEmailDestinatario,
        boolean salvarXml,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
