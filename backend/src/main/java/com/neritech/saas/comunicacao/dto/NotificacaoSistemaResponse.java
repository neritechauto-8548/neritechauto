package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.comunicacao.domain.enums.TipoNotificacao;
import java.time.LocalDateTime;

public record NotificacaoSistemaResponse(
        Long id,
        Long empresaId,
        Long usuarioDestinatarioId,
        TipoNotificacao tipoNotificacao,
        String categoria,
        String titulo,
        String mensagem,
        String dadosContexto,
        String linkAcao,
        String textoBotaoAcao,
        Prioridade prioridade,
        Boolean exigeConfirmacao,
        Boolean lida,
        LocalDateTime dataLeitura,
        Boolean confirmada,
        LocalDateTime dataConfirmacao,
        LocalDateTime dataExpiracao,
        String icone,
        String cor,
        String somNotificacao,
        Boolean enviarEmail,
        Boolean enviarSms,
        Boolean enviarPush,
        String origemSistema,
        LocalDateTime dataCadastro) {
}
