package com.neritech.saas.agendamento.dto;

import jakarta.validation.constraints.*;

/**
 * DTO de requisiÃ§Ã£o para TipoAgendamento
 */
public record TipoAgendamentoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @Size(max = 7) String corIdentificacao,
        @Size(max = 50) String icone,
        @NotNull @Min(1) Integer duracaoPadraoMinutos,
        Boolean permiteEncaixe,
        Boolean requerOrcamento,
        String servicosInclusos,
        String observacoesPadrao,
        Boolean ativo,
        Integer ordemExibicao) {
}
