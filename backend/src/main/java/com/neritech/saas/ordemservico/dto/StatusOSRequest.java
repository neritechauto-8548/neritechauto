package com.neritech.saas.ordemservico.dto;

import jakarta.validation.constraints.*;

public record StatusOSRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @NotBlank @Size(max = 20) String codigo,
        @Size(max = 7) String corIdentificacao,
        @Size(max = 50) String icone,
        Integer ordemExibicao,
        Boolean permiteEdicao,
        Boolean notificaCliente,
        Long templateNotificacaoId,
        Boolean exigeAprovacao,
        Boolean finalizaOS,
        Boolean cancelaOS,
        Boolean ativo) {
}
