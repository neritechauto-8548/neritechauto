package com.neritech.saas.empresa.dto;

import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.*;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import com.neritech.saas.empresa.domain.enums.AnexoSimples;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
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
        
        // --- NFe ---
        String situacaoTributariaIcmsNfe,
        String situacaoTributariaPisNfe,
        String situacaoTributariaCofinsNfe,
        String mensagemDadosAdicionaisNfe,
        Boolean mostrarCnpjNfe,
        Boolean utilizarCodigoProdutoOriginalNfe,

        // --- NFCe ---
        String situacaoTributariaIcmsNfce,
        String situacaoTributariaPisNfce,
        String situacaoTributariaCofinsNfce,
        String mensagemDadosAdicionaisNfce,
        Integer serieNfce,
        String cfopPadraoNfce,

        // --- NFSe Homologacao ---
        Long sequencialRpseHomologacao,
        String serieRpseHomologacao,
        Long sequencialLoteRpseHomologacao,
        String usuarioAcessoProvedorHomologacao,
        String tokenAcessoProvedorHomologacao,

        // --- NFSe Producao ---
        Long sequencialRpseProducao,
        String serieRpseProducao,
        Long sequencialLoteRpseProducao,
        String usuarioAcessoProvedorProducao,
        String tokenAcessoProvedorProducao,

        // --- NFSe Gerais ---
        String cnaeServico,
        String codigoServicoMunicipal,
        String itemListaServico,
        String codigoNbs,
        String codigoTributacaoMunicipio,
        String unidadeServico,
        String descricaoServicoMunicipal,
        String naturezaOperacaoNfse,
        String regimeEspecialTributacaoNfse,
        Boolean issRetidoFonte,
        String outrasInformacoesNfse,
        String logoNfsePath,

        // --- Reforma Tributaria ---
        BigDecimal aliquotaIbs,
        BigDecimal percentualDiferimentoIbs,
        BigDecimal percentualReducaoIbs,
        BigDecimal aliquotaCbs,
        BigDecimal percentualDiferimentoCbs,
        BigDecimal percentualReducaoCbs,
        String situacaoTributariaIbsCbs,
        String classificacaoTributariaIbsCbs,

        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao) {
}
