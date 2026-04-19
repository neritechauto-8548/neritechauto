package com.neritech.saas.empresa.mapper;

import com.neritech.saas.empresa.domain.Empresa;
import com.neritech.saas.empresa.domain.Localizacao;
import com.neritech.saas.empresa.dto.LocalizacaoRequest;
import com.neritech.saas.empresa.dto.LocalizacaoResponse;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-19T13:26:54-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class LocalizacaoMapperImpl implements LocalizacaoMapper {

    @Override
    public Localizacao toEntity(LocalizacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Localizacao localizacao = new Localizacao();

        localizacao.setDescricao( request.descricao() );

        return localizacao;
    }

    @Override
    public LocalizacaoResponse toResponse(Localizacao entity) {
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

        LocalizacaoResponse localizacaoResponse = new LocalizacaoResponse( id, empresaId, empresaNome, descricao, dataCadastro, dataAtualizacao );

        return localizacaoResponse;
    }

    @Override
    public void updateEntityFromRequest(LocalizacaoRequest request, Localizacao entity) {
        if ( request == null ) {
            return;
        }

        entity.setDescricao( request.descricao() );
    }

    private Long entityEmpresaId(Localizacao localizacao) {
        if ( localizacao == null ) {
            return null;
        }
        Empresa empresa = localizacao.getEmpresa();
        if ( empresa == null ) {
            return null;
        }
        Long id = empresa.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityEmpresaNomeFantasia(Localizacao localizacao) {
        if ( localizacao == null ) {
            return null;
        }
        Empresa empresa = localizacao.getEmpresa();
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
