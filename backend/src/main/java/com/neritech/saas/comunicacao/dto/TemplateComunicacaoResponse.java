package com.neritech.saas.comunicacao.dto;

import com.neritech.saas.comunicacao.domain.enums.CategoriaTemplate;
import com.neritech.saas.comunicacao.domain.enums.TipoTemplate;
import java.time.LocalDateTime;

public record TemplateComunicacaoResponse(
        Long id,
        Long empresaId,
        String nome,
        TipoTemplate tipoTemplate,
        CategoriaTemplate categoria,
        String assunto,
        String conteudo,
        String variaveisDisponiveis,
        String anexosPadrao,
        Boolean ativo,
        Boolean padraoCategoria,
        String idioma,
        Boolean personalizavel,
        Boolean requerAprovacao,
        String tags,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
