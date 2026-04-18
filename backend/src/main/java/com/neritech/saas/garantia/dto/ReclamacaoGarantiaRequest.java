package com.neritech.saas.garantia.dto;

import com.neritech.saas.comunicacao.domain.enums.Prioridade;
import com.neritech.saas.garantia.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de requisiÃ§Ã£o para ReclamacaoGarantia
 */
public record ReclamacaoGarantiaRequest(
        @NotNull(message = "Garantia Ã© obrigatÃ³ria") Long garantiaId,

        @NotBlank(message = "NÃºmero da reclamaÃ§Ã£o Ã© obrigatÃ³rio") @Size(max = 30, message = "NÃºmero deve ter no mÃ¡ximo 30 caracteres") String numeroReclamacao,

        @NotNull(message = "Tipo de reclamaÃ§Ã£o Ã© obrigatÃ³rio") TipoReclamacao tipoReclamacao,

        @NotBlank(message = "Problema relatado Ã© obrigatÃ³rio") String problemaRelatado,

        String sintomasObservados,
        String condicoesUso,

        @PositiveOrZero(message = "Kilometragem deve ser positiva ou zero") Integer kilometragemAtual,

        @PositiveOrZero(message = "Tempo de uso deve ser positivo ou zero") Integer tempoUsoDesdeServico,

        String evidenciasFornecidas,
        String fotosProblema,
        String videosProblema,
        String documentosAnexos,

        @NotNull(message = "Prioridade Ã© obrigatÃ³ria") PrioridadeReclamacao prioridade,

        @NotNull(message = "Canal de abertura Ã© obrigatÃ³rio") CanalAberturaReclamacao canalAbertura,

        String clienteSolicitante,
        String contatoSolicitante,
        String enderecoAtendimento,
        LocalDateTime dataAgendamentoAnalise,
        Long tecnicoResponsavelId,

        @NotNull(message = "Status Ã© obrigatÃ³rio") StatusReclamacao status,

        String observacoesInternas) {
}
