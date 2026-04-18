package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.DestinatarioTipo;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComunicacaoEnviadaResponse(
        Long id,
        Long empresaId,
        Long templateId,
        Long campanhaId,
        TipoComunicacao tipoComunicacao,
        DestinatarioTipo destinatarioTipo,
        Long destinatarioId,
        String destinatarioNome,
        String destinatarioContato,
        String assunto,
        String conteudo,
        String anexos,
        LocalDateTime agendadaPara,
        LocalDateTime dataEnvio,
        LocalDateTime dataEntrega,
        LocalDateTime dataLeitura,
        LocalDateTime dataClique,
        StatusComunicacao status,
        Integer tentativasEnvio,
        String erroEnvio,
        BigDecimal custoEnvio,
        String respostaDestinatario,
        LocalDateTime dataResposta,
        Integer avaliacaoConteudo,
        String motivoAvaliacao,
        Boolean automatica,
        Long ordemServicoId,
        Long agendamentoId,
        Long faturaId,
        Long usuarioEnvio,
        LocalDateTime dataCadastro) {
}
