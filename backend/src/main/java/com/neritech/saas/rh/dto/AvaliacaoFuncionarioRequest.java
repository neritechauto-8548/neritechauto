package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.RecomendacaoAvaliacao;
import com.neritech.saas.rh.domain.enums.TipoAvaliacao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AvaliacaoFuncionarioRequest(
        @NotNull Long funcionarioId,
        @NotNull Long avaliadorId,
        TipoAvaliacao tipoAvaliacao,
        @NotBlank @Size(max = 7) String periodoAvaliado,
        @NotNull LocalDate dataAvaliacao,
        BigDecimal notaProdutividade,
        BigDecimal notaQualidade,
        BigDecimal notaPontualidade,
        BigDecimal notaTrabalhoEquipe,
        BigDecimal notaIniciativa,
        BigDecimal notaConhecimentoTecnico,
        BigDecimal notaAtendimentoCliente,
        BigDecimal notaOrganizacao,
        BigDecimal notaLideranca,
        @NotNull BigDecimal notaFinal,
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
        LocalDate dataCienciaFuncionario) {
}
