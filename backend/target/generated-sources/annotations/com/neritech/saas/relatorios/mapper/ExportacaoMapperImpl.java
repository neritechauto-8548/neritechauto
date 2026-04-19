package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.Exportacao;
import com.neritech.saas.relatorios.dto.ExportacaoRequest;
import com.neritech.saas.relatorios.dto.ExportacaoResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:51-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ExportacaoMapperImpl implements ExportacaoMapper {

    @Override
    public Exportacao toEntity(ExportacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Exportacao.ExportacaoBuilder exportacao = Exportacao.builder();

        exportacao.camposExportados( request.getCamposExportados() );
        exportacao.criteriosExportacao( request.getCriteriosExportacao() );
        exportacao.formatoArquivo( request.getFormatoArquivo() );
        exportacao.nomeArquivo( request.getNomeArquivo() );
        exportacao.notificarConclusao( request.getNotificarConclusao() );
        exportacao.observacoes( request.getObservacoes() );
        exportacao.senhaProtegida( request.getSenhaProtegida() );
        exportacao.tipoExportacao( request.getTipoExportacao() );
        exportacao.usuarioSolicitante( request.getUsuarioSolicitante() );

        return exportacao.build();
    }

    @Override
    public ExportacaoResponse toResponse(Exportacao entity) {
        if ( entity == null ) {
            return null;
        }

        ExportacaoResponse exportacaoResponse = new ExportacaoResponse();

        exportacaoResponse.setId( entity.getId() );
        exportacaoResponse.setEmpresaId( entity.getEmpresaId() );
        exportacaoResponse.setUsuarioSolicitante( entity.getUsuarioSolicitante() );
        exportacaoResponse.setTipoExportacao( entity.getTipoExportacao() );
        exportacaoResponse.setFormatoArquivo( entity.getFormatoArquivo() );
        exportacaoResponse.setNomeArquivo( entity.getNomeArquivo() );
        exportacaoResponse.setCaminhoArquivo( entity.getCaminhoArquivo() );
        exportacaoResponse.setTamanhoArquivoBytes( entity.getTamanhoArquivoBytes() );
        exportacaoResponse.setCriteriosExportacao( entity.getCriteriosExportacao() );
        exportacaoResponse.setCamposExportados( entity.getCamposExportados() );
        exportacaoResponse.setTotalRegistros( entity.getTotalRegistros() );
        exportacaoResponse.setDataInicio( entity.getDataInicio() );
        exportacaoResponse.setDataFim( entity.getDataFim() );
        exportacaoResponse.setDuracaoSegundos( entity.getDuracaoSegundos() );
        exportacaoResponse.setStatus( entity.getStatus() );
        exportacaoResponse.setProgressoPercentual( entity.getProgressoPercentual() );
        exportacaoResponse.setErroExportacao( entity.getErroExportacao() );
        exportacaoResponse.setUrlDownload( entity.getUrlDownload() );
        exportacaoResponse.setDataExpiracaoDownload( entity.getDataExpiracaoDownload() );
        exportacaoResponse.setDownloadsRealizados( entity.getDownloadsRealizados() );
        exportacaoResponse.setLimiteDownloads( entity.getLimiteDownloads() );
        exportacaoResponse.setSenhaProtegida( entity.getSenhaProtegida() );
        exportacaoResponse.setNotificarConclusao( entity.getNotificarConclusao() );
        exportacaoResponse.setNotificacaoEnviada( entity.getNotificacaoEnviada() );
        exportacaoResponse.setObservacoes( entity.getObservacoes() );
        exportacaoResponse.setDataCadastro( entity.getDataCadastro() );

        return exportacaoResponse;
    }

    @Override
    public void updateEntity(Exportacao entity, ExportacaoRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setTipoExportacao( request.getTipoExportacao() );
        entity.setFormatoArquivo( request.getFormatoArquivo() );
        entity.setNomeArquivo( request.getNomeArquivo() );
        entity.setCriteriosExportacao( request.getCriteriosExportacao() );
        entity.setCamposExportados( request.getCamposExportados() );
        entity.setObservacoes( request.getObservacoes() );
        entity.setSenhaProtegida( request.getSenhaProtegida() );
        entity.setNotificarConclusao( request.getNotificarConclusao() );
    }
}
