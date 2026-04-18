package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.StatusGarantia;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requisiÃ§Ã£o para Garantia
 */
public record GarantiaRequest(
        @NotBlank(message = "NÃºmero da garantia Ã© obrigatÃ³rio") @Size(max = 30, message = "NÃºmero deve ter no mÃ¡ximo 30 caracteres") String numeroGarantia,

        @NotNull(message = "Tipo de garantia Ã© obrigatÃ³rio") Long tipoGarantiaId,

        @NotNull(message = "Ordem de serviÃ§o Ã© obrigatÃ³ria") Long ordemServicoId,

        @NotNull(message = "Cliente Ã© obrigatÃ³rio") Long clienteId,

        @NotNull(message = "VeÃ­culo Ã© obrigatÃ³rio") Long veiculoId,

        @NotNull(message = "Data de inÃ­cio Ã© obrigatÃ³ria") LocalDate dataInicio,

        @NotNull(message = "Data de fim Ã© obrigatÃ³ria") LocalDate dataFim,

        @NotNull(message = "Dias de garantia Ã© obrigatÃ³rio") @Positive(message = "Dias deve ser positivo") Integer diasGarantia,

        @NotNull(message = "Status Ã© obrigatÃ³rio") StatusGarantia status,

        @NotNull(message = "Valor original do serviÃ§o Ã© obrigatÃ³rio") @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorOriginalServico,

        @NotNull(message = "Valor de cobertura Ã© obrigatÃ³rio") @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorCoberturaGarantia,

        @PositiveOrZero(message = "Kilometragem deve ser positiva ou zero") Integer kilometragemInicio,

        @PositiveOrZero(message = "Kilometragem deve ser positiva ou zero") Integer kilometragemLimite,

        String condicoesEspeciais,
        String observacoes,
        String certificadoUrl,
        String qrCodeVerificacao) {
}
