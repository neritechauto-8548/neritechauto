package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.ProvedorSms;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfiguracaoSmsRequest(
        @NotNull Long empresaId,
        @NotNull ProvedorSms provedorServico,
        @Size(max = 500) String apiEndpoint,
        @Size(max = 255) String apiKey,
        @Size(max = 255) String apiSecret,
        @Size(max = 100) String usuarioApi,
        @Size(max = 255) String senhaApi,
        @Size(max = 20) String remetentePadrao,
        Integer limiteCaracteres,
        BigDecimal custoPorSms,
        @Size(max = 500) String webhookEntrega,
        @Size(max = 500) String webhookResposta,
        Boolean ativa,
        Ambiente ambiente,
        String observacoes) {
}
