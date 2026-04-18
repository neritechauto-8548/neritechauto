package com.neritech.saas.estoque.dto;

import com.neritech.saas.estoque.domain.enums.TipoPerda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PerdaEstoqueRequest(
        @NotNull Long produtoId,
        String loteNumero,
        @NotNull BigDecimal quantidadePerdida,
        TipoPerda tipoPerda,
        @NotBlank String descricao,
        BigDecimal valorPerda,
        String responsavelPerda,
        Long localizacaoId,
        Boolean foiSegurado,
        String numeroSinistro,
        BigDecimal valorIndenizado,
        @NotNull LocalDate dataOcorrencia,
        LocalDate dataDescoberta,
        String boletimOcorrencia,
        String fotosComprovantesUrl,
        String observacoes,
        Long aprovadoPor,
        Long usuarioCadastro) {
}
