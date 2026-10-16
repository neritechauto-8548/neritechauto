package com.neritech.saas.produtoServico.dto;

import jakarta.validation.constraints.Size;
public record TransportadoraPedidoRequest(@Size(max = 120) String documentoRastreio) {}
