package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.StatusCampanha;
import com.neritech.saas.comunicacao.domain.enums.TipoCampanha;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaMarketingRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String nome,
        String descricao,
        @NotNull TipoCampanha tipoCampanha,
        String objetivo,
        String publicoAlvo,
        String canaisComunicacao,
        @NotNull LocalDateTime dataInicio,
        @NotNull LocalDateTime dataFim,
        BigDecimal orcamentoTotal,
        Integer metaAlcance,
        Integer metaConversao,
        Long templateEmailId,
        Long templateSmsId,
        Long templateWhatsappId,
        Long promocaoAssociadaId,
        @NotNull StatusCampanha status,
        String observacoes) {
}
