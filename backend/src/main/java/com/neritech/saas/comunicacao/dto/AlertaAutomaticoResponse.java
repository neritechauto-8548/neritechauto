package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.FrequenciaVerificacao;
import com.neritech.saas.comunicacao.domain.enums.TipoAlerta;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AlertaAutomaticoResponse(
        Long id,
        Long empresaId,
        String nomeAlerta,
        String descricao,
        TipoAlerta tipoAlerta,
        String condicoesDisparo,
        FrequenciaVerificacao frequenciaVerificacao,
        LocalTime horarioVerificacao,
        Integer diaSemanaVerificacao,
        Integer diaMesVerificacao,
        String usuariosNotificar,
        String canaisNotificacao,
        Long templateNotificacaoId,
        String mensagemPersonalizada,
        Boolean ativo,
        LocalDateTime ultimaExecucao,
        LocalDateTime proximaExecucao,
        Integer totalDisparos,
        Integer totalErros,
        String logExecucoes,
        String observacoes,
        Long criadoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
