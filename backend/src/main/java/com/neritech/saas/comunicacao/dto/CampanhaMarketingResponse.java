package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.StatusCampanha;
import com.neritech.saas.comunicacao.domain.enums.TipoCampanha;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CampanhaMarketingResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        TipoCampanha tipoCampanha,
        String objetivo,
        String publicoAlvo,
        String canaisComunicacao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        BigDecimal orcamentoTotal,
        BigDecimal custoRealizado,
        Integer metaAlcance,
        Integer alcanceRealizado,
        Integer metaConversao,
        Integer conversaoRealizada,
        Long templateEmailId,
        Long templateSmsId,
        Long templateWhatsappId,
        Long promocaoAssociadaId,
        StatusCampanha status,
        Long aprovadaPor,
        LocalDateTime dataAprovacao,
        String observacoes,
        String resultadosDetalhados,
        BigDecimal roiCalculado,
        Long criadaPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
