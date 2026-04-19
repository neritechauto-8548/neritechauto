package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.DepartamentoContabio;
import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.dto.DepartamentoContabioRequest;
import com.neritech.saas.empresa.dto.DepartamentoContabioResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T11:12:53-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class DepartamentoContabioMapperImpl implements DepartamentoContabioMapper {

    @Override
    public DepartamentoContabio toEntity(DepartamentoContabioRequest request) {
        if ( request == null ) {
            return null;
        }

        DepartamentoContabio departamentoContabio = new DepartamentoContabio();

        departamentoContabio.setDescricao( request.descricao() );

        return departamentoContabio;
    }

    @Override
    public DepartamentoContabioResponse toResponse(DepartamentoContabio entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        String descricao = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        descricao = entity.getDescricao();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        DepartamentoContabioResponse departamentoContabioResponse = new DepartamentoContabioResponse( id, empresaId, empresaNome, descricao, dataCadastro, dataAtualizacao );

        return departamentoContabioResponse;
    }

    @Override
    public void updateEntityFromRequest(DepartamentoContabioRequest request, DepartamentoContabio entity) {
        if ( request == null ) {
            return;
        }

        entity.setDescricao( request.descricao() );
    }

    private Long entityEmpresaId(DepartamentoContabio departamentoContabio) {
        if ( departamentoContabio == null ) {
            return null;
        }
        Empresa empresa = departamentoContabio.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(DepartamentoContabio departamentoContabio) {
        if ( departamentoContabio == null ) {
            return null;
        }
        Empresa empresa = departamentoContabio.getEmpresa();
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
