package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.TipoIntegracao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfiguracaoWhatsappRequest(
        @NotNull Long empresaId,
        @NotNull TipoIntegracao tipoIntegracao,
        @Size(max = 100) String provedorServico,
        @NotBlank @Size(max = 20) String numeroWhatsapp,
        @Size(max = 500) String tokenAcesso,
        @Size(max = 500) String webhookUrl,
        @Size(max = 255) String webhookToken,
        @Size(max = 50) String phoneNumberId,
        @Size(max = 50) String businessAccountId,
        @Size(max = 50) String appId,
        @Size(max = 255) String appSecret,
        String mensagensTemplateAprovadas,
        LocalTime horarioAtendimentoInicio,
        LocalTime horarioAtendimentoFim,
        String mensagemForaHorario,
        String mensagemBoasVindas,
        String mensagemMenuPrincipal,
        Boolean chatbotAtivo,
        Boolean integracaoAtiva,
        Ambiente ambiente,
        String observacoes) {
}
