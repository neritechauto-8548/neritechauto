package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.CertificadoDigital;
import com.neritech.saas.fiscal.dto.CertificadoDigitalRequest;
import com.neritech.saas.fiscal.dto.CertificadoDigitalResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T14:08:21-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CertificadoDigitalMapperImpl implements CertificadoDigitalMapper {

    @Override
    public CertificadoDigital toEntity(CertificadoDigitalRequest request) {
        if ( request == null ) {
            return null;
        }

        CertificadoDigital.CertificadoDigitalBuilder<?, ?> certificadoDigital = CertificadoDigital.builder();

        byte[] arquivo = request.arquivo();
        if ( arquivo != null ) {
            certificadoDigital.arquivo( Arrays.copyOf( arquivo, arquivo.length ) );
        }
        certificadoDigital.ativo( request.ativo() );
        certificadoDigital.dataValidade( request.dataValidade() );
        certificadoDigital.emissor( request.emissor() );
        certificadoDigital.nomeArquivo( request.nomeArquivo() );
        certificadoDigital.senha( request.senha() );
        certificadoDigital.serialNumber( request.serialNumber() );

        return certificadoDigital.build();
    }

    @Override
    public CertificadoDigitalResponse toResponse(CertificadoDigital entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nomeArquivo = null;
        LocalDateTime dataValidade = null;
        String emissor = null;
        String serialNumber = null;
        boolean ativo = false;

        id = entity.getId();
        nomeArquivo = entity.getNomeArquivo();
        dataValidade = entity.getDataValidade();
        emissor = entity.getEmissor();
        serialNumber = entity.getSerialNumber();
        ativo = entity.isAtivo();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        CertificadoDigitalResponse certificadoDigitalResponse = new CertificadoDigitalResponse( id, nomeArquivo, dataValidade, emissor, serialNumber, ativo, createdAt, updatedAt );

        return certificadoDigitalResponse;
    }

    @Override
    public void updateEntityFromRequest(CertificadoDigitalRequest request, CertificadoDigital entity) {
        if ( request == null ) {
            return;
        }

        entity.setNomeArquivo( request.nomeArquivo() );
        entity.setSenha( request.senha() );
        byte[] arquivo = request.arquivo();
        if ( arquivo != null ) {
            entity.setArquivo( Arrays.copyOf( arquivo, arquivo.length ) );
        }
        else {
            entity.setArquivo( null );
        }
        entity.setDataValidade( request.dataValidade() );
        entity.setEmissor( request.emissor() );
        entity.setSerialNumber( request.serialNumber() );
        entity.setAtivo( request.ativo() );
    }
}
