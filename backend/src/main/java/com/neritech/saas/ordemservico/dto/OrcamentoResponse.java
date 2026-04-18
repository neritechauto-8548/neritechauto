package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.MetodoEnvio;
import com.neritech.saas.ordemservico.domain.enums.StatusOrcamento;
import com.neritech.saas.ordemservico.domain.enums.TipoOrcamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrcamentoResponse(
        Long id,
        Long ordemServicoId,
        String numeroOrcamento,
        Integer versao,
        TipoOrcamento tipoOrcamento,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorMaoObra,
        BigDecimal valorDesconto,
        BigDecimal valorAcrescimo,
        BigDecimal valorTotal,
        Integer prazoValidadeDias,
        LocalDate dataVencimento,
        Integer prazoExecucaoDias,
        LocalDate dataPrevistaConclusao,
        String condicoesPagamento,
        String observacoesComerciais,
        String termosCondicoes,
        StatusOrcamento status,
        LocalDateTime dataEnvioCliente,
        LocalDateTime dataRespostaCliente,
        MetodoEnvio metodoEnvio,
        String aprovadoPorCliente,
        String documentoCliente,
        String ipAprovacao,
        LocalDateTime dataAprovacao,
        String motivoRejeicao,
        Long responsavelOrcamentoId,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versaoRegistro) {
}
