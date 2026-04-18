package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoResolucao;
import com.neritech.saas.garantia.domain.QualidadeResolucao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para ResolucaoGarantia
 */
public record ResolucaoGarantiaResponse(
        Long id,
        Long reclamacaoId,
        TipoResolucao tipoResolucao,
        String descricaoResolucao,
        String servicosExecutados,
        String produtosFornecidos,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorTotalResolucao,
        BigDecimal valorCobradoCliente,
        BigDecimal descontoConcedido,
        String justificativaCobranca,
        Boolean novaGarantiaGerada,
        Long novaGarantiaId,
        Integer prazoNovaGarantiaDias,
        LocalDateTime dataInicioExecucao,
        LocalDateTime dataConclusao,
        Integer tempoResolucaoHoras,
        Long funcionarioExecutorId,
        String funcionarioExecutorNome,
        String equipeExecucao,
        QualidadeResolucao qualidadeResolucao,
        Boolean clienteSatisfeito,
        String observacoesExecucao,
        String fotosAposResolucao,
        String documentosComprobatorios,
        Boolean aprovadaGerencia,
        Long aprovadaPor,
        LocalDateTime dataAprovacao) {
}
