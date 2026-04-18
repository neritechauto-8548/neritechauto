package com.neritech.saas.fiscal.mapper;

import com.neritech.saas.fiscal.domain.SpedFiscal;
import com.neritech.saas.fiscal.dto.SpedFiscalRequest;
import com.neritech.saas.fiscal.dto.SpedFiscalResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T12:53:44-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class SpedFiscalMapperImpl implements SpedFiscalMapper {

    @Override
    public SpedFiscal toEntity(SpedFiscalRequest request) {
        if ( request == null ) {
            return null;
        }

        SpedFiscal.SpedFiscalBuilder<?, ?> spedFiscal = SpedFiscal.builder();

        byte[] arquivoGerado = request.arquivoGerado();
        if ( arquivoGerado != null ) {
            spedFiscal.arquivoGerado( Arrays.copyOf( arquivoGerado, arquivoGerado.length ) );
        }
        spedFiscal.logProcessamento( request.logProcessamento() );
        spedFiscal.periodoFim( request.periodoFim() );
        spedFiscal.periodoInicio( request.periodoInicio() );
        spedFiscal.status( request.status() );
        spedFiscal.tipoArquivo( request.tipoArquivo() );

        return spedFiscal.build();
    }

    @Override
    public SpedFiscalResponse toResponse(SpedFiscal entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        LocalDate periodoInicio = null;
        LocalDate periodoFim = null;
        String tipoArquivo = null;
        String status = null;
        String logProcessamento = null;
        LocalDateTime dataGeracao = null;

        id = entity.getId();
        periodoInicio = entity.getPeriodoInicio();
        periodoFim = entity.getPeriodoFim();
        tipoArquivo = entity.getTipoArquivo();
        status = entity.getStatus();
        logProcessamento = entity.getLogProcessamento();
        dataGeracao = entity.getDataGeracao();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        SpedFiscalResponse spedFiscalResponse = new SpedFiscalResponse( id, periodoInicio, periodoFim, tipoArquivo, status, logProcessamento, dataGeracao, createdAt, updatedAt );

        return spedFiscalResponse;
    }

    @Override
    public void updateEntityFromRequest(SpedFiscalRequest request, SpedFiscal entity) {
        if ( request == null ) {
            return;
        }

        entity.setPeriodoInicio( request.periodoInicio() );
        entity.setPeriodoFim( request.periodoFim() );
        entity.setTipoArquivo( request.tipoArquivo() );
        entity.setStatus( request.status() );
        byte[] arquivoGerado = request.arquivoGerado();
        if ( arquivoGerado != null ) {
            entity.setArquivoGerado( Arrays.copyOf( arquivoGerado, arquivoGerado.length ) );
        }
        else {
            entity.setArquivoGerado( null );
        }
        entity.setLogProcessamento( request.logProcessamento() );
    }
}
