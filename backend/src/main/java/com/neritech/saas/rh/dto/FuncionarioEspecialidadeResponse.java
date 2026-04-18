package com.neritech.saas.rh.dto;

import com.neritech.saas.rh.domain.enums.NivelDominio;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FuncionarioEspecialidadeResponse(
        Long id,
        Long funcionarioId,
        Long especialidadeId,
        NivelDominio nivelDominio,
        LocalDate dataCertificacao,
        String numeroCertificado,
        String entidadeCertificadora,
        LocalDate dataValidadeCertificacao,
        String anexoCertificadoUrl,
        Integer experienciaAnos,
        String observacoes,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
