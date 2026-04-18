package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.PorteEmpresa;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.enums.SituacaoCadastral;
import com.neritech.saas.empresa.domain.enums.TipoEstabelecimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConfiguracaoEmpresaResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        RegimeTributario regimeTributario,
        String codigoCnaePrincipal,
        String codigoCnaeSecundario,
        BigDecimal capitalSocial,
        PorteEmpresa porteEmpresa,
        TipoEstabelecimento tipoEstabelecimento,
        SituacaoCadastral situacaoCadastral,
        LocalDate dataSituacaoCadastral,
        String motivoSituacaoCadastral,
        String situacaoEspecial,
        LocalDate dataSituacaoEspecial,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
