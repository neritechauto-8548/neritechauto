package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.DestinatarioTipo;
import com.neritech.saas.comunicacao.domain.enums.StatusComunicacao;
import com.neritech.saas.comunicacao.domain.enums.TipoComunicacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ComunicacaoEnviadaRequest(
                @NotNull Long empresaId,
                Long templateId,
                Long campanhaId,
                @NotNull TipoComunicacao tipoComunicacao,
                @NotNull DestinatarioTipo destinatarioTipo,
                @NotNull Long destinatarioId,
                @NotBlank @Size(max = 255) String destinatarioNome,
                @NotBlank @Size(max = 255) String destinatarioContato,
                @Size(max = 255) String assunto,
                @NotBlank String conteudo,
                String anexos,
                LocalDateTime agendadaPara,
                StatusComunicacao status,
                Boolean automatica,
                Long ordemServicoId,
                Long agendamentoId,
                Long faturaId,
                Long usuarioEnvio) {
}
