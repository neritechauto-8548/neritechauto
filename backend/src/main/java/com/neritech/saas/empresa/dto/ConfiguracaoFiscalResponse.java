package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.*;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import com.neritech.saas.empresa.domain.enums.AnexoSimples;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConfiguracaoFiscalResponse(
        Long id,
        Long empresaId,
        String empresaNome,
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
        LocalDate validadeCertificado,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
