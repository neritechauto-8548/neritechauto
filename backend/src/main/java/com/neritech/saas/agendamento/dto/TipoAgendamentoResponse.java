package com.neritech.saas.agendamento.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta para TipoAgendamento
 */
public record TipoAgendamentoResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        String corIdentificacao,
        String icone,
        Integer duracaoPadraoMinutos,
        Boolean permiteEncaixe,
        Boolean requerOrcamento,
        String servicosInclusos,
        String observacoesPadrao,
        Boolean ativo,
        Integer ordemExibicao,
        LocalDateTime dataCadastro) {
}
