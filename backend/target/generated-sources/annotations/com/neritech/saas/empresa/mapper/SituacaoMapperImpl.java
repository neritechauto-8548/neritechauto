package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Situacao;
import com.neritech.saas.empresa.dto.SituacaoRequest;
import com.neritech.saas.empresa.dto.SituacaoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:59-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class SituacaoMapperImpl implements SituacaoMapper {

    @Override
    public Situacao toEntity(SituacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Situacao situacao = new Situacao();

        situacao.setNmSituacao( request.nmSituacao() );
        situacao.setDsSituacao( request.dsSituacao() );
        situacao.setCorSituacao( request.corSituacao() );

        return situacao;
    }

    @Override
    public SituacaoResponse toResponse(Situacao entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        String nmSituacao = null;
        String dsSituacao = null;
        String corSituacao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        nmSituacao = entity.getNmSituacao();
        dsSituacao = entity.getDsSituacao();
        corSituacao = entity.getCorSituacao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        SituacaoResponse situacaoResponse = new SituacaoResponse( id, empresaId, empresaNome, nmSituacao, dsSituacao, corSituacao, dataCadastro, dataAtualizacao );

        return situacaoResponse;
    }

    @Override
    public void updateEntityFromRequest(SituacaoRequest request, Situacao entity) {
        if ( request == null ) {
            return;
        }

        entity.setNmSituacao( request.nmSituacao() );
        entity.setDsSituacao( request.dsSituacao() );
        entity.setCorSituacao( request.corSituacao() );
    }

    private Long entityEmpresaId(Situacao situacao) {
        if ( situacao == null ) {
            return null;
        }
        Empresa empresa = situacao.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(Situacao situacao) {
        if ( situacao == null ) {
            return null;
        }
        Empresa empresa = situacao.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        String nomeFantasia = empresa.getNomeFantasia();
        if ( nomeFantasia == null ) {
            return null;
        }
        return nomeFantasia;
    }
}
