package com.neritech.saas.relatorios.mapper;

import com.neritech.saas.relatorios.domain.Backup;
import com.neritech.saas.relatorios.dto.BackupRequest;
import com.neritech.saas.relatorios.dto.BackupResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:48-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class BackupMapperImpl implements BackupMapper {

    @Override
    public Backup toEntity(BackupRequest request) {
        if ( request == null ) {
            return null;
        }

        Backup.BackupBuilder backup = Backup.builder();

        backup.algoritmoCriptografia( request.getAlgoritmoCriptografia() );
        backup.automatico( request.getAutomatico() );
        backup.caminhoArquivo( request.getCaminhoArquivo() );
        backup.compressaoUtilizada( request.getCompressaoUtilizada() );
        backup.criptografado( request.getCriptografado() );
        backup.dataExpiracao( request.getDataExpiracao() );
        backup.dataFim( request.getDataFim() );
        backup.dataInicio( request.getDataInicio() );
        backup.duracaoMinutos( request.getDuracaoMinutos() );
        backup.executadoPor( request.getExecutadoPor() );
        backup.hashArquivo( request.getHashArquivo() );
        backup.localArmazenamento( request.getLocalArmazenamento() );
        backup.nomeArquivo( request.getNomeArquivo() );
        backup.observacoes( request.getObservacoes() );
        backup.provedorNuvem( request.getProvedorNuvem() );
        backup.senhaProtegido( request.getSenhaProtegido() );
        backup.tabelasIncluidas( request.getTabelasIncluidas() );
        backup.tamanhoArquivoBytes( request.getTamanhoArquivoBytes() );
        backup.tipoBackup( request.getTipoBackup() );
        backup.totalRegistros( request.getTotalRegistros() );
        backup.urlDownload( request.getUrlDownload() );

        return backup.build();
    }

    @Override
    public BackupResponse toResponse(Backup entity) {
        if ( entity == null ) {
            return null;
        }

        BackupResponse backupResponse = new BackupResponse();

        backupResponse.setId( entity.getId() );
        backupResponse.setEmpresaId( entity.getEmpresaId() );
        backupResponse.setTipoBackup( entity.getTipoBackup() );
        backupResponse.setNomeArquivo( entity.getNomeArquivo() );
        backupResponse.setCaminhoArquivo( entity.getCaminhoArquivo() );
        backupResponse.setTamanhoArquivoBytes( entity.getTamanhoArquivoBytes() );
        backupResponse.setHashArquivo( entity.getHashArquivo() );
        backupResponse.setDataInicio( entity.getDataInicio() );
        backupResponse.setDataFim( entity.getDataFim() );
        backupResponse.setDuracaoMinutos( entity.getDuracaoMinutos() );
        backupResponse.setStatus( entity.getStatus() );
        backupResponse.setAutomatico( entity.getAutomatico() );
        backupResponse.setTabelasIncluidas( entity.getTabelasIncluidas() );
        backupResponse.setTotalRegistros( entity.getTotalRegistros() );
        backupResponse.setCompressaoUtilizada( entity.getCompressaoUtilizada() );
        backupResponse.setTaxaCompressao( entity.getTaxaCompressao() );
        backupResponse.setSenhaProtegido( entity.getSenhaProtegido() );
        backupResponse.setCriptografado( entity.getCriptografado() );
        backupResponse.setAlgoritmoCriptografia( entity.getAlgoritmoCriptografia() );
        backupResponse.setLocalArmazenamento( entity.getLocalArmazenamento() );
        backupResponse.setProvedorNuvem( entity.getProvedorNuvem() );
        backupResponse.setUrlDownload( entity.getUrlDownload() );
        backupResponse.setDataExpiracao( entity.getDataExpiracao() );
        backupResponse.setTestadoRestauracao( entity.getTestadoRestauracao() );
        backupResponse.setDataTesteRestauracao( entity.getDataTesteRestauracao() );
        backupResponse.setResultadoTeste( entity.getResultadoTeste() );
        backupResponse.setErroBackup( entity.getErroBackup() );
        backupResponse.setObservacoes( entity.getObservacoes() );
        backupResponse.setExecutadoPor( entity.getExecutadoPor() );
        backupResponse.setDataCadastro( entity.getDataCadastro() );

        return backupResponse;
    }

    @Override
    public void updateEntity(Backup entity, BackupRequest request) {
        if ( request == null ) {
            return;
        }

        entity.setTipoBackup( request.getTipoBackup() );
        entity.setNomeArquivo( request.getNomeArquivo() );
        entity.setCaminhoArquivo( request.getCaminhoArquivo() );
        entity.setTamanhoArquivoBytes( request.getTamanhoArquivoBytes() );
        entity.setHashArquivo( request.getHashArquivo() );
        entity.setDataInicio( request.getDataInicio() );
        entity.setDataFim( request.getDataFim() );
        entity.setDuracaoMinutos( request.getDuracaoMinutos() );
        entity.setTabelasIncluidas( request.getTabelasIncluidas() );
        entity.setTotalRegistros( request.getTotalRegistros() );
        entity.setCompressaoUtilizada( request.getCompressaoUtilizada() );
        entity.setAlgoritmoCriptografia( request.getAlgoritmoCriptografia() );
        entity.setLocalArmazenamento( request.getLocalArmazenamento() );
        entity.setProvedorNuvem( request.getProvedorNuvem() );
        entity.setUrlDownload( request.getUrlDownload() );
        entity.setDataExpiracao( request.getDataExpiracao() );
        entity.setObservacoes( request.getObservacoes() );
        entity.setExecutadoPor( request.getExecutadoPor() );
        entity.setAutomatico( request.getAutomatico() );
        entity.setSenhaProtegido( request.getSenhaProtegido() );
        entity.setCriptografado( request.getCriptografado() );
    }
}
