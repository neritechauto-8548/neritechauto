package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.*;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import com.neritech.saas.empresa.domain.enums.AnexoSimples;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfiguracaoFiscalRequest(
        @NotNull Long empresaId,
        RegimeTributario regimeTributario,
        AnexoSimples anexoSimples,
        BigDecimal aliquotaIss,
        BigDecimal aliquotaIcms,
        BigDecimal aliquotaPis,
        BigDecimal aliquotaCofins,
        BigDecimal aliquotaCsll,
        BigDecimal aliquotaIrpj,
        String codigoMunicipioPrestacao,
        String cfopVendaDentroEstado,
        String cfopVendaForaEstado,
        String cfopServico,
        Integer serieNfe,
        Long numeracaoNfe,
        AmbienteNFe ambienteNfe,
        String certificadoDigitalA1,
        String senhaCertificado,
        LocalDate validadeCertificado) {
}
