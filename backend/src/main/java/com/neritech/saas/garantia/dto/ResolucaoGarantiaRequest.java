package com.neritech.saas.garantia.dto;

import com.neritech.saas.garantia.domain.TipoResolucao;
import com.neritech.saas.garantia.domain.QualidadeResolucao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de requisiÃ§Ã£o para ResolucaoGarantia
 */
public record ResolucaoGarantiaRequest(
        @NotNull(message = "ReclamaÃ§Ã£o Ã© obrigatÃ³ria") Long reclamacaoId,

        @NotNull(message = "Tipo de resoluÃ§Ã£o Ã© obrigatÃ³rio") TipoResolucao tipoResolucao,

        @NotBlank(message = "DescriÃ§Ã£o da resoluÃ§Ã£o Ã© obrigatÃ³ria") String descricaoResolucao,

        String servicosExecutados,
        String produtosFornecidos,

        @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorServicos,

        @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorProdutos,

        @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorTotalResolucao,

        @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal valorCobradoCliente,

        @PositiveOrZero(message = "Valor deve ser positivo ou zero") BigDecimal descontoConcedido,

        String justificativaCobranca,
        Boolean novaGarantiaGerada,
        Long novaGarantiaId,

        @Positive(message = "Prazo deve ser positivo") Integer prazoNovaGarantiaDias,

        LocalDateTime dataInicioExecucao,
        LocalDateTime dataConclusao,

        @PositiveOrZero(message = "Tempo deve ser positivo ou zero") Integer tempoResolucaoHoras,

        Long funcionarioExecutorId,
        String equipeExecucao,
        QualidadeResolucao qualidadeResolucao,
        Boolean clienteSatisfeito,
        String observacoesExecucao,
        String fotosAposResolucao,
        String documentosComprobatorios,
        Boolean aprovadaGerencia) {
}
