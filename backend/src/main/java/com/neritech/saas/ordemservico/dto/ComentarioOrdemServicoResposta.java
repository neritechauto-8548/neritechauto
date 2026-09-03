package com.neritech.saas.ordemservico.dto;

import java.time.LocalDateTime;

public record ComentarioOrdemServicoResposta(
        Long id,
        Long ordemServicoId,
        Long usuarioAutorId,
        String nomeAutor,
        String conteudo,
        String visibilidade,
        LocalDateTime dataCadastro) {
}
