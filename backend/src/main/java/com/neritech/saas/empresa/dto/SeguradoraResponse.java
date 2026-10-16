package com.neritech.saas.empresa.dto;

public record SeguradoraResponse(
    Long id, Long empresaId, String nomeFantasia, String razaoSocial, String cnpj,
    String registroSusep, boolean registroSusepVerificado, String nomeContato,
    String telefone, String email, String endereco, String tipoConvenio, Boolean ativo,
    String notas, Integer versao, long totalOS
) {}
