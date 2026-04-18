package com.neritech.saas.fiscal.dto;

import java.time.LocalDateTime;

public record NotaFiscalResponse(
        Long id,
        Long empresaId,
        Long ordemServicoId,
        Long numero,
        String serie,
        LocalDateTime dataEmissao,
        String status,
        String downloadUrl
) {}
