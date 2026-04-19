package com.neritech.saas.rh.mapper;

import com.neritech.saas.rh.domain.FuncionarioEspecialidade;
import com.neritech.saas.rh.domain.enums.NivelDominio;
import com.neritech.saas.rh.dto.FuncionarioEspecialidadeRequest;
import com.neritech.saas.rh.dto.FuncionarioEspecialidadeResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:54-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FuncionarioEspecialidadeMapperImpl implements FuncionarioEspecialidadeMapper {

    @Override
    public FuncionarioEspecialidade toEntity(FuncionarioEspecialidadeRequest request) {
        if ( request == null ) {
            return null;
        }

        FuncionarioEspecialidade funcionarioEspecialidade = new FuncionarioEspecialidade();

        funcionarioEspecialidade.setFuncionarioId( request.funcionarioId() );
        funcionarioEspecialidade.setEspecialidadeId( request.especialidadeId() );
        funcionarioEspecialidade.setNivelDominio( request.nivelDominio() );
        funcionarioEspecialidade.setDataCertificacao( request.dataCertificacao() );
        funcionarioEspecialidade.setNumeroCertificado( request.numeroCertificado() );
        funcionarioEspecialidade.setEntidadeCertificadora( request.entidadeCertificadora() );
        funcionarioEspecialidade.setDataValidadeCertificacao( request.dataValidadeCertificacao() );
        funcionarioEspecialidade.setAnexoCertificadoUrl( request.anexoCertificadoUrl() );
        funcionarioEspecialidade.setExperienciaAnos( request.experienciaAnos() );
        funcionarioEspecialidade.setObservacoes( request.observacoes() );
        funcionarioEspecialidade.setAtivo( request.ativo() );

        return funcionarioEspecialidade;
    }

    @Override
    public FuncionarioEspecialidadeResponse toResponse(FuncionarioEspecialidade entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long funcionarioId = null;
        Long especialidadeId = null;
        NivelDominio nivelDominio = null;
        LocalDate dataCertificacao = null;
        String numeroCertificado = null;
        String entidadeCertificadora = null;
        LocalDate dataValidadeCertificacao = null;
        String anexoCertificadoUrl = null;
        Integer experienciaAnos = null;
        String observacoes = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = entity.getId();
        funcionarioId = entity.getFuncionarioId();
        especialidadeId = entity.getEspecialidadeId();
        nivelDominio = entity.getNivelDominio();
        dataCertificacao = entity.getDataCertificacao();
        numeroCertificado = entity.getNumeroCertificado();
        entidadeCertificadora = entity.getEntidadeCertificadora();
        dataValidadeCertificacao = entity.getDataValidadeCertificacao();
        anexoCertificadoUrl = entity.getAnexoCertificadoUrl();
        experienciaAnos = entity.getExperienciaAnos();
        observacoes = entity.getObservacoes();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        FuncionarioEspecialidadeResponse funcionarioEspecialidadeResponse = new FuncionarioEspecialidadeResponse( id, funcionarioId, especialidadeId, nivelDominio, dataCertificacao, numeroCertificado, entidadeCertificadora, dataValidadeCertificacao, anexoCertificadoUrl, experienciaAnos, observacoes, ativo, dataCadastro, dataAtualizacao );

        return funcionarioEspecialidadeResponse;
    }

    @Override
    public void updateEntity(FuncionarioEspecialidadeRequest request, FuncionarioEspecialidade entity) {
        if ( request == null ) {
            return;
        }

        if ( request.funcionarioId() != null ) {
            entity.setFuncionarioId( request.funcionarioId() );
        }
        if ( request.especialidadeId() != null ) {
            entity.setEspecialidadeId( request.especialidadeId() );
        }
        if ( request.nivelDominio() != null ) {
            entity.setNivelDominio( request.nivelDominio() );
        }
        if ( request.dataCertificacao() != null ) {
            entity.setDataCertificacao( request.dataCertificacao() );
        }
        if ( request.numeroCertificado() != null ) {
            entity.setNumeroCertificado( request.numeroCertificado() );
        }
        if ( request.entidadeCertificadora() != null ) {
            entity.setEntidadeCertificadora( request.entidadeCertificadora() );
        }
        if ( request.dataValidadeCertificacao() != null ) {
            entity.setDataValidadeCertificacao( request.dataValidadeCertificacao() );
        }
        if ( request.anexoCertificadoUrl() != null ) {
            entity.setAnexoCertificadoUrl( request.anexoCertificadoUrl() );
        }
        if ( request.experienciaAnos() != null ) {
            entity.setExperienciaAnos( request.experienciaAnos() );
        }
        if ( request.observacoes() != null ) {
            entity.setObservacoes( request.observacoes() );
        }
        if ( request.ativo() != null ) {
            entity.setAtivo( request.ativo() );
        }
    }
}
