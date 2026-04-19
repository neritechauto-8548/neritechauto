package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ConfiguracaoFiscal;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.enums.AmbienteNFe;
import com.neritech.saas.empresa.domain.enums.AnexoSimples;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoFiscalResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:44-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoFiscalMapperImpl implements ConfiguracaoFiscalMapper {

    @Override
    public ConfiguracaoFiscal toEntity(ConfiguracaoFiscalRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoFiscal configuracaoFiscal = new ConfiguracaoFiscal();

        configuracaoFiscal.setRegimeTributario( request.regimeTributario() );
        configuracaoFiscal.setAnexoSimples( request.anexoSimples() );
        configuracaoFiscal.setAliquotaIss( request.aliquotaIss() );
        configuracaoFiscal.setAliquotaIcms( request.aliquotaIcms() );
        configuracaoFiscal.setAliquotaPis( request.aliquotaPis() );
        configuracaoFiscal.setAliquotaCofins( request.aliquotaCofins() );
        configuracaoFiscal.setAliquotaCsll( request.aliquotaCsll() );
        configuracaoFiscal.setAliquotaIrpj( request.aliquotaIrpj() );
        configuracaoFiscal.setCodigoMunicipioPrestacao( request.codigoMunicipioPrestacao() );
        configuracaoFiscal.setCfopVendaDentroEstado( request.cfopVendaDentroEstado() );
        configuracaoFiscal.setCfopVendaForaEstado( request.cfopVendaForaEstado() );
        configuracaoFiscal.setCfopServico( request.cfopServico() );
        configuracaoFiscal.setSerieNfe( request.serieNfe() );
        configuracaoFiscal.setNumeracaoNfe( request.numeracaoNfe() );
        configuracaoFiscal.setAmbienteNfe( request.ambienteNfe() );
        configuracaoFiscal.setCertificadoDigitalA1( request.certificadoDigitalA1() );
        configuracaoFiscal.setSenhaCertificado( request.senhaCertificado() );
        configuracaoFiscal.setValidadeCertificado( request.validadeCertificado() );

        return configuracaoFiscal;
    }

    @Override
    public ConfiguracaoFiscalResponse toResponse(ConfiguracaoFiscal entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        RegimeTributario regimeTributario = null;
        AnexoSimples anexoSimples = null;
        BigDecimal aliquotaIss = null;
        BigDecimal aliquotaIcms = null;
        BigDecimal aliquotaPis = null;
        BigDecimal aliquotaCofins = null;
        BigDecimal aliquotaCsll = null;
        BigDecimal aliquotaIrpj = null;
        String codigoMunicipioPrestacao = null;
        String cfopVendaDentroEstado = null;
        String cfopVendaForaEstado = null;
        String cfopServico = null;
        Integer serieNfe = null;
        Long numeracaoNfe = null;
        AmbienteNFe ambienteNfe = null;
        LocalDate validadeCertificado = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        regimeTributario = entity.getRegimeTributario();
        anexoSimples = entity.getAnexoSimples();
        aliquotaIss = entity.getAliquotaIss();
        aliquotaIcms = entity.getAliquotaIcms();
        aliquotaPis = entity.getAliquotaPis();
        aliquotaCofins = entity.getAliquotaCofins();
        aliquotaCsll = entity.getAliquotaCsll();
        aliquotaIrpj = entity.getAliquotaIrpj();
        codigoMunicipioPrestacao = entity.getCodigoMunicipioPrestacao();
        cfopVendaDentroEstado = entity.getCfopVendaDentroEstado();
        cfopVendaForaEstado = entity.getCfopVendaForaEstado();
        cfopServico = entity.getCfopServico();
        serieNfe = entity.getSerieNfe();
        numeracaoNfe = entity.getNumeracaoNfe();
        ambienteNfe = entity.getAmbienteNfe();
        validadeCertificado = entity.getValidadeCertificado();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ConfiguracaoFiscalResponse configuracaoFiscalResponse = new ConfiguracaoFiscalResponse( id, empresaId, empresaNome, regimeTributario, anexoSimples, aliquotaIss, aliquotaIcms, aliquotaPis, aliquotaCofins, aliquotaCsll, aliquotaIrpj, codigoMunicipioPrestacao, cfopVendaDentroEstado, cfopVendaForaEstado, cfopServico, serieNfe, numeracaoNfe, ambienteNfe, validadeCertificado, dataCadastro, dataAtualizacao );

        return configuracaoFiscalResponse;
    }

    @Override
    public void updateEntityFromRequest(ConfiguracaoFiscalRequest request, ConfiguracaoFiscal entity) {
        if ( request == null ) {
            return;
        }

        entity.setRegimeTributario( request.regimeTributario() );
        entity.setAnexoSimples( request.anexoSimples() );
        entity.setAliquotaIss( request.aliquotaIss() );
        entity.setAliquotaIcms( request.aliquotaIcms() );
        entity.setAliquotaPis( request.aliquotaPis() );
        entity.setAliquotaCofins( request.aliquotaCofins() );
        entity.setAliquotaCsll( request.aliquotaCsll() );
        entity.setAliquotaIrpj( request.aliquotaIrpj() );
        entity.setCodigoMunicipioPrestacao( request.codigoMunicipioPrestacao() );
        entity.setCfopVendaDentroEstado( request.cfopVendaDentroEstado() );
        entity.setCfopVendaForaEstado( request.cfopVendaForaEstado() );
        entity.setCfopServico( request.cfopServico() );
        entity.setSerieNfe( request.serieNfe() );
        entity.setNumeracaoNfe( request.numeracaoNfe() );
        entity.setAmbienteNfe( request.ambienteNfe() );
        entity.setCertificadoDigitalA1( request.certificadoDigitalA1() );
        entity.setSenhaCertificado( request.senhaCertificado() );
        entity.setValidadeCertificado( request.validadeCertificado() );
    }

    private Long entityEmpresaId(ConfiguracaoFiscal configuracaoFiscal) {
        if ( configuracaoFiscal == null ) {
            return null;
        }
        Empresa empresa = configuracaoFiscal.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(ConfiguracaoFiscal configuracaoFiscal) {
        if ( configuracaoFiscal == null ) {
            return null;
        }
        Empresa empresa = configuracaoFiscal.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        String nomeFantasia = empresa.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
