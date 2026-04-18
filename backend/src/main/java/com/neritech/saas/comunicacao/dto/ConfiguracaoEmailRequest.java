package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Criptografia;
import com.neritech.saas.comunicacao.domain.enums.ProvedorEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfiguracaoEmailRequest(
        @NotNull Long empresaId,
        @NotNull ProvedorEmail provedorServico,
        @Size(max = 255) String servidorSmtp,
        Integer portaSmtp,
        @Size(max = 255) String usuarioSmtp,
        @Size(max = 255) String senhaSmtp,
        Criptografia criptografia,
        @Size(max = 255) String remetenteNome,
        @Size(max = 255) String remetenteEmail,
        @Size(max = 255) String emailResposta,
        @Size(max = 255) String emailCopiaOculta,
        @Size(max = 255) String apiKey,
        @Size(max = 255) String apiSecret,
        @Size(max = 255) String dominioAutorizado,
        Integer limiteEnviosDia,
        @Size(max = 500) String webhookEntrega,
        @Size(max = 500) String webhookAbertura,
        @Size(max = 500) String webhookClique,
        String assinaturaPadrao,
        String templateCabecalho,
        String templateRodape,
        Boolean ativo,
        String observacoes) {
}
