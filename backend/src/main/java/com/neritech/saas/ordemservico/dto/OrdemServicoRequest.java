package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.MetodoAprovacao;
import com.neritech.saas.ordemservico.domain.enums.NivelCombustivel;
import com.neritech.saas.ordemservico.domain.enums.PrioridadeOS;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrdemServicoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 20) String numeroOS,
        Long clienteId,
        Long veiculoId,
        Long statusId,
        @NotNull TipoOS tipoOS,
        PrioridadeOS prioridade,
        LocalDateTime dataAbertura,
        LocalDateTime dataPromessa,
        Integer quilometragemEntrada,
        NivelCombustivel nivelCombustivelEntrada,
        Long consultorResponsavelId,
        Long mecanicoResponsavelId,
        String equipeExecucao,
        String problemaRelatado,
        String solucaoAplicada,
        String observacoesInternas,
        String observacoesCliente,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorDesconto,
        BigDecimal valorAcrescimo,
        @NotNull BigDecimal valorTotal,
        Long formaPagamentoId,
        Long condicaoPagamentoId,
        BigDecimal valorEntrada,
        Boolean aprovadoCliente,
        MetodoAprovacao metodoAprovacao,
        Integer garantiaDias) {
}
