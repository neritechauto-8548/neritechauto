package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.NaturezaSaldo;
import com.neritech.saas.financeiro.domain.enums.TipoConta;
import com.neritech.saas.financeiro.domain.enums.TipoContaContabil;
import java.time.LocalDateTime;

public record PlanoContaResponse(
        Long id,
        Long empresaId,
        String codigo,
        String nome,
        Long contaPaiId,
        String contaPaiNome,
        Integer nivel,
        TipoContaContabil tipoConta,
        NaturezaSaldo naturezaSaldo,
        Boolean aceitaLancamento,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
