package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.StatusTreinamento;
import com.neritech.saas.rh.domain.enums.TipoTreinamento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TreinamentoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String nomeTreinamento,
        String descricao,
        @Size(max = 100) String categoria,
        TipoTreinamento tipoTreinamento,
        Long instrutorInternoId,
        @Size(max = 255) String instrutorExterno,
        @Size(max = 255) String empresaTreinamento,
        @NotNull Integer cargaHoraria,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        @Size(max = 255) String localRealizacao,
        Integer capacidadeMaxima,
        BigDecimal custoTotal,
        BigDecimal custoPorParticipante,
        String objetivoTreinamento,
        String conteudoProgramatico,
        String materialNecessario,
        Boolean certificacaoEmitida,
        @Size(max = 255) String nomeCertificacao,
        Integer validadeCertificacaoMeses,
        Boolean obrigatorio,
        String cargosObrigatorios,
        String departamentosObrigatorios,
        StatusTreinamento status,
        BigDecimal avaliacaoMedia,
        String observacoes) {
}
