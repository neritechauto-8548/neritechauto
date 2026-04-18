package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.*;
import com.neritech.saas.estoque.domain.enums.AcaoDevolucao;
import com.neritech.saas.estoque.domain.enums.CondicaoProduto;
import com.neritech.saas.estoque.domain.enums.MotivoDevolucao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DevolucaoProdutoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Long ordemServicoId,
        Long clienteId,
        BigDecimal quantidadeDevolvida,
        MotivoDevolucao motivoDevolucao,
        String descricaoMotivo,
        CondicaoProduto condicaoProduto,
        AcaoDevolucao acaoTomada,
        BigDecimal valorDevolvido,
        Long aprovadoPor,
        LocalDateTime dataAprovacao,
        String observacoes,
        String fotosProdutoUrl,
        LocalDateTime dataDevolucao,
        Long usuarioResponsavel) {
}
