package com.neritech.saas.integracao.mapper;

import com.neritech.saas.integracao.domain.LogIntegracao;
import com.neritech.saas.integracao.dto.IntegracaoAtivaResponse;
import com.neritech.saas.integracao.dto.LogIntegracaoRequest;
import com.neritech.saas.integracao.dto.LogIntegracaoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:57-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LogIntegracaoMapperImpl implements LogIntegracaoMapper {

    @Autowired
    private IntegracaoAtivaMapper integracaoAtivaMapper;

    @Override
    public LogIntegracao toEntity(LogIntegracaoRequest request) {
        if ( request == null ) {
            return null;
        }

        LogIntegracao.LogIntegracaoBuilder<?, ?> logIntegracao = LogIntegracao.builder();

        logIntegracao.codigoHttp( request.codigoHttp() );
        logIntegracao.dataExecucao( request.dataExecucao() );
        logIntegracao.mensagemErro( request.mensagemErro() );
        logIntegracao.operacao( request.operacao() );
        logIntegracao.requisicao( request.requisicao() );
        logIntegracao.resposta( request.resposta() );
        logIntegracao.status( request.status() );

        return logIntegracao.build();
    }

    @Override
    public LogIntegracaoResponse toResponse(LogIntegracao entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        IntegracaoAtivaResponse integracao = null;
        String operacao = null;
        String status = null;
        String requisicao = null;
        String resposta = null;
        String mensagemErro = null;
        Integer codigoHttp = null;
        LocalDateTime dataExecucao = null;

        id = entity.getId();
        integracao = integracaoAtivaMapper.toResponse( entity.getIntegracao() );
        operacao = entity.getOperacao();
        status = entity.getStatus();
        requisicao = entity.getRequisicao();
        resposta = entity.getResposta();
        mensagemErro = entity.getMensagemErro();
        codigoHttp = entity.getCodigoHttp();
        dataExecucao = entity.getDataExecucao();

        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        LogIntegracaoResponse logIntegracaoResponse = new LogIntegracaoResponse( id, integracao, operacao, status, requisicao, resposta, mensagemErro, codigoHttp, dataExecucao, createdAt, updatedAt );

        return logIntegracaoResponse;
    }

    @Override
    public void updateEntityFromRequest(LogIntegracaoRequest request, LogIntegracao entity) {
        if ( request == null ) {
            return;
        }

        entity.setOperacao( request.operacao() );
        entity.setStatus( request.status() );
        entity.setRequisicao( request.requisicao() );
        entity.setResposta( request.resposta() );
        entity.setMensagemErro( request.mensagemErro() );
        entity.setCodigoHttp( request.codigoHttp() );
        entity.setDataExecucao( request.dataExecucao() );
    }
}
