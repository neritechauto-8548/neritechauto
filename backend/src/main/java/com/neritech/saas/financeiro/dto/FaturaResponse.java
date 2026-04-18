package com.neritech.saas.financeiro.dto;

import com.neritech.saas.financeiro.domain.enums.StatusFatura;
import com.neritech.saas.financeiro.domain.enums.TipoFatura;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FaturaResponse(
        Long id,
        Long empresaId,
        String numeroFatura,
        Long clienteId,
        Long ordemServicoId,
        TipoFatura tipoFatura,
        LocalDate dataEmissao,
        LocalDate dataVencimento,
        BigDecimal valorServicos,
        BigDecimal valorProdutos,
        BigDecimal valorDescontos,
        BigDecimal valorAcrescimos,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        BigDecimal valorPendente,
        StatusFatura status,
        Long formaPagamentoId,
        String formaPagamentoNome,
        Long condicaoPagamentoId,
        String condicaoPagamentoNome,
        String observacoes,
        String observacoesInternas,
        String nfeNumero,
        String nfeChave,
        String nfeUrlDanfe,
        LocalDateTime dataEnvioCliente,
        Boolean enviadaPorEmail,
        Boolean enviadaPorWhatsapp,
        String emailEnvio,
        String whatsappEnvio,
        String boletoNossoNumero,
        String boletoUrl,
        String pixQrCode,
        String pixCodigo,
        List<ItemFaturaResponse> itens,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
