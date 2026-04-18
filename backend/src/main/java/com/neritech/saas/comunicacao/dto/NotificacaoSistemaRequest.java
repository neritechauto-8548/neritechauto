package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.comunicacao.domain.enums.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record NotificacaoSistemaRequest(
        @NotNull Long empresaId,
        @NotNull Long usuarioDestinatarioId,
        @NotNull TipoNotificacao tipoNotificacao,
        @Size(max = 50) String categoria,
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank String mensagem,
        String dadosContexto,
        @Size(max = 500) String linkAcao,
        @Size(max = 50) String textoBotaoAcao,
        @NotNull Prioridade prioridade,
        Boolean exigeConfirmacao,
        LocalDateTime dataExpiracao,
        @Size(max = 50) String icone,
        @Size(max = 7) String cor,
        @Size(max = 100) String somNotificacao,
        Boolean enviarEmail,
        Boolean enviarSms,
        Boolean enviarPush,
        @Size(max = 100) String origemSistema) {
}
