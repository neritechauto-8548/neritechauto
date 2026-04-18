package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.RecomendacaoAvaliacao;
import com.neritech.saas.rh.domain.enums.TipoAvaliacao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AvaliacaoFuncionarioResponse(
        Long id,
        Long funcionarioId,
        Long avaliadorId,
        TipoAvaliacao tipoAvaliacao,
        String periodoAvaliado,
        LocalDate dataAvaliacao,
        BigDecimal notaProdutividade,
        BigDecimal notaQualidade,
        BigDecimal notaPontualidade,
        BigDecimal notaTrabalhoEquipe,
        BigDecimal notaIniciativa,
        BigDecimal notaConhecimentoTecnico,
        BigDecimal notaAtendimentoCliente,
        BigDecimal notaOrganizacao,
        BigDecimal notaLideranca,
        BigDecimal notaFinal,
        String pontosFortes,
        String pontosMelhoria,
        String metasEstabelecidas,
        String planoDesenvolvimento,
        String treinamentosRecomendados,
        RecomendacaoAvaliacao recomendacao,
        BigDecimal aumentoSalarialSugerido,
        BigDecimal bonusSugerido,
        String observacoesAvaliador,
        String comentariosFuncionario,
        LocalDate dataCienciaFuncionario,
        Long aprovadaPor,
        LocalDate dataAprovacao,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
