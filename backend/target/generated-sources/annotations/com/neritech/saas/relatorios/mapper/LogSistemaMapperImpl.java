package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.LogSistema;
import com.neritech.saas.relatorios.dto.LogSistemaRequest;
import com.neritech.saas.relatorios.dto.LogSistemaResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:27:09-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LogSistemaMapperImpl implements LogSistemaMapper {

    @Override
    public LogSistema toEntity(LogSistemaRequest request) {
        if ( request == null ) {
            return null;
        }

        LogSistema.LogSistemaBuilder logSistema = LogSistema.builder();

        logSistema.categoria( request.getCategoria() );
        logSistema.correlacaoId( request.getCorrelacaoId() );
        logSistema.cpuUtilizadaPercent( request.getCpuUtilizadaPercent() );
        logSistema.dadosContexto( request.getDadosContexto() );
        logSistema.empresaId( request.getEmpresaId() );
        logSistema.exceptionClass( request.getExceptionClass() );
        logSistema.exceptionMessage( request.getExceptionMessage() );
        logSistema.funcao( request.getFuncao() );
        logSistema.ipOrigem( request.getIpOrigem() );
        logSistema.memoriaUtilizadaMb( request.getMemoriaUtilizadaMb() );
        logSistema.mensagem( request.getMensagem() );
        logSistema.metodoHttp( request.getMetodoHttp() );
        logSistema.modulo( request.getModulo() );
        logSistema.nivelLog( request.getNivelLog() );
        logSistema.servidor( request.getServidor() );
        logSistema.sessaoId( request.getSessaoId() );
        logSistema.stackTrace( request.getStackTrace() );
        logSistema.tempoRespostaMs( request.getTempoRespostaMs() );
        logSistema.threadId( request.getThreadId() );
        logSistema.urlRequisicao( request.getUrlRequisicao() );
        logSistema.userAgent( request.getUserAgent() );
        logSistema.usuarioId( request.getUsuarioId() );
        logSistema.versaoAplicacao( request.getVersaoAplicacao() );

        return logSistema.build();
    }

    @Override
    public LogSistemaResponse toResponse(LogSistema entity) {
        if ( entity == null ) {
            return null;
        }

        LogSistemaResponse logSistemaResponse = new LogSistemaResponse();

        logSistemaResponse.setId( entity.getId() );
        logSistemaResponse.setEmpresaId( entity.getEmpresaId() );
        logSistemaResponse.setNivelLog( entity.getNivelLog() );
        logSistemaResponse.setCategoria( entity.getCategoria() );
        logSistemaResponse.setModulo( entity.getModulo() );
        logSistemaResponse.setFuncao( entity.getFuncao() );
        logSistemaResponse.setMensagem( entity.getMensagem() );
        logSistemaResponse.setStackTrace( entity.getStackTrace() );
        logSistemaResponse.setDadosContexto( entity.getDadosContexto() );
        logSistemaResponse.setUsuarioId( entity.getUsuarioId() );
        logSistemaResponse.setSessaoId( entity.getSessaoId() );
        logSistemaResponse.setIpOrigem( entity.getIpOrigem() );
        logSistemaResponse.setUserAgent( entity.getUserAgent() );
        logSistemaResponse.setUrlRequisicao( entity.getUrlRequisicao() );
        logSistemaResponse.setMetodoHttp( entity.getMetodoHttp() );
        logSistemaResponse.setTempoRespostaMs( entity.getTempoRespostaMs() );
        logSistemaResponse.setMemoriaUtilizadaMb( entity.getMemoriaUtilizadaMb() );
        logSistemaResponse.setCpuUtilizadaPercent( entity.getCpuUtilizadaPercent() );
        logSistemaResponse.setExceptionClass( entity.getExceptionClass() );
        logSistemaResponse.setExceptionMessage( entity.getExceptionMessage() );
        logSistemaResponse.setCorrelacaoId( entity.getCorrelacaoId() );
        logSistemaResponse.setThreadId( entity.getThreadId() );
        logSistemaResponse.setServidor( entity.getServidor() );
        logSistemaResponse.setVersaoAplicacao( entity.getVersaoAplicacao() );
        logSistemaResponse.setDataOcorrencia( entity.getDataOcorrencia() );
        logSistemaResponse.setResolvido( entity.getResolvido() );
        logSistemaResponse.setDataResolucao( entity.getDataResolucao() );
        logSistemaResponse.setResponsavelResolucao( entity.getResponsavelResolucao() );
        logSistemaResponse.setObservacoesResolucao( entity.getObservacoesResolucao() );

        return logSistemaResponse;
    }
}
