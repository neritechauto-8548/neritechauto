package com.neritech.saas.empresa.dto;
import java.math.BigDecimal;

public record SeguradoraResponse(
    Long id, Long empresaId, String cnpj, String razaoSocial, String nomeFantasia,
    String inscricaoEstadual, String inscricaoMunicipal, String email,
    String observacoes, BigDecimal margemRefatoramento, String site,
    Boolean retencaoIss, Boolean ativo, Integer versao, long totalOS
) {}
