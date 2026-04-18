package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.NivelComplexidade;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record EspecialidadeRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @Size(max = 50) String categoria,
        NivelComplexidade nivelComplexidade,
        Boolean certificacaoObrigatoria,
        @Size(max = 255) String nomeCertificacao,
        @Size(max = 255) String entidadeCertificadora,
        Integer validadeCertificacaoMeses,
        Integer experienciaMinimaAnos,
        BigDecimal adicionalSalarialPercentual,
        Boolean ativo) {
}
