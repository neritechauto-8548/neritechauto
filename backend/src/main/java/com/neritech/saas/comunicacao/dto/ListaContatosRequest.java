package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.FrequenciaAtualizacao;
import com.neritech.saas.comunicacao.domain.enums.TipoLista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ListaContatosRequest(
        @NotNull Long empresaId,
        @NotBlank @Size(max = 100) String nome,
        String descricao,
        @NotNull TipoLista tipoLista,
        String criteriosSegmentacao,
        FrequenciaAtualizacao frequenciaAtualizacao,
        @Size(max = 255) String tags,
        Boolean privada,
        String compartilhadaCom,
        @Size(max = 100) String origemLista,
        Boolean ativa) {
}
