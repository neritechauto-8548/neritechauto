package com.neritech.saas.produtoServico.dto;

import com.neritech.saas.produtoServico.domain.enums.StatusPedidoFornecedor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoFornecedorResponse(
        Long id,
        Long empresaId,
        Long fornecedorId,
        String nomeFornecedor,
        Long numeroPedido,
        String responsavel,
        LocalDate dataPrevisao,
        String numeroNf,
        String observacao,
        String descricaoInterna,
        StatusPedidoFornecedor status,
        LocalDateTime dataCadastro
) {}
