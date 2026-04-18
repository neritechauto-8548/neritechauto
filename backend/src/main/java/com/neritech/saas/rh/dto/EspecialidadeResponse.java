package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.NivelComplexidade;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EspecialidadeResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        String categoria,
        NivelComplexidade nivelComplexidade,
        Boolean certificacaoObrigatoria,
        String nomeCertificacao,
        String entidadeCertificadora,
        Integer validadeCertificacaoMeses,
        Integer experienciaMinimaAnos,
        BigDecimal adicionalSalarialPercentual,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
