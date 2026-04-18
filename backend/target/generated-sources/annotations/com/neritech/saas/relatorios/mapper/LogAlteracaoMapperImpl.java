package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.LogAlteracao;
import com.neritech.saas.relatorios.dto.LogAlteracaoRequest;
import com.neritech.saas.relatorios.dto.LogAlteracaoResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T17:56:18-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LogAlteracaoMapperImpl implements LogAlteracaoMapper {

    @Override
    public LogAlteracao toEntity(LogAlteracaoRequest request) {
        if ( request == null ) {
            return null;
        }

        LogAlteracao.LogAlteracaoBuilder logAlteracao = LogAlteracao.builder();

        logAlteracao.camposAlterados( request.getCamposAlterados() );
        logAlteracao.checkpointAntes( request.getCheckpointAntes() );
        logAlteracao.checkpointDepois( request.getCheckpointDepois() );
        logAlteracao.comandoReversao( request.getComandoReversao() );
        logAlteracao.contextoOperacao( request.getContextoOperacao() );
        logAlteracao.dadosAdicionais( request.getDadosAdicionais() );
        logAlteracao.empresaId( request.getEmpresaId() );
        logAlteracao.ipOrigem( request.getIpOrigem() );
        logAlteracao.motivoAlteracao( request.getMotivoAlteracao() );
        logAlteracao.operacao( request.getOperacao() );
        logAlteracao.registroId( request.getRegistroId() );
        logAlteracao.reversivel( request.getReversivel() );
        logAlteracao.tabelaAfetada( request.getTabelaAfetada() );
        logAlteracao.userAgent( request.getUserAgent() );
        logAlteracao.usuarioResponsavel( request.getUsuarioResponsavel() );
        logAlteracao.valoresAntigos( request.getValoresAntigos() );
        logAlteracao.valoresNovos( request.getValoresNovos() );

        return logAlteracao.build();
    }

    @Override
    public LogAlteracaoResponse toResponse(LogAlteracao entity) {
        if ( entity == null ) {
            return null;
        }

        LogAlteracaoResponse logAlteracaoResponse = new LogAlteracaoResponse();

        logAlteracaoResponse.setId( entity.getId() );
        logAlteracaoResponse.setEmpresaId( entity.getEmpresaId() );
        logAlteracaoResponse.setTabelaAfetada( entity.getTabelaAfetada() );
        logAlteracaoResponse.setRegistroId( entity.getRegistroId() );
        logAlteracaoResponse.setOperacao( entity.getOperacao() );
        logAlteracaoResponse.setCamposAlterados( entity.getCamposAlterados() );
        logAlteracaoResponse.setValoresAntigos( entity.getValoresAntigos() );
        logAlteracaoResponse.setValoresNovos( entity.getValoresNovos() );
        logAlteracaoResponse.setUsuarioResponsavel( entity.getUsuarioResponsavel() );
        logAlteracaoResponse.setIpOrigem( entity.getIpOrigem() );
        logAlteracaoResponse.setUserAgent( entity.getUserAgent() );
        logAlteracaoResponse.setMotivoAlteracao( entity.getMotivoAlteracao() );
        logAlteracaoResponse.setContextoOperacao( entity.getContextoOperacao() );
        logAlteracaoResponse.setDadosAdicionais( entity.getDadosAdicionais() );
        logAlteracaoResponse.setCheckpointAntes( entity.getCheckpointAntes() );
        logAlteracaoResponse.setCheckpointDepois( entity.getCheckpointDepois() );
        logAlteracaoResponse.setReversivel( entity.getReversivel() );
        logAlteracaoResponse.setComandoReversao( entity.getComandoReversao() );
        logAlteracaoResponse.setDataAlteracao( entity.getDataAlteracao() );
        logAlteracaoResponse.setAuditado( entity.getAuditado() );
        logAlteracaoResponse.setDataAuditoria( entity.getDataAuditoria() );
        logAlteracaoResponse.setAuditorResponsavel( entity.getAuditorResponsavel() );
        logAlteracaoResponse.setObservacoesAuditoria( entity.getObservacoesAuditoria() );

        return logAlteracaoResponse;
    }
}
