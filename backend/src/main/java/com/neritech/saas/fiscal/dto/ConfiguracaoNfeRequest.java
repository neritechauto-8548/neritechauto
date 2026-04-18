package com.neritech.saas.fiscal.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import jakarta.validation.constraints.NotNull;

public record ConfiguracaoNfeRequest(
        @NotNull(message = "O ambiente Ã© obrigatÃ³rio") Ambiente ambiente,

        @NotNull(message = "A sÃ©rie Ã© obrigatÃ³ria") Integer serie,

        @NotNull(message = "O prÃ³ximo nÃºmero Ã© obrigatÃ³rio") Long proximoNumero,

        Long certificadoDigitalId,
        String versaoLayout,
        boolean enviarEmailDestinatario,
        boolean salvarXml) {
}
