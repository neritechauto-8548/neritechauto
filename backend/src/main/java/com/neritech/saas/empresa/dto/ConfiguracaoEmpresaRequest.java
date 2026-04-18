package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.PorteEmpresa;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.enums.SituacaoCadastral;
import com.neritech.saas.empresa.domain.enums.TipoEstabelecimento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfiguracaoEmpresaRequest(
        @NotNull(message = "A empresa Ã© obrigatÃ³ria") Long empresaId,

        RegimeTributario regimeTributario,

        @Size(max = 10, message = "O cÃ³digo CNAE principal deve ter no mÃ¡ximo 10 caracteres") String codigoCnaePrincipal,

        String codigoCnaeSecundario,

        @DecimalMin(value = "0.0", message = "O capital social deve ser maior ou igual a zero") BigDecimal capitalSocial,

        PorteEmpresa porteEmpresa,

        TipoEstabelecimento tipoEstabelecimento,

        SituacaoCadastral situacaoCadastral,

        LocalDate dataSituacaoCadastral,

        @Size(max = 255, message = "O motivo da situaÃ§Ã£o cadastral deve ter no mÃ¡ximo 255 caracteres") String motivoSituacaoCadastral,

        @Size(max = 255, message = "A situaÃ§Ã£o especial deve ter no mÃ¡ximo 255 caracteres") String situacaoEspecial,

        LocalDate dataSituacaoEspecial) {
}
