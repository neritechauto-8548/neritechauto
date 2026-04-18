package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.Sincronizacao;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.dto.SincronizacaoRequest;
import com.neritech.saas.integracao.dto.SincronizacaoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:09-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class SincronizacaoMapperImpl implements SincronizacaoMapper {

    @Autowired
    private IntegracaoAtivaMapper integracaoAtivaMapper;

    @Override
    public Sincronizacao toEntity(SincronizacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Sincronizacao.SincronizacaoBuilder<?, ?> sincronizacao = Sincronizacao.builder();

        sincronizacao.dataFim( request.dataFim() );
        sincronizacao.dataInicio( request.dataInicio() );
        sincronizacao.direcao( request.direcao() );
        sincronizacao.entidade( request.entidade() );
        sincronizacao.logErros( request.logErros() );
        sincronizacao.registrosComErro( request.registrosComErro() );
        sincronizacao.registrosProcessados( request.registrosProcessados() );
        sincronizacao.status( request.status() );

        return sincronizacao.build();
    }

    @Override
    public SincronizacaoResponse toResponse(Sincronizacao entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        IntegracaoAtivaResponse integracao = null;
        String entidade = null;
        String direcao = null;
        String status = null;
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        Integer registrosProcessados = null;
        Integer registrosComErro = null;
        String logErros = null;

        id = entity.getId();
        integracao = integracaoAtivaMapper.toResponse( entity.getIntegracao() );
        entidade = entity.getEntidade();
        direcao = entity.getDirecao();
        status = entity.getStatus();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        registrosProcessados = entity.getRegistrosProcessados();
        registrosComErro = entity.getRegistrosComErro();
        logErros = entity.getLogErros();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        SincronizacaoResponse sincronizacaoResponse = new SincronizacaoResponse( id, integracao, entidade, direcao, status, dataInicio, dataFim, registrosProcessados, registrosComErro, logErros, createdAt, updatedAt );

        return sincronizacaoResponse;
    }

    @Override
    public void updateEntityFromRequest(SincronizacaoRequest request, Sincronizacao entity) {
        if ( request == null ) {
            return;
        }

        entity.setRegistrosComErro( request.registrosComErro() );
        entity.setRegistrosProcessados( request.registrosProcessados() );
        entity.setEntidade( request.entidade() );
        entity.setDirecao( request.direcao() );
        entity.setStatus( request.status() );
        entity.setDataInicio( request.dataInicio() );
        entity.setDataFim( request.dataFim() );
        entity.setLogErros( request.logErros() );
    }
}
