package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.MetodoEnvio;
import com.neritech.saas.ordemservico.domain.enums.StatusOrcamento;
import com.neritech.saas.ordemservico.domain.enums.TipoOrcamento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OrcamentoRequest(
        @NotNull Long ordemServicoId,
        String numeroOrcamento,
        Integer versao,
        TipoOrcamento tipoOrcamento,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorMaoObra,
        BigDecimal valorDesconto,
        BigDecimal valorAcrescimo,
        @NotNull BigDecimal valorTotal,
        Integer prazoValidadeDias,
        LocalDate dataVencimento,
        Integer prazoExecucaoDias,
        LocalDate dataPrevistaConclusao,
        String condicoesPagamento,
        String observacoesComerciais,
        String termosCondicoes,
        StatusOrcamento status,
        MetodoEnvio metodoEnvio,
        Long responsavelOrcamentoId) {
}
