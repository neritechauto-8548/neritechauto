package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.*;
import com.neritech.saas.rh.domain.enums.StatusTreinamento;
import com.neritech.saas.rh.domain.enums.TipoTreinamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TreinamentoResponse(
        Long id,
        Long empresaId,
        String nomeTreinamento,
        String descricao,
        String categoria,
        TipoTreinamento tipoTreinamento,
        Long instrutorInternoId,
        String instrutorExterno,
        String empresaTreinamento,
        Integer cargaHoraria,
        LocalDate dataInicio,
        LocalDate dataFim,
        String localRealizacao,
        Integer capacidadeMaxima,
        BigDecimal custoTotal,
        BigDecimal custoPorParticipante,
        String objetivoTreinamento,
        String conteudoProgramatico,
        String materialNecessario,
        Boolean certificacaoEmitida,
        String nomeCertificacao,
        Integer validadeCertificacaoMeses,
        Boolean obrigatorio,
        String cargosObrigatorios,
        String departamentosObrigatorios,
        StatusTreinamento status,
        BigDecimal avaliacaoMedia,
        String observacoes,
        Long criadoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
