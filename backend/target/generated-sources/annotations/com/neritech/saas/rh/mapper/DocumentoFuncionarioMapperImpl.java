package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.DocumentoFuncionario;
import com.neritech.saas.rh.domain.enums.TipoDocumento;
import com.neritech.saas.rh.dto.DocumentoFuncionarioRequest;
import com.neritech.saas.rh.dto.DocumentoFuncionarioResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-02T21:26:52-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DocumentoFuncionarioMapperImpl implements DocumentoFuncionarioMapper {

    @Override
    public DocumentoFuncionario toEntity(DocumentoFuncionarioRequest request) {
        if ( request == null ) {
            return null;
        }

        DocumentoFuncionario documentoFuncionario = new DocumentoFuncionario();

        documentoFuncionario.setFuncionarioId( request.funcionarioId() );
        documentoFuncionario.setTipoDocumento( request.tipoDocumento() );
        documentoFuncionario.setNumeroDocumento( request.numeroDocumento() );
        documentoFuncionario.setOrgaoEmissor( request.orgaoEmissor() );
        documentoFuncionario.setDataEmissao( request.dataEmissao() );
        documentoFuncionario.setDataValidade( request.dataValidade() );
        documentoFuncionario.setArquivoUrl( request.arquivoUrl() );
        documentoFuncionario.setArquivoNome( request.arquivoNome() );
        documentoFuncionario.setArquivoTamanhoKb( request.arquivoTamanhoKb() );
        documentoFuncionario.setObservacoes( request.observacoes() );
        documentoFuncionario.setObrigatorio( request.obrigatorio() );
        documentoFuncionario.setVerificado( request.verificado() );

        return documentoFuncionario;
    }

    @Override
    public DocumentoFuncionarioResponse toResponse(DocumentoFuncionario entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        TipoDocumento tipoDocumento = null;
        String numeroDocumento = null;
        String orgaoEmissor = null;
        LocalDate dataEmissao = null;
        LocalDate dataValidade = null;
        String arquivoUrl = null;
        String arquivoNome = null;
        Integer arquivoTamanhoKb = null;
        String observacoes = null;
        Boolean obrigatorio = null;
        Boolean verificado = null;
        Long verificadoPor = null;
        LocalDate dataVerificacao = null;
        Long cadastradoPor = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        tipoDocumento = entity.getTipoDocumento();
        numeroDocumento = entity.getNumeroDocumento();
        orgaoEmissor = entity.getOrgaoEmissor();
        dataEmissao = entity.getDataEmissao();
        dataValidade = entity.getDataValidade();
        arquivoUrl = entity.getArquivoUrl();
        arquivoNome = entity.getArquivoNome();
        arquivoTamanhoKb = entity.getArquivoTamanhoKb();
        observacoes = entity.getObservacoes();
        obrigatorio = entity.getObrigatorio();
        verificado = entity.getVerificado();
        verificadoPor = entity.getVerificadoPor();
        dataVerificacao = entity.getDataVerificacao();
        cadastradoPor = entity.getCadastradoPor();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        DocumentoFuncionarioResponse documentoFuncionarioResponse = new DocumentoFuncionarioResponse( id, funcionarioId, tipoDocumento, numeroDocumento, orgaoEmissor, dataEmissao, dataValidade, arquivoUrl, arquivoNome, arquivoTamanhoKb, observacoes, obrigatorio, verificado, verificadoPor, dataVerificacao, cadastradoPor, dataCadastro, dataAtualizacao );

        return documentoFuncionarioResponse;
    }

    @Override
    public void updateEntity(DocumentoFuncionarioRequest request, DocumentoFuncionario entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.tipoDocumento() != null ) {
            entity.setTipoDocumento( request.tipoDocumento() );
        }
        if ( request.numeroDocumento() != null ) {
            entity.setNumeroDocumento( request.numeroDocumento() );
        }
        if ( request.orgaoEmissor() != null ) {
            entity.setOrgaoEmissor( request.orgaoEmissor() );
        }
        if ( request.dataEmissao() != null ) {
            entity.setDataEmissao( request.dataEmissao() );
        }
        if ( request.dataValidade() != null ) {
            entity.setDataValidade( request.dataValidade() );
        }
        if ( request.arquivoUrl() != null ) {
            entity.setArquivoUrl( request.arquivoUrl() );
        }
        if ( request.arquivoNome() != null ) {
            entity.setArquivoNome( request.arquivoNome() );
        }
        if ( request.arquivoTamanhoKb() != null ) {
            entity.setArquivoTamanhoKb( request.arquivoTamanhoKb() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.obrigatorio() != null ) {
            entity.setObrigatorio( request.obrigatorio() );
        }
        if ( request.verificado() != null ) {
            entity.setVerificado( request.verificado() );
        }
    }
}
