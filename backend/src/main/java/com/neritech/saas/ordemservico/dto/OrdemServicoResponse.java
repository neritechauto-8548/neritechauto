package com.neritech.saas.ordemservico.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.ordemservico.domain.*;
import com.neritech.saas.ordemservico.domain.enums.MetodoAprovacao;
import com.neritech.saas.ordemservico.domain.enums.NivelCombustivel;
import com.neritech.saas.ordemservico.domain.enums.PrioridadeOS;
import com.neritech.saas.ordemservico.domain.enums.TipoOS;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrdemServicoResponse(
        Long id,
        Long empresaId,
        String numeroOS,
        Long clienteId,
        Long veiculoId,
        Long statusId,
        TipoOS tipoOS,
        PrioridadeOS prioridade,
        LocalDateTime dataAbertura,
        LocalDateTime dataPromessa,
        LocalDateTime dataInicioExecucao,
        LocalDateTime dataFimExecucao,
        LocalDateTime dataEntrega,
        Integer quilometragemEntrada,
        Integer quilometragemSaida,
        NivelCombustivel nivelCombustivelEntrada,
        NivelCombustivel nivelCombustivelSaida,
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
        BigDecimal valorTotal,
        Long formaPagamentoId,
        Long condicaoPagamentoId,
        BigDecimal valorEntrada,
        BigDecimal valorFinanciado,
        Boolean aprovadoCliente,
        LocalDateTime dataAprovacaoCliente,
        MetodoAprovacao metodoAprovacao,
        Integer garantiaDias,
        LocalDate dataVencimentoGarantia,
        Boolean nfeEmitida,
        String numeroNFe,
        Integer notaAvaliacaoCliente,
        Integer tempoTotalExecucaoMinutos,
        BigDecimal margemLucroRealizada,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao,
        Integer versao,
        
        // Campos de Visualização
        String nomeCliente,
        String placaVeiculo,
        String nomeVeiculo,
        String statusNome,
        String statusCor,
        java.util.List<ItemOSServicoResponse> servicos,
        java.util.List<ItemOSProdutoResponse> produtos,
        java.util.List<FotoOSResponse> fotos) {
}
