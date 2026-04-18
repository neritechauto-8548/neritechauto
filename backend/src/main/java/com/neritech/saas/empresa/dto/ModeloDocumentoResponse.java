package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.*;
import com.neritech.saas.empresa.domain.enums.OrientacaoDocumento;
import com.neritech.saas.empresa.domain.enums.TamanhoPapel;
import com.neritech.saas.empresa.domain.enums.TipoDocumento;
import java.time.LocalDateTime;

public record ModeloDocumentoResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        TipoDocumento tipoDocumento,
        String nome,
        String templateHtml,
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
        Long atualizadoPor,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
