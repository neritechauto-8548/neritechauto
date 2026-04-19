package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Setor;
import com.neritech.saas.empresa.dto.SetorRequest;
import com.neritech.saas.empresa.dto.SetorResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:43-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class SetorMapperImpl implements SetorMapper {

    @Override
    public Setor toEntity(SetorRequest request) {
        if ( request == null ) {
            return null;
        }

        Setor setor = new Setor();

        setor.setNome( request.nome() );
        setor.setAtivo( request.ativo() );

        return setor;
    }

    @Override
    public SetorResponse toResponse(Setor entity) {
        if ( entity == null ) {
            return null;
        }

        Long empresaId = null;
        String empresaNome = null;
        Long id = null;
        String nome = null;
        Boolean ativo = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        empresaId = entityEmpresaId( entity );
        empresaNome = entityEmpresaNomeFantasia( entity );
        id = entity.getId();
        nome = entity.getNome();
        ativo = entity.getAtivo();
        dataCadastro = entity.getDataCadastro();
        dataAtualizacao = entity.getDataAtualizacao();

        SetorResponse setorResponse = new SetorResponse( id, empresaId, empresaNome, nome, ativo, dataCadastro, dataAtualizacao );

        return setorResponse;
    }

    @Override
    public void updateEntityFromRequest(SetorRequest request, Setor entity) {
        if ( request == null ) {
            return;
        }

        entity.setNome( request.nome() );
        entity.setAtivo( request.ativo() );
    }

    private Long entityEmpresaId(Setor setor) {
        if ( setor == null ) {
            return null;
        }
        Empresa empresa = setor.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(Setor setor) {
        if ( setor == null ) {
            return null;
        }
        Empresa empresa = setor.getEmpresa();
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
