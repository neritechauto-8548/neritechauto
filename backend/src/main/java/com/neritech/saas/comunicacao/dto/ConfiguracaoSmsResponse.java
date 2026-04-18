package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.ProvedorSms;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConfiguracaoSmsResponse(
        Long id,
        Long empresaId,
        ProvedorSms provedorServico,
        String apiEndpoint,
        String apiKey, // Should be masked in real implementation, but returning as is for now or
                       // handled by mapper
        String apiSecret, // Should be masked
        String usuarioApi,
        String remetentePadrao,
        Integer limiteCaracteres,
        BigDecimal custoPorSms,
        Integer creditosDisponiveis,
        String webhookEntrega,
        String webhookResposta,
        Boolean ativo,
        Ambiente ambiente,
        String observacoes,
        LocalDateTime dataConfiguracao,
        Long configuradoPor,
        LocalDateTime dataUltimaSincronizacao) {
}
