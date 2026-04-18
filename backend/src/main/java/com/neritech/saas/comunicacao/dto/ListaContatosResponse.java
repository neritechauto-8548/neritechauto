package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.FrequenciaAtualizacao;
import com.neritech.saas.comunicacao.domain.enums.TipoLista;
import java.time.LocalDateTime;

public record ListaContatosResponse(
        Long id,
        Long empresaId,
        String nome,
        String descricao,
        TipoLista tipoLista,
        String criteriosSegmentacao,
        Integer totalContatos,
        Integer contatosAtivos,
        LocalDateTime dataUltimaAtualizacao,
        FrequenciaAtualizacao frequenciaAtualizacao,
        LocalDateTime proximaAtualizacao,
        String tags,
        Boolean privada,
        String compartilhadaCom,
        String origemLista,
        Boolean ativa,
        Long criadaPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
