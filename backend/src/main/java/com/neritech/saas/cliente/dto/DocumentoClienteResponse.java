package com.neritech.saas.cliente.dto;

import com.neritech.saas.cliente.domain.DocumentoCliente;

public record DocumentoClienteResponse(
        Long id,
        Long clienteId,
        String tipoDocumento,
        String descricao,
        String arquivoNome,
        String arquivoTipo,
        Long arquivoTamanho
) {
    public static DocumentoClienteResponse from(DocumentoCliente entity) {
        return new DocumentoClienteResponse(entity.getId(), entity.getCliente().getId(),
                entity.getTipoDocumento(), entity.getDescricao(),
                entity.getArquivoNome(), entity.getArquivoTipo(), entity.getArquivoTamanho());
    }
}
