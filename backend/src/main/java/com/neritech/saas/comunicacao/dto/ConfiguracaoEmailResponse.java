package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Criptografia;
import com.neritech.saas.comunicacao.domain.enums.ProvedorEmail;
import java.time.LocalDateTime;

public record ConfiguracaoEmailResponse(
        Long id,
        Long empresaId,
        ProvedorEmail provedorServico,
        String servidorSmtp,
        Integer portaSmtp,
        String usuarioSmtp,
        Criptografia criptografia,
        String remetenteNome,
        String remetenteEmail,
        String emailResposta,
        String emailCopiaOculta,
        String dominioAutorizado,
        Integer limiteEnviosDia,
        Integer enviosRealizadosHoje,
        String webhookEntrega,
        String webhookAbertura,
        String webhookClique,
        String assinaturaPadrao,
        String templateCabecalho,
        String templateRodape,
        Boolean ativo,
        Boolean testado,
        LocalDateTime dataUltimoTeste,
        String observacoes,
        LocalDateTime dataConfiguracao,
        Long configuradoPor) {
}
