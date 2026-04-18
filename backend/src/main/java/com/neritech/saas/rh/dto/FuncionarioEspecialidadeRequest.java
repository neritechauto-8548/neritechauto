package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.NivelDominio;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record FuncionarioEspecialidadeRequest(
        @NotNull Long funcionarioId,
        @NotNull Long especialidadeId,
        NivelDominio nivelDominio,
        LocalDate dataCertificacao,
        @Size(max = 100) String numeroCertificado,
        @Size(max = 255) String entidadeCertificadora,
        LocalDate dataValidadeCertificacao,
        @Size(max = 500) String anexoCertificadoUrl,
        Integer experienciaAnos,
        String observacoes,
        Boolean ativo) {
}
