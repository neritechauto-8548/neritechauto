package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.comunicacao.domain.enums.Ambiente;
import com.neritech.saas.fiscal.domain.ConfiguracaoNfe;
import com.neritech.saas.fiscal.dto.CertificadoDigitalResponse;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeRequest;
import com.neritech.saas.fiscal.dto.ConfiguracaoNfeResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:49-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ConfiguracaoNfeMapperImpl implements ConfiguracaoNfeMapper {

    @Autowired
    private CertificadoDigitalMapper certificadoDigitalMapper;

    @Override
    public ConfiguracaoNfe toEntity(ConfiguracaoNfeRequest request) {
        if ( request == null ) {
            return null;
        }

        ConfiguracaoNfe.ConfiguracaoNfeBuilder<?, ?> configuracaoNfe = ConfiguracaoNfe.builder();

        configuracaoNfe.ambiente( request.ambiente() );
        configuracaoNfe.enviarEmailDestinatario( request.enviarEmailDestinatario() );
        configuracaoNfe.proximoNumero( request.proximoNumero() );
        configuracaoNfe.salvarXml( request.salvarXml() );
        configuracaoNfe.serie( request.serie() );
        configuracaoNfe.versaoLayout( request.versaoLayout() );

        return configuracaoNfe.build();
    }

    @Override
    public ConfiguracaoNfeResponse toResponse(ConfiguracaoNfe entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Ambiente ambiente = null;
        Integer serie = null;
        Long proximoNumero = null;
        CertificadoDigitalResponse certificadoDigital = null;
        String versaoLayout = null;
        boolean enviarEmailDestinatario = false;
        boolean salvarXml = false;

        id = entity.getId();
        ambiente = entity.getAmbiente();
        serie = entity.getSerie();
        proximoNumero = entity.getProximoNumero();
        certificadoDigital = certificadoDigitalMapper.toResponse( entity.getCertificadoDigital() );
        versaoLayout = entity.getVersaoLayout();
        enviarEmailDestinatario = entity.isEnviarEmailDestinatario();
        salvarXml = entity.isSalvarXml();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        ConfiguracaoNfeResponse configuracaoNfeResponse = new ConfiguracaoNfeResponse( id, ambiente, serie, proximoNumero, certificadoDigital, versaoLayout, enviarEmailDestinatario, salvarXml, createdAt, updatedAt );

        return configuracaoNfeResponse;
    }

    @Override
    public void updateEntityFromRequest(ConfiguracaoNfeRequest request, ConfiguracaoNfe entity) {
        if ( request == null ) {
            return;
        }

        entity.setEnviarEmailDestinatario( request.enviarEmailDestinatario() );
        entity.setSalvarXml( request.salvarXml() );
        entity.setAmbiente( request.ambiente() );
        entity.setSerie( request.serie() );
        entity.setProximoNumero( request.proximoNumero() );
        entity.setVersaoLayout( request.versaoLayout() );
    }
}
