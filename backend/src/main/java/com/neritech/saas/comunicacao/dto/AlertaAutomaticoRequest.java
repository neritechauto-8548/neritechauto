package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.FrequenciaVerificacao;
import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public record AlertaAutomaticoRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 255) String nomeAlerta,
        String descricao,
        @NotNull TipoAlerta tipoAlerta,
        String condicoesDisparo,
        @NotNull FrequenciaVerificacao frequenciaVerificacao,
        LocalTime horarioVerificacao,
        Integer diaSemanaVerificacao,
        Integer diaMesVerificacao,
        String usuariosNotificar,
        String canaisNotificacao,
        Long templateNotificacaoId,
        String mensagemPersonalizada,
        Boolean ativo,
        String observacoes) {
}
