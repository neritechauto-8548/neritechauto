package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.*;
import com.neritech.saas.empresa.domain.enums.OrientacaoDocumento;
import com.neritech.saas.empresa.domain.enums.TamanhoPapel;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import jakarta.validation.constraints.*;

public record ModeloDocumentoRequest(
        @NotNull Long empresaId,
        TipoDocumento tipoDocumento,
        @NotBlank @Size(max = 100) String nome,
        @NotBlank String templateHtml,
        String templateCss,
        String variaveisDisponiveis,
        OrientacaoDocumento orientacao,
        TamanhoPapel tamanhoPapel,
        String margensCm,
        Boolean cabecalhoPadrao,
        Boolean rodapePadrao,
        Boolean numeracaoAutomatica,
        Boolean padrao,
        Boolean ativo,
        Long criadoPor,
        Long atualizadoPor) {
}
