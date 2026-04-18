package com.neritech.saas.estoque.dto;

import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.NivelPrioridade;
import com.neritech.saas.estoque.domain.enums.StatusAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AlertaEstoqueRequest(
        @NotNull Long empresaId,
        @NotNull Long produtoId,
        @NotNull TipoAlerta tipoAlerta,
        @NotBlank String descricao,
        NivelPrioridade nivelPrioridade,
        BigDecimal quantidadeAtual,
        BigDecimal quantidadeReferencia,
        LocalDate dataVencimento,
        Integer diasParado,
        BigDecimal valorEnvolvido,
        StatusAlerta status,
        Long usuarioResponsavel,
        String observacoesResolucao) {
}
