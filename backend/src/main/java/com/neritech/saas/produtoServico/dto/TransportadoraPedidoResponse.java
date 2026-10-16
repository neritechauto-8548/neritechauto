package com.neritech.saas.produtoServico.dto;

public record TransportadoraPedidoResponse(Long pedidoId, Long empresaId,
        Long fornecedorId, Long transportadoraId, String nomeTransportadora,
        String numeroNf, String documentoRastreio) {}
