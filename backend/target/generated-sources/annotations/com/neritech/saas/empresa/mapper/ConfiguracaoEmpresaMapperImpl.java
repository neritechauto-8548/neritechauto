package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.ConfiguracaoEmpresa;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.enums.PorteEmpresa;
import com.neritech.saas.empresa.domain.enums.RegimeTributario;
import com.neritech.saas.empresa.domain.enums.SituacaoCadastral;
import com.neritech.saas.empresa.domain.enums.TipoEstabelecimento;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaRequest;
import com.neritech.saas.empresa.dto.ConfiguracaoEmpresaResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T16:16:23-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoEmpresaMapperImpl implements ConfiguracaoEmpresaMapper {

    @Override
    public ConfiguracaoEmpresa toEntity(ConfiguracaoEmpresaRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoEmpresa configuracaoEmpresa = new ConfiguracaoEmpresa();

        configuracaoEmpresa.setRegimeTributario( request.regimeTributario() );
        configuracaoEmpresa.setCodigoCnaePrincipal( request.codigoCnaePrincipal() );
        configuracaoEmpresa.setCodigoCnaeSecundario( request.codigoCnaeSecundario() );
        configuracaoEmpresa.setCapitalSocial( request.capitalSocial() );
        configuracaoEmpresa.setPorteEmpresa( request.porteEmpresa() );
        configuracaoEmpresa.setTipoEstabelecimento( request.tipoEstabelecimento() );
        configuracaoEmpresa.setSituacaoCadastral( request.situacaoCadastral() );
        configuracaoEmpresa.setDataSituacaoCadastral( request.dataSituacaoCadastral() );
        configuracaoEmpresa.setMotivoSituacaoCadastral( request.motivoSituacaoCadastral() );
        configuracaoEmpresa.setSituacaoEspecial( request.situacaoEspecial() );
        configuracaoEmpresa.setDataSituacaoEspecial( request.dataSituacaoEspecial() );

        return configuracaoEmpresa;
    }

    @Override
    public ConfiguracaoEmpresaResponse toResponse(ConfiguracaoEmpresa entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        RegimeTributario regimeTributario = null;
        String codigoCnaePrincipal = null;
        String codigoCnaeSecundario = null;
        BigDecimal capitalSocial = null;
        PorteEmpresa porteEmpresa = null;
        TipoEstabelecimento tipoEstabelecimento = null;
        SituacaoCadastral situacaoCadastral = null;
        LocalDate dataSituacaoCadastral = null;
        String motivoSituacaoCadastral = null;
        String situacaoEspecial = null;
        LocalDate dataSituacaoEspecial = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        regimeTributario = entity.getRegimeTributario();
        codigoCnaePrincipal = entity.getCodigoCnaePrincipal();
        codigoCnaeSecundario = entity.getCodigoCnaeSecundario();
        capitalSocial = entity.getCapitalSocial();
        porteEmpresa = entity.getPorteEmpresa();
        tipoEstabelecimento = entity.getTipoEstabelecimento();
        situacaoCadastral = entity.getSituacaoCadastral();
        dataSituacaoCadastral = entity.getDataSituacaoCadastral();
        motivoSituacaoCadastral = entity.getMotivoSituacaoCadastral();
        situacaoEspecial = entity.getSituacaoEspecial();
        dataSituacaoEspecial = entity.getDataSituacaoEspecial();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        ConfiguracaoEmpresaResponse configuracaoEmpresaResponse = new ConfiguracaoEmpresaResponse( id, empresaId, empresaNome, regimeTributario, codigoCnaePrincipal, codigoCnaeSecundario, capitalSocial, porteEmpresa, tipoEstabelecimento, situacaoCadastral, dataSituacaoCadastral, motivoSituacaoCadastral, situacaoEspecial, dataSituacaoEspecial, dataCadastro, dataAtualizacao );

        return configuracaoEmpresaResponse;
    }

    @Override
    public void updateEntityFromRequest(ConfiguracaoEmpresaRequest request, ConfiguracaoEmpresa entity) {
        if ( request == null ) {
            return;
        }

        entity.setRegimeTributario( request.regimeTributario() );
        entity.setCodigoCnaePrincipal( request.codigoCnaePrincipal() );
        entity.setCodigoCnaeSecundario( request.codigoCnaeSecundario() );
        entity.setCapitalSocial( request.capitalSocial() );
        entity.setPorteEmpresa( request.porteEmpresa() );
        entity.setTipoEstabelecimento( request.tipoEstabelecimento() );
        entity.setSituacaoCadastral( request.situacaoCadastral() );
        entity.setDataSituacaoCadastral( request.dataSituacaoCadastral() );
        entity.setMotivoSituacaoCadastral( request.motivoSituacaoCadastral() );
        entity.setSituacaoEspecial( request.situacaoEspecial() );
        entity.setDataSituacaoEspecial( request.dataSituacaoEspecial() );
    }

    private Long entityEmpresaId(ConfiguracaoEmpresa configuracaoEmpresa) {
        if ( configuracaoEmpresa == null ) {
            return null;
        }
        Empresa empresa = configuracaoEmpresa.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(ConfiguracaoEmpresa configuracaoEmpresa) {
        if ( configuracaoEmpresa == null ) {
            return null;
        }
        Empresa empresa = configuracaoEmpresa.getEmpresa();
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
