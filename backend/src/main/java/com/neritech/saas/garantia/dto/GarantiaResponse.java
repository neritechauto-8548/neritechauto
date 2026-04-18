package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.StatusGarantia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Garantia
 */
public record GarantiaResponse(
        Long id,
        Long empresaId,
        String numeroGarantia,
        Long tipoGarantiaId,
        String tipoGarantiaNome,
        Long ordemServicoId,
        Long clienteId,
        Long veiculoId,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer diasGarantia,
        StatusGarantia status,
        BigDecimal valorOriginalServico,
        BigDecimal valorCoberturaGarantia,
        Integer kilometragemInicio,
        Integer kilometragemLimite,
        String condicoesEspeciais,
        String observacoes,
        String certificadoUrl,
        String qrCodeVerificacao,
        Long transferidaParaClienteId,
        LocalDateTime dataTransferencia,
        String motivoTransferencia,
        Long renovadaDeGarantiaId,
        LocalDateTime dataRenovacao,
        Long canceladaPor,
        LocalDateTime dataCancelamento,
        String motivoCancelamento,
        Integer totalAcionamentos,
        BigDecimal valorTotalAcionamentos,
        LocalDateTime dataUltimoAcionamento,
        Boolean alertaVencimentoEnviado,
        LocalDateTime dataAlertaVencimento,
        LocalDateTime dataCadastro,
        Long emitidaPor) {
}
