package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.comunicacao.domain.enums.TipoIntegracao;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ConfiguracaoWhatsappResponse(
        Long id,
        Long empresaId,
        TipoIntegracao tipoIntegracao,
        String provedorServico,
        String numeroWhatsapp,
        String webhookUrl,
        String phoneNumberId,
        String businessAccountId,
        String appId,
        String mensagensTemplateAprovadas,
        LocalTime horarioAtendimentoInicio,
        LocalTime horarioAtendimentoFim,
        String mensagemForaHorario,
        String mensagemBoasVindas,
        String mensagemMenuPrincipal,
        Boolean chatbotAtivo,
        Boolean integracaoAtiva,
        Ambiente ambiente,
        String observacoes,
        LocalDateTime dataConfiguracao,
        Long configuradoPor,
        LocalDateTime dataUltimaVerificacao,
        String statusVerificacao) {
}
