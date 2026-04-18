package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.AcaoDevolucao;
import com.neritech.saas.estoque.domain.enums.CondicaoProduto;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record DevolucaoProdutoRequest(
        @NotNull Long produtoId,
        Long ordemServicoId,
        Long clienteId,
        @NotNull BigDecimal quantidadeDevolvida,
        MotivoDevolucao motivoDevolucao,
        String descricaoMotivo,
        CondicaoProduto condicaoProduto,
        AcaoDevolucao acaoTomada,
        BigDecimal valorDevolvido,
        Long aprovadoPor,
        String observacoes,
        String fotosProdutoUrl,
        Long usuarioResponsavel) {
}
