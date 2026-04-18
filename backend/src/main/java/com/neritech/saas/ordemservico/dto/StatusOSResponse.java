package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record StatusOSResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        String codigo,
        String corIdentificacao,
        String icone,
        Integer ordemExibicao,
        Boolean permiteEdicao,
        Boolean notificaCliente,
        Long templateNotificacaoId,
        Boolean exigeAprovacao,
        Boolean finalizaOS,
        Boolean cancelaOS,
        Boolean sistema,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao) {
}
